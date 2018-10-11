package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.checkout.repository.CheckoutModel;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentModel;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppAbstractModule {
  @Binds abstract ICheckoutModel bindCheckModel(CheckoutModel checkoutModel);
  @Binds abstract IStudentModel bindStudentModel(StudentModel studentModel);


}
