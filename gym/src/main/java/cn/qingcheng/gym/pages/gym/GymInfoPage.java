package cn.qingcheng.gym.pages.gym;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.bean.GymType;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymInfoPageBinding;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.views.fragments.CommonInputTextFragment;
import cn.qingchengfit.widgets.BottomChooseDialog;
import com.afollestad.materialdialogs.DialogAction;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@Leaf(module = "gym", path = "/gym/info") public class GymInfoPage
    extends GymBaseFragment<GyGymInfoPageBinding, GymInfoViewModel> {
  @Need public Shop shop = new Shop();
  @Need public Brand brand;

  BottomChooseDialog gymTypeDialog;
  private ChoosePictureFragmentNewDialog choosePicFragment;

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
        mBinding.civGymType.setContent(findGymTypeValueByType(shop.gym_type, types));
      }
    });
    mViewModel.deleteResult.observe(this, aBoolean -> {
      hideLoading();
      ToastUtils.show(aBoolean ? "删除场馆成功" : "删除场馆失败");
      if (aBoolean) {
        getActivity().onBackPressed();
      }
    });
    mViewModel.deleteResult.observe(this, aBoolean -> {
      hideLoading();
      ToastUtils.show(aBoolean ? "离职退出场馆成功" : "离职退出场馆失败");
      if (aBoolean) {
        getActivity().onBackPressed();
      }
    });
  }

  public String findGymTypeValueByType(int type, List<GymType> types) {
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

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RxBusAddAllLife(EventTxT.class).subscribe(new Action1<EventTxT>() {
      @Override public void call(EventTxT eventTxT) {
        shop.description = eventTxT.txt;
        mBinding.civGymMark.setContent(CmStringUtils.delHTMLTag(shop.description));
      }
    });
    RxBusAddAllLife(EventAddress.class).subscribe(new Action1<EventAddress>() {
      @Override public void call(EventAddress eventAddress) {
        shop.address = eventAddress.address;
        shop.gd_lat = eventAddress.lat;
        shop.gd_lng = eventAddress.log;
        shop.gd_district_id = eventAddress.city_code + "";
        mBinding.civGymAddress.setContent(eventAddress.address);
      }
    });
  }

  @Override
  public GyGymInfoPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymInfoPageBinding.inflate(inflater, container, false);
    initToolbar();
    if (shop == null) {
      shop = new Shop();
    }
    initView();
    initListener();

    RxTextView.afterTextChangeEvents(mBinding.civGymName.getEditText())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(text -> {
          shop.name = text.editable().toString();
        });
    RxTextView.afterTextChangeEvents(mBinding.civGymSquare.getEditText())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(text -> {
          try {
            String s = text.editable().toString();
            if (TextUtils.isEmpty(s)) {
              mBinding.civGymSquare.setContent("0");
              shop.area = 0;
              return;
            }
            shop.area = Float.valueOf(s);
          } catch (NumberFormatException e) {
            ToastUtils.show("请输入正确数字");
            mBinding.civGymSquare.setContent("");
            shop.area = 0;
          }
        });
    RxTextView.afterTextChangeEvents(mBinding.civGymPhone.getEditText())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(text -> {
          shop.contact = text.editable().toString();
        });
    return mBinding;
  }

  public void onLayoutGymImgClicked() {
    if (choosePicFragment == null) {
      choosePicFragment = ChoosePictureFragmentNewDialog.newInstance();
      choosePicFragment.setResult(new ChoosePictureFragmentNewDialog.ChoosePicResult() {
        @Override public void onChoosefile(String filePath) {

        }

        @Override public void onUploadComplete(String filePaht, String url) {
          PhotoUtils.smallCircle(mBinding.imgGymPhoto, url);
          shop.photo = url;
        }
      });
    }
    if (!choosePicFragment.isVisible()) {
      choosePicFragment.show(getChildFragmentManager(), "");
    }
  }

  private void initListener() {
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
    mBinding.imgGymPhoto.setOnClickListener(v -> onLayoutGymImgClicked());
  }

  private void initView() {
    mBinding.civGymName.setContent(shop.name);
    mBinding.civGymPhone.setContent(shop.contact);
    mBinding.civGymAddress.setContent(shop.address);
    mBinding.civGymMark.setContent(CmStringUtils.delHTMLTag(shop.description));
    mBinding.civGymSquare.setContent(shop.area + "");
    mBinding.viewShadow.setVisibility(View.VISIBLE);
    mBinding.tvGymAction.setText("离职退出该场馆");
    mBinding.civGymType.setContent(
        findGymTypeValueByType(shop.gym_type, mViewModel.gymTypes.getValue()));

    Glide.with(getContext())
        .load(shop.photo)
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(mBinding.imgGymPhoto, getContext()));

    mBinding.tvGymAction.setOnClickListener(v -> showFireGymDialog());
  }

  public void showFireGymDialog() {
    DialogUtils.showIconDialog(getContext(), -1, "确认退出", "退出后将无法恢复，\n需由该健身房工作人员添加", "取消", "确定",
        (materialDialog, dialogAction) -> {
          materialDialog.dismiss();
          if (dialogAction == DialogAction.POSITIVE) {
            showLoading();
            mViewModel.quiteGym(shop.id);
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
    Toolbar toolbar = mBinding.includeToolbar.toolbar;
    toolbar.inflateMenu(R.menu.menu_save);
    initToolbar(toolbar);
  }

  public boolean checkShop(Shop shop) {
    if (TextUtils.isEmpty(shop.name)) {
      ToastUtils.show("请填写场馆名称");
    } else if (StringUtils.checkPhoneNumber(shop.phone, false)) {
      ToastUtils.show("请填写正确手机号");
    } else if (TextUtils.isEmpty(shop.address)) {
      ToastUtils.show("请选择场馆地址");
    } else if (shop.area <= 0) {
      ToastUtils.show("请输入场馆面积");
    } else {
      return true;
    }
    return false;
  }
}
