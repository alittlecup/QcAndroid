package cn.qingcheng.gym.pages.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingchengfit.gym.databinding.GyGymBrandCreatePageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.SimpleScrollPicker;

@Leaf(module = "gym", path = "/gym/brand/create") public class GymBrandCreatePage
    extends GymBaseFragment<GyGymBrandCreatePageBinding, GymBrandCreateViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public GyGymBrandCreatePageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = GyGymBrandCreatePageBinding.inflate(inflater, container, false);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.civBrandCount.setContent(String.valueOf(2));
    mBinding.setToolbarModel(new ToolbarModel("创建连锁品牌"));
    mBinding.civBrandCount.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showScrollPicker(Integer.valueOf(mBinding.civBrandCount.getContent()) - 2);
      }
    });
  }

  private void showScrollPicker(int pos) {
    SimpleScrollPicker simpleScrollPicker = new SimpleScrollPicker(getContext());
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        mBinding.civBrandCount.setContent(String.valueOf(pos + 2));
      }
    });
    simpleScrollPicker.show(2, 300, pos);
  }
}
