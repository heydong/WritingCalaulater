package com.myscript.atk.math.sample.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myscript.atk.math.sample.ui.CalculateFragment;
import com.myscript.atk.math.sample.ui.TabFragment;
import com.myscript.atk.math.sample.ui.WriteFragment;

/**
 * Created by hd_chen on 2016/8/15.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"手写计算器", "传统计算器"};
    private Context context;
    private TabFragment fragment1, fragment2;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new CalculateFragment();
        } else {
            return new WriteFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
