package com.agrotrading.kancher.qubash.views;

import android.content.Context;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agrotrading.kancher.qubash.R;
import com.agrotrading.kancher.qubash.database.models.Quote;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.quote_list_item)
public class QuoteItemView extends RelativeLayout {

    @ViewById(R.id.id_text)
    TextView idText;

    @ViewById(R.id.html_text)
    TextView htmlText;

    public QuoteItemView(Context context) {
        super(context);
    }

    public void bind(Quote quote) {
        idText.setText(quote.getId());
        htmlText.setText(Html.fromHtml(quote.getHtml()));
    }
}
