package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.facebook.drawee.view.SimpleDraweeView;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.SettingActivity;

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
    //    @Bind(R.id.myhome_appBar)
//    AppBarLayout myhomeAppBar;
    @Bind(R.id.myhome_viewpager)
    ViewPager myhomeViewpager;
    @Bind(R.id.myhome_tab)
    TabLayout myhomeTab;
    //    @Bind(R.id.myhome_coolaosingtoorbar)
//    CollapsingToolbarLayout myhomeCoolaosingtoorbar;
    private FragmentCallBack fragmentCallBack;
    public MyHomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_home_test, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("我的主页");
        toolbar.inflateMenu(R.menu.menu_myhome);
        toolbar.setOnMenuItemClickListener(item -> {
            getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            return true;
        });
//        myhomeCoolaosingtoorbar.setTitle("我的主页");
        initView();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(BaseInfoFragment.newInstance("", ""));
        fragments.add(new RecordComfirmFragment());
        fragments.add(new WorkExperienceFragment());
        fragments.add(new StudentJudgeFragment());
        FragmentAdatper adatper = new FragmentAdatper(getActivity().getSupportFragmentManager(), fragments);
        myhomeViewpager.setAdapter(adatper);
        myhomeViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myhomeTab));
        myhomeTab.setupWithViewPager(myhomeViewpager);

        ViewTreeObserver observer = myhomeViewpager.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            ViewGroup.LayoutParams lp = myhomeViewpager.getLayoutParams();
            int hei = MeasureUtils.getTrueheight(getActivity()) - toolbar.getHeight() - myhomeTab.getHeight();
            lp.height = hei;
            myhomeViewpager.setLayoutParams(lp);
        });

        return view;
    }


    private void initView() {

    }
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            fragmentCallBack = (FragmentCallBack) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        fragmentCallBack = null;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class FragmentAdatper extends FragmentPagerAdapter {

        List<Fragment> fragments;

        public FragmentAdatper(FragmentManager fm, List<Fragment> fs) {
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
