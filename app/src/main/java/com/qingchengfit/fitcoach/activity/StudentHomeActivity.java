package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.adapter.FragmentAdapter;
import com.qingchengfit.fitcoach.fragment.StudentBaseInfoFragment;
import com.qingchengfit.fitcoach.fragment.StudentBodyTestListFragment;
import com.qingchengfit.fitcoach.fragment.StudentCardFragment;
import com.qingchengfit.fitcoach.fragment.StudentClassRecordFragment;

import java.util.ArrayList;


/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/11/19 2015.
 */
public class StudentHomeActivity extends BaseAcitivity {

    private View mMyhomeBgView;
    private ImageView mHeaderImageView;
    private ImageView mGenderImageView;
    private TextView mNameTextView;
    private TabLayout mTabTabLayout;
    private AppBarLayout mMyhomeAppBarAppBarLayout;
    private ViewPager mStudentViewPager;
    private FragmentAdapter mAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        mMyhomeBgView = (View) findViewById(R.id.myhome_bg);
        mHeaderImageView = (ImageView) findViewById(R.id.header);
        mGenderImageView = (ImageView) findViewById(R.id.gender);
        mNameTextView = (TextView) findViewById(R.id.name);
        mTabTabLayout = (TabLayout) findViewById(R.id.tab);
        mMyhomeAppBarAppBarLayout = (AppBarLayout) findViewById(R.id.myhome_appBar);
        mStudentViewPager = (ViewPager) findViewById(R.id.student);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("学员详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initHeader();
        initViewPager();


    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new StudentBaseInfoFragment());
        fragments.add(new StudentClassRecordFragment());
        fragments.add(new StudentCardFragment());
        fragments.add(new StudentBodyTestListFragment());
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        mStudentViewPager.setAdapter(mAdapter);
        mTabTabLayout.setupWithViewPager(mStudentViewPager);
    }

    private void initHeader() {

    }
}
