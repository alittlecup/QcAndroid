package cn.qingcheng.gym.pages.gym;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingchengfit.gym.databinding.GyGymInfoPageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

@Leaf(module = "gym", path = "/gym/info") public class GymInfoPage
    extends GymBaseFragment<GyGymInfoPageBinding, GymInfoViewModel> {
  @Need String gymID;

  @Override protected void subscribeUI() {

  }

  @Override
  public GyGymInfoPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = GyGymInfoPageBinding.inflate(inflater, container, false);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("场馆信息"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
