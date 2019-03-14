package cn.qingcheng.gym.pages.create;

import android.view.View;
import android.widget.LinearLayout;
import cn.qingcheng.gym.bean.ShopCreateBody;
import cn.qingcheng.gym.pages.gym.GymInfoPage;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.DensityUtil;
import com.google.gson.Gson;
import javax.inject.Inject;

@Leaf(module = "gym", path = "/gym/create") public class GymCreatePage extends GymInfoPage {
  @Inject GymWrapper gymWrapper;

  @Override public void initToolbar() {
    super.initToolbar();
    mBinding.setToolbarModel(new ToolbarModel("创建场馆"));
  }

  public void initView() {
    super.initView();
    mBinding.civGymType.setShowRight(true);
    mBinding.civGymAddress.setShowRight(true);

    mBinding.civGymMark.setShowRight(true);
    mBinding.imgPhotoArrow.setVisibility(View.VISIBLE);
    mBinding.viewShadow.setVisibility(View.GONE);

    mBinding.tvGymAction.setTextColor(getResources().getColor(R.color.white));
    mBinding.tvGymAction.setBackgroundResource(R.color.primary);
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
    mViewModel.shopSingleLiveEvent.observe(this, shop -> {
      if (shop != null) {
        ToastUtils.show("创建场馆成功");
        if (AppUtils.getCurApp(getContext()) == 1) {
          if(brand==null){
            brand=new Brand();
            brand.setId(shop.brand_id);
          }
          CoachService coachService =
              new Gson().fromJson(new Gson().toJson(shop), CoachService.class);
          gymWrapper.setBrand(brand);
          gymWrapper.setCoachService(coachService);
          QcRouteUtil.setRouteOptions(new RouteOptions("staff").setActionName("open/gymdetail"))
              .call();
        } else {
          QcRouteUtil.setRouteOptions(new RouteOptions("trainer").setActionName("open/gymdetail"))
              .call();
        }
        getActivity().finish();
      } else {
        ToastUtils.show("创建场馆失败");
      }
    });
    mBinding.tvGymAction.setOnClickListener(v -> {
      if (checkShop(shop)) {
        if (brand != null) {
          shop.brand_id = brand.id;
        }
        ShopCreateBody shopCreateBody = new ShopCreateBody();
        shopCreateBody.shop = shop;
        if (brand != null) {
          shopCreateBody.brand_id = brand.id;
        }
        mViewModel.createShop(shopCreateBody);
      }
    });
  }
}
