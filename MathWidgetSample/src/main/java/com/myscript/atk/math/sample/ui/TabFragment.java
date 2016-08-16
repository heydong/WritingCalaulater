package com.myscript.atk.math.sample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myscript.atk.math.sample.adapter.PagerAdapter;
import com.myscript.atk.math.sample.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hd_chen on 2016/8/15.
 */
public class TabFragment extends Fragment {
    @Bind(R.id.history_viewpager)
    ViewPager viewPager;
    @Bind(R.id.history_sliding_tabs)
    TabLayout tabLayout;

    private View view;

    private FragmentManager fm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return view;
        }
        view = inflater.inflate(R.layout.switch_tab, container, false);
        ButterKnife.bind(this, view);
        PagerAdapter pagerAdapter = new PagerAdapter(fm, this.getActivity());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }
}
