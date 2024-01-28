package me.weishu.kernelsu.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.weishu.kernelsu.R;
import me.weishu.kernelsu.adapter.RootMangerAdapter;
import me.weishu.kernelsu.adapter.SetPropAdapter;
import me.weishu.kernelsu.bean.AppItem;
import me.weishu.kernelsu.bean.EventMessage;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.databinding.FragmentParametsBinding;
import me.weishu.kernelsu.dialog.TaskInfoDialog;

import me.weishu.kernelsu.utils.AppUtils;
import me.weishu.kernelsu.utils.EventCode;
import me.weishu.kernelsu.utils.NumberUtils;

public class SetPropFragment extends Fragment {

    private FragmentParametsBinding inflate;


    private TaskInfoDialog dialog;
    private SetPropAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_paramets, container, false);
        inflate = FragmentParametsBinding.bind(mRootView);
        EventBus.getDefault().register(this);
        initData();
        return mRootView;
    }


    private String[] androidVers = new String[]{"13", "12", "11", "10", "9", "8"};

    private void initData() {

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        inflate.appList.setLayoutManager(manager);

        List<AppItem> apps = AppUtils.getApps(getContext());

        adapter = new SetPropAdapter(getContext(),apps);
        inflate.appList.setAdapter(adapter);

        inflate.btnSaveParment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                dialog = new TaskInfoDialog(getContext());
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "开始改机"));
                String packageInfos ="";
                List<AppItem> list = adapter.getList();
                for (AppItem app : list) {

                    if (app.isCheck()) {
                        packageInfos+=app.getPackageName()+",";
                    }
                }


                if (TextUtils.isEmpty(packageInfos)) {
                    return;
                }

                int verIndex = NumberUtils.randNum(androidVers.length);
                String sdkVer = androidVers[verIndex];

                EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "修改参数"));
//                Disposable subscribe = CommonRetrofitManager.getInstance().modifyPhone(packageInfos, sdkVer).subscribe(new Consumer<HttpResult>() {
//                    @Override
//                    public void accept(HttpResult result) throws Exception {
//                        EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "参数修改成功"));
//                        if (!result.equals("err")) {
//                            EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "改机成功"));
//                            for (AppItem app : list) {
//                                app.setCheck(false);
//                                adapter.notifyDataSetChanged();
//                            }
//                        } else {
//                            EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "改机失败"));
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
//                        EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO, "改机失败"));
//                    }
//                });
            }
        });


    }

    private void showMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true,priority = 1)
    public void onReceiveMsg(EventMessage message) {
        if (message.getType()== EventCode.SLELECT_SET_PROP) {
            List<AppItem> apps = AppUtils.getApps(getContext());
            adapter.setList(apps);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消事件
        EventBus.getDefault().unregister(this);
    }
}
