package cn.qingcheng.gym.pages.gym;

import android.Manifest;
import android.arch.persistence.room.util.StringUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.bean.GymType;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymInfoPageBinding;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.Util;
import cn.qingchengfit.utils.Utils;
import cn.qingchengfit.views.fragments.CommonInputTextFragment;
import cn.qingchengfit.widgets.BottomChooseDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@Leaf(module = "gym", path = "/gym/info") public class GymInfoPage
    extends GymBaseFragment<GyGymInfoPageBinding, GymInfoViewModel> {
  @Need public Shop shop;
  BottomChooseDialog gymTypeDialog;

  @Override protected void subscribeUI() {
    mViewModel.gymTypes.observe(this, types -> {
      if (types != null && !types.isEmpty()) {
        List<BottomChooseData> datas = new ArrayList<>();
        for (GymType type : types) {
          datas.add(new BottomChooseData(type.gym_type_value));
        }
        gymTypeDialog = new BottomChooseDialog(getContext(), "场馆类型", datas);
        gymTypeDialog.setOnItemClickListener(new BottomChooseDialog.onItemClickListener() {
          @Override public boolean onItemClick(int position) {
            mBinding.civGymType.setContent(
                mViewModel.gymTypes.getValue().get(position).gym_type_value);
            shop.gym_type = mViewModel.gymTypes.getValue().get(position).gym_type;
            return false;
          }
        });
        findGymTypeValueByType(shop.gym_type,types);
      }
    });
    mViewModel.deleteResult.observe(this, aBoolean -> {
      hideLoading();
      ToastUtils.show(aBoolean ? "删除场馆成功" : "删除场馆失败");
      if (aBoolean) {
        getActivity().onBackPressed();
      }
    });
  }

  public String findGymTypeValueByType(int type,List<GymType> types) {
    if (types == null || types.isEmpty()) {
      mViewModel.loadGymTypes();
    } else {
      for (GymType value : types) {
        if (value.gym_type == type) {
          return value.gym_type_value;
        }
      }
    }
    return "";
  }

  @Override
  public GyGymInfoPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymInfoPageBinding.inflate(inflater, container, false);
    initToolbar();
    initView();
    RxBusAdd(EventTxT.class).subscribe(new Action1<EventTxT>() {
      @Override public void call(EventTxT eventTxT) {
        mBinding.civGymMark.setContent(CmStringUtils.delHTMLTag(eventTxT.txt));
        shop.description = eventTxT.txt;
      }
    });
    mBinding.viewShadow.setVisibility(View.VISIBLE);
    return mBinding;
  }

  private void initView() {
    mBinding.civGymName.setContent(shop.name);
    mBinding.civGymPhone.setContent(shop.contact);
    mBinding.civGymAddress.setContent(shop.address);
    mBinding.civGymMark.setContent(CmStringUtils.delHTMLTag(shop.description));
    mBinding.civGymSquare.setContent(shop.area + "");
    mBinding.civGymType.setContent(findGymTypeValueByType(shop.gym_type,mViewModel.gymTypes.getValue()));

    Glide.with(getContext())
        .load(shop.photo)
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(mBinding.imgGymPhoto, getContext()));

    mBinding.tvGymAction.setText("删除场馆");
    mBinding.tvGymAction.setVisibility(View.GONE);

    mBinding.civGymType.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (gymTypeDialog == null) {
          mViewModel.loadGymTypes();
        } else {
          gymTypeDialog.show();
        }
      }
    });
    mBinding.civGymAddress.setOnClickListener(v -> onAddressClicked());
    mBinding.civGymMark.setOnClickListener(
        v -> routeTo(CommonInputTextFragment.newInstance("描述您的健身房", shop.description, "请填写健身房描述")));
  }

  public void onAddressClicked() {
    new RxPermissions(getActivity()).request(Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Boolean>() {
          @Override public void call(Boolean aBoolean) {
            if (aBoolean) {
              Gym gym = new Gym();
              gym.gd_lng = shop.gd_lng;
              gym.gd_lat = shop.gd_lat;
              gym.address = shop.address;
              BaseRouter.toChoose(getContext(), 71, gym);
            } else {
              ToastUtils.show("您拒绝了定位权限");
            }
          }
        });
  }

  public void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("场馆信息"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
