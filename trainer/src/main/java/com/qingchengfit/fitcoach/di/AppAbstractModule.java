package com.qingchengfit.fitcoach.di;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.card.network.CardRealModel;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.repository.RepoCoachService;
import cn.qingchengfit.repository.RepoCoachServiceImpl;
import cn.qingchengfit.saasbase.course.batch.views.BatchFragmentVM;
import cn.qingchengfit.saasbase.course.detail.ScheduleDetailVM;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.view.detail.ClassRecordTempFragment;
import cn.qingchengfit.student.view.detail.NotSignFilterFragment;
import com.qingchengfit.fitcoach.fragment.NotificationFragment;
import com.qingchengfit.fitcoach.fragment.card.CardModel;
import com.qingchengfit.fitcoach.fragment.card.StaffModel;
import com.qingchengfit.fitcoach.fragment.card.StudentModel;
import com.qingchengfit.fitcoach.fragment.checkout.CheckoutModel;
import com.qingchengfit.fitcoach.fragment.manage.ManageViewModel;
import com.qingchengfit.fitcoach.fragment.schedule.ScheduesFragment;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module public abstract class AppAbstractModule {
  @Binds abstract ICardModel bindCardModel(CardModel cardModel);

  @Binds abstract CardRealModel bindCardRealModel(CardModel cardModel);

  @Binds abstract ICheckoutModel bindCheckoutModel(CheckoutModel cardModel);

  @Binds abstract IStaffModel bindStaffModel(StaffModel cardModel);

  @Binds abstract IStudentModel bindStudentModel(StudentModel cardModel);

  @ContributesAndroidInjector abstract ClassRecordTempFragment bindClassRecordTempFragment();
  @ContributesAndroidInjector abstract NotSignFilterFragment bindNotSignFilterFragment();


  @ContributesAndroidInjector() abstract ScheduesFragment contributeScheduesFragment();
  @ContributesAndroidInjector() abstract QRActivity contributeQRActivity();
  @ContributesAndroidInjector() abstract NotificationFragment contributeNotificationFragment();

  @Binds abstract RepoCoachService bindRepoCoachService(RepoCoachServiceImpl repoCoachService);

  @Binds @IntoMap @ViewModelKey(ManageViewModel.class)
  abstract ViewModel bindManageViewModel(ManageViewModel manageViewModel);


  @Binds @IntoMap @ViewModelKey(ScheduleDetailVM.class)
  abstract ViewModel bindCourseDetailViewModel(ScheduleDetailVM model);

  @Binds @IntoMap @ViewModelKey(BatchFragmentVM.class)
  abstract ViewModel bindBatchFragmentVM(BatchFragmentVM model);


}
