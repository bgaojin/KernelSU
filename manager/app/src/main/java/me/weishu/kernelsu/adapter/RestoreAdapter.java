package me.weishu.kernelsu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;

import me.weishu.kernelsu.R;
import me.weishu.kernelsu.databinding.ItemRestoreBinding;

public class RestoreAdapter extends BaseAdapter {
    private Context context;

    private ItemRestoreBinding inflate;

    private List<String> list;
    private int checkID;
    // 用于记录每个RadioButton的状态，并保证只可选一个
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();

    public RestoreAdapter(Context paramContext, List<String> paramArrayList) {
        this.context = paramContext;
        this.list = paramArrayList;
    }

    public void setCheck(int i) {
        checkID = i;
        states.put(String.valueOf(i), true);
        notifyDataSetChanged();
    }

    public int getCheckID() {
        return checkID;
    }

    public void setList(List<String> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int paramInt) {
        return null;
    }

    public long getItemId(int paramInt) {
        return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        ViewHolder holder = null;
        if (paramView == null) {
            ItemRestoreBinding itemRestoreBinding = ItemRestoreBinding.inflate(LayoutInflater.from(this.context));
            this.inflate = itemRestoreBinding;
            paramView = itemRestoreBinding.getRoot();
            holder = new ViewHolder(paramView);
            paramView.setTag(holder);
        } else {
            holder = (ViewHolder) paramView.getTag();
        }
        String item = list.get(paramInt);

        final RadioButton radio = (RadioButton) paramView.findViewById(R.id.rb_btn);
        holder.rb_state = radio;
        holder.rb_state.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // 重置，确保最多只有一项被选中
                for (String key : states.keySet()) {
                    states.put(key, false);

                }
                states.put(String.valueOf(paramInt), radio.isChecked());
                notifyDataSetChanged();
            }
        });
        boolean res = false;
        if (states.get(String.valueOf(paramInt)) == null
                || states.get(String.valueOf(paramInt)) == false) {
            res = false;
            states.put(String.valueOf(paramInt), false);
        } else
            res = true;

        holder.rb_state.setChecked(res);

        holder.saveID.setText(item);
        return paramView;
    }


    static class ViewHolder {
        TextView saveID;
        RadioButton rb_state;

        ViewHolder(View param1View) {
            this.saveID = (TextView) param1View.findViewById(R.id.saveid);
            this.rb_state = (RadioButton) param1View.findViewById(R.id.rb_btn);
        }
    }
}