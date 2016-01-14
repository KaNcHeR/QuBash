package com.agrotrading.kancher.qubash.views;

import android.content.Context;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agrotrading.kancher.qubash.R;
import com.agrotrading.kancher.qubash.database.models.Quote;
import com.agrotrading.kancher.qubash.utils.ConstantManager;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.quote_favorite_list_item)
public class QuoteFavoriteIemView extends RelativeLayout {

    @ViewById(R.id.text_id)
    TextView idText;

    @ViewById(R.id.text_html)
    TextView htmlText;

    public QuoteFavoriteIemView(Context context) {
        super(context);
    }

    public void bind(Quote quote) {
        idText.setText(ConstantManager.PREFIX_ID.concat(quote.getStringId()));
        htmlText.setText(Html.fromHtml(quote.getHtml()));

    }

}
