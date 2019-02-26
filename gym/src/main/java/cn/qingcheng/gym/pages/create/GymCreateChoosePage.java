package cn.qingcheng.gym.pages.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingchengfit.gym.databinding.GyGymCreateChoosePageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "gym",path="/gym/choose/create")
public class GymCreateChoosePage
    extends GymBaseFragment<GyGymCreateChoosePageBinding, GymCreateChooseViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public GyGymCreateChoosePageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = GyGymCreateChoosePageBinding.inflate(inflater, container, false);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.setToolbarModel(new ToolbarModel("创建健身房"));
    mBinding.rlGym.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        changeChecked(false);
      }
    });
    mBinding.rlBrand.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        changeChecked(true);
      }
    });
    mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (isBrandChecked) {

        } else {

        }
      }
    });
  }

  private boolean isBrandChecked;

  private void changeChecked(boolean isBrandCheked) {
    this.isBrandChecked = isBrandCheked;
    mBinding.rlBrand.setSelected(isBrandCheked);
    mBinding.imgBrandChecked.setVisibility(isBrandCheked ? View.VISIBLE : View.GONE);

    mBinding.rlGym.setSelected(!isBrandCheked);
    mBinding.imgGymChecked.setVisibility(!isBrandCheked ? View.VISIBLE : View.GONE);
  }
}
