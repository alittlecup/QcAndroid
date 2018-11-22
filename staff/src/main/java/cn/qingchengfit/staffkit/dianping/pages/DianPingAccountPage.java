package cn.qingchengfit.staffkit.dianping.pages;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.staff.routers.StaffParamsInjector;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.databinding.PageDianpingAccountBinding;
import cn.qingchengfit.staffkit.dianping.vo.DianPingChooseDataEvent;
import cn.qingchengfit.staffkit.dianping.vo.DianPingChooseType;
import cn.qingchengfit.staffkit.dianping.vo.DianPingShop;
import cn.qingchengfit.staffkit.dianping.vo.GymFacility;
import cn.qingchengfit.staffkit.dianping.vo.GymTag;
import cn.qingchengfit.staffkit.dianping.vo.ISimpleChooseData;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PermissionUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.ChooseAddressFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@Leaf(module = "dianping", path = "/dianping/account") public class DianPingAccountPage
    extends SaasBindingFragment<PageDianpingAccountBinding, DianPingAccountViewModel> {
  @Need String barCode = "";
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.gymInfo.observe(this, this::updateGymInfo);
    mViewModel.dianPingAccountResult.observe(this, aBoolean -> {
      if (aBoolean) {
        routeTo("/dianping/success", null);
      }
    });
  }

  @Override
  public PageDianpingAccountBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageDianpingAccountBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("完善信息"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mViewModel.loadGymInfo();
    initRxbus();
    initListener();
    initView();
    return mBinding;
  }

  private void initView() {
    if (permissionModel.check(PermissionServerUtils.STUDIO_LIST_CAN_CHANGE)) {
      mBinding.civGymName.setEnable(false);
      mBinding.civGymLocation.setEnable(false);
      mBinding.civGymPhone.setEnable(false);
      mBinding.civGymTags.setEnable(false);
      mBinding.civGymArea.setEnable(false);
      mBinding.civGymFacility.setEnable(false);
    }
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    StaffParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  private void initListener() {
    mBinding.civGymLocation.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onAddressClicked();
      }
    });
    mBinding.civGymFacility.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeTo("/dianping/choose",
            new DianPingChoosePageParams().type(DianPingChooseType.CHOOSE_FACILITY)
                .selectedDatas(new ArrayList<>(mViewModel.gymInfo.getValue().getShop_services()))
                .build());
      }
    });
    mBinding.civGymTags.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeTo("/dianping/choose",
            new DianPingChoosePageParams().type(DianPingChooseType.CHOOSE_TAGS)
                .selectedDatas(new ArrayList<>(mViewModel.gymInfo.getValue().getTags()))
                .build());
      }
    });
    mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        btnConfirm();
      }
    });
  }

  public void onAddressClicked() {
    new RxPermissions(getActivity()).request(Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Boolean>() {
          @Override public void call(Boolean aBoolean) {
            if (aBoolean) {
              DianPingShop mGym = mViewModel.gymInfo.getValue();
              Fragment fragment =
                  ChooseAddressFragment.newInstance(mGym.getGd_lng(), mGym.getGd_lat(),
                      mGym.getAddress(), mGym.getGd_city().getName(), mGym.getGd_city().getCode());
              routeTo(fragment);
            } else {
              ToastUtils.show("您拒绝了定位权限");
            }
          }
        });
  }

  private void initRxbus() {
    RxRegiste(RxBus.getBus().register(DianPingChooseDataEvent.class).subscribe(event -> {
      if (event.getType() == DianPingChooseType.CHOOSE_TAGS) {
        updateGymTags(event.getDatas());
      } else if (event.getType() == DianPingChooseType.CHOOSE_FACILITY) {
        updateGymFacilities(event.getDatas());
      }
    }));
    RxBusAdd(EventAddress.class).subscribe(new Action1<EventAddress>() {
      @Override public void call(EventAddress eventAddress) {
        mViewModel.gymInfo.getValue().setAddress(eventAddress.address);
        mViewModel.gymInfo.getValue().setGd_lat(eventAddress.lat);
        mViewModel.gymInfo.getValue().setGd_lng(eventAddress.log);
        DianPingShop.GdCityBean gdCityBean = new DianPingShop.GdCityBean();
        gdCityBean.setCode(String.valueOf(eventAddress.city_code));
        gdCityBean.setCode(eventAddress.city);
        mViewModel.gymInfo.getValue().setGd_city(gdCityBean);
        mBinding.civGymLocation.setContent(eventAddress.address);
      }
    });
  }

  private void updateGymTags(List<ISimpleChooseData> datas) {
    List<GymTag> tags = new ArrayList<>();
    if (datas != null && !datas.isEmpty()) {
      for (ISimpleChooseData data : datas) {
        if (data instanceof GymTag) {
          tags.add((GymTag) data);
        }
      }
    }
    mViewModel.gymInfo.getValue().setTags(tags);
    mBinding.civGymTags.setContent(mViewModel.gymInfo.getValue().getTagsString());
  }

  private void updateGymFacilities(List<ISimpleChooseData> datas) {
    List<GymFacility> facilities = new ArrayList<>();
    if (datas != null && !datas.isEmpty()) {
      for (ISimpleChooseData data : datas) {
        if (data instanceof GymFacility) {
          facilities.add((GymFacility) data);
        }
      }
    }
    mViewModel.gymInfo.getValue().setShop_services(facilities);
    mBinding.civGymFacility.setContent(mViewModel.gymInfo.getValue().getFacilitiesString());
  }

  private void updateGymInfo(DianPingShop gymInfo) {
    mBinding.civGymName.setContent(gymInfo.getName());
    mBinding.civGymLocation.setContent(gymInfo.getAddress());
    mBinding.civGymPhone.setContent(gymInfo.getPhone());
    mBinding.civGymArea.setContent(String.valueOf(gymInfo.getArea()));
    mBinding.civGymTags.setContent(gymInfo.getTagsString());
    mBinding.civGymFacility.setContent(gymInfo.getFacilitiesString());
  }

  private void btnConfirm() {
    DialogUtils.shwoConfirm(getContext(),
        "确认场馆【" + mViewModel.gymInfo.getValue().getName() + "】成为美团点评认证商家吗？",
        new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
            if (dialogAction == DialogAction.POSITIVE) {
              if (permissionModel.check(PermissionServerUtils.STUDIO_LIST_CAN_CHANGE)) {
                mViewModel.putGymInfo(mViewModel.gymInfo.getValue(), barCode);
              } else {
                mViewModel.postDianPingAccount(
                    String.valueOf(mViewModel.gymInfo.getValue().getId()), barCode);
              }
            }
          }
        });
  }
}
