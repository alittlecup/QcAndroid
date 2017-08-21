package cn.qingchengfit.saasbase.staff.di;

import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.cardtypes.di.CardTplDI;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by fb on 2017/8/21.
 */

@Module public abstract class BindStaffModule {
  @ContributesAndroidInjector(modules = {
      StaffDI.StaffListFragmentModule.class,
  }) abstract SaasContainerActivity contributeSassContainerActivityInjector();
}
