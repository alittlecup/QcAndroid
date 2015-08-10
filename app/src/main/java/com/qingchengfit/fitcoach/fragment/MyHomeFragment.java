package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingchengfit.fitcoach.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyHomeFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.myhome_header)
    SimpleDraweeView myhomeHeader;
    @Bind(R.id.myhome_gender)
    SimpleDraweeView myhomeGender;
    @Bind(R.id.myhome_appBar)
    AppBarLayout myhomeAppBar;
    @Bind(R.id.myhome_viewpager)
    ViewPager myhomeViewpager;
    @Bind(R.id.myhome_tab)
    TabLayout myhomeTab;

    public MyHomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_home, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("我的主页");
        initView();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(BaseInfoFragment.newInstance("",""));
        fragments.add(BaseInfoFragment.newInstance("",""));
        fragments.add(BaseInfoFragment.newInstance("",""));
        FragmentAdatper adatper = new FragmentAdatper(getActivity().getSupportFragmentManager(),fragments);
        myhomeViewpager.setAdapter(adatper);
        myhomeViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myhomeTab));
        myhomeTab.setupWithViewPager(myhomeViewpager);
        return view;
    }

    private void initView() {
        
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class FragmentAdatper extends FragmentPagerAdapter{

        List<Fragment>  fragments;

        public FragmentAdatper(FragmentManager fm,List<Fragment> fs) {
            super(fm);
            this.fragments = fs;
        }




        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "测试";
        }
    }


}
