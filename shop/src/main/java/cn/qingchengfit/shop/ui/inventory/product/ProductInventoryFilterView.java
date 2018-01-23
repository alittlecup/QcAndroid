package cn.qingchengfit.shop.ui.inventory.product;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterCustomFragment;
import cn.qingchengfit.saasbase.student.views.followup.FilterListStringFragment;
import cn.qingchengfit.shop.ui.inventory.ShopTimeFilterFragment;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/19.
 */

public class ProductInventoryFilterView extends BaseFilterFragment {

  FilterCustomFragment filterTimesFragment;
  FilterListStringFragment goodsListFragment;

  ProductInventoryViewModel viewModel;
  private List<Good> goods = new ArrayList<>();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = ViewModelProviders.of(getParentFragment()).get(ProductInventoryViewModel.class);
    initFragment();
  }

  private void initFragment() {
    goodsListFragment = new FilterListStringFragment();
    viewModel.getGoodNames().observe(this, goods -> {
      List<String> goodNames = new ArrayList<>();
      if (goods != null && !goods.isEmpty()) {
        for (Good good : goods) {
          goodNames.add(good.getName());
        }
        this.goods.clear();
        this.goods.addAll(goods);
      }
      goodsListFragment.setStrings(goodNames.toArray(new String[goodNames.size()]));
    });

    goodsListFragment.setOnSelectListener(position -> {
      Good good = goods.get(position);
      viewModel.getParams().put("goods_id", good.getId());
      viewModel.loadSource(viewModel.getParams());
    });
    filterTimesFragment = ShopTimeFilterFragment.newInstance("时间段");
    filterTimesFragment.setOnBackFilterDataListener(
        new FilterCustomFragment.OnBackFilterDataListener() {
          @Override public void onSettingData(String start, String end) {
            viewModel.getParams().put("start", start);
            viewModel.getParams().put("end", end);
            viewModel.loadSource(viewModel.getParams());
          }

          @Override public void onBack() {

          }
        });
    filterTimesFragment.setSelectTime(true);
    filterTimesFragment.limitDay = 30;
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
