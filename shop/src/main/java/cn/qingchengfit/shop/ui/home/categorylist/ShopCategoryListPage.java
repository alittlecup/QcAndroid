package cn.qingchengfit.shop.ui.home.categorylist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.base.ShopPermissionUtils;
import cn.qingchengfit.shop.databinding.PageCategoryListBinding;
import cn.qingchengfit.shop.ui.category.ShopCategoryPage;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.jakewharton.rxbinding.widget.RxTextView;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopCategoryListPage
    extends ShopBaseFragment<PageCategoryListBinding, ShopCategoryListViewModel> {
  CommonFlexAdapter adapter;
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.getAddEvent().observe(this, aVoid -> {
      if (permissionModel.check(ShopPermissionUtils.COMMODITY_CATEGORY_CAN_WRITE)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      ShopCategoryPage.getInstance(new Category(), ShopCategoryPage.ADD)
          .show(getChildFragmentManager(), "");
    });
    mViewModel.getDeleteEvent().observe(this, category -> {
      if (permissionModel.check(ShopPermissionUtils.COMMODITY_CATEGORY_CAN_DELETE)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      ShopCategoryPage.getInstance(category, ShopCategoryPage.DELETE)
          .show(getChildFragmentManager(), "");
    });
    mViewModel.getUpdateEvent().observe(this, category -> {
      if (permissionModel.check(ShopPermissionUtils.COMMODITY_CATEGORY_CAN_CHANGE)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      ShopCategoryPage.getInstance(category, ShopCategoryPage.UPDATE)
          .show(getChildFragmentManager(), "");
    });
    mViewModel.getLiveItems().observe(this, items -> {
      if (items == null || items.isEmpty()) {
        setEmptyView();
      } else {
        mViewModel.items.set(items);
        mBinding.swipeRefresh.setRefreshing(false);
      }
    });
  }

  private void setEmptyView() {
    String hintString="";
    if(!TextUtils.isEmpty(mBinding.etSearch.getText().toString().trim())){
      hintString="未找到相关结果";
    }else{
      hintString="暂无分类，赶快去添加吧～";
    }
    CommonNoDataItem item = new CommonNoDataItem(R.drawable.vd_img_empty_universe, hintString);
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(item);
    adapter.updateDataSet(items);
  }

  @Override
  public PageCategoryListBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageCategoryListBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    initSearchProduct();
    initRecyclerView();
    if (permissionModel.check(ShopPermissionUtils.COMMODITY_CATEGORY)) {
      mViewModel.loadSource(new HashMap<>());
    } else {
      List<CommonNoDataItem> items = new ArrayList<>();
      items.add(new CommonNoDataItem(R.drawable.ic_403, getString(R.string.no_access),
          getString(R.string.no_current_page_permission)));
      adapter.updateDataSet(items);
      mBinding.fragmentMark.setVisibility(View.VISIBLE);
    }
    return mBinding;
  }

  private void initSearchProduct() {
    RxTextView.afterTextChangeEvents(mBinding.etSearch)
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> {
          String key = event.editable().toString().trim();
          if (!TextUtils.isEmpty(key)) {
            Map<String, Object> params = new HashMap<>();
            params.put("q", key);
            mViewModel.loadSource(params);
          } else {
            mViewModel.loadSource(new HashMap<>());
          }
        });
  }

  private void initRecyclerView() {
    adapter = new CommonFlexAdapter(new ArrayList());
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.setAdapter(adapter);
    mBinding.recyclerview.addItemDecoration(
        new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    mBinding.swipeRefresh.setOnRefreshListener(() -> mViewModel.loadSource(new HashMap<>()));
  }
}
