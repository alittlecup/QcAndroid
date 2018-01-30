package cn.qingchengfit.shop.ui.product.deliverchannel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductDeliverBinding;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.widget.CompoundButton.*;

/**
 * Created by huangbaole on 2017/12/20.
 */
@Leaf(module = "shop", path = "/product/deliver") public class ProductDeliverPage
    extends ShopBaseFragment<PageProductDeliverBinding, ProductDeliverViewModel> {
  @Override protected void subscribeUI() {

  }

  @Need ArrayList<Integer> delivers;

  @Override
  public PageProductDeliverBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductDeliverBinding.inflate(inflater, container, false);
    initToolbar();
    initListener();
    return mBinding;
  }

  private void initListener() {
    if (delivers == null) delivers = new ArrayList<>();

    if (delivers.contains(1)) {
      mBinding.deliverByIn.setOpen(true);
    }
    if (delivers.contains(2)) {
      mBinding.deliverBySelf.setOpen(true);
    }
    if (delivers.contains(3)) {
      mBinding.deliverBySend.setOpen(true);
    }

    mBinding.deliverByIn.setOnCheckListener((buttonView, isChecked) -> {
      if (isChecked) {
        delivers.add(1);
      } else {
        delivers.remove((Integer) 1);
      }
    });
    mBinding.deliverBySelf.setOnCheckListener((buttonView, isChecked) -> {
      if (isChecked) {
        delivers.add(2);
      } else {
        delivers.remove((Integer) 2);
      }
    });
    mBinding.deliverBySend.setOnCheckListener((buttonView, isChecked) -> {
      if (isChecked) {
        delivers.add(3);
      } else {
        delivers.remove((Integer) 3);
      }
    });

  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.product_deliver_channal)));
    initToolbar(mBinding.includeToolbar.toolbar);
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    toolbar.setNavigationOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putIntegerArrayListExtra("delivers", delivers);
      getActivity().setResult(Activity.RESULT_OK, intent);
      getActivity().onBackPressed();
    });
  }
}
