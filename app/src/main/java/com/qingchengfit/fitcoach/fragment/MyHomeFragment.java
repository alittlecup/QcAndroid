package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ShareUtils;
import com.qingchengfit.fitcoach.activity.SettingActivity;
import com.qingchengfit.fitcoach.component.HalfScrollView;

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
    //    @Bind(R.id.myhome_header)
//    SimpleDraweeView myhomeHeader;
//    @Bind(R.id.myhome_gender)
//    SimpleDraweeView myhomeGender;
    //    @Bind(R.id.myhome_appBar)
//    AppBarLayout myhomeAppBar;
    @Bind(R.id.myhome_viewpager)
    ViewPager myhomeViewpager;
    @Bind(R.id.myhome_tab)
    TabLayout myhomeTab;
    @Bind(R.id.myhome_name)
    TextView myhomeName;
    @Bind(R.id.myhome_brief)
    TextView myhomeBrief;
    @Bind(R.id.myhome_student_judge)
    FrameLayout myhomeStudentJudge;
    @Bind(R.id.myhome_scroller)
    HalfScrollView myhomeScroller;
    @Bind(R.id.myhome_bg)
    SimpleDraweeView myhomeBg;
    private int mHomeBgHeight = 1;
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
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_myhome_settings)
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            else if (item.getItemId() == R.id.action_myhome_share)
                ShareUtils.oneKeyShared(getActivity());
            return true;
        });
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(BaseInfoFragment.newInstance("", ""));
        fragments.add(new RecordComfirmFragment());
        fragments.add(new WorkExperienceFragment());
        fragments.add(new StudentJudgeFragment());
        FragmentAdatper adatper = new FragmentAdatper(getChildFragmentManager(), fragments);
        myhomeViewpager.setAdapter(adatper);
        myhomeViewpager.setOffscreenPageLimit(4);
        myhomeViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myhomeTab));
        myhomeTab.setupWithViewPager(myhomeViewpager);
        ViewTreeObserver observer = myhomeViewpager.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams lp = myhomeViewpager.getLayoutParams();
                int hei = MeasureUtils.getTrueheight(getActivity()) - toolbar.getHeight() - myhomeTab.getHeight();
                lp.height = hei;
                myhomeViewpager.setLayoutParams(lp);
                mHomeBgHeight = myhomeBg.getHeight();
                myhomeViewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        myhomeScroller.setListener(new HalfScrollView.HalfViewListener() {
            @Override
            public void onScroll(int i) {
                if (i > mHomeBgHeight)
                    i = mHomeBgHeight;
                toolbar.setBackgroundColor(Color.argb(255 * i / mHomeBgHeight, 32, 191, 189));
            }
        });
        getChildFragmentManager().beginTransaction().add(R.id.myhome_student_judge, new StudentJudgeFragment(), "").commit();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public interface TouchListener {
        public void onTouchEvent(MotionEvent event);
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
            switch (position) {
                case 0:
                    return "基本信息";
                case 1:
                    return "资质认证";
                case 2:
                    return "工作经历";
                case 3:
                    return "学员评价";
                default:
                    return "";
            }
        }
    }
}
