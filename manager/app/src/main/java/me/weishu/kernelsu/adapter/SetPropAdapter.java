package me.weishu.kernelsu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.weishu.kernelsu.bean.AppItem;
import me.weishu.kernelsu.databinding.ItemRootMangerBinding;
import me.weishu.kernelsu.databinding.ItemSetPropBinding;

public class SetPropAdapter extends RecyclerView.Adapter<SetPropAdapter.ViewHolder> {
    private Context context;
    private List<AppItem> list;

    public SetPropAdapter(Context context, List<AppItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SetPropAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSetPropBinding inflate = ItemSetPropBinding.inflate(LayoutInflater.from(this.context));
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SetPropAdapter.ViewHolder holder, int position) {
        AppItem appItem = list.get(position);
        holder.appName.setText(appItem.getAppName());
        holder.appIcon.setImageDrawable(appItem.getAppIcon());
        holder.appState.setText(appItem.getRootState());
        holder.checkBox.setChecked(appItem.isCheck());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appItem.setCheck(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        ImageView appIcon;

        TextView appName;

        TextView appState;

        ViewHolder(ItemSetPropBinding view) {
            super(view.getRoot());
            checkBox = view.checkBox;
            appIcon = view.appIcon;
            appName = view.appName;
            appState = view.appState;
        }
    }
}
