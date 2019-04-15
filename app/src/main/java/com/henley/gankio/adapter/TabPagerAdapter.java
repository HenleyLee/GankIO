package com.henley.gankio.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * ViewPager适配器
 *
 * @author Henley
 * @date 2018/7/9 14:18
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private List<String> mTiltes;
    private List<? extends Fragment> mFragments;

    public TabPagerAdapter(FragmentManager fm, List<String> tiltes, List<? extends Fragment> fragments) {
        super(fm);
        mTiltes = tiltes;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTiltes != null ? mTiltes.get(position) : null;
    }

}
