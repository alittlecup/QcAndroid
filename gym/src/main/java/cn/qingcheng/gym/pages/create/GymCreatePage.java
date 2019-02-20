package cn.qingcheng.gym.pages.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import cn.qingcheng.gym.pages.gym.GymInfoPage;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.others.ToolbarModel;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.DensityUtil;
@Leaf(module = "gym",path = "/gym/create")
public class GymCreatePage extends GymInfoPage {
  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.setToolbarModel(new ToolbarModel("创建场馆"));
    initView();
  }

  private void initView() {
    mBinding.civGymName.setCanBeNull(false);
    mBinding.civGymName.setEditable(true);

    mBinding.civGymType.setEditable(false);
    mBinding.civGymType.setCanClick(true);
    mBinding.civGymAddress.setShowRight(true);
    mBinding.civGymType.setCanBeNull(false);

    mBinding.civGymPhone.setEditable(true);
    mBinding.civGymPhone.setIsNum(true);
    mBinding.civGymPhone.setCanBeNull(false);

    mBinding.civGymAddress.setEditable(false);
    mBinding.civGymAddress.setCanClick(true);
    mBinding.civGymAddress.setShowRight(true);
    mBinding.civGymAddress.setCanBeNull(false);

    mBinding.civGymSquare.setEditable(true);
    mBinding.civGymSquare.setCanClick(false);
    mBinding.civGymSquare.setShowRight(true);
    mBinding.civGymSquare.setCanBeNull(false);

    mBinding.civGymMark.setEditable(false);
    mBinding.civGymMark.setCanClick(true);
    mBinding.civGymMark.setShowRight(true);
    mBinding.civGymMark.setCanBeNull(false);

    mBinding.tvGymAction.setTextColor(getResources().getColor(R.color.primary));
    mBinding.tvGymAction.setBackgroundResource(R.drawable.btn_color_primary);
    LinearLayout.LayoutParams layoutParams =
        (LinearLayout.LayoutParams) mBinding.tvGymAction.getLayoutParams();
    layoutParams.leftMargin = DensityUtil.dip2px(getContext(), 15);
    layoutParams.rightMargin = DensityUtil.dip2px(getContext(), 15);
    mBinding.tvGymAction.setLayoutParams(layoutParams);
    mBinding.tvGymAction.setText("完成");
  }
}
