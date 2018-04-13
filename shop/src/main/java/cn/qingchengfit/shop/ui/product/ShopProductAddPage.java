package cn.qingchengfit.shop.ui.product;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.ui.items.product.GoodProductItem;
import cn.qingchengfit.shop.ui.product.productdetail.ShopProductDetailPageParams;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.ShopSensorsConstants;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangbaole on 2018/1/31.
 */
@Leaf(module = "shop", path = "/product/add") public class ShopProductAddPage
    extends ShopProductPage {
  private static final int TO_PRODUCT_DETAIL = 103;

  @Override protected Class<ShopProductViewModel> getVMClass() {
    return ShopProductViewModel.class;
  }

  long startTime = 0;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    startTime = System.currentTimeMillis() / 1000;
    SensorsUtils.track(ShopSensorsConstants.SHOP_COMMODITY_ADD_VISIT).commit(getContext());
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    setBackPressNull();
    if (startTime > 0) {
      SensorsUtils.track(ShopSensorsConstants.SHOP_COMMODITY_ADD_LEAVE)
          .addProperty(ShopSensorsConstants.QC_PAGE_STAY_TIME,
              System.currentTimeMillis() / 1000 - startTime)
          .commit(getContext());
    }
  }

  @Override protected void subscribeUI() {
    super.subscribeUI();
    mViewModel.detailEvent.observe(this, aVoid -> {
      Uri uri = Uri.parse(AppUtils.getCurAppSchema(getContext()) + "://shop/product/detail");
      String desc = mViewModel.getProduct().getDesc();
      if (!TextUtils.isEmpty(desc)) {
        if (!desc.contains("<p>")) {
          desc = desc + "<p> </p>";
        }
      }
      toOtherFragmentForBack(uri, new ShopProductDetailPageParams().content(desc).build(),
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
          if (goodsAdapter.getItemCount() == 1 && !((GoodProductItem) item).isExpend()) {
            good.setName("");
          }
          goods.add(good);
        }
      }
      mViewModel.getProduct().setGoods(goods);
      if (checkProductInfo(mViewModel.getProduct())) {
        mViewModel.saveProduct();
        if (mViewModel.getProduct().getProductStatus()) {
          SensorsUtils.track(ShopSensorsConstants.SHOP_ADD_AND_ACTIVATE_COMMODITY_CANCEL_BTN_CLICK)
              .commit(getContext());
        } else {
          SensorsUtils.track(ShopSensorsConstants.SHOP_ADD_COMMODITY_CONFIRM_BTN_CLICK)
              .commit(getContext());
        }
      }
    });
  }

  @Override public boolean onFragmentBackPress() {
    ViewUtil.instanceDelDialog(getContext(), getString(R.string.sure_give_up_add_product),
        (dialog, which) -> {
          sensorsTrack(ShopSensorsConstants.SHOP_ADD_COMMODITY_CANCEL_BTN_CLICK);
          setBackPressNull();
          getActivity().onBackPressed();
        }).show();
    return ((BaseActivity) getActivity()).getBackPress() != null;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.addImageContainer.setVisibility(View.VISIBLE);
  }

  @Override protected void initToolBar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.add_product)));
    super.initToolBar();
    setBackPress();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == TO_PRODUCT_DETAIL) {
        String detail = data.getStringExtra("detail");
        if (!TextUtils.isEmpty(detail)) {
          if (!detail.contains("<p>")) {
            mBinding.productDesc.setContent("图片");
          } else {
            String s = delHtml(detail);
            if (TextUtils.isEmpty(s)) {
              mBinding.productDesc.setContent("图片");
            } else {
              if (s.length() > 5) {
                mBinding.productDesc.setContent(s.substring(0, 5) + "...");
              } else {
                mBinding.productDesc.setContent(s);
              }
            }
          }
          mViewModel.getProduct().setDesc(detail);
        } else {
          mViewModel.getProduct().setDesc("");
          mBinding.productDesc.setContent("");
        }
      }
    }
  }

  private String delHtml(String content) {
    String regEx_html = "<p>(.*?)</p>";

    Pattern p_html = Pattern.compile(regEx_html);
    Matcher m_html = p_html.matcher(content);
    StringBuilder stringBuilder = new StringBuilder();
    while (m_html.find()) {
      stringBuilder.append(m_html.group(1));
    }
    return stringBuilder.toString().replace("<br/>", "");
  }
}
