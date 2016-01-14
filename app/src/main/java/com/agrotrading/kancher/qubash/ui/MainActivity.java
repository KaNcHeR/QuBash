package com.agrotrading.kancher.qubash.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.agrotrading.kancher.qubash.R;
import com.agrotrading.kancher.qubash.adapters.SectionsPagerAdapter;
import com.agrotrading.kancher.qubash.ui.fragments.AllQuotesFragment_;
import com.agrotrading.kancher.qubash.ui.fragments.FavoriteQuotesFragment_;
import com.agrotrading.kancher.qubash.utils.ConstantManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public SectionsPagerAdapter mSectionsPagerAdapter;
    public AllQuotesFragment_ allQuotesFragment;
    public FavoriteQuotesFragment_ favoriteQuotesFragment;

    @ViewById(R.id.container)
    public ViewPager mViewPager;

    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @AfterViews
    void ready() {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        allQuotesFragment = (AllQuotesFragment_) getCurrentPagerFragment(ConstantManager.POSITION_FRAGMENT_ALL_QUOTES);
        favoriteQuotesFragment = (FavoriteQuotesFragment_) getCurrentPagerFragment(ConstantManager.POSITION_FRAGMENT_FAVORITE_QUOTES);

    }

    public Fragment getCurrentPagerFragment(int position) {
        String fragmentTag = "android:switcher:" + mViewPager.getId() + ":" + position;
        return getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }



}