package cn.qingchengfit.staffkit.views.signin.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.SignInItem;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.staffkit.databinding.FragmentSignInMemberBinding;
import cn.qingchengfit.staffkit.views.signin.SignInActivity;
import cn.qingchengfit.staffkit.views.signin.bean.SignInCheckInQrCodeBean;
import cn.qingchengfit.staffkit.views.wardrobe.choose.ChooseWardrobeActivity;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;

public class SignInMemberCheckinFragment
    extends SaasBindingFragment<FragmentSignInMemberBinding, SignInMemberVM> {
  public static SignInMemberCheckinFragment getWithQrCode(String qrcode) {
    SignInMemberCheckinFragment fragment = new SignInMemberCheckinFragment();
    fragment.setArguments(new BundleBuilder().withString("qrcode", qrcode).build());
    return fragment;
  }

  @Override protected void subscribeUI() {
    mViewModel.data.observe(this, data -> {
      if (data != null) {
        upDateUser(data.user);
        upDateCard(data.card);
      }
    });
    mViewModel.checkInResult.observe(this, result -> {
      hideLoading();
      if (result != null && result) {
        ToastUtils.show("确认入场成功");
        getActivity().onBackPressed();
      }
    });
  }

  @Override
  public FragmentSignInMemberBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = FragmentSignInMemberBinding.inflate(inflater, container, false);
    if (getArguments() != null) {
      String qrcode = getArguments().getString("qrcode", "");
      if (!TextUtils.isEmpty(qrcode)) {
        mViewModel.loadMemberInfo(qrcode);
      } else {
        ToastUtils.show("扫码内容不能为空");
      }
    }
    initToolbar();

    if (SignInActivity.checkinWithLocker == 1) {
      mBinding.llSignLocker.setVisibility(View.VISIBLE);
    } else {
      mBinding.llSignLocker.setVisibility(View.GONE);
    }
    mBinding.civChooseSite.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent chooseLocker = new Intent(getActivity(), ChooseWardrobeActivity.class);
        chooseLocker.putExtra("locker", selectedLocker);
        startActivityForResult(chooseLocker, 1001);
      }
    });
    mBinding.btnApply.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        checkIn();
      }
    });

    return mBinding;
  }

  private void checkIn() {
    SignInCheckInQrCodeBean.Data value = mViewModel.data.getValue();
    if (value != null) {
      mViewModel.checkIn(value.user.id, value.card.getId(),
          selectedLocker == null ? "" : selectedLocker.id + "");
    }
  }

  private Locker selectedLocker;

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1001) {
        if (data != null) {
          Locker locker = data.getParcelableExtra("locker");
          if (locker != null) {
            selectedLocker = locker;
            mBinding.civChooseSite.setContent(locker.name);
          }
        }
      }
    }
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("签到入场"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void upDateUser(Personage user) {
    PhotoUtils.smallCircle(mBinding.imgUserPhoto, user.getAvatar());
    mBinding.tvUserName.setText(user.getUsername());
    mBinding.tvUserPhone.setText(user.getPhone());
  }

  private void upDateCard(Card card) {
    mBinding.tvCardName.setText(card.getName());
    mBinding.tvCardDesc.setText("余额: "+ CardBusinessUtils.getCardBlance(card));
    if (card.getType() == 3) {
      mBinding.tvCost.setText("期限卡不扣费");
    } else {
      mBinding.tvCost.setText(
          " 扣费: " + card.cost + CardBusinessUtils.getCardTypeCategoryUnit(card.getType()));
    }
  }
}
