package cn.qingchengfit.shop.ui.product;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.ui.items.product.GoodProductItem;
import cn.qingchengfit.shop.ui.product.productdetail.ShopProductDetailPageParams;
import cn.qingchengfit.shop.vo.Good;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2018/1/31.
 */
@Leaf(module = "shop", path = "/product/add") public class ShopProductAddPage
    extends ShopProductPage {
  private static final int TO_PRODUCT_DETAIL = 103;

  @Override protected Class<ShopProductViewModel> getVMClass() {
    return ShopProductViewModel.class;
  }

  @Override protected void subscribeUI() {
    super.subscribeUI();
    mViewModel.detailEvent.observe(this, aVoid -> {
      Uri uri = Uri.parse("qcstaff://shop/product/detail");
      toOtherFragmentForBack(uri,
          new ShopProductDetailPageParams().content(mViewModel.getProduct().getDesc()).build(),
          TO_PRODUCT_DETAIL);
    });
    mViewModel.saveProductEvent.observe(this, aVoid -> {
      List<Good> goods = new ArrayList<>();
      for (Object item : goodsAdapter.getMainItems()) {
        if (item instanceof GoodProductItem) {
          Good good = ((GoodProductItem) item).getGood();
          // 移除会员卡价格
          if (!mViewModel.getProduct().getSupport_card()) {
            good.removeCardPrice();
          }
          if(goodsAdapter.getItemCount()==1&&!((GoodProductItem) item).isExpend()){
            good.setName("");
          }
          goods.add(good);
        }
      }
      mViewModel.getProduct().setGoods(goods);
      if (checkProductInfo(mViewModel.getProduct())) {
        mViewModel.saveProduct();
      }
    });
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.addImageContainer.setVisibility(View.VISIBLE);
  }

  @Override protected void initToolBar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.add_product)));
    super.initToolBar();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == TO_PRODUCT_DETAIL) {
        String detail = data.getStringExtra("detail");
        if (!TextUtils.isEmpty(detail)) {
          mViewModel.getProduct().setDesc(detail);
          mBinding.productDesc.setContent("已填写");
        }else {
          mViewModel.getProduct().setDesc("");
          mBinding.productDesc.setContent("");
        }
      }
    }
  }
}
