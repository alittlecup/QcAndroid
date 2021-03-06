package cn.qingchengfit.staffkit.allocate.coach;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;


import cn.qingchengfit.model.others.ToolbarBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.fragment.AllocateCoachListFragment;
import cn.qingchengfit.staffkit.views.custom.MyDrawerLayout;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragment;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilterFragmentBuilder;
import cn.qingchengfit.views.FragCallBack;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import rx.functions.Action1;

/**
 * Created by fb on 2017/5/2.
 */

public class AllocateCoachActivity extends BaseActivity implements FragCallBack, DrawerLayout.DrawerListener {

	public MyDrawerLayout drawer;
    public StudentFilter studentFilter = new StudentFilter();
	FrameLayout filterFrameLayout;
    StudentFilterFragment filterFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_coach);
      drawer = (MyDrawerLayout) findViewById(R.id.allocate_coach_drawer);
      filterFrameLayout = (FrameLayout) findViewById(R.id.frame_student_filter);

      setFilterFragment();
        getSupportFragmentManager().beginTransaction()
            .replace(getFragId(), new AllocateCoachListFragment())
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out)
            .commit();
    }

    public void setFilterFragment() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        filterFrameLayout.setVisibility(View.VISIBLE);
        drawer.addDrawerListener(this);
        filterFragment = new StudentFilterFragmentBuilder(0).build();
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out)
            .replace(R.id.frame_student_filter, filterFragment)
            .commit();
    }

    public void removeFilterFragment() {
        filterFrameLayout.setVisibility(View.GONE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override public int getFragId() {
        return R.id.frame_allocate_coach;
    }

    @Override public void setToolbar(String title, boolean showRight, View.OnClickListener titleClick, @MenuRes int menu,
        Toolbar.OnMenuItemClickListener listener) {
    }

    @Override public void cleanToolbar() {

    }

    @Override public void openSeachView(String hint, Action1<CharSequence> action1) {

    }

    @Override public void onChangeFragment(BaseFragment fragment) {

    }

    @Override public void setBar(ToolbarBean bar) {

    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override public void onDrawerOpened(View drawerView) {

    }

    @Override public void onDrawerClosed(View drawerView) {
    }

    @Override public void onDrawerStateChanged(int newState) {

    }
}
