package cn.qingchengfit.shop.ui.inventory.product;

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

public class ProductInventoryFilterView extends BaseFilterFragment {

  FilterTimesFragment filterTimesFragment;
  FilterListStringFragment goodsListFragment;

  ProductInventoryViewModel viewModel;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = ViewModelProviders.of(getParentFragment()).get(ProductInventoryViewModel.class);
    initFragment();
  }

  private void initFragment() {

    goodsListFragment = new FilterListStringFragment();

    goodsListFragment.setOnSelectListener(position -> {

    });

    filterTimesFragment = FilterTimesFragment.getInstance(1, 30);
  }

  @Override protected String[] getTags() {
    return new String[] { "goods", "date" };
  }

  @Override protected Fragment getFragmentByTag(String tag) {
    if (tag.equalsIgnoreCase(getTags()[0])) {
      return goodsListFragment;
    } else if (tag.equalsIgnoreCase(getTags()[1])) {
      return filterTimesFragment;
    }
    return new EmptyFragment();
  }
}
