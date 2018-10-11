package cn.qingchengfit.saasbase.mvvm_student;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.saasbase.SaasContainerActivity;
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
    //ChooseAndSearchStudentFragment.class,
    ////        StudentListFragment.class,
    //StudentHomeFragment.class, StudentOperationFragment.class, AllotSellerFragment.class,
    //AllotCoachFragment.class, SaleDetailFragment.class, CoachDetailFragment.class,
    //MultiAllotSalesFragment.class, MultiAllotCoachFragment.class, ChooseStaffFragment.class,
    //ChooseCoachFragment.class, FollowUpHomeFragment.class, FollowUpStatusTopFragment.class,
    //FollowUpStatusFragment.class, FollowUpFilterFragment.class, FilterTimesFragment.class,
    //FollowUpTopSalerView.class, StudentTransferFragment.class, StudentAttendanceFragment.class,
    //AttendanceStudentListFragment.class, AttendanceRankFragment.class,
    //
    //AttendanceNoSignFragment.class, AttendanceStudentPage.class, AttendanceAbsentPage.class,
    //AttendanceRankPage.class, AttendanceNosignPage.class, TransferStudentPage.class,
    //FollowUpStudentPage.class, FollowUpStatusPage.class, AllotStaffListPage.class,
    //AllotStaffDetailPage.class, AllotMultiStaffPage.class, AllotChooseSalerPage.class,
    //AllotChooseCoachPage.class,  StudentFilterView.class, ChooseStaffPage.class,
    //FollowUpFilterEndView.class, ViewModelModule.class,
    //StudentAddFragment.class,


}) public class StudentActivity extends SaasContainerActivity {


  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ViewModelProviders.of(this).get(StudentActivityViewModel.class);
  }

  @Override public String getModuleName() {
    return "student";
  }


}
