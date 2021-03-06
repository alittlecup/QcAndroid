package cn.qingchengfit.recruit.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.RecruitRouter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.PagerSlidingTabImageStrip;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/7/5.
 */
public class JobFairsAllFragment extends BaseFragment{

  Toolbar toolbar;
  TextView toolbarTitle;
  TabLayout strip;
  ViewPager vp;
  @Inject RecruitRouter router;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_jobfairs_all, container, false);
    toolbar = view.findViewById(R.id.toolbar);
    toolbarTitle = view.findViewById(R.id.toolbar_title);
    strip = view.findViewById(R.id.strip);
    vp = view.findViewById(R.id.vp);
    initToolbar(toolbar);
    vp.setAdapter(new JobFairsAllAdapter(getChildFragmentManager()));
    strip.setupWithViewPager(vp);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("专场招聘会");
  }

  @Override public String getFragmentName() {
    return JobFairsAllFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  private class JobFairsAllAdapter extends FragmentStatePagerAdapter
      implements PagerSlidingTabImageStrip.ImageTabProvider {

    JobFairsAllAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      if (position == 0) {
        return JobFairListFragment.newDoingJobFairs();
      } else {
        return JobFairListFragment.newOutDateJobFairs();
      }
    }

    @Override public int getCount() {
      return 2;
    }

    @Override public CharSequence getPageTitle(int position) {
      return getTextStr(position);
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public String getTextStr(int position) {
      switch (position) {
        case 1:
          return "已结束";
        default:
          return "正在报名";
      }
    }

    @Override public boolean getShowRed(int position) {
      return false;
    }
  }
}
