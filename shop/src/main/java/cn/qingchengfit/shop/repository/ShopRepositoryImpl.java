package cn.qingchengfit.shop.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.ICardShopChooseItemData;
import cn.qingchengfit.saasbase.common.mvvm.LiveDataReactiveStreams;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.student.other.RxHelper;
import cn.qingchengfit.shop.repository.remote.ShopRemoteRepository;
import cn.qingchengfit.shop.repository.response.RecordListResponse;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by huangbaole on 2017/12/19.
 */
@Singleton
public class ShopRepositoryImpl implements ShopRepository {

  @Inject ShopRemoteRepository remoteService;
  @Inject ICardModel cardModel;

  @Inject public ShopRepositoryImpl() {
  }

  private static <T> LiveData<T> toLiveData(Flowable<QcDataResponse<T>> flowable) {
    return LiveDataReactiveStreams.fromPublisher(
        flowable.compose(Rx2Helper.schedulersTransformer()));
  }

  @Override public LiveData<List<Product>> qcLoadProductList(String staff_id,
      HashMap<String, Object> params) {
    return Transformations.map(toLiveData(remoteService.qcLoadProducts(staff_id, params)),
        input -> input.products);
  }

  @Override
  public LiveData<List<Category>> qcLoadCategories(String staff_id, HashMap<String, Object> map) {
    return Transformations.map(toLiveData(remoteService.qcLoadCategories(staff_id, map)),
        input -> input.categories);
  }

  @Override public LiveData<Boolean> qcPostCategory(String staff_id, Category category,
      HashMap<String, Object> map) {
    return toLiveData(remoteService.qcPostCategory(staff_id, category, map).map(qcDataResponse -> {
      qcDataResponse.setData(qcDataResponse.getStatus() == 200);
      return qcDataResponse;
    }));
  }

  @Override public LiveData<Boolean> qcDeleteCategory(String staff_id, String category_id,
      HashMap<String, Object> map) {
    return toLiveData(
        remoteService.qcDeleteCategory(staff_id, category_id, map).map(qcDataResponse -> {
          qcDataResponse.setData(qcDataResponse.getStatus() == 200);
          return qcDataResponse;
        }));
  }

  @Override
  public LiveData<Boolean> qcPutCategory(String staff_id, String category_id, Category category,
      HashMap<String, Object> map) {
    return toLiveData(
        remoteService.qcPutCategory(staff_id, category_id, category, map).map(qcDataResponse -> {
          qcDataResponse.setData(qcDataResponse.getStatus() == 200);
          return qcDataResponse;
        }));
  }

  @Override public LiveData<RecordListResponse> qcLoadInventoryRecord(String staff_id,
      HashMap<String, Object> params) {
    return toLiveData(remoteService.qcLoadInventoryRecords(staff_id, params));
  }

  @Override public LiveData<Boolean> qcUpdateInventoryRecord(String staff_id,
      HashMap<String, Object> params) {
    return toLiveData(
        remoteService.qcUpdateInventoryRecord(staff_id, params).map(qcDataResponse -> {
          qcDataResponse.setData(qcDataResponse.getStatus() == 200);
          return qcDataResponse;
        }));
  }

  @Override
  public LiveData<List<Good>> qcLoadGoodInfo(String staff_id, HashMap<String, Object> params) {
    return Transformations.map(toLiveData(remoteService.qcLoadGoodInfo(staff_id, params)),
        input -> input.goods);
  }

  @Override public LiveData<List<Product>> qcLoadAllProductInfo(String staff_id,
      HashMap<String, Object> params) {
    return Transformations.map(toLiveData(remoteService.qcLoadAllProductInfo(staff_id, params)),
        input -> input.products);
  }

  @Override public LiveData<Boolean> qcPostProduct(String staff_id, Product json,
      HashMap<String, Object> map) {
    return toLiveData(remoteService.qcPostProduct(staff_id, map, json).map(qcDataResponse -> {
      qcDataResponse.setData(qcDataResponse.getStatus() == 200);
      return qcDataResponse;
    }));
  }

  @Override public LiveData<Boolean> qcDeleteProduct(String staff_id, String product_id,
      HashMap<String, Object> map) {
    return toLiveData(
        remoteService.qcDeleteProduct(staff_id, map, product_id).map(qcDataResponse -> {
          qcDataResponse.setData(qcDataResponse.getStatus() == 200);
          return qcDataResponse;
        }));
  }

  @Override public LiveData<Boolean> qcPutProduct(String staff_id, Product json,
      HashMap<String, Object> map) {
    return toLiveData(remoteService.qcPutProduct(staff_id, map, json).map(qcDataResponse -> {
      qcDataResponse.setData(qcDataResponse.getStatus() == 200);
      return qcDataResponse;
    }));
  }

  @Override public LiveData<Product> qcLoadProductInfo(String staff_id, String product_id,
      HashMap<String, Object> map) {
    return toLiveData(remoteService.qcLoadProductInfo(staff_id, map, product_id));
  }

  @Override
  public LiveData<List<ICardShopChooseItemData>> qcLoadCardTpls(String type, String isEnable) {
    return Transformations.map(
        LiveDataReactiveStreams.fromPublisher(cardModel.qcGetCardTpls(type, isEnable).compose(
            RxHelper.schedulersTransformer())),
        input -> new ArrayList<>(input.card_tpls));

  }
}
