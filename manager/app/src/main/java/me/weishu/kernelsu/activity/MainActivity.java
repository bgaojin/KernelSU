package me.weishu.kernelsu.activity;


import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.ShellUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.weishu.kernelsu.Natives;
import me.weishu.kernelsu.R;
import me.weishu.kernelsu.bean.EventMessage;
import me.weishu.kernelsu.databinding.ActivityMainBinding;
import me.weishu.kernelsu.fragment.BackUpFragment;
import me.weishu.kernelsu.fragment.SetPropFragment;
import me.weishu.kernelsu.fragment.ResetFragment;
import me.weishu.kernelsu.utils.AppUtils;
import me.weishu.kernelsu.utils.EventCode;

public class MainActivity extends AppCompatActivity {


    private FragmentManager mFragmentManager;
    private ArrayList<String> mFragmentTags;
    private int mCurrIndex = 0;


    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mainBinding = activityMainBinding;
        setContentView(activityMainBinding.getRoot());
        mainBinding.tvTitle.title.setText("改机");
        mainBinding.rbParamets.setChecked(true);
        mFragmentManager = getSupportFragmentManager();
        String[] tabSArray = new String[]{"one", "two", "three"};
        mFragmentTags = new ArrayList<>(Arrays.asList(tabSArray));
//        if (accessibilityManager.isEnabled()) {
//            ((ArrayList) F0).add("AccessibilityManager.isEnabled");
//        }
//        if (accessibilityManager.isTouchExplorationEnabled()) {
//            ((ArrayList) F0).add("AccessibilityManager.isTouchExplorationEnabled");
//        }
        RadioGroup radioGroup = findViewById(R.id.rg_navigation);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_paramets) {
                    mCurrIndex = 0;
                    mainBinding.tvTitle.title.setText("改机");
                    EventBus.getDefault().postSticky(new EventMessage(EventCode.SLELECT_SET_PROP));
                } else if (i == R.id.rb_backup) {
                    mCurrIndex = 1;
                    mainBinding.tvTitle.title.setText("备份");
                    EventBus.getDefault().postSticky(new EventMessage(EventCode.SLELECT_BACKUP));

                } else if (i == R.id.rb_reset) {
                    mCurrIndex = 2;
                    mainBinding.tvTitle.title.setText("还原");
                    EventBus.getDefault().postSticky(new EventMessage(EventCode.SLELECT_REST));
                }
                showFragment();
            }
        });
        showFragment();
        mainBinding.openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBinding.drawer.openDrawer(GravityCompat.START);
            }
        });
        //抽屉布局添加点击监听
        mainBinding.drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {


            }
        });
        mainBinding.vpnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VpnSetActivity.class));
                mainBinding.drawer.close();
            }
        });

        mainBinding.rootManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RootMangerActivity.class));
                mainBinding.drawer.close();
            }
        });
        //ifconfig wlan0 down &ifconfig wlan0 hw ether 00:11:22:33:44:55&ifconfig wlan0 up

//        Shell.Result result = Shell.cmd("su", "ifconfig wlan0 hw ether 94:b4:6b:38:84:c8").exec();


        String packageName = getPackageName();

        boolean manager = Natives.INSTANCE.becomeManager(packageName);
        if (manager) {
            try {
                ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(packageName, 0);
                Natives.Profile profile = Natives.INSTANCE.getAppProfile(packageName, applicationInfo.uid);
                if (!profile.getAllowSu()) {
                    Natives.Profile copy = profile.copy(packageName, profile.getCurrentUid(),
                            true, true, null,
                            0, 0, new ArrayList<>(), new ArrayList<>(),
                            "u:r:su:s0", 0, true,
                            true, "");

                    Natives.INSTANCE.setAppProfile(copy);

                }
//                Shell.Result result = Shell.cmd("su","cd /sdcard", "ls -a").exec();
//                List<String> out = result.getOut();
//                for (String s : out) {
//                    System.out.println("sss="+s);
//                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
        boolean appInstalled = AppUtils.isAppInstalled(this, "com.android.ghost.service");
        if (appInstalled) {
            boolean appAlive = AppUtils.isAppAlive(this, "com.android.ghost.service.http.NanoHttpService");
            if (!appAlive) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("ghost://ghost.service.android.com/main"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }


    private void showFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(mFragmentTags.get(mCurrIndex));
        if (fragment == null) {
            fragment = instantFragment(mCurrIndex);
        }
        for (int i = 0; i < mFragmentTags.size(); i++) {
            Fragment f = mFragmentManager.findFragmentByTag(mFragmentTags.get(i));

            if (f != null && f != fragment && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }

        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.container, fragment, mFragmentTags.get(mCurrIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();

    }

    private Fragment instantFragment(int mCurrIndex) {
        switch (mCurrIndex) {
            case 0://
                return new SetPropFragment();
            case 1:
                return new BackUpFragment();
            case 2:
                return new ResetFragment();
            default:
                return new SetPropFragment();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
