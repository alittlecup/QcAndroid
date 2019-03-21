package cn.qingchengfit.gym.pages.create;

import android.view.View;
import android.widget.LinearLayout;
import cn.qingchengfit.gym.bean.ShopCreateBody;
import cn.qingchengfit.gym.pages.gym.GymInfoPage;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.DensityUtil;

@Leaf(module = "gym", path = "/gym/create") public class GymCreatePage extends GymInfoPage {

  @Override public void initToolbar() {
    super.initToolbar();
    mBinding.setToolbarModel(new ToolbarModel("创建场馆"));
  }

  public void initView() {
    super.initView();
    mBinding.civGymType.setShowRight(true);
    mBinding.civGymAddress.setShowRight(true);
    mBinding.imgGymPhoto.setClickable(true);

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
    mBinding.dividerTop.setVisibility(View.GONE);
    mBinding.dividerBottom.setVisibility(View.GONE);

    if (brand != null) {
      PhotoUtils.smallCircle(mBinding.imgGymPhoto, brand.getPhoto());
      shop.photo = brand.getPhoto();
    }
    mViewModel.shopSingleLiveEvent.observe(this, shop -> {
      if (shop != null) {
        ToastUtils.show("创建场馆成功");
        createGymSuccess();
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

  @Override public void routeToPage() {
    if (AppUtils.getCurApp(getContext()) == 1) {
      QcRouteUtil.setRouteOptions(new RouteOptions("staff").setActionName("open/gymdetail")).call();
    } else {
      QcRouteUtil.setRouteOptions(new RouteOptions("trainer").setActionName("open/gymdetail"))
          .call();
    }
    getActivity().finish();
  }
}
