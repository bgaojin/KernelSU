package me.weishu.kernelsu.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.weishu.kernelsu.Natives;
import me.weishu.kernelsu.R;
import me.weishu.kernelsu.bean.EventMessage;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.databinding.FragmentParametsBinding;
import me.weishu.kernelsu.dialog.TaskInfoDialog;
import me.weishu.kernelsu.net.CommonRetrofitManager;
import me.weishu.kernelsu.net.HttpUtils;
import me.weishu.kernelsu.utils.ApiUtils;
import me.weishu.kernelsu.utils.EventCode;
import me.weishu.kernelsu.utils.NumberUtils;
import me.weishu.kernelsu.utils.SpUtils;

public class ParametsFragment extends Fragment {

    private FragmentParametsBinding mainBinding;

    private PackageManager packageManager;
    private ArrayList list;
    private TaskInfoDialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_paramets, container, false);
        mainBinding = FragmentParametsBinding.bind(mRootView);
        initData();
        return mRootView;
    }

    public boolean filterApp(ApplicationInfo info) {

        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //判断是不是系统应用
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        } else if (info.packageName.equals("com.lin.oneghost")) {
            return false;
        }
        return false;
    }

    private List<PackageInfo> installedPackages;

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
//            AppItem appItem = new AppItem(appName, packageName);
            if (!"me.weishu.kernelsu".equals(packageName)) {
                list.add(appName + "--" + packageName);
            }

        }
        return list;
    }

    private static String[] androidVers = new String[]{"13", "12", "11", "10", "9", "8"};

    private void initData() {

        list = new ArrayList<>();
        packageManager = getActivity().getPackageManager();
        // 获取已安装的应用程序列表
        installedPackages = packageManager.getInstalledPackages(0);
        List<String> apps = getApps();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, apps);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainBinding.app.setAdapter((SpinnerAdapter) arrayAdapter);

        mainBinding.btnSaveParment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                dialog = new TaskInfoDialog(getContext());
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "开始改机"));
                String packageInfos = (String) mainBinding.app.getSelectedItem();
                if (TextUtils.isEmpty(packageInfos)) {
                    return;
                }
                String[] split = packageInfos.split("--");
                String packageName = split[1];
                int verIndex = NumberUtils.randNum(androidVers.length);
                String sdkVer = androidVers[verIndex];
                String url = ApiUtils.BASE_URL+"/app/v1/modifyPhone?destPackageInfos=" + packageName + "&sdkVersion=" + sdkVer;
                EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "修改参数"));
                CommonRetrofitManager.getInstance().modifyPhone(packageName,sdkVer).subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult result) throws Exception {
                        EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "参数修改成功"));
                        if (!result.equals("err")) {
                            EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "改机成功"));
                        } else {
                            EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "改机失败"));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "改机失败"));
                    }
                });
//                HttpUtils.requestGet(url, new HttpUtils.RequestListener() {
//                    @Override
//                    public void onSubscribe() {
//
//                    }
//
//                    @Override
//                    public void onSuccess(String result) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onFailed() {
//                        EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "改机失败"));
//                    }
//                });


            }
        });

        mainBinding.setroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageInfos = (String) mainBinding.app.getSelectedItem();
                if (TextUtils.isEmpty(packageInfos)) {
                    return;
                }
                String[] split = packageInfos.split("--");
                String packageName = split[1];
                try {
                    ApplicationInfo applicationInfo = getContext().getPackageManager().getApplicationInfo(packageName, 0);
                    int uid = applicationInfo.uid;
                    Natives.Profile profile = Natives.INSTANCE.getAppProfile(packageInfos, uid);

                    Natives.Profile copy = profile.copy(packageName,profile.getCurrentUid(),
                            true,true,null,
                            0,0,new ArrayList<>(),new ArrayList<>(),
                            "u:r:su:s0",0,true,
                            true,"");
                    boolean result = Natives.INSTANCE.setAppProfile(copy);

                    if (result){
                        showMsg("设置成功");
                    }else{
                        showMsg("设置失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
