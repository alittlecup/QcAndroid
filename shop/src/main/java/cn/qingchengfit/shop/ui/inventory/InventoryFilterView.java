package cn.qingchengfit.shop.ui.inventory;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
import cn.qingchengfit.saasbase.student.views.followup.FilterListStringFragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;

/**
 * Created by huangbaole on 2017/12/19.
 */

public class InventoryFilterView extends BaseFilterFragment {

  FilterTimesFragment filterTimesFragment;
  FilterListStringFragment goodsListFragment;
  FilterListStringFragment productsListFragment;

  ShopInventoryViewModel viewModel;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = ViewModelProviders.of(getParentFragment()).get(ShopInventoryViewModel.class);
    initFragment();
  }

  private void initFragment() {
    productsListFragment = new FilterListStringFragment();
    String[] products = new String[] { "全部", "沙拉", "水果", "健身餐" };
    productsListFragment.setStrings(products);
    productsListFragment.setOnSelectListener(position -> {

    });

    goodsListFragment = new FilterListStringFragment();

    goodsListFragment.setOnSelectListener(position -> {

    });

    filterTimesFragment = FilterTimesFragment.getInstance(1, 30);
  }

  @Override protected String[] getTags() {
    return new String[] { "products", "goods", "date" };
  }

  @Override protected Fragment getFragmentByTag(String tag) {
    if (tag.equalsIgnoreCase(getTags()[0])) {
      return productsListFragment;
    } else if (tag.equalsIgnoreCase(getTags()[1])) {
      return goodsListFragment;
    } else if (tag.equalsIgnoreCase(getTags()[2])) {
      return filterTimesFragment;
    }
    return new EmptyFragment();
  }
}
