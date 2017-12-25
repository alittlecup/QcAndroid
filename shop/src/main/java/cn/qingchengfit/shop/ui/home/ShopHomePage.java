package cn.qingchengfit.shop.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import com.anbillon.flabellum.annotations.Leaf;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopHomeBinding;
import cn.qingchengfit.shop.ui.home.categorylist.ShopCategoryListPage;
import cn.qingchengfit.shop.ui.home.inventorylist.ShopInventoryListPage;
import cn.qingchengfit.shop.ui.home.productlist.ShopProductsListPage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop",path = "/shop/home")
public class ShopHomePage extends ShopBaseFragment<PageShopHomeBinding, ShopHomeViewModel> {
  private List<Pair<String, Fragment>> fragmentList;

  @Override protected void subscribeUI() {

  }

  @Override public PageShopHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopHomeBinding.inflate(inflater, container, false);
    initToolBar();
    mBinding.setViewModel(mViewModel);
    initView();
    return mBinding ;
  }

  private void initView() {
    mBinding.viewpager.setOffscreenPageLimit(3);
    mBinding.viewpager.setAdapter(new ShopHomePageAdapter(getChildFragmentManager(), getFragmentList()));
    mBinding.tabview.setupWithViewPager(mBinding.viewpager);
  }

  private void initToolBar() {
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.setToolbarModel(new ToolbarModel("商店"));
  }

  public List<Pair<String,Fragment>> getFragmentList() {
    if(fragmentList==null){
      fragmentList=new ArrayList<>();
      // TODO: 2017/12/18 确定viewpager的itemFragment
      fragmentList.add(new Pair<>(getString(R.string.on_sale),new ShopProductsListPage()));
      fragmentList.add(new Pair<>(getString(R.string.off_sale),new ShopProductsListPage()));
      fragmentList.add(new Pair<>(getString(R.string.category_manage),new ShopCategoryListPage()));
      fragmentList.add(new Pair<>(getString(R.string.inventory_manage),new ShopInventoryListPage()));

    }
    return fragmentList;
  }
}
