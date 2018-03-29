package cn.qingchengfit.shop.ui.product;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.base.ShopPermissionUtils;
import cn.qingchengfit.shop.databinding.PageShopProductBinding;
import cn.qingchengfit.shop.ui.items.product.GoodProductItem;
import cn.qingchengfit.shop.ui.product.addsuccess.ProductAddSuccessPageParams;
import cn.qingchengfit.shop.ui.product.bottom.ShopBottomCategoryFragment;
import cn.qingchengfit.shop.ui.product.choosepic.MultiChoosePicFragment;
import cn.qingchengfit.shop.ui.product.deliverchannel.ProductDeliverPageParams;
import cn.qingchengfit.shop.ui.product.paycardchannel.ProductPayPageParams;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.DensityUtil;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/product") public class ShopProductPage
    extends ShopBaseFragment<PageShopProductBinding, ShopProductViewModel> {
  private static final int TO_DELIVER_CODE = 101;
  private static final int TO_CARD_CODE = 102;
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.payChannelEvent.observe(this, aVoid -> {
      routeTo("/product/paychannel", null);
    });
    mViewModel.addToCategory.observe(this, aVoid -> {
      if (!permissionModel.check(ShopPermissionUtils.COMMODITY_CATEGORY)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      ShopBottomCategoryFragment shopBottomCategoryFragment =
          ShopBottomCategoryFragment.newInstance(mViewModel.getProduct().getCategory_id());
      shopBottomCategoryFragment.setConfimAction(data -> {
        if (data instanceof Category) {
          mViewModel.getProduct().setCategory((Category) data);
          mViewModel.getProduct().setCategory_id(data.getId());
          mBinding.productCetegory.setContent(mViewModel.getProduct().getCategory().getName());
        }
      });
      shopBottomCategoryFragment.show(getChildFragmentManager(), "");
    });
    mViewModel.addImagesEvent.observe(this, aVoid -> {
      MultiChoosePicFragment multiChoosePicFragment =
          MultiChoosePicFragment.newInstance(mViewModel.getProduct().getImages());
      multiChoosePicFragment.setUpLoadImageCallback(uris -> {
        if (uris != null && !uris.isEmpty()) {
          initViewPager(uris);
          mBinding.addImageContainer.setVisibility(View.GONE);
          mBinding.fabToCamera.setVisibility(View.VISIBLE);
          mViewModel.getProduct().setImages(uris);
        }
      });
      multiChoosePicFragment.show(getChildFragmentManager(), "");
    });
    mViewModel.deliverChannelEvent.observe(this, aVoid -> {
      Uri uri = Uri.parse("qcstaff://shop/product/deliver");

      toOtherFragmentForBack(uri, new ProductDeliverPageParams().delivers(
          (ArrayList<Integer>) mViewModel.getProduct().getDelivery_types()).build(),
          TO_DELIVER_CODE);
    });
    mViewModel.chooseCardEvent.observe(this, aVoid -> {
      Uri uri = Uri.parse("qcstaff://shop/product/paycard");
      List<Integer> card_tpl_ids = mViewModel.getProduct().getCard_tpl_ids();
      if (card_tpl_ids != null && !card_tpl_ids.isEmpty()) {
        toOtherFragmentForBack(uri,
            new ProductPayPageParams().ids(new ArrayList<>(card_tpl_ids)).build(), TO_CARD_CODE);
      } else {
        toOtherFragmentForBack(uri, null, TO_CARD_CODE);
      }
    });

    mViewModel.getPostProductResult().observe(this, aBoolean -> {
      if (aBoolean) {
        routeTo("/add/success",
            new ProductAddSuccessPageParams().status(mViewModel.getProduct().getProductStatus())
                .build());
        popBack();
      }
    });
  }

  protected boolean checkProductInfo(Product product) {
    if (product.getImages() == null || product.getImages().isEmpty()) {
      ToastUtils.show("请添加商品图片");
      return false;
    }
    if (TextUtils.isEmpty(product.getName())) {
      ToastUtils.show("请输入商品名称");
      return false;
    }
    if (TextUtils.isEmpty(product.getUnit())) {
      ToastUtils.show("请输入商品单位");
      return false;
    }
    if (product.getSupport_card()) {
      if (product.getCard_tpl_ids() == null || product.getCard_tpl_ids().isEmpty()) {
        ToastUtils.show("请选择支持的会员卡种类");
        return false;
      }
    }
    if (product.getGoods() != null && !product.getGoods().isEmpty()) {
      for (Good good : product.getGoods()) {
        if (TextUtils.isEmpty(good.getRmbPrices())) {
          ToastUtils.show("请输入商品价格");
          return false;
        }

        if (TextUtils.isEmpty(good.getName())) {
          if (goodsAdapter.getItemCount() == 1) {
            if (goodsAdapter.getItem(0) instanceof GoodProductItem) {
              boolean isExpend = ((GoodProductItem) goodsAdapter.getItem(0)).isExpend();
              if (isExpend) {
                ToastUtils.show("请输入规格名称");
                return false;
              }
            }
          } else {
            ToastUtils.show("请输入规格名称");
            return false;
          }
        }

        if (good.getInventory() == null) {
          ToastUtils.show("请输入规格库存");
          return false;
        }
        if (product.getSupport_card() && TextUtils.isEmpty(good.getCardPrices())) {
          ToastUtils.show("请输入会员卡支付价格");
          return false;
        }
      }
    }
    if (product.getDelivery_types() == null || product.getDelivery_types().isEmpty()) {
      ToastUtils.show("请选择交货方式");
      return false;
    }
    return true;
  }

  protected void toOtherFragmentForBack(Uri uri, Bundle params, int requestCode) {
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    if (params != null) {
      intent.putExtras(params);
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    startActivityForResult(intent, requestCode);
  }

  @Override
  public PageShopProductBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopProductBinding.inflate(inflater, container, false);
    initToolBar();
    initGoodRecyclerView();
    initEditListener();
    mBinding.setViewModel(mViewModel);
    mBinding.productName.getEditText().setMaxWidth(DensityUtil.dip2px(getContext(), 500));
    return mBinding;
  }

  private void initEditListener() {
    RxTextView.afterTextChangeEvents(mBinding.productWeight.getEditText()).subscribe(it -> {
      String s = it.editable().toString();
      if (TextUtils.isEmpty(s)) {
        mViewModel.getProduct().setPriority(0);
      } else {
        try {
          int weight = Integer.valueOf(s);
          if (weight > 10000000) {
            ToastUtils.show("权重不能超过10000000");
            mBinding.productWeight.setContent(s.substring(0, s.length() - 1));
            return;
          } else {
            mViewModel.getProduct().setPriority(weight);
          }
        } catch (NumberFormatException e) {
          ToastUtils.show("请输入正确数字");
        }
      }
    });

    mBinding.productName.addTextWatcher(new GoodProductItem.AfterTextWatcher() {
      @Override public void afterTextChanged(Editable s) {
        String trim = s.toString().trim();
        if (trim.length() > 20) {
          ToastUtils.show("商品名称不能大于20个字符");
          mBinding.productName.setContent(trim.substring(0, 20));
        }
      }
    });
    mBinding.productUnit.addTextWatcher(new GoodProductItem.AfterTextWatcher() {
      @Override public void afterTextChanged(Editable s) {
        String trim = s.toString().trim();
        if (trim.length() > 10) {
          ToastUtils.show("商品单位不能大于个10字符");
          mBinding.productUnit.setContent(trim.substring(0, 10));
        }
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == TO_DELIVER_CODE) {
        ArrayList<Integer> deliver = data.getIntegerArrayListExtra("delivers");
        dealDeliverTypes(deliver);
      } else if (requestCode == TO_CARD_CODE) {
        ArrayList<Integer> ids = data.getIntegerArrayListExtra("card_tpls");
        dealCardTplIds(ids);
      }
    }
  }

  protected void dealCardTplIds(ArrayList<Integer> ids) {
    if (ids == null || ids.isEmpty()) {
      mBinding.productCardTpl.setContent("");
      mViewModel.getProduct().setCard_tpl_ids(new ArrayList<>());
    } else {
      mBinding.productCardTpl.setContent(ids.size() + "种");
      mViewModel.getProduct().setCard_tpl_ids(ids);
    }
  }

  protected void dealDeliverTypes(ArrayList<Integer> deliver) {
    StringBuilder sb = new StringBuilder();
    if (deliver.contains(1)) {
      sb.append(getString(R.string.deliver_onsale)).append(" ");
    }
    if (deliver.contains(2)) {
      sb.append(getString(R.string.deliver_by_self)).append(" ");
    }
    if (deliver.contains(3)) {
      sb.append(getString(R.string.deliver_send)).append(" ");
    }
    mViewModel.getProduct().setDelivery_types(deliver);
    mBinding.productDeliver.setContent(sb.toString());
  }

  public CommonFlexAdapter goodsAdapter;

  private void initGoodRecyclerView() {
    List<GoodProductItem> goods = new ArrayList<>();
    goods.add(new GoodProductItem(new Good()));
    mBinding.goodsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.goodsRecyclerview.setAdapter(goodsAdapter = new CommonFlexAdapter(goods));
    mBinding.addProductCategory.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (goodsAdapter.getItemCount() == 1 && !((GoodProductItem) goodsAdapter.getItem(
            0)).isExpend()) {
          ((GoodProductItem) goodsAdapter.getItem(0)).setExpend(true);
        } else {
          goodsAdapter.addItem(new GoodProductItem(new Good()));
        }
        goodsAdapter.notifyDataSetChanged();
        ViewUtil.resetRecyclerViewHeight(mBinding.goodsRecyclerview);
      }
    });
    mBinding.priceCardSwitch.setOnCheckListener((buttonView, isChecked) -> {
      if (isChecked) {
        goodsAdapter.setStatus(GoodProductItem.SHOW_CARD_PRICE);
      } else {
        goodsAdapter.setStatus(0);
      }
      mBinding.productCardTpl.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      mViewModel.getProduct().setSupport_card(isChecked);
      goodsAdapter.notifyDataSetChanged();
      ViewUtil.resetRecyclerViewHeight(mBinding.goodsRecyclerview);
    });
  }

  protected void initToolBar() {
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private PagerAdapter adapter;

  protected void initViewPager(List<String> uris) {
    if (uris != null && !uris.isEmpty()) {
      adapter = new ImageViewAdapter(uris);
      mBinding.viewpager.setAdapter(adapter);
      mBinding.viewpager.setCurrentItem(0);
      adapter.notifyDataSetChanged();
    }
  }

  private class ImageViewAdapter extends PagerAdapter {
    private List<String> uris;

    public ImageViewAdapter(List<String> uris) {
      this.uris = uris;
    }

    @Override public int getCount() {
      return uris.size();
    }

    @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
      return view == object;
    }

    @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
      FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(container.getContext())
          .inflate(R.layout.item_empty_image, null);
      ImageView imageView = frameLayout.findViewById(R.id.image);
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      container.addView(frameLayout);
      imageView.setTag(R.id.image, position);
      Glide.with(imageView.getContext())
          .load(uris.get(position))
          .asBitmap()
          .placeholder(cn.qingchengfit.widgets.R.drawable.img_loadingimage)
          .into(imageView);
      return frameLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      container.removeView((View) object);
    }
  }
}
