package com.agrotrading.kancher.qubash.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.agrotrading.kancher.qubash.R;
import com.agrotrading.kancher.qubash.adapters.QuotesAdapter;
import com.agrotrading.kancher.qubash.adapters.QuotesFavoriteAdapter;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.utils.ConstantManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_favorite_quotes)
public class FavoriteQuotesFragment extends Fragment {

    public RecyclerView.OnScrollListener scrollStateChangedListener, scrolledListener;

    @ViewById(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    @ViewById(R.id.rv_quotes)
    public RecyclerView quotesRecyclerView;

    @ViewById(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Bean
    QuotesFavoriteAdapter quotesFavoriteAdapter;

    @InstanceState
    int quantityQuotes = Integer.parseInt(ConstantManager.QUANTITY_QUOTES);

    @AfterViews
    void ready() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        quotesRecyclerView.setLayoutManager(linearLayoutManager);

        scrollStateChangedListener = getScrollStateChangedListener();
        scrolledListener = getScrolledListener();
        quotesRecyclerView.addOnScrollListener(scrollStateChangedListener);
        quotesRecyclerView.addOnScrollListener(scrolledListener);

    }

    @Click(R.id.fab)
    void scrollToTop() {
        quotesRecyclerView.smoothScrollToPosition(0);
    }


    @UiThread
    void notifyInserted() {
        quotesFavoriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(0, quantityQuotes);
    }


    public void addQuote(Quote quote) {
        quotesFavoriteAdapter.addItem(quote);
    }

    private void loadData(final int offset, final int count){
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<List<Quote>>() {
            @Override
            public Loader<List<Quote>> onCreateLoader(int id, Bundle args) {
                final AsyncTaskLoader<List<Quote>> loader = new AsyncTaskLoader<List<Quote>>(getActivity()) {
                    @Override
                    public List<Quote> loadInBackground() {
                        return Quote.getAllFavoriteQuotes(offset, count);
                    }
                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<List<Quote>> loader, List<Quote> data) {

                if (data.size() == 0 && offset != 0) {
                    quotesRecyclerView.removeOnScrollListener(scrollStateChangedListener);
                    return;
                }

                if (quotesRecyclerView.getAdapter() != null) {
                    quotesFavoriteAdapter.addItems(data);
                    quantityQuotes = quotesFavoriteAdapter.getItemCount();
                } else {
                    quotesRecyclerView.setAdapter(quotesFavoriteAdapter.addItems(data));
                }

                notifyInserted();

            }

            @Override
            public void onLoaderReset(Loader<List<Quote>> loader) {
            }
        });
    }

    private RecyclerView.OnScrollListener getScrollStateChangedListener() {
        return new RecyclerView.OnScrollListener() {
            int positionLast;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == 0) {
                    positionLast = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1));
                    if(positionLast + 1 > quotesFavoriteAdapter.getItemCount() - ConstantManager.QUANTITY_ITEMS_TO_LOAD ) {
                        loadData(quotesFavoriteAdapter.getItemCount(), ConstantManager.QUANTITY_OF_SUBSEQUENT_ITEMS);
                    }
                }
            }
        };
    }

    private RecyclerView.OnScrollListener getScrolledListener() {
        return new RecyclerView.OnScrollListener() {
            int positionFirst;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                positionFirst = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0));
                if(positionFirst == 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                }
            }
        };
    }

}
