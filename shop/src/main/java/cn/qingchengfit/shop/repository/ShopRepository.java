package cn.qingchengfit.shop.repository;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.shop.repository.response.RecordListResponse;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.shop.vo.ProductWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangbaole on 2017/12/18.
 */

public interface ShopRepository {
  LiveData<List<Product>> qcLoadProductList(String staff_id, HashMap<String, Object> params);

  LiveData<List<Category>> qcLoadCategories(String staff_id, HashMap<String, Object> map);

  LiveData<QcDataResponse> qcPostCategory(String staff_id, Category category,
      HashMap<String, Object> map);

  LiveData<Boolean> qcDeleteCategory(String staff_id, String category_id,
      HashMap<String, Object> map);

  LiveData<Boolean> qcPutCategory(String staff_id, String category_id, Category category,
      HashMap<String, Object> map);

  LiveData<RecordListResponse> qcLoadInventoryRecord(String staff_id,
      HashMap<String, Object> params);

  LiveData<Boolean> qcUpdateInventoryRecord(String staff_id, HashMap<String, Object> params);

  LiveData<List<Good>> qcLoadGoodInfo(String staff_id, HashMap<String, Object> params);

  LiveData<List<Product>> qcLoadAllProductInfo(String staff_id, HashMap<String, Object> params);

  LiveData<Boolean> qcPostProduct(String staff_id, Product json, HashMap<String, Object> map);

  LiveData<Boolean> qcDeleteProduct(String staff_id, String product_id,
      HashMap<String, Object> map);

  LiveData<Boolean> qcPutProduct(String staff_id, Product json, HashMap<String, Object> map);

  LiveData<Boolean> qcPutProductStatus(String staff_id, String product_id,
      Map<String, Object> status, HashMap<String, Object> map);

  LiveData<ProductWrapper> qcLoadProductInfo(String staff_id, String product_id,
      HashMap<String, Object> map);

  //LiveData<List<ICardShopChooseItemData>> qcLoadCardTpls(String type, String isEnable);
}
