package cn.qingchengfit.shop.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.widget.bubble.BubbleViewUtil;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopHomeBinding;
import cn.qingchengfit.shop.ui.home.categorylist.ShopCategoryListPage;
import cn.qingchengfit.shop.ui.home.inventorylist.ShopInventoryListPage;
import cn.qingchengfit.shop.ui.home.productlist.ShopProductsListPage;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.views.activity.WebActivity;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/home") public class ShopHomePage
    extends ShopBaseFragment<PageShopHomeBinding, ShopHomeViewModel> {
  private List<Pair<String, Fragment>> fragmentList;
  @Inject GymWrapper gymWrapper;

  private BubbleViewUtil bubbleViewUtil;

  @Override protected void subscribeUI() {

  }

  @Override public PageShopHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopHomeBinding.inflate(inflater, container, false);
    initToolBar();
    initView();
    mBinding.setViewModel(mViewModel);
    mBinding.showWebPreview.setOnClickListener(v -> {
      bubbleViewUtil.closeBubble();
      String url = gymWrapper.getCoachService().getHost()
          + "/shop/"
          + gymWrapper.shop_id()
          + "/mobile/user/commodity/";
      WebActivity.startWeb(url, getActivity());
    });
    bubbleViewUtil = new BubbleViewUtil(getContext());
    bubbleViewUtil.showBubbleOnce(mBinding.showWebPreview, "点击预览商店并推广", "shopHome",
            Gravity.BOTTOM, 750, 0);
    return mBinding;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    bubbleViewUtil.closeBubble();
  }

  private int preIndex = 0;

  private void initView() {
    mBinding.viewpager.setOffscreenPageLimit(3);
    mBinding.viewpager.setAdapter(
        new ShopHomePageAdapter(getChildFragmentManager(), getFragmentList()));
    mBinding.tabview.setupWithViewPager(mBinding.viewpager);
    mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        if (preIndex == position) return;
        preIndex = position;
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });

  }


  private void initToolBar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.shop)));
    initToolbar(mBinding.includeToolbar.toolbar);
    if (!CompatUtils.less21()
        && mBinding.showWebPreview.getParent() instanceof ViewGroup
        && isfitSystemPadding()) {
      mBinding.showWebPreview.setPadding(0, MeasureUtils.getStatusBarHeight(this.getContext()), 0,
          0);
    }
  }

  public List<Pair<String, Fragment>> getFragmentList() {
    if (fragmentList == null) {
      fragmentList = new ArrayList<>();
      fragmentList.add(
          new Pair<>(getString(R.string.on_sale), ShopProductsListPage.newInstance(1)));
      fragmentList.add(
          new Pair<>(getString(R.string.off_sale), ShopProductsListPage.newInstance(0)));
      fragmentList.add(new Pair<>(getString(R.string.category_manage), new ShopCategoryListPage()));
      fragmentList.add(
          new Pair<>(getString(R.string.inventory_manage), new ShopInventoryListPage()));
    }
    return fragmentList;
  }
}
