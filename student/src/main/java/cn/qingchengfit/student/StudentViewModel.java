package cn.qingchengfit.student;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentModel;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.respository.StudentRepositoryImpl;
import cn.qingchengfit.student.routers.StudentRouterCenter;
import cn.qingchengfit.student.routers.studentImpl;
import cn.qingchengfit.student.view.allot.AllotChooseViewModel;
import cn.qingchengfit.student.view.allot.AllotListViewModel;
import cn.qingchengfit.student.view.allot.SalerStudentsPage;
import cn.qingchengfit.student.view.allot.SalerStudentsViewModel;
import cn.qingchengfit.student.view.allot.StudentAllotViewModel;
import cn.qingchengfit.student.view.birthday.StudentBirthdayViewModel;
import cn.qingchengfit.student.view.followrecord.FollowRecordEditViewModel;
import cn.qingchengfit.student.view.followrecord.FollowRecordStatusViewModel;
import cn.qingchengfit.student.view.followrecord.FollowRecordViewModel;
import cn.qingchengfit.student.view.followup.IncreaseMemberSortViewModel;
import cn.qingchengfit.student.view.followup.IncreaseMemberTopViewModel;
import cn.qingchengfit.student.view.followup.IncreaseMemberViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentSortViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentTopViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentViewModel;
import cn.qingchengfit.student.view.home.StudentAllViewModel;
import cn.qingchengfit.student.view.home.StudentFilterViewModel;
import cn.qingchengfit.student.view.home.StudentHomeViewModel;
import cn.qingchengfit.student.view.home.StudentListViewModel;
import cn.qingchengfit.student.view.home.StudentRecyclerSortViewModel;
import cn.qingchengfit.student.view.state.StudentStateInfoViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import javax.inject.Singleton;

@Module public abstract class StudentViewModel {

  @Provides static StudentRouterCenter provideStudentRouterCenter() {
    return new StudentRouterCenter().registe(new studentImpl());
  }

  @Binds
  abstract StudentRepository bindStudentRespository(StudentRepositoryImpl studentRespository);

  @Binds abstract IStudentModel bindStudentModel(StudentModel studentModel);



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


  @Binds @IntoMap @ViewModelKey(StudentBirthdayViewModel.class)
  abstract ViewModel bindStudentBirthdayViewModel(StudentBirthdayViewModel model);
}

