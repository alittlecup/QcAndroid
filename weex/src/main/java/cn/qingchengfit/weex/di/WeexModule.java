package cn.qingchengfit.weex.di;

import cn.qingchengfit.weex.ui.WxPageActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by huangbaole on 2018/2/28.
 */
@Module public abstract class WeexModule {
  @ContributesAndroidInjector abstract WxPageActivity contributesWxPageActivty();
}
