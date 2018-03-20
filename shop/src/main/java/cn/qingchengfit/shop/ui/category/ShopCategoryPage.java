package cn.qingchengfit.shop.ui.category;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.databinding.PageShopCategoryBinding;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseDialogFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Inject;
import rx.functions.Action1;

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
    mViewModel.getAddResult().observe(this, result);
    mViewModel.getDeleteResult().observe(this, result);
    mViewModel.getPutResult().observe(this, result);
  }

  private Observer<Boolean> result = new Observer<Boolean>() {
    @Override public void onChanged(@Nullable Boolean aBoolean) {
      RxBus.getBus().post(ShopCategoryPage.class,aBoolean);
      ToastUtils.show(aBoolean?"操作成功":"操作失败");
      dismiss();
    }
  };

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
          checkUpCategory(category,category1 -> mViewModel.addShopCategory(category1));
        });
        break;
      case 1:
        mBinding.categoryTitle.setText(getString(R.string.update_category));
        mBinding.includeBottom.postive.setOnClickListener(view -> {
          checkUpCategory(category,category1 -> mViewModel.updateShopCategory(category1));
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
    mBinding.categoryName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
    mBinding.categoryWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
    mBinding.includeBottom.cancel.setOnClickListener(view -> dismiss());
    if (!TextUtils.isEmpty(category.getName())) {
      mBinding.categoryName.setText(category.getName());
    }
    return mBinding;
  }

  private void checkUpCategory(Category category,Action1<Category> action) {
    String prority = mBinding.categoryWeight.getText().toString();
    String name = mBinding.categoryName.getText().toString();
    if (!TextUtils.isEmpty(name)) {
      category.setName(name);
    } else {
      ToastUtils.show("分类名称不能为空");
      return;
    }
    if (!TextUtils.isEmpty(prority)) {
      try {
        int integer = Integer.valueOf(mBinding.categoryWeight.getText().toString());
        if (integer > 10000000) {
          ToastUtils.show("权重不能超过10000000");
        } else {
          category.setPriority(integer);
          action.call(category);
        }
      } catch (NumberFormatException e) {
        ToastUtils.show("请输入数字");
      }
    } else {
      ToastUtils.show("分类权重不能为空");
    }
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
