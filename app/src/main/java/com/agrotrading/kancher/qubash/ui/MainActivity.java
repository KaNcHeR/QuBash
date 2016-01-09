package com.agrotrading.kancher.qubash.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.agrotrading.kancher.qubash.R;
import com.agrotrading.kancher.qubash.adapters.QuotesAdapter;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.rest.RestService;
import com.agrotrading.kancher.qubash.rest.model.BashModel;
import com.agrotrading.kancher.qubash.utils.ConstantManager;
import com.agrotrading.kancher.qubash.utils.NetworkStatusChecker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/*В этой ветке будут всяческие исправления, т.к. я забыл создать ветку с самого начала*/

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public FragmentManager fragmentManager;
    public RecyclerView.OnScrollListener scrollStateChangedListener, scrolledListener;

    @ViewById(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @ViewById(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    @ViewById(R.id.rv_quotes)
    RecyclerView quotesRecyclerView;

    @ViewById(R.id.fab)
    FloatingActionButton floatingActionButton;

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

        scrollStateChangedListener = getScrollStateChangedListener();
        scrolledListener = getScrolledListener();
        quotesRecyclerView.addOnScrollListener(scrollStateChangedListener);
        quotesRecyclerView.addOnScrollListener(scrolledListener);

    }

    @Click(R.id.fab)
    void scrollToTop() {
        quotesRecyclerView.smoothScrollToPosition(0);
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
            showSnackbar(getString(R.string.network_not_available));
            return;
        }

        ArrayList<BashModel> quotes;

        RestService restService = new RestService();
        try {
            quotes = restService.getQuotes();
        } catch (RetrofitError e) {
            showSnackbar(getString(R.string.retrofit_error));
            return;
        }

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
            showSnackbar(getString(R.string.not_new_quotes));
            return;
        }

        quotesAdapter.addItemsStart(notExists);
        showSnackbar(getString(R.string.new_quotes, notExists.size()));
        notifyInserted();

    }

    @UiThread
    void showSnackbar(String message) {
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @UiThread
    void notifyInserted() {
        quotesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(0, quantityQuotes);
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
                    quotesRecyclerView.removeOnScrollListener(scrollStateChangedListener);
                    return;
                }

                if (quotesRecyclerView.getAdapter() != null) {
                    quotesAdapter.addItems(data);
                    quantityQuotes = quotesAdapter.getItemCount();
                } else {
                    quotesRecyclerView.setAdapter(quotesAdapter.addItems(data));
                }

                notifyInserted();

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
                    if(positionLast + 1 > quotesAdapter.getItemCount() - ConstantManager.QUANTITY_ITEMS_TO_LOAD ) {
                        loadData(quotesAdapter.getItemCount(), ConstantManager.QUANTITY_OF_SUBSEQUENT_ITEMS);
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