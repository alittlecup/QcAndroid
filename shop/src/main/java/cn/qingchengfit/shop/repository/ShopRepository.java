package cn.qingchengfit.shop.repository;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.shop.repository.response.RecordListResponse;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/18.
 */

public interface ShopRepository {
  LiveData<List<Product>> qcLoadProductList(String staff_id, HashMap<String, Object> params);

  LiveData<List<Category>> qcLoadCategories(String staff_id, HashMap<String, Object> map);

  LiveData<Boolean> qcPostCategory(String staff_id, Category category);

  LiveData<Boolean> qcDeleteCategory(String staff_id, int category_id);

  LiveData<Boolean> qcPutCategory(String staff_id, int category_id, Category category);

  LiveData<RecordListResponse> qcLoadInventoryRecord(String staff_id,
      HashMap<String, Object> params);

  LiveData<Boolean> qcUpdateInventoryRecord(String staff_id, HashMap<String, Object> params);

  LiveData<List<Good>> qcLoadGoodInfo(String staff_id, HashMap<String, Object> params);

  LiveData<List<Product>> qcLoadAllProductInfo(String staff_id, HashMap<String, Object> params);
}
