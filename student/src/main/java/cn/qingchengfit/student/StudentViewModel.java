package cn.qingchengfit.student;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentModel;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.respository.StudentRepositoryImpl;
import cn.qingchengfit.student.view.allot.AllotChooseViewModel;
import cn.qingchengfit.student.view.allot.AllotListViewModel;
import cn.qingchengfit.student.view.allot.SalerStudentsPage;
import cn.qingchengfit.student.view.allot.SalerStudentsViewModel;
import cn.qingchengfit.student.view.allot.StudentAllotViewModel;
import cn.qingchengfit.student.view.attendance.AttendanceStudentViewModel;
import cn.qingchengfit.student.view.attendance.absent.AttendanceAbsentViewModel;
import cn.qingchengfit.student.view.attendance.nosign.AttendanceNosignViewModel;
import cn.qingchengfit.student.view.attendance.rank.AttendanceRankViewModel;
import cn.qingchengfit.student.view.birthday.StudentBirthdayViewModel;
import cn.qingchengfit.student.view.choose.ChooseStaffViewModel;
import cn.qingchengfit.student.view.followrecord.FollowRecordEditViewModel;
import cn.qingchengfit.student.view.followrecord.FollowRecordStatusViewModel;
import cn.qingchengfit.student.view.followrecord.FollowRecordViewModel;
import cn.qingchengfit.student.view.followrecord.NotiOthersPage;
import cn.qingchengfit.student.view.followrecord.NotiOthersVM;
import cn.qingchengfit.student.view.followup.IncreaseMemberSortViewModel;
import cn.qingchengfit.student.view.followup.IncreaseMemberTopViewModel;
import cn.qingchengfit.student.view.followup.IncreaseMemberViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentSortViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentTopViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentViewModel;
import cn.qingchengfit.student.view.home.StudentAllViewModel;
import cn.qingchengfit.student.view.home.StudentFilterViewModel;
import cn.qingchengfit.student.view.home.StudentHomePieChartViewModel;
import cn.qingchengfit.student.view.home.StudentHomeViewModel;
import cn.qingchengfit.student.view.home.StudentListViewModel;
import cn.qingchengfit.student.view.home.StudentRecyclerSortViewModel;
import cn.qingchengfit.student.view.state.SalerStudentStateViewModel;
import cn.qingchengfit.student.view.state.StudentStateInfoViewModel;
import cn.qingchengfit.student.view.transfer.TransferStudentViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import javax.inject.Singleton;

@Module public abstract class StudentViewModel {
  private static StudentWrap studentWrap;

  @Provides static StudentWrap provideStudent(){
    if (studentWrap == null)
      studentWrap = new StudentWrap();
    return studentWrap;
  }
  @Binds
  abstract StudentRepository bindStudentRepository(StudentRepositoryImpl studentRepository);




  @Binds @IntoMap @ViewModelKey(StudentHomeViewModel.class)
  abstract ViewModel bindStudentHomeViewModel(StudentHomeViewModel model);

  @Binds @IntoMap @ViewModelKey(StudentFilterViewModel.class)
  abstract ViewModel bindStudentFilterViewModel(StudentFilterViewModel model);

  @Binds @IntoMap @ViewModelKey(StudentAllViewModel.class)
  abstract ViewModel bindStudentAllViewModel(StudentAllViewModel model);

  @Binds @IntoMap @ViewModelKey(StudentRecyclerSortViewModel.class)
  abstract ViewModel bindStudentRecyclerSortViewModel(StudentRecyclerSortViewModel model);

  @Binds @IntoMap @ViewModelKey(StudentListViewModel.class)
  abstract ViewModel bindStudentListViewModel(StudentListViewModel model);

  @Binds @IntoMap @ViewModelKey(StudentAllotViewModel.class)
  abstract ViewModel bindStudentAllotViewModel(StudentAllotViewModel model);

  @Binds @IntoMap @ViewModelKey(IncreaseStudentViewModel.class)
  abstract ViewModel bindIncreaseStudentViewModel(IncreaseStudentViewModel model);

