package cn.qingchengfit.shop.ui.product.paycardchannel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.cards.bean.ICardShopChooseItemData;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductPayBinding;
import cn.qingchengfit.shop.ui.items.product.CardSwitchItem;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/20.
 */
@Leaf(module = "shop", path = "/product/paycard") public class ProductPayPage
    extends ShopBaseFragment<PageProductPayBinding, ProductPayViewModel> {
  CommonFlexAdapter adapter;
  @Need ArrayList<Integer> ids;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      mViewModel.items.set(items);
      hideLoadingTrans();
      mBinding.recyclerview.post(new Runnable() {
        @Override public void run() {
          upDateSelectPosition(items);
        }
      });
    });
  }

  private void upDateSelectPosition(List<CardSwitchItem> items) {
    if (ids != null && !ids.isEmpty()) {
      for (int pos = 0; pos < items.size(); pos++) {
        for (Integer id : ids) {
          if (String.valueOf(id).equals(items.get(pos).getData().getShopCardTplId())) {
            adapter.toggleSelection(pos);
          }
        }
      }
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public PageProductPayBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductPayBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    mViewModel.loadSource("1");
    showLoadingTrans();
    mBinding.setViewModel(mViewModel);
    return mBinding;
  }

  private void initRecyclerView() {
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    adapter.setMode(SelectableAdapter.Mode.MULTI);
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.choose_product_pay_card)));
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    initToolbar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        List<Integer> selectedPositions = adapter.getSelectedPositions();
        Intent intent = new Intent();
        if (selectedPositions != null && !selectedPositions.isEmpty()) {
          ArrayList<Integer> ids = new ArrayList<>();
          for (Integer pos : selectedPositions) {
            CardSwitchItem item = (CardSwitchItem) adapter.getItem(pos);
            ICardShopChooseItemData data = item.getData();
            ids.add(Integer.valueOf(data.getShopCardTplId()));
          }
          intent.putIntegerArrayListExtra("card_tpls", ids);
        }
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().onBackPressed();
      }
    });
  }
}
