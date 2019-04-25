package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.Personage;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.staffkit.databinding.FragmentSignInMemberBinding;
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
    return mBinding;
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
    mBinding.tvCardDesc.setText(card.getBrand_id());
    if (card.getType() == 3) {
      mBinding.tvCardDesc.setText("期限卡不扣费");
    } else {
      mBinding.tvCardDesc.setText("余额: " + CardBusinessUtils.getCardBlance(card));
    }
  }
}
