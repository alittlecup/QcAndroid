package com.example.huangbaole.shop.di;

import com.example.huangbaole.shop.repository.ShopRepository;
import com.example.huangbaole.shop.repository.ShopRepositoryImpl;
import com.example.huangbaole.shop.repository.remote.ShopRemoteRepository;
import com.example.huangbaole.shop.repository.remote.ShopRemoteRepositoryImpl;
import dagger.Binds;
import dagger.Module;

/**
 * Created by huangbaole on 2017/12/19.
 */
@Module public abstract class ShopModule {
  @Binds
  abstract ShopRemoteRepository bindShopRemoteService(ShopRemoteRepositoryImpl remoteResitory);
  @Binds
  abstract ShopRepository bindShopRepository(ShopRepositoryImpl repository);
}
