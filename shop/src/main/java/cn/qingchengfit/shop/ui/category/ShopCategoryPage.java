package cn.qingchengfit.shop.ui.category;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.PageShopCategoryBinding;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop", path = "/shop/category/") public class ShopCategoryPage
    extends BaseDialogFragment {

  @IntDef({ ADD, UPDATE, DELETE }) @Retention(RetentionPolicy.SOURCE)
  private @interface CategoryAction {
  }

  public static final int UPDATE = 1;
  public static final int ADD = 0;
  public static final int DELETE = 2;

  @CategoryAction @Need Integer action;//0-新建,1-更新，2-删除
  @Need Category category;

  PageShopCategoryBinding mBinding;

  ShopCategoryViewModel mViewModel;

  @Inject ViewModelProvider.Factory factory;

  public static ShopCategoryPage getInstance(Category category,
      @CategoryAction @NonNull Integer action) {
    ShopCategoryPage categoryPage = new ShopCategoryPage();
    Bundle bundle = new Bundle();
    bundle.putInt("action", action);
    bundle.putParcelable("category", category);
    categoryPage.setArguments(bundle);
    return categoryPage;
  }

  protected void subscribeUI() {
    mViewModel.setAction(action);
    mViewModel.getActionEvent().observe(this, aVoid -> {
      dismiss();
    });
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mViewModel = ViewModelProviders.of(this, factory).get(ShopCategoryViewModel.class);
    subscribeUI();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    this.action = getArguments().getInt("action");
    this.category = getArguments().getParcelable("category");
    return initDataBinding(inflater, container, savedInstanceState).getRoot();
  }

  public PageShopCategoryBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageShopCategoryBinding.inflate(inflater, container, false);
    switch (action) {
      case 0:
        mBinding.categoryTitle.setText(getString(R.string.add_category));
        mBinding.includeBottom.postive.setOnClickListener(view -> {
          Category category = new Category();
          category.setName(mBinding.categoryName.toString());
          category.setPriority(Integer.valueOf(mBinding.categoryWeight.toString()));
          mViewModel.addShopCategory(category);
        });
        break;
      case 1:
        mBinding.categoryTitle.setText(getString(R.string.update_category));
        mBinding.includeBottom.postive.setOnClickListener(view -> {
          category.setName(mBinding.categoryName.toString());
          category.setPriority(Integer.valueOf(mBinding.categoryWeight.toString()));
          mViewModel.updateShopCategory(category);
        });
        break;
      case 2:
        mBinding.categoryName.setVisibility(View.GONE);
        mBinding.categoryWeight.setVisibility(View.GONE);
        mBinding.categoryTitle.setText(getString(R.string.delete_category));
        mBinding.includeBottom.postive.setOnClickListener(view -> {
          mViewModel.deleteShopCategory(category.getId());
        });
        break;
    }
    mBinding.includeBottom.cancel.setOnClickListener(view -> dismiss());
    return mBinding;
  }

  @Override public void onStart() {
    super.onStart();
    Dialog dialog = getDialog();
    if (dialog != null) {
      DisplayMetrics dm = new DisplayMetrics();
      getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
      dialog.getWindow()
          .setLayout((int) (dm.widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
  }
}
