package cn.qingchengfit.shop.ui.product;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.base.ShopBaseViewModel;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.shop.vo.ProductWrapper;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductViewModel extends ShopBaseViewModel {
  public final ObservableBoolean isEdit = new ObservableBoolean(true);
  public final ActionLiveEvent payChannelEvent = new ActionLiveEvent();
  public final ActionLiveEvent addToCategory = new ActionLiveEvent();
  public final ActionLiveEvent addImagesEvent = new ActionLiveEvent();
  public final ActionLiveEvent deliverChannelEvent = new ActionLiveEvent();
  public final ActionLiveEvent saveProductEvent = new ActionLiveEvent();
  public final ActionLiveEvent chooseCardEvent = new ActionLiveEvent();
  public final ActionLiveEvent detailEvent = new ActionLiveEvent();
  private ObservableField<Product> product = new ObservableField<>();

  public MediatorLiveData<Boolean> getPostProductResult() {
    return postProductResult;
  }

  private MediatorLiveData<Boolean> postProductResult = new MediatorLiveData<>();
  public  MediatorLiveData<Boolean> putProductResult = new MediatorLiveData<>();
  public  MediatorLiveData<Boolean> putProductStatus = new MediatorLiveData<>();


  public MediatorLiveData<Boolean> getDeleteProductResult() {
    return deleteProductResult;
  }

  private MediatorLiveData<Boolean> deleteProductResult = new MediatorLiveData<>();

  public LiveData<Product> getLoadProductResult() {
    return loadProductResult;
  }

  private MediatorLiveData<Product> loadProductResult = new MediatorLiveData<>();
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  @Inject public ShopProductViewModel() {
  }

  public void setProduct(Product product) {
    this.product.set(product);
  }

  public Product getProduct() {
    if (product.get() == null) {
      product.set(new Product());
    }
    return product.get();
  }

  public void chooseImageClick() {
    addImagesEvent.call();
  }

  public void toPayChannel() {
    payChannelEvent.call();
  }

  public void toPriceDetail() {

  }

  public void toProductDesc() {
    detailEvent.call();
  }

  public void chooseCardTpl() {
    chooseCardEvent.call();
  }

  public void toCategory() {
    addToCategory.call();
  }

  public void toDeliverChannel() {
    deliverChannelEvent.call();
  }

  public void toSaveProduct(Boolean status) {
    product.get().setStatus(status);
    saveProductEvent.call();
  }

  public void loadProduct(String id) {
    LiveData<ProductWrapper> productLiveData =
        repository.qcLoadProductInfo(loginStatus.staff_id(), id, gymWrapper.getParams());
    loadProductResult.addSource(productLiveData, product1 -> {
      loadProductResult.setValue(product1.product);
      loadProductResult.removeSource(productLiveData);
    });
  }

  public void saveProduct() {
    LiveData<Boolean> booleanLiveData =
        repository.qcPostProduct(loginStatus.staff_id(), product.get(), gymWrapper.getParams());
    postProductResult.addSource(booleanLiveData, aBoolean -> {
      postProductResult.setValue(aBoolean);
      postProductResult.removeSource(booleanLiveData);
    });
  }
  public void putProduct() {
    LiveData<Boolean> booleanLiveData =
        repository.qcPutProduct(loginStatus.staff_id(), product.get(), gymWrapper.getParams());
    putProductResult.addSource(booleanLiveData, aBoolean -> {
      putProductResult.setValue(aBoolean);
      putProductResult.removeSource(booleanLiveData);
    });
  }
  public void changeProductSaleStatus(boolean status){
    Map<String, Object> statusParams = new HashMap<>();
    statusParams.put("status", status);
    LiveData<Boolean> booleanLiveData =
        repository.qcPutProductStatus(loginStatus.staff_id(),product.get().getProductId(),statusParams, gymWrapper.getParams());
    putProductStatus.addSource(booleanLiveData, aBoolean -> {
      putProductStatus.setValue(aBoolean);
      putProductStatus.removeSource(booleanLiveData);
    });
  }
  public void deleteProduct(String id) {
    LiveData<Boolean> booleanLiveData =
        repository.qcDeleteProduct(loginStatus.staff_id(), id, gymWrapper.getParams());
    deleteProductResult.addSource(booleanLiveData, aBoolean -> {
      deleteProductResult.setValue(aBoolean);
      deleteProductResult.removeSource(booleanLiveData);
    });
  }

  @Override protected void onCleared() {
    Log.d("TAG", "onCleared: ");
  }
}
