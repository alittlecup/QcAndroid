package cn.qingchengfit.shop.ui.product;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopProductBinding;
import cn.qingchengfit.shop.ui.widget.CategoryItemView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/product") public class ShopProductPage
    extends ShopBaseFragment<PageShopProductBinding, ShopProductViewModel> {
  @Need Boolean isUpdate=false;

  @Override protected void subscribeUI() {
    mViewModel.payChannelEvent.observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/product/paychannel");
      routeTo(uri, null);
    });
  }

  @Override
  public PageShopProductBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopProductBinding.inflate(inflater, container, false);
    initToolBar();
    initViewPager();
    initCategoryView();
    if (isUpdate) {
      //mViewModel.loadProductDetail(id);
    }
    mBinding.setViewModel(mViewModel);
    return mBinding;
  }

  private void initToolBar() {

    if (isUpdate) {
      ToolbarModel toolbarModel = new ToolbarModel(getString(R.string.product_detail));
      toolbarModel.setMenu(R.menu.menu_edit);
      toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          if (item.getItemId() == R.id.action_save) {
            toolbarModel.setMenu(R.menu.menu_compelete);
            mViewModel.isEdit.set(true);
          } else if (item.getItemId() == R.id.complete) {
            // TODO: 2018/1/17  提交修改后的商品数据
            mViewModel.isEdit.set(false);
          }
          return false;
        }
      });
      mBinding.setToolbarModel(toolbarModel);
    } else {
      mBinding.setToolbarModel(new ToolbarModel(getString(R.string.add_product)));
    }
    initToolbar(mBinding.includeToolbar.toolbar);
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
