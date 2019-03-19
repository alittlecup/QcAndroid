package cn.qingchengfit.staffkit.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.model.responese.SignInUrl;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.qrcode.qrgenearator.QRGContents;
import cn.qingchengfit.saascommon.qrcode.qrgenearator.QRGEncoder;
import cn.qingchengfit.staff.routers.StaffParamsInjector;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.databinding.FragmentGymSettingSuccessBinding;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.wxpreview.old.WebActivityForGuide;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.google.zxing.WriterException;
import javax.inject.Inject;
import rx.functions.Action1;

@Leaf(module = "staff", path = "/gym/setting/success") public class GymServiceSettingSuccessFragment
    extends SaasCommonFragment {
  FragmentGymSettingSuccessBinding mBinding;
  @Need Integer type;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StaffRespository qcRestRepository;

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
        loadSignUrl();
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

  private void loadSignUrl() {
    RxRegiste(qcRestRepository.getStaffAllApi()
        .qcGetCheckinUrl(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(new Action1<SignInUrl>() {
          @Override public void call(SignInUrl signInUrl) {
            if (signInUrl != null) {
              String checkin_url = signInUrl.data.getCheckin_url();
              QRGEncoder qrgEncoder = new QRGEncoder(checkin_url, null, QRGContents.Type.TEXT,
                  MeasureUtils.dpToPx(180f, getResources()));
              try {
                mBinding.imgSignCode.setImageBitmap(qrgEncoder.encodeAsBitmap());
                mBinding.imgSignCode.setVisibility(View.VISIBLE);
              } catch (WriterException e) {
                e.printStackTrace();
              }
            }
          }
        }, new NetWorkThrowable()));
  }
}
