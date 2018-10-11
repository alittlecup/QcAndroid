package cn.qingchengfit.shop.ui.inventory;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saascommon.filtertime.FilterTimesFragment;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.DoubleListFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/19.
 */

public class InventoryFilterView extends BaseFilterFragment {

  FilterTimesFragment filterTimesFragment;
  DoubleListFilterFragment productsListFragment;

  ShopInventoryViewModel viewModel;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = ViewModelProviders.of(getParentFragment()).get(ShopInventoryViewModel.class);
    initFragment();
  }

  private List<Product> mProducts = new ArrayList<>();

  private void initFragment() {
    productsListFragment = new DoubleListFilterFragment();
    viewModel.getProducts().observe(this, products -> {
      mProducts = products;
      productsListFragment.setDatas(addMoreData(products));
    });
    productsListFragment.setDoubleListSelectListener((leftPos, rightPos) -> {
      if (leftPos != 0) {
        viewModel.getParams().put("product_id", mProducts.get(leftPos - 1).getId());
      } else if (viewModel.getParams().containsKey("product_id")) {
        viewModel.getParams().remove("product_id");
      }
      if (rightPos != 0) {
        viewModel.getParams()
            .put("goods_id", mProducts.get(leftPos - 1).getGoods().get(rightPos).getId());
      } else if (viewModel.getParams().containsKey("goods_id")) {
        viewModel.getParams().remove("goods_id");
      }
      viewModel.loadSource(viewModel.getParams());
      viewModel.filterVisible.set(false);
      if (datas != null) {
        String s = datas.get(leftPos).getChildText().get(rightPos);
        RxBus.getBus().post(DoubleListFilterFragment.class, s);
      }
    });

    filterTimesFragment = FilterTimesFragment.getInstance(1, 30);
    filterTimesFragment.setSelectDayAction((s, s2, s3) -> {
      viewModel.getParams().put("start", s);
      viewModel.getParams().put("end", s2);
      viewModel.loadSource(viewModel.getParams());
      viewModel.filterVisible.set(false);
      if (!TextUtils.isEmpty(s3)) {
        RxBus.getBus().post(FilterTimesFragment.class, s3);
      }
    });
  }

  private List<DoubleListFilterFragment.IDoubleListData> datas;

  @Override public void dismiss() {
    viewModel.filterVisible.set(false);
  }

  private List<DoubleListFilterFragment.IDoubleListData> addMoreData(List<Product> products) {
    List<DoubleListFilterFragment.IDoubleListData> datas = new ArrayList<>();
    datas.add(new DoubleListFilterFragment.IDoubleListData() {
      @Override public String getText() {
        return "全部商品";
      }

      @Override public List<String> getChildText() {
        List<String> list = new ArrayList<>();
        list.add("全部商品规格");
        return list;
      }
    });
    if (null == products || products.isEmpty()) return datas;
    for (Product product : products) {
      Good good = new Good();
      good.setName("全部" + product.getName() + "规格");
      good.setId("");
      product.getGoods().add(0, good);
    }
    datas.addAll(products);
    this.datas = datas;
    return datas;
  }

  @Override protected String[] getTags() {
    return new String[] { "products", "day" };
  }

  @Override protected Fragment getFragmentByTag(String tag) {
    if (tag.equalsIgnoreCase(getTags()[0])) {
      return productsListFragment;
    } else if (tag.equalsIgnoreCase(getTags()[1])) {
      return filterTimesFragment;
    }
    return new EmptyFragment();
  }
}
