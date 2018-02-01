package cn.qingchengfit.shop.ui.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by huangbaole on 2018/1/31.
 */
@Leaf(module = "shop",path = "/product/add")
public class ShopProductAddPage extends ShopProductPage {

  @Override protected Class<ShopProductViewModel> getVMClass() {
    return ShopProductViewModel.class;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.addImageContainer.setVisibility(View.VISIBLE);
  }

  @Override protected void initToolBar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.add_product)));
    super.initToolBar();
  }
}
