package cn.qingchengfit.shop.ui.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2018/2/1.
 */
@Leaf(module = "shop", path = "/product/modify") public class ShopProductModifyPage
    extends ShopProductPage {
  @Need String productId;
  @Need Boolean productStatus;//true 出售中，false 已下架

  @Override protected Class<ShopProductViewModel> getVMClass() {
    return ShopProductViewModel.class;
  }

  @Override protected void subscribeUI() {
    super.subscribeUI();
    mViewModel.getLoadProductResult().observe(this, product -> {
      mViewModel.setProduct(product);
      changeUI(product);
    });

    mViewModel.getDeleteProductResult().observe(this,aBoolean -> {
      ToastUtils.show("delete-->"+aBoolean);
    });

  }

  private void changeUI(Product product) {
    initViewPager(product.getImages());
    mBinding.priceCardSwitch.setOpen(product.getSupport_card());
    if (product.getSupport_card()) {
      dealCardTplIds(new ArrayList<>(product.getCard_tpl_ids()));
    }
    updateGoodList();
    if (product.getCategory() != null) {
      // TODO: 2018/2/1 分类规则
      mBinding.productCetegory.setContent(product.getCategory().getName());
    }
    mBinding.productWeight.setContent(
        product.getPriority() == null ? "" : product.getPriority().toString());
    dealDeliverTypes(new ArrayList<>(product.getDelivery_types()));
  }

  private void updateGoodList() {

  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewModel.loadProduct(productId);
    mBinding.framelayoutClick.setVisibility(View.VISIBLE);
    initBottom();
  }

  private void initBottom() {
    mBinding.buttonLeft.setText(getString(R.string.delete));
    mBinding.buttonRight.setText(productStatus ? "下架" : "上架");

    mBinding.buttonLeft.setOnClickListener(v -> mViewModel.deleteProduct(productId));
    mBinding.buttonRight.setOnClickListener(v -> {
      mViewModel.getProduct().setStatus(!productStatus);
      mViewModel.saveProduct();
    });
  }

  @Override protected void initToolBar() {
    ToolbarModel toolbarModel = new ToolbarModel(getString(R.string.product_detail));
    toolbarModel.setMenu(R.menu.menu_edit);
    toolbarModel.setListener(item -> {
      if (item.getTitle().equals("编辑")) {
        item.setTitle("完成");
        mBinding.framelayoutClick.setVisibility(View.GONE);
      } else if (item.getTitle().equals("完成")) {
        item.setTitle("编辑");
        mBinding.framelayoutClick.setVisibility(View.VISIBLE);
        AppUtils.hideKeyboard(getActivity());
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
          currentFocus.clearFocus();
        }
        mViewModel.saveProduct();
      }
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    super.initToolBar();
  }
}
