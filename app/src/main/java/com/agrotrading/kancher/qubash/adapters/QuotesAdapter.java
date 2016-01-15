package com.agrotrading.kancher.qubash.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.agrotrading.kancher.qubash.ViewWrapper;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.views.QuoteItemView;
import com.agrotrading.kancher.qubash.views.QuoteItemView_;

import org.androidannotations.annotations.EBean;

@EBean
public class QuotesAdapter extends RecyclerViewAdapterBase<Quote, QuoteItemView> {

    @Override
    public void onBindViewHolder(ViewWrapper<QuoteItemView> holder, int position) {
        QuoteItemView view = holder.getView();
        Quote quote = items.get(position);
        view.bind(quote, position);
    }

    @Override
    protected QuoteItemView onCreateItemView(ViewGroup parent, int viewType) {
        return QuoteItemView_.build(parent.getContext());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public int findItem(long id) {

        int position = -1;

        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getId() == id) {
                position = i;
                break;
            }
        }

        return position;
    }
}
