package com.example.huangbaole.shop.repository;

import com.example.huangbaole.shop.repository.remote.ShopRemoteRepositoryImpl;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by huangbaole on 2017/12/19.
 */
@Singleton public class ShopRepositoryImpl implements ShopRepository {

  @Inject ShopRemoteRepositoryImpl remoteService;

  @Inject public ShopRepositoryImpl() {
  }
}
