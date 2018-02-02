package cn.qingchengfit.shop.di;

import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.repository.ShopRepositoryImpl;
import cn.qingchengfit.shop.repository.remote.ShopRemoteRepository;
import cn.qingchengfit.shop.repository.remote.ShopRemoteRepositoryImpl;
import dagger.Binds;
import dagger.Module;

/**
 * Created by huangbaole on 2017/12/19.
 */
@Module(includes = { BindShopActivity.class, ShopViewModel.class })
public abstract class ShopModule {
  @Binds
  abstract ShopRemoteRepository bindShopRemoteService(ShopRemoteRepositoryImpl remoteResitory);

  @Binds abstract ShopRepository bindShopRepository(ShopRepositoryImpl repository);

  //@Binds abstract ICardModel bindCardModel(CardModel cardModel);
}
