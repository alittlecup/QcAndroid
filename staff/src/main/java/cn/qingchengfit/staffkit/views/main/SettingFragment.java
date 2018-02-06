package cn.qingchengfit.staffkit.views.main;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventFreshUnloginAd;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.events.EventSessionError;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.errors.BusEventThrowable;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.MainActivity;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rxbus.event.UpdateEvent;
import cn.qingchengfit.saasbase.login.LoginActivity;
import cn.qingchengfit.staffkit.views.setting.FixNotifySettingFragment;
import cn.qingchengfit.staffkit.views.setting.FixPhoneFragment;
import cn.qingchengfit.staffkit.views.setting.FixPwFragment;
import cn.qingchengfit.staffkit.views.setting.FixSelfInfoFragment;
import cn.qingchengfit.staffkit.views.setting.ReportFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.android.pushservice.PushManager;
import com.bumptech.glide.Glide;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tencent.TIMManager;
import com.tencent.qcloud.timchat.common.AppData;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.Date;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/16 2016.
 */
public class SettingFragment extends BaseFragment implements SettingView {
    public static final int RESULT_LOGIN = 1;
    @BindView(R.id.header_icon) ImageView headerIcon;
    @BindView(R.id.drawer_name) TextView drawerName;
    @BindView(R.id.update_time) TextView updateTime;
    @BindView(R.id.logout) TextView logout;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject GymWrapper gymWrapper;
    @Inject RestRepository mRestRepository;
    @Inject SettingPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject BusEventThrowable busEventThrowable;
  @Inject BaseRouter baseRouter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        delegatePresenter(presenter, this);
        onVisible();
      RxBusAdd(EventSessionError.class)
          .onBackpressureDrop()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<EventSessionError>() {
            @Override public void call(EventSessionError eventSessionError) {
              onLogout();
              ToastUtils.show("登录过期");
              goLogin();
            }
          },busEventThrowable);

        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
      super.initToolbar(toolbar);
      toolbar.setNavigationIcon(null);
        toolbarTitile.setText(R.string.setting);
    }

    @Override protected void onVisible() {
        super.onVisible();
        if (getContext() != null) {
            if (!loginStatus.isLogined()) {
                /*
                 * 未登陆用户
                 */
                Glide.with(getContext())
                    .load(PhotoUtils.getSmall(Configs.HEADER_COACH_MALE))
                    .asBitmap()
                    .into(new CircleImgWrapper(headerIcon, getContext()));
                drawerName.setText("未登录");
                logout.setVisibility(View.GONE);
            } else {
                /*
                 * 已登录用户
                 */
                presenter.getSelfInfo();
            }
        }
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        updateTime.setText(BuildConfig.DEBUG ? "DEBUG版本-更新时间:" + DateUtils.Date2YYYYMMDDHHmm(
            new Date(PreferenceUtils.getPrefLong(getContext(), "update", 0L) * 1000)) : AppUtils.getAppVer(getActivity()));
    }

    @OnClick(R.id.drawer_headerview) public void onHeader() {
        if (!loginStatus.isLogined()) {
            goLogin();
            return;
        }

        FixSelfInfoFragment.start(this, 0, loginStatus.getLoginUser());
    }

    @OnClick(R.id.setting_fixpw) public void onFixPw() {
        if (!loginStatus.isLogined()) {
            goLogin();
            return;
        }
        FixPwFragment.start(this, 1);
    }

    @OnClick(R.id.setting_fixphone) public void onFixPhone() {
        if (!loginStatus.isLogined()) {
            goLogin();
            return;
        }
        FixPhoneFragment.start(this, 2);
    }

    @OnClick(R.id.setting_fix_notify) public void onFixCheckin() {
        if (!loginStatus.isLogined()) {
            goLogin();
            return;
        }
        FixNotifySettingFragment.start(this, 4);
    }

    @OnClick(R.id.aboutus) public void onAboutUs() {
        WebActivity.startWeb(Configs.ABOUT_US, true, getActivity());
    }

    @OnClick(R.id.share) public void onShare() {
        ShareDialogFragment.newInstance("健身房管理", getString(R.string.str_share_app), "http://qcresource.b0.upaiyun.com/14.pic_thumb.jpg",
            Configs.DOWNLOAD_MANAGE).show(getFragmentManager(), "");
    }

    @OnClick(R.id.web_site) public void onWebSite() {
        new MaterialDialog.Builder(getContext()).autoDismiss(true)
            .content("网页端地址\n" + "http://cloud.qingchengfit.cn/backend/settings/")
            .positiveText(R.string.common_comfirm)
            .negativeText(R.string.copy_link)
            .autoDismiss(true)
            .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText("qingcheng", "http://cloud.qingchengfit.cn/backend/settings/"));
                    ToastUtils.showS("已复制");
                }
            })
            .show();
    }

    @OnClick(R.id.report) public void onReport() {
        ReportFragment.start(this, 3);
    }

    @OnClick(R.id.update) public void onUpdate() {
        RxBus.getBus().post(new UpdateEvent());
    }

    @OnClick(R.id.logout) public void onLogout() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            loginStatus.logout(getContext());
          App.staffId = "";
          PushManager.stopWork(getContext().getApplicationContext());
          AppData.clear(getContext());
          TIMManager.getInstance().logout();
          PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION, "");
          PreferenceUtils.setPrefString(getContext(), Configs.PREFER_WORK_ID, "");
          PreferenceUtils.setPrefString(getContext(), Configs.CUR_BRAND_ID, "");
          MiPushClient.unregisterPush(getContext().getApplicationContext());
            RxBus.getBus().post(new EventLoginChange());
            onVisible();
        }
    }

  @OnClick(R.id.civ_resume) public void onMyResume() {
    if (!loginStatus.isLogined()) {
      baseRouter.toLogin(this);
      return;
    }
    baseRouter.routerTo("recruit", "resume", getContext(), 1001);
  }

  @OnClick(R.id.civ_orders) public void onOders() {
    if (!loginStatus.isLogined()) {
      baseRouter.toLogin(this);
      return;
    }
    BaseRouter.routerToWeb(Configs.Server + Configs.HOST_ORDERS, getContext());
  }


    @Override public String getFragmentName() {
        return SettingFragment.class.getName();
    }

    @Override public void onSelfInfo(Staff bean) {
        hideLoading();
        logout.setVisibility(View.VISIBLE);
        loginStatus.setLoginUser(bean);
        Glide.with(getContext())
            .load(PhotoUtils.getSmall(bean.getAvatar()))
            .asBitmap()
            .into(new CircleImgWrapper(headerIcon, getContext()));
        drawerName.setText(bean.getUsername());
        PreferenceUtils.setPrefString(App.context, Configs.PREFER_WORK_NAME, bean.getUsername());
        if (!BuildConfig.DEBUG) {

            try {
                JSONObject properties = SensorsDataAPI.sharedInstance(getActivity().getApplicationContext()).getSuperProperties();
                if (properties == null) properties = new JSONObject();
                properties.put("qc_user_id", bean.getId());
                properties.put("qc_user_phone", bean.getPhone());
                SensorsDataAPI.sharedInstance(getActivity().getApplicationContext()).registerSuperProperties(properties);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            showLoading();
            presenter.getSelfInfo();
            /*
             * 发送消息告诉未登录页面已登录
             */
            RxBus.getBus().post(new EventFreshUnloginAd());
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    void goLogin() {
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        toLogin.putExtra("isRegiste", false);
        toLogin.putExtra("setting", true);
        startActivityForResult(toLogin, RESULT_LOGIN);
    }
}
