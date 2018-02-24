package com.qingchengfit.fitcoach.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.ShareDialogFragment;
import com.baidu.android.pushservice.PushManager;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.fragment.AdviceFragment;
import com.qingchengfit.fitcoach.fragment.BaseSettingFragment;
import com.qingchengfit.fitcoach.fragment.CalSyncFragment;
import com.tencent.qcloud.timchat.common.AppData;
import com.xiaomi.mipush.sdk.MiPushClient;
import javax.inject.Inject;

/**
 * setting view
 */
public class SettingFragment extends BaseSettingFragment {

    @BindView(R.id.settting_modifyinfo) RelativeLayout setttingModifyinfo;
    @BindView(R.id.setting_modifypw) RelativeLayout settingModifypw;
    @BindView(R.id.setting_advice) RelativeLayout settingAdvice;
    @BindView(R.id.setting_aboutus) RelativeLayout settingAboutus;
    FragmentManager mFragmentManager;
    @BindView(R.id.version_code) TextView versionCode;
    @Inject LoginStatus loginStatus;
    @Inject BaseRouter baseRouter;
    private Unbinder unbinder;
    private String versionStr;

    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
    }



    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, R.drawable.ic_arrow_left, "设置");
        versionCode.setText(AppUtils.getAppVer(getContext()));
        return view;
    }

    @Override public void onResume() {
        super.onResume();
    }

    @OnClick({
        R.id.setting_aboutus, R.id.setting_advice, R.id.setting_modifypw, R.id.settting_modifyinfo, R.id.setting_comfirm,
        R.id.setting_workexpe, R.id.setting_logout, R.id.setting_modifyphone, R.id.setting_calsync
    }) public void onClickUs(View view) {
        switch (view.getId()) {
            case R.id.setting_modifypw:
                //fragmentCallBack.onFragmentChange(ModifyPwFragment.newInstance("", ""));
                break;
            case R.id.setting_advice:
                fragmentCallBack.onFragmentChange(new AdviceFragment());
                break;
            case R.id.setting_aboutus:
                Intent toWeb = new Intent(getContext(), WebActivity.class);
                toWeb.putExtra("url", Configs.Server + "/aboutus/?oem=" + getString(R.string.oem_tag));
                startActivity(toWeb);
                break;
            case R.id.setting_modifyphone:
                //fragmentCallBack.onFragmentChange(new ModifyPhoneFragment());
                break;
            case R.id.setting_calsync:
                fragmentCallBack.onFragmentChange(CalSyncFragment.newInstance("", ""));
                break;
            default:
                break;
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({ R.id.setting_share, R.id.setting_update, R.id.setting_logout }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_share:
                ShareDialogFragment.newInstance(getString(R.string.app_name), getString(R.string.app_slogan),
                    getString(R.string.app_icon_net), getString(R.string.app_download_link)).show(getFragmentManager(), "");
                break;
            case R.id.setting_update:

                break;
            case R.id.setting_logout:
                loginStatus.logout(getContext());
              PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION, "");
              PreferenceUtils.setPrefString(getContext(), "user_info", "");
              PreferenceUtils.setPrefString(getContext(), "coach", "");

              PushManager.stopWork(getContext().getApplicationContext());
              MiPushClient.unregisterPush(getContext().getApplicationContext());
              AppData.clear(getContext());
              App.coachid = 0;
              RxBus.getBus().post(new EventLoginChange());

                Intent it = new Intent(getActivity(), Main2Activity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                it.putExtra(Main2Activity.ACTION, Main2Activity.LOGOUT);
                startActivity(it);
                break;
        }
    }
}
