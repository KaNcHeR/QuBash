package com.agrotrading.kancher.qubash.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.agrotrading.kancher.qubash.R;
import com.agrotrading.kancher.qubash.ui.fragments.AllQuotesFragment_;
import com.agrotrading.kancher.qubash.ui.fragments.FavoriteQuotesFragment_;
import com.agrotrading.kancher.qubash.utils.ConstantManager;

import static com.agrotrading.kancher.qubash.utils.ConstantManager.POSITION_FRAGMENT_ALL_QUOTES;
import static com.agrotrading.kancher.qubash.utils.ConstantManager.POSITION_FRAGMENT_FAVORITE_QUOTES;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Resources resources;
    private AllQuotesFragment_ allQuotesFragment;
    private FavoriteQuotesFragment_ favoriteQuotesFragment;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        resources = context.getResources();
        allQuotesFragment = new AllQuotesFragment_();
        favoriteQuotesFragment = new FavoriteQuotesFragment_();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case POSITION_FRAGMENT_ALL_QUOTES:
                return allQuotesFragment;
            case POSITION_FRAGMENT_FAVORITE_QUOTES:
                return favoriteQuotesFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return ConstantManager.COUNT_FRAGMENTS_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case POSITION_FRAGMENT_ALL_QUOTES:
                return resources.getString(R.string.all_quotes);
            case POSITION_FRAGMENT_FAVORITE_QUOTES:
                return resources.getString(R.string.favorite_quotes);
        }
        return null;
    }

}
