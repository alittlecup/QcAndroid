package cn.qingchengfit.inject.moudle;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.checkout.repository.CheckoutModel;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.saasbase.staff.views.CommonUserSearchFragment;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.staffkit.views.signin.config.SignInMemberVM;
import cn.qingchengfit.staffkit.views.signin.config.SignInTimeSettingVM;
import cn.qingchengfit.staffkit.views.signin.config.SignTimeListVM;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentModel;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module public abstract class AppAbstractModule {
  @Binds abstract ICheckoutModel bindCheckModel(CheckoutModel checkoutModel);

  @Binds abstract IStudentModel bindStudentModel(StudentModel studentModel);



  @ContributesAndroidInjector abstract CommonUserSearchFragment bindCommonUserSearchFragment();



  @Binds @IntoMap @ViewModelKey(SignTimeListVM.class)
  abstract ViewModel bindSignTimeListVM(SignTimeListVM model);

  @Binds @IntoMap @ViewModelKey(SignInTimeSettingVM.class)
  abstract ViewModel bindSignInTimeSettingVM(SignInTimeSettingVM model);

  @Binds @IntoMap @ViewModelKey(SignInMemberVM.class)
  abstract ViewModel bindSignInMemberVM(SignInMemberVM model);





}
