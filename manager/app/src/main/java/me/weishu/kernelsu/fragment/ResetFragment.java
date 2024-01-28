package me.weishu.kernelsu.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.ShellUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import io.reactivex.functions.Consumer;
import me.weishu.kernelsu.R;
import me.weishu.kernelsu.adapter.RestoreAdapter;
import me.weishu.kernelsu.bean.EventMessage;
import me.weishu.kernelsu.bean.HttpResult;
import me.weishu.kernelsu.databinding.FragmentResetBinding;
import me.weishu.kernelsu.dialog.TaskInfoDialog;

import me.weishu.kernelsu.net.CommonRetrofitManager;
import me.weishu.kernelsu.ui.util.KsuCli;
import me.weishu.kernelsu.utils.ApiUtils;
import me.weishu.kernelsu.utils.AppUtils;
import me.weishu.kernelsu.utils.EventCode;
import me.weishu.kernelsu.utils.FileMetadata;
import me.weishu.kernelsu.utils.FileUtils;
//import me.weishu.kernelsu.utils.GsonUtils;
import me.weishu.kernelsu.utils.TarBackupReader;


public class ResetFragment extends Fragment {

    private FragmentResetBinding bind;

    private RestoreAdapter restoreAdapter;
    private ArrayList<String> fileNames;
    private String backPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/backupData";
    private ArrayList<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_reset, container, false);
        bind = FragmentResetBinding.bind(mRootView);
        initData();
        initView();
        EventBus.getDefault().register(this);

        return mRootView;
    }

    private void initView() {

    }

    private void initData() {
        fileNames = new ArrayList<>();
        list = new ArrayList<>();
        restoreAdapter = new RestoreAdapter(getContext(), fileNames);
        bind.listview.setAdapter(restoreAdapter);
        bind.listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        bind.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String fileName = fileNames.get(i);
                    if (TextUtils.isEmpty(fileName)){
                        return;
                    }
                    resetApp(fileName.split(":")[1]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true,priority = 1)
    public void onReceiveMsg(EventMessage message) {
        if (message.getType()== EventCode.SLELECT_REST){
            list.clear();

            File files = new File(backPath);
            if (!files.exists()) {
                files.mkdirs();
            }

            String[] strFileNames = files.list();
            if (strFileNames==null){
                return;
            }

            for (int i = 0; i < strFileNames.length; i++) {
                try {
                    String s = strFileNames[i];
                    String pkgName = readHeader(s);
                    String appName = AppUtils.getAppName(getContext(), pkgName);
                    if (!TextUtils.isEmpty(appName)) {
                        s = appName + ":" + s;
                    }
                    list.add(i, s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            restoreAdapter.setList(list);
        }

    }
    /***
     * 根据文件读取包名
     * @param fileName
     * @throws Exception
     */
    private String readHeader(String fileName) throws Exception {
        String packageName = "";
        ParcelFileDescriptor fileDescriptor = FileUtils.getFileDescriptor(backPath, fileName);

        FileInputStream rawInStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        InputStream inputStream = TarBackupReader.parseBackupFileHeaderAndReturnTarStream(rawInStream, "");

        TarBackupReader tarBackupReader = new TarBackupReader(inputStream);

        FileMetadata fileMetadata = tarBackupReader.readTarHeaders();
        if (fileMetadata != null) {
            packageName = fileMetadata.packageName;
        }
        return packageName;
    }


    private void resetApp(String fileName) throws Exception {
        TaskInfoDialog dialog = new TaskInfoDialog(getContext());

        if (!dialog.isShowing()) {
            dialog.show();
        }

        EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"开始还原"));

        CommonRetrofitManager.getInstance().restApp("com.mmbox.xbrowser",fileName).subscribe(new Consumer<HttpResult>() {
            @Override
            public void accept(HttpResult result) throws Exception {
                if ("1".equals(result.getRet())) {
                    EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"还原完成"));
                }else{
                    EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"还原失败"));
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                EventBus.getDefault().post(new EventMessage(EventCode.SET_TASK_INFO,"还原失败"));
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消事件
        EventBus.getDefault().unregister(this);
    }
}
