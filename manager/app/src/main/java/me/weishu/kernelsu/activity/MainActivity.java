package me.weishu.kernelsu.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

import me.weishu.kernelsu.R;
import me.weishu.kernelsu.bean.EventMessage;
import me.weishu.kernelsu.databinding.ActivityMainBinding;
import me.weishu.kernelsu.fragment.BackUpFragment;
import me.weishu.kernelsu.fragment.ParametsFragment;
import me.weishu.kernelsu.fragment.ResetFragment;
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

        mainBinding.rbParamets.setChecked(true);
        mFragmentManager = getSupportFragmentManager();
        String[] tabSArray = new String[]{"one", "two", "three"};
        mFragmentTags = new ArrayList<>(Arrays.asList(tabSArray));

        RadioGroup radioGroup = findViewById(R.id.rg_navigation);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_paramets) {
                    mCurrIndex = 0;
                } else if (i == R.id.rb_backup) {
                    mCurrIndex = 1;
                } else if (i == R.id.rb_reset) {
                    mCurrIndex = 2;
                    EventBus.getDefault().postSticky(new EventMessage(EventCode.SLELECT_BACKUP));
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
            }
        });

        mainBinding.rootManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RootMangerActivity.class));
            }
        });
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
                return new ParametsFragment();
            case 1:
                return new BackUpFragment();
            case 2:
                return new ResetFragment();
            default:
                return new ParametsFragment();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
