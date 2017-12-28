package cn.qingchengfit.shop.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopProductBinding;
import cn.qingchengfit.shop.ui.widget.CategoryItemView;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/product") public class ShopProductPage
    extends ShopBaseFragment<PageShopProductBinding, ShopProductViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageShopProductBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopProductBinding.inflate(inflater, container, false);
    initViewPager();
    initCategoryView();
    return mBinding;
  }

  private void initCategoryView() {
    mBinding.addProductCategory.setOnClickListener(view -> {
      CategoryItemView childAt = (CategoryItemView) mBinding.llCategoryContainer.getChildAt(0);
      if (mBinding.llCategoryContainer.getChildCount() == 1 && !childAt.isCategoryVisible()) {
        childAt.setCategoryVisible(true);
        childAt.setDeleteVisible(true);
      } else {
        mBinding.llCategoryContainer.addView(new CategoryItemView(getContext()));
      }
    });
    CategoryItemView categoryItemIView = new CategoryItemView(getContext());
    categoryItemIView.setCategoryVisible(false);
    categoryItemIView.setDeleteVisible(false);
    mBinding.llCategoryContainer.addView(categoryItemIView);
  }

  private void initViewPager() {

  }
}
