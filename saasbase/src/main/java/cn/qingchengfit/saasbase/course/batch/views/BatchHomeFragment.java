package cn.qingchengfit.saasbase.course.batch.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.views.FragmentAdapter;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import javax.inject.Inject;

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
 * Created by Paper on 16/3/22 2016.
 */
public class BatchHomeFragment extends BaseFragment {

  public static final String TAG = "GymCoursesFragment";
	ImageView gymImg;
	TextView gymName;
	ImageView gymTitleTag;
	TextView gymCount;
	TabLayout myhomeTab;
	ViewPager viewpager;
  ArrayList<Fragment> fragments = new ArrayList<>();
  @Inject IPermissionModel serPermisAction;
  private FragmentAdapter fragmentAdater;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_batch_home, container, false);
    gymImg = (ImageView) view.findViewById(R.id.gym_img);
    gymName = (TextView) view.findViewById(R.id.gym_name);
    gymTitleTag = (ImageView) view.findViewById(R.id.gym_title_tag);
    gymCount = (TextView) view.findViewById(R.id.gym_count);
    myhomeTab = (TabLayout) view.findViewById(R.id.myhome_tab);
    viewpager = (ViewPager) view.findViewById(R.id.viewpager);

    mCallbackActivity.setToolbar("课程排期", false, null, 0, null);
    if (fragments.size() == 0) {
      fragments.clear();
      //fragments.add(CourseListFragment.newInstance(Configs.TYPE_GROUP));
      //fragments.add(CourseListFragment.newInstance(Configs.TYPE_PRIVATE));
    }
    fragmentAdater = new FragmentAdapter(getChildFragmentManager(), fragments);
    viewpager.setAdapter(fragmentAdater);
    viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myhomeTab));
    myhomeTab.setupWithViewPager(viewpager);
    initRxBus();
    return view;
  }

  private void initRxBus() {
    // TODO: 2017/9/11 新增排期
    //mAddObserable = RxBus.getBus().register(RxAddCourse.class);
    //mAddObserable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RxAddCourse>() {
    //    @Override public void call(RxAddCourse o) {
    //        Course c = new Course();
    //
    //        if ((!c.is_private && !permissionModel.checkAtLeastOne(PermissionServerUtils.TEAMARRANGE_CALENDAR_CAN_CHANGE))
    //            || (c.is_private && !permissionModel.checkAtLeastOne(PermissionServerUtils.PRIARRANGE_CALENDAR_CAN_CHANGE))) {
    //            showAlert(R.string.alert_permission_forbid);
    //            return;
    //        }
    //
    //        if (((RxAddCourse) o).type == Configs.TYPE_GROUP) {
    //            //跳转到新增团课
    //            c.is_private = false;
    //        } else if (((RxAddCourse) o).type == Configs.TYPE_PRIVATE) {//新增私教排期
    //            c.is_private = true;
    //            ChooseCoachFragment.start(BatchHomeFragment.this, 2, "", Configs.INIT_TYPE_CHOOSE);
    //        }
    //    }
    //}, new Action1<Throwable>() {
    //    @Override public void call(Throwable throwable) {
    //
    //    }
    //});

  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 2) {  //新增私教排期 选择教练
        //                getFragmentManager().beginTransaction()
        //                        .add(mCallbackActivity.getFragId(), ManageCourseBatchFragment.newInstance())
      }
    }
  }

  @Override public String getFragmentName() {
    return BatchHomeFragment.class.getName();
  }
}
