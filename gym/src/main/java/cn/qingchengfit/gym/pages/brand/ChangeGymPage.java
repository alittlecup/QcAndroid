package cn.qingchengfit.gym.pages.brand;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.gym.GymBaseFragment;
import cn.qingchengfit.gym.databinding.GyChangeGymPageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;

@Leaf(module = "gym", path = "/gym/change") public class ChangeGymPage
    extends GymBaseFragment<GyChangeGymPageBinding, ChangeGymViewModel> {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

  }

  @Override
  public GyChangeGymPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = GyChangeGymPageBinding.inflate(inflater, container, false);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList()));
    mBinding.setToolbarModel(new ToolbarModel("切换场馆"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