  @Binds @IntoMap @ViewModelKey(IncreaseStudentTopViewModel.class)
  abstract ViewModel bindIncreaseStudentTopViewModel(IncreaseStudentTopViewModel model);

  @Binds @IntoMap @ViewModelKey(IncreaseStudentSortViewModel.class)
  abstract ViewModel bindIncreaseStudentSortViewModel(IncreaseStudentSortViewModel model);

  @Binds @IntoMap @ViewModelKey(IncreaseMemberViewModel.class)
  abstract ViewModel bindIncreaseMemberViewModel(IncreaseMemberViewModel model);

  @Binds @IntoMap @ViewModelKey(IncreaseMemberTopViewModel.class)
  abstract ViewModel bindIncreaseFollowViewModel(IncreaseMemberTopViewModel model);

  @Binds @IntoMap @ViewModelKey(IncreaseMemberSortViewModel.class)
  abstract ViewModel bindIncreaseMemberSortViewModel(IncreaseMemberSortViewModel model);

  @Binds @IntoMap @ViewModelKey(StudentStateInfoViewModel.class)
  abstract ViewModel bindStudentStateInfoViewModel(StudentStateInfoViewModel model);

  @Binds @IntoMap @ViewModelKey(FollowRecordViewModel.class)
  abstract ViewModel bindFollowRecordViewModel(FollowRecordViewModel model);

  @Binds @IntoMap @ViewModelKey(FollowRecordEditViewModel.class)
  abstract ViewModel bindFollowRecordEditViewModel(FollowRecordEditViewModel model);

  @Binds @IntoMap @ViewModelKey(FollowRecordStatusViewModel.class)
  abstract ViewModel bindFollowRecordStatusViewModel(FollowRecordStatusViewModel model);

  @Binds @IntoMap @ViewModelKey(AllotListViewModel.class)
  abstract ViewModel bindAllotListViewModel(AllotListViewModel model);

  @Binds @IntoMap @ViewModelKey(SalerStudentsViewModel.class)
  abstract ViewModel bindSalerStudentsViewModel(SalerStudentsViewModel model);


  @Binds @IntoMap @ViewModelKey(AllotChooseViewModel.class)
  abstract ViewModel bindAllotChooseViewModel(AllotChooseViewModel model);

  @Binds @IntoMap @ViewModelKey(NotiOthersVM.class)
  abstract ViewModel bindNotiOtherViewModel(NotiOthersVM model);

  @Binds @IntoMap @ViewModelKey(StudentBirthdayViewModel.class)
  abstract ViewModel bindStudentBirthdayViewModel(StudentBirthdayViewModel model);

  @Binds @IntoMap @ViewModelKey(TransferStudentViewModel.class)
  abstract ViewModel bindTransferStudentViewModel(TransferStudentViewModel model);

  @Binds @IntoMap @ViewModelKey(AttendanceStudentViewModel.class)
  abstract ViewModel bindAttendanceStudentViewModel(AttendanceStudentViewModel model);

  @Binds @IntoMap @ViewModelKey(AttendanceRankViewModel.class)
  abstract ViewModel bindAttendanceRankViewModel(AttendanceRankViewModel model);

  @Binds @IntoMap @ViewModelKey(AttendanceNosignViewModel.class)
  abstract ViewModel bindAttendanceNosignViewModel(AttendanceNosignViewModel model);

  @Binds @IntoMap @ViewModelKey(AttendanceAbsentViewModel.class)
  abstract ViewModel bindAttendanceAbsentViewModel(AttendanceAbsentViewModel model);

  @Binds @IntoMap @ViewModelKey(SalerStudentStateViewModel.class)
  abstract ViewModel bindSalerStudentStateViewModel(SalerStudentStateViewModel model);


  @Binds @IntoMap @ViewModelKey(ChooseStaffViewModel.class)
  abstract ViewModel bindChooseStaffViewModel(ChooseStaffViewModel model);



  @Binds @IntoMap @ViewModelKey(StudentHomePieChartViewModel.class)
  abstract ViewModel bindStudentHomePieChartViewModel(StudentHomePieChartViewModel model);

}

