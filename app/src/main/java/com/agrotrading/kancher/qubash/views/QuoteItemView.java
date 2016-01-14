package com.agrotrading.kancher.qubash.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agrotrading.kancher.qubash.R;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.ui.MainActivity;
import com.agrotrading.kancher.qubash.utils.ConstantManager;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.quote_list_item)
public class QuoteItemView extends RelativeLayout {

    private Quote quote;
    private int position;
    private MainActivity mainActivity;

    @ViewById(R.id.text_id)
    TextView idText;

    @ViewById(R.id.text_html)
    TextView htmlText;

    @ViewById(R.id.button_favorites)
    Button favoritesButton;

    @Click(R.id.button_favorites)
    void actionButtonFavorites() {
        addToFavorites();
    }

    public QuoteItemView(Context context) {
        super(context);
        mainActivity = (MainActivity) context;
    }

    public void bind(Quote quote, int position) {

        this.quote = quote;
        this.position = position;

        idText.setText(ConstantManager.PREFIX_ID.concat(quote.getStringId()));
        htmlText.setText(Html.fromHtml(quote.getHtml()));

        if(quote.isFavorites()) {
            favoritesButton.setEnabled(false);
            favoritesButton.setText(R.string.already_in_favorites);
        } else {
            favoritesButton.setEnabled(true);
            favoritesButton.setText(R.string.add_to_favorites);
        }
    }

    public void addToFavorites() {
        mainActivity.getSupportLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<Void>() {
            @Override
            public Loader<Void> onCreateLoader(int id, Bundle args) {
                final AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(mainActivity) {
                    @Override
                    public Void loadInBackground() {
                        Quote.addToFavorites(quote.getId());
                        return null;
                    }
                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Void> loader, Void data) {
                reviewCard();
            }

            @Override
            public void onLoaderReset(Loader<Void> loader) {
            }

        });
    }


    void reviewCard() {

        favoritesButton.setEnabled(false);
        favoritesButton.setText(R.string.already_in_favorites);
        quote.setFavorites(true);

        mainActivity.allQuotesFragment.changeAdapter(quote, position);
        mainActivity.favoriteQuotesFragment.addQuote(quote);

    }

}
