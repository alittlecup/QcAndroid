package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.component.GlideCircleTransform;
import com.paper.paperbaselibrary.utils.MeasureUtils;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ShareUtils;
import com.qingchengfit.fitcoach.activity.SettingActivity;
import com.qingchengfit.fitcoach.component.HalfScrollView;
import com.qingchengfit.fitcoach.component.MyhomeViewPager;
import com.qingchengfit.fitcoach.http.bean.User;

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
    ImageView myhomeHeader;
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
    ImageView myhomeBg;
    @Bind(R.id.myhome_gender)
    ImageView myhomeGender;
    @Bind(R.id.myhome_location)
    TextView myhomeLocation;
    @Bind(R.id.myhome_sawtooth)
    ImageView myhomeSawtooth;
    @Bind(R.id.myhome_tab_layout)
    RelativeLayout myhomeTabLayout;
    @Bind(R.id.myhome_viewpager)
    MyhomeViewPager myhomeViewpager;
    @Bind(R.id.halfscroll_first)
    LinearLayout halfscrollFirst;
    private int mHomeBgHeight = 1;

    private User user;
    private Gson gson;
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
        gson = new Gson();
        toolbar.setTitle("我的主页");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.inflateMenu(R.menu.menu_myhome);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_myhome_settings)
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
            else if (item.getItemId() == R.id.action_myhome_share)
                ShareUtils.oneKeyShared(getActivity(), "http://www.qingchengfit.cn/"
                        , "http://www.qingchengfit.cn/static/images/photo3.png"
                        , "我是分享");
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
        Glide.with(App.AppContex).load(R.drawable.img_selfinfo_bg).into(myhomeBg);
        initUser();
        return view;
    }

    public void initHead() {
        int gender = R.drawable.img_default_female;
        Glide.with(App.AppContex)
                .load(R.drawable.ic_gender_signal_female)
                .into(myhomeGender);
        if (user.gender == 0) {
            gender = R.drawable.img_default_male;
            Glide.with(App.AppContex)
                    .load(R.drawable.ic_gender_signal_male)
                    .into(myhomeGender);
        }
        if (TextUtils.isEmpty(user.avatar)) {
            Glide.with(App.AppContex)
                    .load(gender)
                    .transform(new GlideCircleTransform(App.AppContex))
                    .into(myhomeHeader);
        } else {
            Glide.with(App.AppContex)
                    .load(user.avatar)
                    .placeholder(gender)
                    .transform(new GlideCircleTransform(App.AppContex))
                    .into(myhomeHeader);
        }

    }

    private void initUser() {
        String u = PreferenceUtils.getPrefString(App.AppContex, "user_info", "");
        if (!TextUtils.isEmpty(u)) {
            user = gson.fromJson(u, User.class);

        } else {
            //TODO ERROR
        }
        myhomeName.setText(user.username);
        myhomeLocation.setText(user.city);
        initHead();

    }

    @Override
    public void onStart() {
        super.onStart();

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
