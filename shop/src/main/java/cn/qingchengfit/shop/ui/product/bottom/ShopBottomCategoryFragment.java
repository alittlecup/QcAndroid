package cn.qingchengfit.shop.ui.product.bottom;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopPermissionUtils;
import cn.qingchengfit.shop.databinding.ViewBottomShopCategoryBinding;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.category.ShopCategoryPage;
import cn.qingchengfit.shop.ui.items.category.CategoryChooseItem;
import cn.qingchengfit.shop.ui.items.category.ICategotyItemData;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.afollestad.materialdialogs.MaterialDialog;
import dagger.android.support.AndroidSupportInjection;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by huangbaole on 2018/1/27.
 */

public class ShopBottomCategoryFragment extends BottomSheetDialogFragment
    implements FlexibleAdapter.OnItemClickListener {
  ViewBottomShopCategoryBinding mBinding;
  CommonFlexAdapter adapter;
  ShopBottomCategoryViewModel mViewModel;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject ShopRepository repository;
  @Inject IPermissionModel permissionModel;
  Subscription subscribe;

  public static ShopBottomCategoryFragment newInstance(String id) {
    ShopBottomCategoryFragment fragment = new ShopBottomCategoryFragment();
    Bundle bundle = new Bundle();
    bundle.putString("curId", id);
    fragment.setArguments(bundle);
    return fragment;
  }

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
    if (getArguments() != null) {
      String curId = getArguments().getString("curId");
      if (!TextUtils.isEmpty(curId)) {
        curAddCategoryId = curId;
      }
    }
    mViewModel = ViewModelProviders.of(this, new ViewModelProvider.NewInstanceFactory())
        .get(ShopBottomCategoryViewModel.class);
    subscribeUI();
    mBinding.setViewModel(mViewModel);
    initRxbus();
    mBinding.confirm.setEnabled(false);
    return mBinding.getRoot();
  }

  private String curAddCategoryId = "";

  private void initRxbus() {
    subscribe = RxBus.getBus()
        .register(ShopCategoryPage.class, String.class)
        .subscribeOn(rx.schedulers.Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
          @Override public void call(String id) {
            curAddCategoryId = id;
            loadData();
          }
        });
  }

  private void loadData() {
    mViewModel.loadCategoryList(repository, loginStatus.staff_id(), gymWrapper.getParams())
        .observe(this, items -> {
          if (items != null && !items.isEmpty()) {
            List<CategoryChooseItem> categoryChooseItems = new ArrayList<>();
            for (Category category : items) {
              categoryChooseItems.add(new CategoryChooseItem(category));
            }
            mViewModel.items.set(categoryChooseItems);
            if (items.size() > 4) {
              mBinding.recyclerview.postDelayed(() -> {
                int height = mBinding.recyclerview.getChildAt(0).getHeight();
                ViewGroup.LayoutParams layoutParams = mBinding.recyclerview.getLayoutParams();
                layoutParams.height = height * 4;
                mBinding.recyclerview.setLayoutParams(layoutParams);
              }, 50);
            }
            adapter.clearSelection();
            for (int i = 0; i < mViewModel.items.get().size(); i++) {
              CategoryChooseItem item = mViewModel.items.get().get(i);
              if (!TextUtils.isEmpty(curAddCategoryId)) {
                if (curAddCategoryId.equals(item.getData().getId())) {
                  adapter.addSelection(i);
                  adapter.notifyItemChanged(i);
                }
              }
            }
          }
        });
  }

  private void subscribeUI() {
    loadData();
    mViewModel.cancelEvent.observe(this, aVoid -> dismiss());
    mViewModel.confimEvent.observe(this, aVoid -> {
      dismiss();
      Integer integer = adapter.getSelectedPositions().get(0);
      CategoryChooseItem item = (CategoryChooseItem) adapter.getItem(integer);
      if (dataConsumer != null) {
        dataConsumer.accept(item.getData());
      }
    });
    mViewModel.addCategoryEvent.observe(this, aVoid -> {
      if (!permissionModel.check(ShopPermissionUtils.COMMODITY_CATEGORY_CAN_WRITE)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      ShopCategoryPage.getInstance(new Category(), ShopCategoryPage.ADD)
          .show(getChildFragmentManager(), "");
    });
  }

  private void initRecyclerView() {
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    adapter.setMode(SelectableAdapter.Mode.SINGLE);
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerview.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.color.divider_grey).withOffset(2));
    adapter.addListener(this);
  }

  private Consumer<ICategotyItemData> dataConsumer;

  public void setConfimAction(Consumer<ICategotyItemData> dataConsumer) {
    this.dataConsumer = dataConsumer;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (subscribe != null) {
      subscribe.unsubscribe();
      subscribe = null;
    }
  }

  @Override public boolean onItemClick(int position) {
    adapter.toggleSelection(position);
    adapter.notifyDataSetChanged();
    if (adapter.getSelectedItemCount() == 0) {
      mBinding.confirm.setEnabled(false);
    } else {
      mBinding.confirm.setEnabled(true);
    }
    return false;
  }

  private void showAlert(int stringid) {
    new MaterialDialog.Builder(getContext()).positiveText("知道了")
        .autoDismiss(true)
        .content(getString(stringid))
        .canceledOnTouchOutside(true)
        .build()
        .show();
  }
}
