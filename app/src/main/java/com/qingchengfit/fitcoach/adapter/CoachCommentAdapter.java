package com.qingchengfit.fitcoach.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.qingchengfit.fitcoach.bean.CourseTeacher;
import com.qingchengfit.fitcoach.component.PagerSlidingTabImageStrip;
import com.qingchengfit.fitcoach.fragment.course.CoachCommentDetailFragment;
import java.util.ArrayList;
import java.util.List;

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
public class CoachCommentAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabImageStrip.ImageTabProvider {

    List<Fragment> fragments;
    FragmentManager fm;
    List<CourseTeacher> mCoaches = new ArrayList<>();

    //    public CoachCommentAdapter(FragmentManager fm, ArrayList<Fragment> fs, List<Coach> coaches) {
    //        super(fm);
    //        this.fragments = fs;
    //        this.fm = fm;
    //        this.mCoaches = coaches;
    //
    //    }
    public CoachCommentAdapter(FragmentManager fm, List<CourseTeacher> coaches) {
        super(fm);
        this.fm = fm;
        this.mCoaches = coaches;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        return fragment;
    }

    @Override public Fragment getItem(int position) {
        return CoachCommentDetailFragment.newInstance(mCoaches.get(position));
    }

    @Override public int getCount() {
        return mCoaches.size();
    }

    @Override public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //    public CharSequence getPageTitle(int position) {
    //        Fragment f = fragments.get(position);
    //        if (f instanceof TitleFragment) {
    //            return ((TitleFragment) f).getTitle();
    //        } else return "";
    //
    //    }

    @Override public String getTextStr(int position) {
        return mCoaches.get(position).getUser().name;
    }

    @Override public boolean getShowRed(int position) {
        return false;
    }

    //    @Override
    public String getHeaderUrl(int position) {
        return mCoaches.get(position).getUser().header;
    }
}

