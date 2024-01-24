package me.weishu.kernelsu.fragment;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import me.weishu.kernelsu.R;
import me.weishu.kernelsu.bean.EventMessage;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.databinding.FragmentBackupBinding;
import me.weishu.kernelsu.dialog.TaskInfoDialog;
import me.weishu.kernelsu.net.CommonRetrofitManager;
import me.weishu.kernelsu.net.HttpUtils;
import me.weishu.kernelsu.utils.ApiUtils;
import me.weishu.kernelsu.utils.EventCode;
import me.weishu.kernelsu.utils.GsonUtils;

public class BackUpFragment extends Fragment {


    private List<String> list;
    private PackageManager packageManager;
    private List<PackageInfo> installedPackages;
    private FragmentBackupBinding bind;
    String backPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/backData";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_backup, container, false);
        bind = FragmentBackupBinding.bind(mRootView);
        initData();
        initView();
        return mRootView;
    }

    private void initView() {

    }

    private void initData() {
        list = new ArrayList<>();
        packageManager = getActivity().getPackageManager();
        // 获取已安装的应用程序列表
        installedPackages = packageManager.getInstalledPackages(0);
        List<String> apps = getApps();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, apps);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bind.app.setAdapter((SpinnerAdapter) arrayAdapter);

        bind.backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    backUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void backUp() throws Exception {
        TaskInfoDialog dialog = new TaskInfoDialog(getContext());
        if (!dialog.isShowing()) {
            dialog.show();
        }
        EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"开始备份"));


        String selectedItem = (String) bind.app.getSelectedItem();
        String[] split = selectedItem.split("--");

        String pkgName = split[1];

        CommonRetrofitManager.getInstance().backUpApp(pkgName).subscribe(new Consumer<HttpResult>() {
            @Override
            public void accept(HttpResult result) throws Exception {
                if ("1".equals(result.getRet())) {
                    EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"备份完成"));
                }else{
                    EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"备份失败"));
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"备份失败"));
            }
        });


    }


    public List<String> getApps() {

        for (PackageInfo info : installedPackages) {
            ApplicationInfo appInfo = info.applicationInfo;
            //去除系统应用
            if (!filterApp(appInfo)) {
                continue;
            }
            //拿到应用程序的图标
//            Drawable icon = appInfo.loadIcon(packageManager);
            //拿到应用程序的程序名
            String appName = appInfo.loadLabel(packageManager).toString();
            //拿到应用程序的包名
            String packageName = appInfo.packageName;
            //拿到应用程序apk路径
//            String apkePath = appInfo.sourceDir;
            //获取应用程序启动意图
//            Intent intent = packageManager.getLaunchIntentForPackage(packageName);

            if (!"me.weishu.kernelsu".equals(packageName)){
                list.add(appName + "--" + packageName);
            }

        }
        return list;
    }

    public boolean filterApp(ApplicationInfo info) {
        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //判断是不是系统应用
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }

}
