package cn.qingcheng.gym.pages.create;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingchengfit.gym.databinding.GyGymBrandCreatePageBinding;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.SimpleScrollPicker;
import com.jakewharton.rxbinding.widget.RxTextView;
import rx.android.schedulers.AndroidSchedulers;

@Leaf(module = "gym", path = "/gym/brand/create") public class GymBrandCreatePage
    extends GymBaseFragment<GyGymBrandCreatePageBinding, GymBrandCreateViewModel> {
  private ChoosePictureFragmentNewDialog choosePicFragment;
  private BrandPostBody postBody = new BrandPostBody();

  @Override protected void subscribeUI() {
    mViewModel.brand.observe(this, brand -> {
      hideLoading();
      if (brand != null) {
        routeTo(brand.brand);
      }
    });
  }

  private void routeTo(Brand brand) {
    routeTo("/gym/create", new BundleBuilder().withParcelable("brand", brand).build());
  }

  @Override
  public GyGymBrandCreatePageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymBrandCreatePageBinding.inflate(inflater, container, false);
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.civBrandCount.setContent(String.valueOf(2));
    mBinding.setToolbarModel(new ToolbarModel("创建连锁品牌"));
    mBinding.civBrandCount.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showScrollPicker(Integer.valueOf(mBinding.civBrandCount.getContent()) - 2);
      }
    });
    postBody.shop_num = 2;
    mBinding.imgGymPhoto.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onLayoutGymImgClicked();
      }
    });
    RxTextView.afterTextChangeEvents(mBinding.civBrandName.getEditText())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(text -> {
          postBody.name = text.editable().toString();
        });
    mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (checkPostBody(postBody)) {
          mViewModel.postBrandInfo(postBody);
        }
      }
    }); return mBinding;
  }

  private boolean checkPostBody(BrandPostBody body) {
    if (TextUtils.isEmpty(body.name)) {
      ToastUtils.show("请填写品牌名称");
    } else if (TextUtils.isEmpty(body.photo)) {
      ToastUtils.show("请选择品牌Logo");
    } else {
      return true;
    }
    return false;
  }

  private void showScrollPicker(int pos) {
    SimpleScrollPicker simpleScrollPicker = new SimpleScrollPicker(getContext());
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        mBinding.civBrandCount.setContent(String.valueOf(pos + 2));
        postBody.shop_num = pos + 2;
      }
    });
    simpleScrollPicker.show(2, 300, pos);
  }

  public void onLayoutGymImgClicked() {
    if (choosePicFragment == null) {
      choosePicFragment = ChoosePictureFragmentNewDialog.newInstance();
      choosePicFragment.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
        @Override public void onChoosefile(String filePath) {

        }

        @Override public void onUploadComplete(String filePaht, String url) {
          PhotoUtils.smallCircle(mBinding.imgGymPhoto, url);
          postBody.photo = url;
        }
      });
    }
    if (!choosePicFragment.isVisible()) {
      choosePicFragment.show(getChildFragmentManager(), "");
    }
  }
}
