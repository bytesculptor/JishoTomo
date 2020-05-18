package net.emojiparty.android.jishotomo.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import net.emojiparty.android.jishotomo.BR;

import java.util.ArrayList;
import java.util.List;

public class DataBindingAdapter extends RecyclerView.Adapter {
    private List<?> items = new ArrayList<>();
    private int layoutId;

    public DataBindingAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    public void setItems(List<?> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewDataBinding binding =
                DataBindingUtil.inflate(layoutInflater, layoutId, parent, false);
        return new DataBindingViewHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataBindingViewHolder viewHolder = (DataBindingViewHolder) holder;
        viewHolder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // https://medium.com/google-developers/android-data-binding-recyclerview-db7c40d9f0e4
    static class DataBindingViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;
        Context context;

        DataBindingViewHolder(ViewDataBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        void bind(Object presenter) {
            binding.setVariable(BR.presenter, presenter);
            binding.executePendingBindings();
        }
    }
}
