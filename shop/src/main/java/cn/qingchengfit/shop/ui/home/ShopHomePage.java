package cn.qingchengfit.shop.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.bubble.BubblePopupView;
import cn.qingchengfit.saasbase.utils.SharedPreferenceUtils;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopHomeBinding;
import cn.qingchengfit.shop.listener.ShopHomePageTabStayListener;
import cn.qingchengfit.shop.ui.home.categorylist.ShopCategoryListPage;
import cn.qingchengfit.shop.ui.home.inventorylist.ShopInventoryListPage;
import cn.qingchengfit.shop.ui.home.productlist.ShopProductsListPage;
import cn.qingchengfit.shop.vo.ShopSensorsConstants;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.SensorsUtils;
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

  private BubblePopupView bubblePopupView;
  private SharedPreferenceUtils sharedPreferenceUtils;

  private Handler popupHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      boolean isFirst = sharedPreferenceUtils.IsFirst("shopHome");
      if(isFirst) {
        bubblePopupView = new BubblePopupView(getContext());
        bubblePopupView.show(mBinding.showWebPreview, "点击预览商品并推广", Gravity.BOTTOM, 400);
        sharedPreferenceUtils.saveFlag("shopHome", false);
      }
    }
  };

  @Override protected void subscribeUI() {

  }

  @Override public PageShopHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopHomeBinding.inflate(inflater, container, false);
    popupHandler.sendEmptyMessageDelayed(0, 1000);
    sharedPreferenceUtils = new SharedPreferenceUtils(getContext());
    initToolBar();
    initView();
    mBinding.setViewModel(mViewModel);
    mBinding.showWebPreview.setOnClickListener(v -> {
      String url = gymWrapper.getCoachService().getHost()
          + "/shop/"
          + gymWrapper.shop_id()
          + "/mobile/user/commodity/";
      WebActivity.startWeb(url, getActivity());
      SensorsUtils.track(ShopSensorsConstants.SHOP_PREVIEW_MALL_BTN_CLICK).commit(getContext());
    });
    return mBinding;
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
        Fragment preFragment = getFragmentList().get(preIndex).second;
        if (preFragment instanceof ShopHomePageTabStayListener) {
          ((ShopHomePageTabStayListener) preFragment).onLeave();
        }
        Fragment curFragment = getFragmentList().get(position).second;
        if (curFragment instanceof ShopHomePageTabStayListener) {
          ((ShopHomePageTabStayListener) curFragment).onVisit();
        }
        preIndex = position;
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
    Fragment first = getFragmentList().get(preIndex).second;
    if (first instanceof ShopHomePageTabStayListener) {
      ((ShopHomePageTabStayListener) first).onVisit();
    }
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
