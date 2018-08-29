package com.qingchengfit.fitcoach.di;

import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.student.respository.IStudentModel;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.fragment.card.CardModel;
import com.qingchengfit.fitcoach.fragment.card.StaffModel;
import com.qingchengfit.fitcoach.fragment.card.StudentModel;
import com.qingchengfit.fitcoach.fragment.checkout.CheckoutModel;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import javax.inject.Singleton;

@Module
public abstract class AppAbstractModule {
  @Binds abstract ICardModel bindCardModel(CardModel cardModel);
  @Binds abstract ICheckoutModel bindCheckoutModel(CheckoutModel cardModel);
  @Binds abstract IStaffModel bindStaffModel(StaffModel cardModel);
  @Binds abstract IStudentModel bindStudentModel(StudentModel cardModel);
  @Singleton
  @ContributesAndroidInjector()
  abstract FragActivity contributeFragActivityInjector();

}
