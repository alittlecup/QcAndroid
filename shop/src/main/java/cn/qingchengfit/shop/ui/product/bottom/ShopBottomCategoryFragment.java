package cn.qingchengfit.shop.ui.product.bottom;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.ViewBottomShopCategoryBinding;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.category.ShopCategoryPage;
import cn.qingchengfit.shop.ui.items.category.CategoryChooseItem;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import dagger.android.support.AndroidSupportInjection;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2018/1/27.
 */

public class ShopBottomCategoryFragment extends BottomSheetDialogFragment {
  ViewBottomShopCategoryBinding mBinding;
  CommonFlexAdapter adapter;
  ShopBottomCategoryViewModel mViewModel;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidSupportInjection.inject(this);
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding =
        DataBindingUtil.inflate(inflater, R.layout.view_bottom_shop_category, container, false);
    initRecyclerView();
    mViewModel = ViewModelProviders.of(this, new ViewModelProvider.NewInstanceFactory())
        .get(ShopBottomCategoryViewModel.class);
    subscribeUI();
    mBinding.setViewModel(mViewModel);
    return mBinding.getRoot();
  }

  private void subscribeUI() {
    mViewModel.loadCategoryList(repository, loginStatus.staff_id(), gymWrapper.getParams())
        .observe(this, items -> {
          if (items != null && !items.isEmpty()) {
            List<CategoryChooseItem> categoryChooseItems = new ArrayList<>();
            for (Category category : items) {
              categoryChooseItems.add(new CategoryChooseItem(category));
            }
            mViewModel.items.set(categoryChooseItems);
          }
        });
    mViewModel.cancelEvent.observe(this, aVoid -> dismiss());
    mViewModel.confimEvent.observe(this, aVoid -> {
      // TODO: 2018/1/27 回传数据
    });
    mViewModel.addCategoryEvent.observe(this, aVoid -> {
      ShopCategoryPage.getInstance(new Category(), ShopCategoryPage.ADD)
          .show(getChildFragmentManager(), "");
    });
  }

  private void initRecyclerView() {
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.color.divider_grey).withOffset(2));
  }
}
