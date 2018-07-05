package cn.qingchengfit.student;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.student.routers.StudentRouterCenter;
import cn.qingchengfit.student.view.allot.StudentAllotPage;
import cn.qingchengfit.student.view.allot.StudentAllotStaffPage;
import cn.qingchengfit.student.view.followup.FollowUpFilterView;
import cn.qingchengfit.student.view.followup.FollowUpTopSalerView;
import cn.qingchengfit.student.view.followup.IncreaseMemberPage;
import cn.qingchengfit.student.view.followup.IncreaseMemberTopView;
import cn.qingchengfit.student.view.followup.IncreaseStudentPage;
import cn.qingchengfit.student.view.followup.IncreaseStudentSortView;
import cn.qingchengfit.student.view.followup.IncreaseStudentTopView;
import cn.qingchengfit.student.view.home.StudentAllPage;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentHomePage;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.student.view.home.StudentRecyclerSortView;
import cn.qingchengfit.student.view.state.StudentStateInfoPage;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    StudentFilterView.class,



    StudentViewModel.class, StudentHomePage.class, StudentAllPage.class,
    StudentRecyclerSortView.class, StudentListView.class, StudentAllotPage.class,
    IncreaseStudentSortView.class, IncreaseStudentTopView.class, IncreaseStudentPage.class,
    IncreaseMemberTopView.class, IncreaseMemberPage.class, StudentStateInfoPage.class,
    StudentAllotStaffPage.class
}) public class StudentActivity extends SaasCommonActivity {
  @Inject StudentRouterCenter studentRouterCenter;

  @Override public String getModuleName() {
    return "student";
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return studentRouterCenter.getFragment(intent.getData(), intent.getExtras());
  }
}
