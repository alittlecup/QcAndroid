package cn.qingchengfit.saasbase.course.course.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.course.course.bean.CourseTeacher;
import cn.qingchengfit.saasbase.course.course.views.CoachCommentDetailFragment;
import cn.qingchengfit.widgets.PagerSlidingTabImageStrip;
import java.util.ArrayList;
import java.util.List;

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
        return mCoaches.get(position).getUser().username;
    }

    @Override public boolean getShowRed(int position) {
        return false;
    }

    //@Override public String getHeaderUrl(int position) {
    //    return mCoaches.get(position).getUser().avatar;
    //}
}
