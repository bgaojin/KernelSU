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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


import me.weishu.kernelsu.R;
import me.weishu.kernelsu.bean.AppItem;
import me.weishu.kernelsu.bean.EventMessage;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.databinding.FragmentBackupBinding;
import me.weishu.kernelsu.dialog.TaskInfoDialog;

import me.weishu.kernelsu.utils.ApiUtils;
import me.weishu.kernelsu.utils.AppUtils;
import me.weishu.kernelsu.utils.EventCode;
//import me.weishu.kernelsu.utils.GsonUtils;

public class BackUpFragment extends Fragment {


    private List<String> list;

    private FragmentBackupBinding bind;
    private ArrayAdapter arrayAdapter;

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
        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bind.app.setAdapter((SpinnerAdapter) arrayAdapter);
        String[] a = new String[4];
        System.out.println(a.length);
        getApps();


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

    private void initData() {
        EventBus.getDefault().register(this);
    }

    private void getApps() {
        list.clear();
        List<AppItem> apps = AppUtils.getApps(getContext());
        for (AppItem app : apps) {
            String appName = app.getAppName();
            String packageName = app.getPackageName();
            list.add(appName + "--" + packageName);
        }
        arrayAdapter.notifyDataSetChanged();
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

//        CommonRetrofitManager.getInstance().backUpApp(pkgName).subscribe(new Consumer<HttpResult>() {
//            @Override
//            public void accept(HttpResult result) throws Exception {
//                if ("1".equals(result.getRet())) {
//                    EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"备份完成"));
//                }else{
//                    EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"备份失败"));
//                }
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                throwable.printStackTrace();
//                EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"备份失败"));
//            }
//        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true,priority = 1)
    public void onReceiveMsg(EventMessage message) {
        if (message.getType()== EventCode.SLELECT_BACKUP) {
            getApps();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消事件
        EventBus.getDefault().unregister(this);
    }
}
