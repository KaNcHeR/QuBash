package com.agrotrading.kancher.qubash;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.agrotrading.kancher.qubash.adapters.QuotesAdapter;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.rest.RestService;
import com.agrotrading.kancher.qubash.rest.model.BashModel;
import com.agrotrading.kancher.qubash.utils.ConstantManager;
import com.agrotrading.kancher.qubash.utils.NetworkStatusChecker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public FragmentManager fragmentManager;

    @ViewById(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @ViewById(R.id.rv_quotes)
    RecyclerView quotesRecyclerView;

    @Bean
    QuotesAdapter quotesAdapter;

    @InstanceState
    int quantityQuotes = Integer.parseInt(ConstantManager.QUANTITY_QUOTES);

    @InstanceState
    boolean autoRequest = true;

    @AfterViews
    void ready() {
        fragmentManager = getSupportFragmentManager();
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        quotesRecyclerView.setLayoutManager(linearLayoutManager);
        quotesRecyclerView.addOnScrollListener(getListener());

    }

    @Override
    public void onRefresh() {

        request();
    }
    @Background
    void animation() {
        swipeRefreshLayout.setRefreshing(true);
    }
    @Background
    void request() {

        if(!NetworkStatusChecker.isNetworkAvailable(getApplicationContext())) {
            noConnection();
            return;
        }

        RestService restService = new RestService();
        ArrayList<BashModel> quotes = restService.getQuotes();
        ArrayList<Quote> notExists = new ArrayList<>();

        for(BashModel quote : quotes) {
            Quote newQuote = new Quote();
            newQuote.setId(Long.parseLong(quote.getLink().replace(ConstantManager.PARSE_ID, "")));
            newQuote.setHtml(quote.getElementPureHtml());
            if(!newQuote.exists()) {
                newQuote.save();
                notExists.add(newQuote);
            } else {
                break;
            }
        }

        if(notExists.size() == 0) {
            noNewQuotes();
            return;
        }

        quotesAdapter.addItemsStart(notExists);
        refresh(notExists.size());

    }

    @UiThread
    void noConnection() {
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(swipeRefreshLayout, getString(R.string.network_not_available), Snackbar.LENGTH_SHORT).show();
    }

    @UiThread
    void noNewQuotes() {
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(swipeRefreshLayout, getString(R.string.not_new_quotes), Snackbar.LENGTH_SHORT).show();
    }

    @UiThread
    void refresh(int quantityQuotes) {
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(swipeRefreshLayout, getString(R.string.new_quotes, quantityQuotes), Snackbar.LENGTH_SHORT).show();
        notifyInserted();
    }

    @UiThread
    void notifyInserted() {
        quotesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(0, quantityQuotes);
        if(autoRequest) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            onRefresh();
            autoRequest = false;
        }
    }


    private void loadData(final int offset, final int count){
        getSupportLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<List<Quote>>() {
            @Override
            public Loader<List<Quote>> onCreateLoader(int id, Bundle args) {
                final AsyncTaskLoader<List<Quote>> loader = new AsyncTaskLoader<List<Quote>>(getApplicationContext()) {
                    @Override
                    public List<Quote> loadInBackground() {
                        return Quote.getAllQuotes(offset, count);
                    }
                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<List<Quote>> loader, List<Quote> data) {

                if (data.size() == 0 && offset != 0) {
                    quotesRecyclerView.clearOnScrollListeners();
                    return;
                }

                if (quotesRecyclerView.getAdapter() != null) {
                    quotesAdapter.addItems(data);
                    quantityQuotes = quotesAdapter.getItemCount();
                } else {
                    quotesRecyclerView.setAdapter(quotesAdapter.addItems(data));
                }

                notifyInserted();
            }

            @Override
            public void onLoaderReset(Loader<List<Quote>> loader) {

            }
        });
    }

    private RecyclerView.OnScrollListener getListener() {

        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == 0) {
                    int positionLast = recyclerView.getLayoutManager()
                            .getPosition(
                                    recyclerView.getChildAt(
                                            recyclerView.getChildCount() - 1
                                    )
                            );
                    if(positionLast > quotesAdapter.getItemCount() - 5) {
                        loadData(quotesAdapter.getItemCount(), 10);
                    }
                }
            }
        };
    }

}