package cn.qingchengfit.student;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.student.routers.StudentRouterCenter;
import cn.qingchengfit.student.view.allot.AllotChooseCoachPage;
import cn.qingchengfit.student.view.allot.AllotChooseSellerPage;
import cn.qingchengfit.student.view.allot.SalerStudentsPage;
import cn.qingchengfit.student.view.allot.StudentAllotPage;
import cn.qingchengfit.student.view.allot.StudentAllotStaffPage;
import cn.qingchengfit.student.view.attendance.AttendanceStudentPage;
import cn.qingchengfit.student.view.attendance.absent.AttendanceAbsentPage;
import cn.qingchengfit.student.view.attendance.nosign.AttendanceNosignPage;
import cn.qingchengfit.student.view.attendance.rank.AttendanceRankPage;
import cn.qingchengfit.student.view.birthday.StudentBirthdayPage;
import cn.qingchengfit.student.view.choose.ChooseAndSearchStudentFragment;
import cn.qingchengfit.student.view.choose.ChooseStaffPage;
import cn.qingchengfit.student.view.choose.SearchStudentFragment;
import cn.qingchengfit.student.view.choose.TrainerChooseAndSearchFragment;
import cn.qingchengfit.student.view.detail.StudentDetailWithCardPage;
import cn.qingchengfit.student.view.export.ImportExportFragment;
import cn.qingchengfit.student.view.followrecord.FollowRecordEditPage;
import cn.qingchengfit.student.view.followrecord.FollowRecordStatusPage;
import cn.qingchengfit.student.view.followrecord.NotiOthersPage;
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
import cn.qingchengfit.student.view.home.StudentHomePieChartView;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.student.view.home.StudentRecyclerSortView;
import cn.qingchengfit.student.view.sendmsg.MsgSendFragmentFragment;
import cn.qingchengfit.student.view.sendmsg.SendMsgHomeFragment;
import cn.qingchengfit.student.view.sendmsg.ShortMsgDetailFragment;
import cn.qingchengfit.student.view.state.SalerStudentStatePage;
import cn.qingchengfit.student.view.state.StudentStateInfoPage;
import cn.qingchengfit.student.view.transfer.TransferStudentPage;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    StudentFilterView.class, StudentHomePage.class, StudentAllPage.class,
    StudentRecyclerSortView.class, StudentListView.class, StudentAllotPage.class,
    IncreaseStudentSortView.class, IncreaseStudentTopView.class, IncreaseStudentPage.class,
    IncreaseMemberTopView.class, IncreaseMemberPage.class, StudentStateInfoPage.class,
    StudentAllotStaffPage.class, FollowRecordEditPage.class, FollowRecordStatusPage.class,
    SendMsgHomeFragment.class, ShortMsgDetailFragment.class, FollowUpTopSalerView.class,
    FollowUpFilterView.class, MsgSendFragmentFragment.class, ImportExportFragment.class,
    SalerStudentsPage.class, AllotChooseSellerPage.class, AllotChooseCoachPage.class,
    StudentBirthdayPage.class, TransferStudentPage.class, AttendanceStudentPage.class,
    AttendanceRankPage.class, AttendanceNosignPage.class, AttendanceAbsentPage.class,
    NotiOthersPage.class, SalerStudentStatePage.class, ChooseStaffPage.class,
    ChooseAndSearchStudentFragment.class, StudentHomePieChartView.class,
    TrainerChooseAndSearchFragment.class, SearchStudentFragment.class,StudentDetailWithCardPage.class
}) public class StudentActivity extends SaasCommonActivity {
  @Inject StudentRouterCenter studentRouterCenter;

  @Override public String getModuleName() {
    return "student";
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return studentRouterCenter.getFragment(intent.getData(), intent.getExtras());
  }
}
