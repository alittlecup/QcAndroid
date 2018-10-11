package cn.qingchengfit.saasbase.mvvm_student.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import cn.qingchengfit.saasbase.course.batch.viewmodel.BatchCopyViewModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.saascommon.mvvm.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by huangbaole on 2017/11/15.
 */

@Module public abstract class ViewModelModule {

  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

  //@Binds
  //abstract StudentRepository bindStudentRespository(StudentRepositoryImpl studentRespository);
  //
  ////@Binds abstract IStudentModel bindStudentModel(StudentModel studentModel);
  //
  //@Binds @IntoMap @ViewModelKey(AttendanceStudentViewModel.class)
  //abstract ViewModel bindAttendanceStudentViewModel(AttendanceStudentViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(AttendanceAbsentViewModel.class)
  //abstract ViewModel bindAttendanceAbsentViewModel(AttendanceAbsentViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(AttendanceRankViewModel.class)
  //abstract ViewModel bindAttendanceRankViewModel(AttendanceRankViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(AttendanceNosignViewModel.class)
  //abstract ViewModel bindAttendanceNosignViewModel(AttendanceNosignViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(TransferStudentViewModel.class)
  //abstract ViewModel bindTransferStudentViewModel(TransferStudentViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(FollowUpStudentViewModel.class)
  //abstract ViewModel bindFollowUpStudentViewModel(FollowUpStudentViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(FollowUpStatusViewModel.class)
  //abstract ViewModel bindFollowUpStatusViewModel(FollowUpStatusViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(AllotListViewModel.class)
  //abstract ViewModel bindAllotListViewModel(AllotListViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(AllotStaffDetailViewModel.class)
  //abstract ViewModel bindAllotStaffDetailViewModel(AllotStaffDetailViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(AllotMultiStaffViewModel.class)
  //abstract ViewModel bindAllotMultiStaffViewModel(AllotMultiStaffViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(AllotChooseViewModel.class)
  //abstract ViewModel bindAllotChooseViewModel(AllotChooseViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(StudentHomeViewModel.class)
  //abstract ViewModel bindStudentHomeViewModel(StudentHomeViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(StudentFilterViewModel.class)
  //abstract ViewModel bindStudentFilterViewModel(StudentFilterViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(ChooseStaffViewModel.class)
  //abstract ViewModel bindChooseStaffViewModel(ChooseStaffViewModel model);
  //
  //@Binds @IntoMap @ViewModelKey(FollowUpFilterViewModel.class)
  //abstract ViewModel bindFollowUpFilterViewModel(FollowUpFilterViewModel model);
  //
  @Binds @IntoMap @ViewModelKey(BatchCopyViewModel.class)
  abstract ViewModel bindBatchCopyViewModel(BatchCopyViewModel model);


  //@Named("commonFilter") @Singleton @Provides static StudentFilter providesStudentFilter() {
  //  return new StudentFilter();
  //}
  //
  //@Named("topFilter") @Singleton @Provides static StudentFilter providesTopStudentFilter() {
  //  return new StudentFilter();
  //}

  /**
   * new
   */

}
