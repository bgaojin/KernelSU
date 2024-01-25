package me.weishu.kernelsu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.weishu.kernelsu.R;
import me.weishu.kernelsu.databinding.ItemTaskBinding;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private ItemTaskBinding itemRestoreBinding;

    public TaskAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            itemRestoreBinding = ItemTaskBinding.inflate(LayoutInflater.from(this.context));
            view = itemRestoreBinding.getRoot();
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String item = list.get(i);
        holder.info.setText(item);
        return view;
    }

    static class ViewHolder {
        TextView info;

        ViewHolder(View param1View) {
            info = (TextView) param1View.findViewById(R.id.info);
        }
    }
}
