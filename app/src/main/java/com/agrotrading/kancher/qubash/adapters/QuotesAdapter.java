package com.agrotrading.kancher.qubash.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.agrotrading.kancher.qubash.ViewWrapper;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.views.QuoteItemView;
import com.agrotrading.kancher.qubash.views.QuoteItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class QuotesAdapter extends RecyclerViewAdapterBase<Quote, QuoteItemView> {

    @RootContext
    Context context;

    @Override
    public void onBindViewHolder(ViewWrapper<QuoteItemView> holder, int position) {
        QuoteItemView view = holder.getView();
        Quote quotes = items.get(position);
        view.bind(quotes);
    }

    @Override
    protected QuoteItemView onCreateItemView(ViewGroup parent, int viewType) {
        return QuoteItemView_.build(parent.getContext());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
