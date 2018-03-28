package cn.qingchengfit.shop.ui.product;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopPermissionUtils;
import cn.qingchengfit.shop.ui.items.product.GoodProductItem;
import cn.qingchengfit.shop.ui.product.productdetail.ShopProductModifyDetailPageParams;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2018/2/1.
 */
@Leaf(module = "shop", path = "/product/modify") public class ShopProductModifyPage
    extends ShopProductPage {
  @Need String productId;
  @Need Boolean productStatus;//true 出售中，false 已下架
  @Inject IPermissionModel permissionModel;

  @Override protected Class<ShopProductViewModel> getVMClass() {
    return ShopProductViewModel.class;
  }

  @Override protected void subscribeUI() {
    super.subscribeUI();
    mViewModel.getLoadProductResult().observe(this, product -> {
      mViewModel.setProduct(product);
      changeUI(product);
    });

    mViewModel.getDeleteProductResult().observe(this, aBoolean -> {
      if (aBoolean) {
        ToastUtils.show("删除成功");
        getActivity().onBackPressed();
      }
    });
    mViewModel.putProductStatus.observe(this, aBoolean -> {
      if (aBoolean) {
        ToastUtils.show("操作成功");
        getActivity().onBackPressed();
      }
    });
    mViewModel.detailEvent.observe(this, aVoid -> {
      Uri uri = Uri.parse("qcstaff://shop/modify/detail");
      toOtherFragmentForBack(uri,
          new ShopProductModifyDetailPageParams().content(mViewModel.getProduct().getDesc())
              .build(), 202);
    });
    mViewModel.putProductResult.observe(this, aBoolean -> {
      if (aBoolean) {
        ToastUtils.show("保存成功");
        Toolbar toolbar = mBinding.includeToolbar.toolbar;
        toolbar.getMenu().getItem(0).setTitle("编辑");
        inUpdate = false;
        setCurPageStatus(false);
      }
    });
  }

  private void changeUI(Product product) {
    initViewPager(product.getImages());
    mBinding.priceCardSwitch.setOpen(product.getSupport_card());
    if (product.getSupport_card()) {
      dealCardTplIds(new ArrayList<>(product.getCard_tpl_ids()));
    }
    if (product.getSupport_card()) {
      goodsAdapter.setStatus(GoodProductItem.SHOW_CARD_PRICE);
    } else {
      goodsAdapter.setStatus(0);
    }
    updateGoodList();
    if (product.getCategory() != null) {
      mBinding.productCetegory.setContent(product.getCategory().getName());
    }
    mBinding.productWeight.setContent(
        product.getPriority() == null ? "" : product.getPriority().toString());
    dealDeliverTypes(new ArrayList<>(product.getDelivery_types()));

    mBinding.productName.setContent(mViewModel.getProduct().getName());
    mBinding.productUnit.setContent(mViewModel.getProduct().getUnit());

    if (!TextUtils.isEmpty(product.getDesc())) {
      if (!product.getDesc().contains("<p>")) {
        mBinding.productDesc.setContent("图片");
      } else {
        mBinding.productDesc.setContent(CmStringUtils.delHTMLTag(product.getDesc()));
      }
    }
  }

  private void updateGoodList() {
    List<Good> goods = mViewModel.getProduct().getGoods();
    List<GoodProductItem> items = new ArrayList<>();
    for (Good good : goods) {
      items.add(new GoodProductItem(good));
    }
    if (inUpdate) {
      goodsAdapter.setTag(GoodProductItem.HIDE_DELETE_KEY, false);
    } else {
      goodsAdapter.setTag(GoodProductItem.HIDE_DELETE_KEY, true);
    }
    goodsAdapter.updateDataSet(items);
    mBinding.goodsRecyclerview.postDelayed(new Runnable() {
      @Override public void run() {
        ViewUtil.resetRecyclerViewHeight(mBinding.goodsRecyclerview);
      }
    }, 50);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    mViewModel.loadProduct(productId);
    mBinding.framelayoutClick.setVisibility(View.VISIBLE);
    AppUtils.hideKeyboard(getActivity());
    initBottom();
    return view;
  }

  private void initBottom() {
    mBinding.llBottomContainer.setVisibility(View.GONE);
    mBinding.llBottomContainerModify.setVisibility(View.VISIBLE);
    mBinding.fabToCamera.setVisibility(View.GONE);

    mBinding.buttonDelete.setText(getString(R.string.delete));
    mBinding.buttonSale.setText(productStatus ? "下架" : "上架");
    mBinding.buttonDelete.setOnClickListener(v -> {
      if (!permissionModel.check(ShopPermissionUtils.COMMODITY_LIST_CAN_DELETE)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      ViewUtil.instanceDelDialog(getContext(), "确定删除商品？", (dialog, which) -> {
        mViewModel.deleteProduct(productId);
      }).show();
    });
    mBinding.buttonSale.setOnClickListener(v -> {
      if (!permissionModel.check(ShopPermissionUtils.COMMODITY_CATEGORY_CAN_CHANGE)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      mViewModel.changeProductSaleStatus(!productStatus);
    });
  }

  private boolean inUpdate = false;

  @Override protected void initToolBar() {
    ToolbarModel toolbarModel = new ToolbarModel(getString(R.string.product_detail));
    toolbarModel.setMenu(R.menu.menu_edit);
    toolbarModel.setListener(item -> {
      if (!permissionModel.check(ShopPermissionUtils.COMMODITY_CATEGORY_CAN_CHANGE)) {
        showAlert(R.string.sorry_for_no_permission);
        return false;
      }
      if (item.getTitle().equals("编辑")) {
        item.setTitle("完成");
        inUpdate = true;
        setCurPageStatus(inUpdate);
      } else if (item.getTitle().equals("完成")) {
        putProduct();
      }
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    super.initToolBar();
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    toolbar.setNavigationOnClickListener(v -> {
      if (inUpdate) {
        ViewUtil.instanceDelDialog(getContext(), "确定要放弃当前修改么？", (dialog, which) -> {
          getActivity().onBackPressed();
        }).show();
      } else {
        getActivity().onBackPressed();
      }
    });
  }

  private void setCurPageStatus(boolean inUpdate) {
    mBinding.framelayoutClick.setVisibility(inUpdate ? View.GONE : View.VISIBLE);
    mBinding.fabToCamera.setVisibility(inUpdate ? View.VISIBLE : View.GONE);
    mBinding.llBottomContainerModify.setVisibility(inUpdate ? View.GONE : View.VISIBLE);
    if (!inUpdate) {
      AppUtils.hideKeyboard(getActivity());
      View currentFocus = getActivity().getCurrentFocus();
      if (currentFocus != null) {
        currentFocus.clearFocus();
      }
    }
    goodsAdapter.setTag(GoodProductItem.HIDE_DELETE_KEY, !inUpdate);
    goodsAdapter.notifyDataSetChanged();
  }

  private void putProduct() {
    List<Good> goods = new ArrayList<>();
    for (Object item : goodsAdapter.getMainItems()) {
      if (item instanceof GoodProductItem) {
        Good good = ((GoodProductItem) item).getGood();
        // 移除会员卡价格
        if (!mViewModel.getProduct().getSupport_card()) {
          good.removeCardPrice();
        }
        goods.add(good);
      }
    }
    mViewModel.getProduct().setGoods(goods);
    if (checkProductInfo(mViewModel.getProduct())) {
      mViewModel.putProduct();
    }
  }
}
