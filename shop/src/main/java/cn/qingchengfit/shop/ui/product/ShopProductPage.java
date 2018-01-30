package cn.qingchengfit.shop.ui.product;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopProductBinding;
import cn.qingchengfit.shop.ui.items.product.GoodProductItem;
import cn.qingchengfit.shop.ui.product.bottom.ShopBottomCategoryFragment;
import cn.qingchengfit.shop.ui.product.choosepic.MultiChoosePicFragment;
import cn.qingchengfit.shop.ui.product.deliverchannel.ProductDeliverPage;
import cn.qingchengfit.shop.ui.product.deliverchannel.ProductDeliverPageParams;
import cn.qingchengfit.shop.ui.widget.CategoryItemView;
import cn.qingchengfit.shop.util.ViewUtil;
import cn.qingchengfit.shop.vo.Good;
import cn.qingchengfit.utils.Corner4dpImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.views.activity.BaseActivity;
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
  @Need Integer productId = 0;
  private static final int TO_DELIVER_CODE = 101;

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
        if (uris != null && !uris.isEmpty()) {
          initViewPager(uris);
        }
      });
      multiChoosePicFragment.show(getChildFragmentManager(), "");
    });
    mViewModel.deliverChannelEvent.observe(this, aVoid -> {
      Uri uri = Uri.parse("shop://shop/product/deliver");

      toOtherFragmentForBack(uri,
          new ProductDeliverPageParams().delivers(
              (ArrayList<Integer>) mViewModel.getProduct().getDelivery_types())
              .build(), TO_DELIVER_CODE);
    });
  }

  private void toOtherFragmentForBack(Uri uri, Bundle params, int requestCode) {
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    if (params != null) {
      intent.putExtras(params);
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    startActivityForResult(intent, requestCode);
  }

  @Override
  public PageShopProductBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopProductBinding.inflate(inflater, container, false);
    initToolBar();
    initGoodRecyclerView();
    if (productId != 0) {
      //mViewModel.loadProductDetail(productId);
    }
    mBinding.setViewModel(mViewModel);

    return mBinding;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      // TODO: 2018/1/30  返回值处理
      if (requestCode == TO_DELIVER_CODE) {
        ArrayList<Integer> deliver = data.getIntegerArrayListExtra("delivers");
        dealDeliverTypes(deliver);
      }
    }
  }

  private void dealDeliverTypes(ArrayList<Integer> deliver) {
    StringBuilder sb = new StringBuilder();
    if (deliver.contains(1)) {
      sb.append(getString(R.string.deliver_onsale)).append(" ");
    }
    if (deliver.contains(2)) {
      sb.append(getString(R.string.deliver_by_self)).append(" ");
    }
    if (deliver.contains(3)) {
      sb.append(getString(R.string.deliver_send)).append(" ");
    }
    mViewModel.getProduct().setDelivery_types(deliver);
    mBinding.productDeliver.setContent(sb.toString());
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

    if (productId != 0) {
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
      FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(container.getContext())
          .inflate(R.layout.item_empty_image, null);
      ImageView imageView = frameLayout.findViewById(R.id.image);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      container.addView(frameLayout);
      imageView.setTag(R.id.image, position);
      Glide.with(imageView.getContext())
          .load(uris.get(position))
          .asBitmap()
          .placeholder(cn.qingchengfit.widgets.R.color.backgroud_grey)
          .into(imageView);
      return frameLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      container.removeView((View) object);
    }
  }
}
