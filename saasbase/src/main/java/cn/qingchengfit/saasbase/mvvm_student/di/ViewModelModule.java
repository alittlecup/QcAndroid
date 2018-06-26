package cn.qingchengfit.saasbase.mvvm_student.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.saascommon.mvvm.ViewModelFactory;
import cn.qingchengfit.saasbase.course.batch.viewmodel.BatchCopyViewModel;
import cn.qingchengfit.saasbase.mvvm_student.respository.StudentRepository;
import cn.qingchengfit.saasbase.mvvm_student.respository.StudentRepositoryImpl;
import cn.qingchengfit.saasbase.mvvm_student.view.allot.StudentAllotViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseMemberSortViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseMemberTopViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseMemberViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseStudentSortViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseStudentTopViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.followup.IncreaseStudentViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentAllViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentListViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.home.StudentRecyclerSortViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.state.StudentStateInfoViewModel;
import cn.qingchengfit.saasbase.mvvm_student.view.webchoose.ChooseStaffViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot.AllotChooseViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot.AllotListViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot.AllotMultiStaffViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot.AllotStaffDetailViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.attendance.AttendanceStudentViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.attendance.absent.AttendanceAbsentViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.attendance.nosign.AttendanceNosignViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.attendance.rank.AttendanceRankViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.followup.FollowUpFilterViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.followup.FollowUpStatusViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.followup.FollowUpStudentViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentHomeViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.transfer.TransferStudentViewModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by huangbaole on 2017/11/15.
 */

@Module public abstract class ViewModelModule {

  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

  @Binds
  abstract StudentRepository bindStudentRespository(StudentRepositoryImpl studentRespository);

  //@Binds abstract IStudentModel bindStudentModel(StudentModel studentModel);

  @Binds @IntoMap @ViewModelKey(AttendanceStudentViewModel.class)
  abstract ViewModel bindAttendanceStudentViewModel(AttendanceStudentViewModel model);

  @Binds @IntoMap @ViewModelKey(AttendanceAbsentViewModel.class)
  abstract ViewModel bindAttendanceAbsentViewModel(AttendanceAbsentViewModel model);

  @Binds @IntoMap @ViewModelKey(AttendanceRankViewModel.class)
  abstract ViewModel bindAttendanceRankViewModel(AttendanceRankViewModel model);

  @Binds @IntoMap @ViewModelKey(AttendanceNosignViewModel.class)
  abstract ViewModel bindAttendanceNosignViewModel(AttendanceNosignViewModel model);

  @Binds @IntoMap @ViewModelKey(TransferStudentViewModel.class)
  abstract ViewModel bindTransferStudentViewModel(TransferStudentViewModel model);

  @Binds @IntoMap @ViewModelKey(FollowUpStudentViewModel.class)
  abstract ViewModel bindFollowUpStudentViewModel(FollowUpStudentViewModel model);

  @Binds @IntoMap @ViewModelKey(FollowUpStatusViewModel.class)
  abstract ViewModel bindFollowUpStatusViewModel(FollowUpStatusViewModel model);

  @Binds @IntoMap @ViewModelKey(AllotListViewModel.class)
  abstract ViewModel bindAllotListViewModel(AllotListViewModel model);

  @Binds @IntoMap @ViewModelKey(AllotStaffDetailViewModel.class)
  abstract ViewModel bindAllotStaffDetailViewModel(AllotStaffDetailViewModel model);

  @Binds @IntoMap @ViewModelKey(AllotMultiStaffViewModel.class)
  abstract ViewModel bindAllotMultiStaffViewModel(AllotMultiStaffViewModel model);

  @Binds @IntoMap @ViewModelKey(AllotChooseViewModel.class)
  abstract ViewModel bindAllotChooseViewModel(AllotChooseViewModel model);

  @Binds @IntoMap @ViewModelKey(StudentHomeViewModel.class)
  abstract ViewModel bindStudentHomeViewModel(StudentHomeViewModel model);

  @Binds @IntoMap @ViewModelKey(StudentFilterViewModel.class)
  abstract ViewModel bindStudentFilterViewModel(StudentFilterViewModel model);

  @Binds @IntoMap @ViewModelKey(ChooseStaffViewModel.class)
  abstract ViewModel bindChooseStaffViewModel(ChooseStaffViewModel model);

  @Binds @IntoMap @ViewModelKey(FollowUpFilterViewModel.class)
  abstract ViewModel bindFollowUpFilterViewModel(FollowUpFilterViewModel model);

  @Binds @IntoMap @ViewModelKey(BatchCopyViewModel.class)
  abstract ViewModel bindBatchCopyViewModel(BatchCopyViewModel model);

  @Named("commonFilter") @Singleton @Provides static StudentFilter providesStudentFilter() {
    return new StudentFilter();
  }

  @Named("topFilter") @Singleton @Provides static StudentFilter providesTopStudentFilter() {
    return new StudentFilter();
  }

  /**
   * new
   */
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
}
