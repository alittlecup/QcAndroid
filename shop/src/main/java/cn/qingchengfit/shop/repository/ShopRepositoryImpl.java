package cn.qingchengfit.shop.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.ICardShopChooseItemData;
import cn.qingchengfit.saasbase.common.mvvm.LiveDataReactiveStreams;
import cn.qingchengfit.shop.repository.remote.ShopRemoteRepository;
import cn.qingchengfit.shop.repository.response.RecordListResponse;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.Channel;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/19.
 */
 public class ShopRepositoryImpl implements ShopRepository {

  @Inject ShopRemoteRepository remoteService;
  //@Inject ICardModel cardModel;

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

  @Override public LiveData<Boolean> qcPostCategory(String staff_id, Category category) {
    return toLiveData(remoteService.qcPostCategory(staff_id, category).map(qcDataResponse -> {
      qcDataResponse.setData(qcDataResponse.getStatus() == 200);
      return qcDataResponse;
    }));
  }

  @Override public LiveData<Boolean> qcDeleteCategory(String staff_id, int category_id) {
    return toLiveData(remoteService.qcDeleteCategory(staff_id, category_id).map(qcDataResponse -> {
      qcDataResponse.setData(qcDataResponse.getStatus() == 200);
      return qcDataResponse;
    }));
  }

  @Override
  public LiveData<Boolean> qcPutCategory(String staff_id, int category_id, Category category) {
    return toLiveData(
        remoteService.qcPutCategory(staff_id, category_id, category).map(qcDataResponse -> {
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

  @Override public LiveData<Boolean> qcPostProduct(String staff_id, String json) {
    return toLiveData(remoteService.qcPostProduct(staff_id, json).map(qcDataResponse -> {
      qcDataResponse.setData(qcDataResponse.getStatus() == 200);
      return qcDataResponse;
    }));
  }

  @Override public LiveData<Boolean> qcDeleteProduct(String staff_id, String product_id) {
    return toLiveData(remoteService.qcDeleteProduct(staff_id, product_id).map(qcDataResponse -> {
      qcDataResponse.setData(qcDataResponse.getStatus() == 200);
      return qcDataResponse;
    }));
  }

  @Override public LiveData<Boolean> qcPutProduct(String staff_id, String json) {
    return toLiveData(remoteService.qcPutProduct(staff_id, json).map(qcDataResponse -> {
      qcDataResponse.setData(qcDataResponse.getStatus() == 200);
      return qcDataResponse;
    }));
  }

  @Override public LiveData<Product> qcLoadProductInfo(String staff_id, String product_id) {
    //return toLiveData(remoteService.qcLoadProductInfo(staff_id, product_id));
    MutableLiveData<Product> productLiveData = new MutableLiveData<>();
    Product product = new Product();
    product.setName("name");
    product.setUnit("22");
    product.setSupport_card(true);
    ArrayList<String> arrayList = new ArrayList<>();
    arrayList.add("333");
    arrayList.add("333");
    arrayList.add("333");
    product.setCard_tpl_ids(arrayList);
    product.setPriority(33);
    ArrayList<Integer> list = new ArrayList<>();
    list.add(2);
    list.add(3);
    product.setDelivery_types(list);
    Category category = new Category();
    category.setName("category");
    product.setCategory(category);
    product.setDesc("adsffffffffffffffdasfads");
    List<Good> goods = new ArrayList<>();
    Good good = new Good();
    good.setName("good");
    good.setPrice(33.33, Channel.CARD);
    good.setPrice(44, Channel.RMB);
    good.setInventory(33);
    goods.add(good);
    product.setGoods(goods);
    productLiveData.setValue(product);

    return productLiveData;
  }

  @Override
  public LiveData<List<ICardShopChooseItemData>> qcLoadCardTpls(String type, String isEnable) {
    //return Transformations.map(
    //    LiveDataReactiveStreams.fromPublisher(cardModel.qcGetCardTpls(type, isEnable)),
    //    input -> new ArrayList<>(input.card_tpls));
    MutableLiveData<List<ICardShopChooseItemData>> liveData = new MutableLiveData<>();
    List<ICardShopChooseItemData> datas = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      int pos = i;
      datas.add(new ICardShopChooseItemData() {
        @Override public String getShopCardTplId() {
          return pos + "id";
        }

        @Override public String getShopCardTplName() {
          return pos + "位置";
        }
      });
    }
    liveData.setValue(datas);
    return liveData;
  }
}
