package cn.qingchengfit.saasbase.mvvm_student;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.mvvm_student.di.ViewModelModule;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.AllotChooseCoachPage;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.AllotChooseSalerPage;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.AllotMultiStaffPage;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.AllotStaffDetailPage;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.AllotStaffListPage;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.StudentAllotPage;
import cn.qingchengfit.saasbase.mvvm_student.view.attendance.AttendanceStudentPage;
import cn.qingchengfit.saasbase.mvvm_student.view.attendance.absent.AttendanceAbsentPage;
import cn.qingchengfit.saasbase.mvvm_student.view.attendance.nosign.AttendanceNosignPage;
import cn.qingchengfit.saasbase.mvvm_student.view.attendance.rank.AttendanceRankPage;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.FollowUpFilterEndView;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.FollowUpStatusPage;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.FollowUpStudentPage;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseMemberPage;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseMemberTopView;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseStudentPage;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseStudentSortView;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseStudentTopView;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentAllPage;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentFilterView;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentHomePage;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentListView;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentRecyclerSortView;
import cn.qingchengfit.saasbase.mvvm_student.view.transfer.TransferStudentPage;
import cn.qingchengfit.saasbase.mvvm_student.view.webchoose.ChooseStaffPage;
import cn.qingchengfit.saasbase.student.other.ChooseCoachFragment;
import cn.qingchengfit.saasbase.student.other.ChooseStaffFragment;
import cn.qingchengfit.saasbase.student.views.ChooseAndSearchStudentFragment;
import cn.qingchengfit.saasbase.student.views.StudentAddFragment;
import cn.qingchengfit.saasbase.student.views.allot.AllotCoachFragment;
import cn.qingchengfit.saasbase.student.views.allot.AllotSellerFragment;
import cn.qingchengfit.saasbase.student.views.allot.CoachDetailFragment;
import cn.qingchengfit.saasbase.student.views.allot.MultiAllotCoachFragment;
import cn.qingchengfit.saasbase.student.views.allot.MultiAllotSalesFragment;
import cn.qingchengfit.saasbase.student.views.allot.SaleDetailFragment;
import cn.qingchengfit.saasbase.student.views.attendance.AttendanceNoSignFragment;
import cn.qingchengfit.saasbase.student.views.attendance.AttendanceRankFragment;
import cn.qingchengfit.saasbase.student.views.attendance.AttendanceStudentListFragment;
import cn.qingchengfit.saasbase.student.views.attendance.StudentAttendanceFragment;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpFilterFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpHomeFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpStatusFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpStatusTopFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpTopSalerView;
import cn.qingchengfit.saasbase.student.views.home.StudentHomeFragment;
import cn.qingchengfit.saasbase.student.views.home.StudentOperationFragment;
import cn.qingchengfit.saasbase.student.views.transfer.StudentTransferFragment;
import com.anbillon.flabellum.annotations.Trunk;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/4 2016.
 */
@Trunk(fragments = {
    ChooseAndSearchStudentFragment.class,
    //        StudentListFragment.class,
    StudentHomeFragment.class, StudentOperationFragment.class, AllotSellerFragment.class,
    AllotCoachFragment.class, SaleDetailFragment.class, CoachDetailFragment.class,
    MultiAllotSalesFragment.class, MultiAllotCoachFragment.class, ChooseStaffFragment.class,
    ChooseCoachFragment.class, FollowUpHomeFragment.class, FollowUpStatusTopFragment.class,
    FollowUpStatusFragment.class, FollowUpFilterFragment.class, FilterTimesFragment.class,
    FollowUpTopSalerView.class, StudentTransferFragment.class, StudentAttendanceFragment.class,
    AttendanceStudentListFragment.class, AttendanceRankFragment.class,

    AttendanceNoSignFragment.class, AttendanceStudentPage.class, AttendanceAbsentPage.class,
    AttendanceRankPage.class, AttendanceNosignPage.class, TransferStudentPage.class,
    FollowUpStudentPage.class, FollowUpStatusPage.class, AllotStaffListPage.class,
    AllotStaffDetailPage.class, AllotMultiStaffPage.class, AllotChooseSalerPage.class,
    AllotChooseCoachPage.class,  StudentFilterView.class, ChooseStaffPage.class,
    FollowUpFilterEndView.class, ViewModelModule.class,
    StudentAddFragment.class,

    StudentHomePage.class, StudentAllPage.class, StudentRecyclerSortView.class, StudentListView.class,
    StudentAllotPage.class, IncreaseStudentSortView.class, IncreaseStudentTopView.class,
    IncreaseStudentPage.class,  IncreaseMemberTopView.class,
    IncreaseMemberPage.class
}) public class StudentActivity extends SaasContainerActivity {


  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelProviders.of(this).get(StudentActivityViewModel.class);
  }

  @Override public String getModuleName() {
    return "student";
  }


}
