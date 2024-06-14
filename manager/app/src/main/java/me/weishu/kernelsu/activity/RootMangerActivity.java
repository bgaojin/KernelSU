package me.weishu.kernelsu.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.topjohnwu.superuser.Shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.weishu.kernelsu.IKsuInterface;
import me.weishu.kernelsu.Natives;
import me.weishu.kernelsu.R;
import me.weishu.kernelsu.adapter.RootMangerAdapter;
import me.weishu.kernelsu.bean.AppItem;
import me.weishu.kernelsu.bean.RootResult;
import me.weishu.kernelsu.databinding.ActivityRootMangerBinding;
import me.weishu.kernelsu.ui.KsuService;
import me.weishu.kernelsu.ui.util.KsuCli;
import me.weishu.kernelsu.utils.AppUtils;
import rikka.parcelablelist.ParcelableListSlice;

public class RootMangerActivity extends AppCompatActivity {

    private IKsuInterface.Stub binder;
    private List<AppItem> apps;
    private RootMangerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRootMangerBinding inflate = ActivityRootMangerBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        inflate.tvTitle.title.setText("ROOT管理");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        inflate.appList.setLayoutManager(manager);

        apps = AppUtils.getApps(this,true);

        adapter = new RootMangerAdapter(this, apps);
        inflate.appList.setAdapter(adapter);

        inflate.clearRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAppRoot(false);
            }
        });

        inflate.setRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAppRoot(true);
            }
        });
    }

    private void setAppRoot(boolean allowSu) {
        ArrayList<RootResult> results = new ArrayList<>();
        for (AppItem app : apps) {
            String pkgName = app.getPackageName();
            if (!app.isCheck()) {
                continue;
            }
            Natives.Profile profile = Natives.INSTANCE.getAppProfile(pkgName, app.getUid());

            Natives.Profile copy = profile.copy(pkgName,profile.getCurrentUid(),
                    allowSu,true,null,
                    0,0,new ArrayList<>(),new ArrayList<>(),
                    "u:r:su:s0",0,true,
                    true,"");

            boolean result = Natives.INSTANCE.setAppProfile(copy);
            if (result&&allowSu){
                app.setRootState("ROOT");
            }else {
                app.setRootState("UNKNOW");
            }
            //清空勾选
            app.setCheck(false);
            RootResult rootResult = new RootResult();
            rootResult.setAppName(app.getAppName());
            rootResult.setResult(result);
            results.add(rootResult);
        }
        if (results.size()==0){
            showMsg("请选择app");
            return;
        }
        String errMsg = "";
        for (RootResult result : results) {
            if (!result.isResult()) {
                errMsg+=result.getAppName();
            }
        }
        if (!TextUtils.isEmpty(errMsg)){
            errMsg = "设置失败:"+errMsg;
            showMsg(errMsg);
        }else{
            showMsg("设置成功");
        }
        adapter.notifyDataSetChanged();
        setRootPkg();
    }

    private void setRootPkg(){
        String rootPkgs ="";
        for (AppItem app : apps) {
            String rootState = app.getRootState();
            if ("ROOT".equals(rootState)){
                String packageName = app.getPackageName();
                rootPkgs+=packageName+",";
            }

        }

        Settings.System.putString(getContentResolver(),"xzz_set_pkgs",rootPkgs);
    }

    private void showMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void connectKsuService() throws IOException {
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                binder = (IKsuInterface.Stub) iBinder;
                try {
                    ParcelableListSlice<PackageInfo> packages = IKsuInterface.Stub.asInterface(binder).getPackages(0);
                    List<PackageInfo> list = packages.getList();
                    PackageInfo packageInfo = list.get(0);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent(this, KsuService.class);
        Shell.Task task = KsuService.bindOrTask(intent, Shell.EXECUTOR, connection);
        Shell shell = KsuCli.INSTANCE.getSHELL();
        if (task!=null) {
            shell.execTask(task);
        }

    }

    private void stopKsuService() {
        Intent intent = new Intent(this, KsuService.class);
        KsuService.stop(intent);
    }
}
