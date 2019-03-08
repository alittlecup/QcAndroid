package cn.qingchengfit.staffkit.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.staff.routers.StaffParamsInjector;
import cn.qingchengfit.staffkit.databinding.FragmentGymSettingSuccessBinding;
import cn.qingchengfit.staffkit.views.gym.GymActivity;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.wxpreview.old.WebActivityForGuide;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

@Leaf(module = "staff", path = "/gym/setting/success") public class GymServiceSettingSuccessFragment
    extends SaasCommonFragment {
  FragmentGymSettingSuccessBinding mBinding;
  @Need Integer type;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentGymSettingSuccessBinding.inflate(inflater, container, false);
    initView();
    mBinding.setToolbarModel(new ToolbarModel("设置成功"));
    initToolbar(mBinding.includeToolbar.toolbar);
    return mBinding.getRoot();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    StaffParamsInjector.inject(this);
    super.onCreate(savedInstanceState);
  }

  private void initView() {
    switch (type) {
      case 1:
        mBinding.tvSuccess.setText("恭喜您，团课设置成功 !");
        mBinding.tvSuccessHint.setText("推广给会员开始约课吧");
        PreferenceUtils.setPrefBoolean(getContext(), "gym_setting_group", true);
        break;
      case 2:
        mBinding.tvSuccess.setText("恭喜您，私教设置成功 !");
        mBinding.tvSuccessHint.setText("推广给会员开始约课吧");
        PreferenceUtils.setPrefBoolean(getContext(), "gym_setting_private", true);

        break;
      case 3:
        mBinding.tvSuccess.setText("恭喜您，自主训练设置成功 !");
        mBinding.tvSuccessHint.setText("以下是场馆的签到二维码");
        mBinding.imgSignCode.setVisibility(View.VISIBLE);
        PreferenceUtils.setPrefBoolean(getContext(), "gym_setting_train", true);
        break;
      case 4:
        mBinding.tvSuccess.setText("恭喜您，商品添加成功 !");
        mBinding.tvSuccessHint.setText("推广给会员开始购买吧");
        PreferenceUtils.setPrefBoolean(getContext(), "gym_setting_mall", true);
        break;
    }
    mBinding.btnPreview.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        guideToStudentPreview();
      }
    });
    int gymSettingCount = PreferenceUtils.getPrefInt(getContext(), "GymSettingCount", 0);
    int gymSettedCount = PreferenceUtils.getPrefInt(getContext(), "GymSettedCount", 0);
    if (gymSettingCount > 0 && gymSettingCount == gymSettedCount) {
      mBinding.btnAction.setText("返回场馆主页");
      mBinding.btnAction.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          getActivity().finish();
        }
      });
    } else {
      gymSettedCount += 1;
      PreferenceUtils.setPrefInt(getContext(), "GymSettedCount", gymSettedCount);
      mBinding.btnAction.setText("继续设置");
      mBinding.btnAction.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          getActivity().onBackPressed();
        }
      });
    }
  }

  private void guideToStudentPreview() {
    Intent toWebForGuide = new Intent(getActivity(), WebActivityForGuide.class);
    toWebForGuide.putExtra("url", PreferenceUtils.getPrefString(getContext(), "mPreViewUrl", ""));
    toWebForGuide.putExtra("copyurl", PreferenceUtils.getPrefString(getContext(), "mCopyUrl", ""));
    startActivity(toWebForGuide);
  }
}
