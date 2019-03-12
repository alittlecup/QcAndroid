package cn.qingcheng.gym.pages.apply;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingchengfit.gym.databinding.GyGymApplyDealFinishBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "gym", path = "/gym/deal/finish") public class GymApplyDealFinishPage
    extends GymBaseFragment<GyGymApplyDealFinishBinding, GymApplyDealFinishVM> {
  @Override protected void subscribeUI() {

  }

  @Override
  public GyGymApplyDealFinishBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = GyGymApplyDealFinishBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("审批加入申请"));
    initToolbar(mBinding.includeToolbar.toolbar);
    return mBinding;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });
  }
}
