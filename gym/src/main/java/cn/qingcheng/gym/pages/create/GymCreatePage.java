package cn.qingcheng.gym.pages.create;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import cn.qingcheng.gym.pages.gym.GymInfoPage;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.lib.DensityUtil;

@Leaf(module = "gym", path = "/gym/create") public class GymCreatePage extends GymInfoPage {

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.setToolbarModel(new ToolbarModel("创建场馆"));
    initView();
  }

  private void initView() {
    mBinding.civGymType.setShowRight(true);
    mBinding.civGymAddress.setShowRight(true);

    mBinding.civGymMark.setShowRight(true);
    mBinding.imgPhotoArrow.setVisibility(View.VISIBLE);
    mBinding.viewShadow.setVisibility(View.GONE);

    mBinding.tvGymAction.setTextColor(getResources().getColor(R.color.primary));
    mBinding.tvGymAction.setBackgroundResource(R.drawable.btn_color_primary);
    LinearLayout.LayoutParams layoutParams =
        (LinearLayout.LayoutParams) mBinding.tvGymAction.getLayoutParams();
    layoutParams.leftMargin = DensityUtil.dip2px(getContext(), 15);
    layoutParams.rightMargin = DensityUtil.dip2px(getContext(), 15);
    mBinding.tvGymAction.setLayoutParams(layoutParams);
    mBinding.tvGymAction.setText("完成");
    mBinding.tvGymAction.setVisibility(View.VISIBLE);

    if (brand != null) {
      PhotoUtils.smallCircle(mBinding.imgGymPhoto, brand.getPhoto());
    }
    mBinding.tvGymAction.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (checkShop(shop)) {
          if(brand!=null){
            shop.brand_id = brand.id;
          }
          mViewModel.createShop(shop).observe(GymCreatePage.this, new Observer<Shop>() {
            @Override public void onChanged(@Nullable Shop shop) {
              if (shop != null) {
                ToastUtils.show("创建场馆成功");
                getActivity().onBackPressed();
              } else {
                ToastUtils.show("创建场馆失败");
              }
            }
          });
        }
      }
    });
  }
}
