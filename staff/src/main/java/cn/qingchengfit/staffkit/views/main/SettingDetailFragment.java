package cn.qingchengfit.staffkit.views.main;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventFreshUnloginAd;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.model.responese.SettingUsecase;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.FragmentSettingDetailBinding;
import cn.qingchengfit.staffkit.rxbus.event.UpdateEvent;
import cn.qingchengfit.staffkit.views.setting.FixNotifySettingFragment;
import cn.qingchengfit.staffkit.views.setting.ReportFragment;
import cn.qingchengfit.staffkit.views.setting.SettingActivity;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.baidu.android.pushservice.PushManager;
import com.tencent.TIMManager;
import com.tencent.qcloud.timchat.common.AppData;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.Date;
import javax.inject.Inject;

@Leaf(module = "setting", path = "/setting/detail") public class SettingDetailFragment
    extends SaasBaseFragment {
  FragmentSettingDetailBinding mBinding;
  @Inject LoginStatus loginStatus;
  @Inject SettingUsecase usercase;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentSettingDetailBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.setToolbarModel(new ToolbarModel("设置"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.setFragment(this);
  }
  @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    mBinding.updateTime.setText(BuildConfig.DEBUG ? "DEBUG版本-更新时间:" + DateUtils.Date2YYYYMMDDHHmm(
        new Date(PreferenceUtils.getPrefLong(getContext(), "update", 0L) * 1000))
        : AppUtils.getAppVer(getActivity()));
  }


  public void onLogout() {
    if (getActivity() != null && getActivity() instanceof SettingActivity) {
      loginStatus.logout(getContext());
      App.staffId = "";
      PushManager.stopWork(getContext().getApplicationContext());
      AppData.clear(getContext());
      TIMManager.getInstance().logout();
      PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION, "");
      PreferenceUtils.setPrefString(getContext(), Configs.PREFER_WORK_ID, "");
      PreferenceUtils.setPrefString(getContext(), Configs.CUR_BRAND_ID, "");
      PreferenceUtils.setPrefString(getContext(), "session_id", "");
      MiPushClient.unregisterPush(getContext().getApplicationContext());
      RxBus.getBus().post(new EventLoginChange());
      getActivity().onBackPressed();
    }
  }

  public void onAboutUs() {
    WebActivity.startWeb(Configs.ABOUT_US, true, getActivity());
  }

  public void onReport() {
    ReportFragment.start(this, 3);
  }

  public void onUpdate() {
    RxBus.getBus().post(new UpdateEvent());
  }

  public void onFixCheckin() {
    if (!loginStatus.isLogined()) {
      goLogin();
      return;
    }
    FixNotifySettingFragment.start(this, 4);
  }

  public void onWebSite() {
    new MaterialDialog.Builder(getContext()).autoDismiss(true)
        .content("网页端地址\n" + "http://cloud.qingchengfit.cn/backend/settings/")
        .positiveText(R.string.common_comfirm)
        .negativeText(R.string.copy_link)
        .autoDismiss(true)
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            ClipboardManager cmb =
                (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText("qingcheng",
                "http://cloud.qingchengfit.cn/backend/settings/"));
            ToastUtils.showS("已复制");
          }
        })
        .show();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      showLoading();
      usercase.getSelfInfo(qcResponseSelfInfo -> {
        if (qcResponseSelfInfo.getStatus() == ResponseConstant.SUCCESS) {
          loginStatus.setLoginUser(qcResponseSelfInfo.data.staff);
        } else {

        }
      });
      RxBus.getBus().post(new EventFreshUnloginAd());
    }
  }

  public void onShare() {
    ShareDialogFragment.newInstance("健身房管理", getString(R.string.str_share_app),
        "http://qcresource.b0.upaiyun.com/14.pic_thumb.jpg", Configs.DOWNLOAD_MANAGE)
        .show(getFragmentManager(), "");
  }

  void goLogin() {
    Intent toLogin = new Intent(getContext().getPackageName(),
        Uri.parse(AppUtils.getCurAppSchema(getContext()) + "://login/"));
    toLogin.putExtra("isRegiste", false);
    toLogin.putExtra("setting", true);
    startActivityForResult(toLogin, 1);
  }
}
