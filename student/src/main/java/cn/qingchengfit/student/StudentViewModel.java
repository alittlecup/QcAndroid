package cn.qingchengfit.student;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.student.view.allot.StudentAllotViewModel;
import cn.qingchengfit.student.view.followup.IncreaseMemberSortViewModel;
import cn.qingchengfit.student.view.followup.IncreaseMemberTopViewModel;
import cn.qingchengfit.student.view.followup.IncreaseMemberViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentSortViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentTopViewModel;
import cn.qingchengfit.student.view.followup.IncreaseStudentViewModel;
import cn.qingchengfit.student.view.home.StudentAllViewModel;
import cn.qingchengfit.student.view.home.StudentListViewModel;
import cn.qingchengfit.student.view.home.StudentRecyclerSortViewModel;
import cn.qingchengfit.student.view.state.StudentStateInfoViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module public abstract class StudentViewModel {
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
