package cn.qingchengfit.shop.ui.product.productdetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.PageShopProductDetailBinding;
import cn.qingchengfit.views.fragments.RichTxtFragment;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by huangbaole on 2018/3/15.
 */
@Leaf(module = "shop", path = "/product/detail") public class ShopProductDetailPage
    extends SaasBaseFragment {
  RichTxtFragment richTxtFragment;
  PageShopProductDetailBinding mBinding;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding =
        DataBindingUtil.inflate(inflater, R.layout.page_shop_product_detail, container, false);
    richTxtFragment = new RichTxtFragment();
    getChildFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, richTxtFragment)
        .commit();
    initToolbar();

    return mBinding.getRoot();
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("商品描述详情"));
  }
}
