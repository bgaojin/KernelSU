package me.weishu.kernelsu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.weishu.kernelsu.bean.AppItem;
import me.weishu.kernelsu.databinding.ItemSetPropBinding;

public class SetPropAdapter extends RecyclerView.Adapter<SetPropAdapter.ViewHolder> {
    private Context context;
    private List<AppItem> list;

    public SetPropAdapter(Context context, List<AppItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<AppItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<AppItem> getList() {
        return list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSetPropBinding inflate = ItemSetPropBinding.inflate(LayoutInflater.from(this.context));
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppItem appItem = list.get(position);
        holder.appName.setText(appItem.getAppName());
        holder.appIcon.setImageDrawable(appItem.getAppIcon());
//        holder.appState.setText(appItem.getRootState());
        holder.checkBox.setChecked(appItem.isCheck());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appItem.setCheck(b);
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.checkBox.setChecked(!holder.checkBox.isChecked());
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
        ConstraintLayout parent;
        ViewHolder(ItemSetPropBinding view) {
            super(view.getRoot());
            checkBox = view.checkBox;
            appIcon = view.appIcon;
            appName = view.appName;
            appState = view.appState;
            parent = view.parent;
        }
    }
}
