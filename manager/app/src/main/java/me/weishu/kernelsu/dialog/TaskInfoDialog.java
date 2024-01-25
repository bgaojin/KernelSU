package me.weishu.kernelsu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import me.weishu.kernelsu.R;
import me.weishu.kernelsu.adapter.TaskAdapter;
import me.weishu.kernelsu.bean.EventMessage;
import me.weishu.kernelsu.utils.EventCode;

public class TaskInfoDialog extends Dialog {

    private ListView listView;
    private Button ok;
    private StringBuffer stringBuffer;
    private ArrayList<String> list;
    private TaskAdapter adapter;

    public TaskInfoDialog(@NonNull Context context) {
        super(context);
    }

    public TaskInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_task_info);
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
        listView = findViewById(R.id.list_view);
        adapter = new TaskAdapter(getContext(),list);
        listView.setAdapter(adapter);
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true,priority = 1)
    public void onReceiveMsg(EventMessage message) {
        if (message.getType()== EventCode.SET_TASK_INFO){
            list.add(message.getMessage());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }
}
