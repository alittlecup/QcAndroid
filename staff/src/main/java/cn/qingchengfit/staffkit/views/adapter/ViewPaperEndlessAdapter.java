package cn.qingchengfit.staffkit.views.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class ViewPaperEndlessAdapter extends PagerAdapter {

    List<View> pageViews;

    public ViewPaperEndlessAdapter(List<View> pageViews) {
        this.pageViews = pageViews;
    }

    @Override public int getCount() {
        return pageViews.size();
    }

    @Override public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void addEndless(View view) {
        pageViews.add(view);
    }

    @Override public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(pageViews.get(arg1));
    }

    @Override public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(pageViews.get(arg1),
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return pageViews.get(arg1);
    }
}