package com.agrotrading.kancher.qubash.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.agrotrading.kancher.qubash.ViewWrapper;

import java.util.List;

public abstract class RecyclerViewAdapterBase<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected List<T> items;

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    public RecyclerViewAdapterBase setItems(List<T> items) {
        this.items = items;
        return this;
    }

    public void addItemsStart(List<T> items) {
        this.items.addAll(0, items);
    }

    public RecyclerViewAdapterBase addItems(List<T> items) {
        if(this.items == null){
            return setItems(items);
        }

        this.items.addAll(items);
        return this;
    }

    public void addItem(T item) {
        items.add(0, item);
        notifyDataSetChanged();
    }

    public void setItem(int position, T item) {
        items.set(position, item);
    }

}
