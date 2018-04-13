package cn.qingchengfit.shop.ui.inventory.product;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.student.views.filtertime.FilterTimesFragment;
import cn.qingchengfit.saasbase.student.views.followup.FilterListStringFragment;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/19.
 */

public class ProductInventoryFilterView extends BaseFilterFragment {

  FilterTimesFragment filterTimesFragment;
  FilterListStringFragment goodsListFragment;

  ProductInventoryViewModel viewModel;
  private List<Good> goods = new ArrayList<>();

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = ViewModelProviders.of(getParentFragment()).get(ProductInventoryViewModel.class);
    initFragment();
  }

  @Override public void dismiss() {
    viewModel.fragVisible.setValue(false);
  }

  private void initFragment() {
    goodsListFragment = new FilterListStringFragment();
    viewModel.getGoodNames().observe(this, goods -> {
      List<String> goodNames = new ArrayList<>();
      goodNames.add("全部规格");
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
      if (position == 0) {
        if (viewModel.getParams().containsKey("goods_id")) {
          viewModel.getParams().remove("goods_id");
        }
      } else {
        Good good = goods.get(position - 1);
        viewModel.getParams().put("goods_id", good.getId());
      }
      viewModel.loadSource(viewModel.getParams());
      dismiss();
    });

    filterTimesFragment = FilterTimesFragment.getInstance(1, 30);

    filterTimesFragment.setSelectDayAction((s, s2, s3) -> {
      viewModel.getParams().put("start", s);
      viewModel.getParams().put("end", s2);
      viewModel.loadSource(viewModel.getParams());
      dismiss();
    });
  }

  @Override protected String[] getTags() {
    return new String[] { "goods", "day" };
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
