package cn.qingchengfit.shop.di;

import cn.qingchengfit.weex.di.WeexModule;
import dagger.Module;

/**
 * Created by huangbaole on 2017/12/19.
 */
@Module(includes = {BindShopActivity.class, WeexModule.class}) public abstract class ShopModule {

}
