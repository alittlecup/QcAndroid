package cn.qingchengfit.shop.ui.inventory.product;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageUpdateInventoryBinding;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/update/inventory") public class UpdateInventoryPage
    extends ShopBaseFragment<PageUpdateInventoryBinding, UpdateInventoryViewModel> {
  public static final int ADD = 0;
  public static final int REDUCE = 1;
  @Need @UpdateAction Integer action;

  @IntDef({ ADD, REDUCE }) @Retention(RetentionPolicy.SOURCE) private @interface UpdateAction {
  }

  @Override protected void subscribeUI() {

  }

  @Override
  public PageUpdateInventoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageUpdateInventoryBinding.inflate(inflater);
    initToolbar();
    return mBinding;
  }

  private void initToolbar() {
    String title = "";
    if (action == ADD) {
      title = getString(R.string.add_inventory);
    } else if (action == REDUCE) {
      title = getString(R.string.reduce_inventory);
    }
    mBinding.setToolbarModel(new ToolbarModel(title));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
