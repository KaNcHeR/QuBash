package com.agrotrading.kancher.qubash.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.agrotrading.kancher.qubash.ViewWrapper;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.views.QuoteFavoriteIemView;
import com.agrotrading.kancher.qubash.views.QuoteFavoriteIemView_;

import org.androidannotations.annotations.EBean;

@EBean
public class QuotesFavoriteAdapter extends RecyclerViewAdapterBase<Quote, QuoteFavoriteIemView> {

    @Override
    public void onBindViewHolder(ViewWrapper<QuoteFavoriteIemView> holder, int position) {
        QuoteFavoriteIemView view = holder.getView();
        Quote quote = items.get(position);
        view.bind(quote, position);
    }

    @Override
    protected QuoteFavoriteIemView onCreateItemView(ViewGroup parent, int viewType) {
        return QuoteFavoriteIemView_.build(parent.getContext());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}