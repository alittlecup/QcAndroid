package cn.qingchengfit.shop.ui.product;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopProductBinding;
import cn.qingchengfit.shop.ui.items.product.GoodProductItem;
import cn.qingchengfit.shop.ui.product.bottom.ShopBottomCategoryFragment;
import cn.qingchengfit.shop.ui.product.choosepic.MultiChoosePicFragment;
import cn.qingchengfit.shop.ui.widget.CategoryItemView;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/product") public class ShopProductPage
    extends ShopBaseFragment<PageShopProductBinding, ShopProductViewModel> {
  @Need Boolean isUpdate = false;

  @Override protected void subscribeUI() {
    mViewModel.payChannelEvent.observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/product/paychannel");
      routeTo(uri, null);
    });
    mViewModel.addToCategory.observe(this, aVoid -> {
      new ShopBottomCategoryFragment().show(getChildFragmentManager(), "");
    });
    mViewModel.addImagesEvent.observe(this, aVoid -> {
      MultiChoosePicFragment multiChoosePicFragment = MultiChoosePicFragment.newInstance(null);
      multiChoosePicFragment.setUpLoadImageCallback(uris -> {
        if (adapter != null && uris != null && !uris.isEmpty()) {
          initViewPager(uris);
        }
      });
      multiChoosePicFragment.show(getChildFragmentManager(), "");
    });
  }

  @Override
  public PageShopProductBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopProductBinding.inflate(inflater, container, false);
    initToolBar();
    initGoodRecyclerView();
    if (isUpdate) {
      //mViewModel.loadProductDetail(id);
    }
    mBinding.setViewModel(mViewModel);
    return mBinding;
  }

  CommonFlexAdapter goodsAdapter;

  private void initGoodRecyclerView() {
    List<GoodProductItem> goods = new ArrayList<>();
    goods.add(new GoodProductItem(new Good()));
    mBinding.goodsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.goodsRecyclerview.setAdapter(goodsAdapter = new CommonFlexAdapter(goods));
    mBinding.addProductCategory.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (goodsAdapter.getItemCount() == 1 && !((GoodProductItem) goodsAdapter.getItem(
            0)).isExpend()) {
          ((GoodProductItem) goodsAdapter.getItem(0)).setExpend(true);
        } else {
          goodsAdapter.addItem(new GoodProductItem(new Good()));
        }
        goodsAdapter.notifyDataSetChanged();
        ViewUtil.resetRecyclerViewHeight(mBinding.goodsRecyclerview);
      }
    });
    mBinding.priceCardSwitch.setOnCheckListener((buttonView, isChecked) -> {
      if (isChecked) {
        goodsAdapter.setStatus(GoodProductItem.SHOW_CARD_PRICE);
      } else {
        goodsAdapter.setStatus(0);
      }
      goodsAdapter.notifyDataSetChanged();
      ViewUtil.resetRecyclerViewHeight(mBinding.goodsRecyclerview);
    });
  }

  private void initToolBar() {

    if (isUpdate) {
      ToolbarModel toolbarModel = new ToolbarModel(getString(R.string.product_detail));
      toolbarModel.setMenu(R.menu.menu_edit);
      toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          if (item.getItemId() == R.id.action_save) {
            toolbarModel.setMenu(R.menu.menu_compelete);
            mViewModel.isEdit.set(true);
          } else if (item.getItemId() == R.id.complete) {
            // TODO: 2018/1/17  提交修改后的商品数据
            mViewModel.isEdit.set(false);
          }
          return false;
        }
      });
      mBinding.setToolbarModel(toolbarModel);
    } else {
      mBinding.setToolbarModel(new ToolbarModel(getString(R.string.add_product)));
    }
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private PagerAdapter adapter;

  private void initViewPager(List<String> uris) {
    adapter = new ImageViewAdapter(uris);
    mBinding.viewpager.setAdapter(adapter);
    mBinding.viewpager.setCurrentItem(0);
    adapter.notifyDataSetChanged();
  }

  private class ImageViewAdapter extends PagerAdapter {
    private List<String> uris;

    public ImageViewAdapter(List<String> uris) {
      this.uris = uris;
    }

    @Override public int getCount() {
      return uris.size();
    }

    @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
      return view == object;
    }

    @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
      ImageView imageView = new ImageView(container.getContext());
      Glide.with(container.getContext()).load(uris.get(position)).asBitmap().into(imageView);
      container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT));
      return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      container.removeView((View) object);
    }
  }
}
