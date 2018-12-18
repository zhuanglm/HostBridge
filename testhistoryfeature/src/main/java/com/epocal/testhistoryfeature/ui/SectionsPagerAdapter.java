package com.epocal.testhistoryfeature.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class maps a fragment to the position of page (tab)
 * when used with TabLayout integrated with ViewPager layout.
 *
 * @since 2018-10-06
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String>   mFragmentTitleList = new ArrayList<>();

    SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    void addFragment(Fragment frag, String title) {
        mFragmentList.add(frag);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
