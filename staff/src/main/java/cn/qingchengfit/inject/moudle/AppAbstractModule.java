package cn.qingchengfit.inject.moudle;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.checkout.repository.CheckoutModel;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.saasbase.course.batch.views.BatchFragmentVM;
import cn.qingchengfit.saasbase.course.detail.ScheduleDetailVM;
import cn.qingchengfit.saasbase.staff.views.CommonUserSearchFragment;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.staffkit.views.signin.config.SignInMemberVM;
import cn.qingchengfit.staffkit.views.signin.config.SignInTimeSettingVM;
import cn.qingchengfit.staffkit.views.signin.config.SignTimeListVM;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentModel;
import cn.qingchengfit.student.view.detail.ClassRecordTempFragment;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module public abstract class AppAbstractModule {
  @Binds abstract ICheckoutModel bindCheckModel(CheckoutModel checkoutModel);

  @Binds abstract IStudentModel bindStudentModel(StudentModel studentModel);

  @ContributesAndroidInjector abstract CommonUserSearchFragment bindCommonUserSearchFragment();

  @ContributesAndroidInjector abstract ClassRecordTempFragment bindClassRecordTempFragment();

  @Binds @IntoMap @ViewModelKey(SignTimeListVM.class)
  abstract ViewModel bindSignTimeListVM(SignTimeListVM model);

  @Binds @IntoMap @ViewModelKey(SignInTimeSettingVM.class)
  abstract ViewModel bindSignInTimeSettingVM(SignInTimeSettingVM model);

  @Binds @IntoMap @ViewModelKey(SignInMemberVM.class)
  abstract ViewModel bindSignInMemberVM(SignInMemberVM model);

  @Binds @IntoMap @ViewModelKey(BatchFragmentVM.class)
  abstract ViewModel bindBatchFragmentVM(BatchFragmentVM model);

  @Binds @IntoMap @ViewModelKey(ScheduleDetailVM.class)
  abstract ViewModel bindCourseDetailViewModel(ScheduleDetailVM model);
}
