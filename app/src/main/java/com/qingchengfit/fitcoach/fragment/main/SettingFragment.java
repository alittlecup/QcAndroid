package com.qingchengfit.fitcoach.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.component.DialogSheet;
import com.qingchengfit.fitcoach.fragment.AdviceFragment;
import com.qingchengfit.fitcoach.fragment.BaseSettingFragment;
import com.qingchengfit.fitcoach.fragment.CalSyncFragment;
import com.qingchengfit.fitcoach.fragment.ModifyPhoneFragment;
import com.qingchengfit.fitcoach.fragment.ModifyPwFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * setting view
 */
public class SettingFragment extends BaseSettingFragment {


    @BindView(R.id.settting_modifyinfo)
    RelativeLayout setttingModifyinfo;
    @BindView(R.id.setting_modifypw)
    RelativeLayout settingModifypw;
    @BindView(R.id.setting_advice)
    RelativeLayout settingAdvice;
    @BindView(R.id.setting_aboutus)
    RelativeLayout settingAboutus;
    FragmentManager mFragmentManager;
    DialogSheet logoutSheet;
    @BindView(R.id.version_code)
    TextView versionCode;
    private Unbinder unbinder;

    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
    }

    public void showDialog() {
        if (logoutSheet == null) {
            logoutSheet = DialogSheet.builder(getContext());
            logoutSheet.addButton("退出登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutSheet.dismiss();
                    Intent it = new Intent(getActivity(), MainActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    it.putExtra(MainActivity.ACTION, MainActivity.LOGOUT);
                    startActivity(it);
                }
            });
        }
        logoutSheet.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder=ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0 ,R.drawable.ic_arrow_left,"设置");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.setting_aboutus,
            R.id.setting_advice,
            R.id.setting_modifypw,
            R.id.settting_modifyinfo,
            R.id.setting_comfirm,
            R.id.setting_workexpe,
            R.id.setting_logout,
            R.id.setting_modifyphone,
            R.id.setting_calsync
    })
    public void onClickUs(View view) {
        switch (view.getId()) {
//            case R.id.settting_modifyinfo:
//                fragmentCallBack.onFragmentChange(ModifyInfoFragment.newInstance("", ""));
//                break;
//
            case R.id.setting_modifypw:
                fragmentCallBack.onFragmentChange(ModifyPwFragment.newInstance("", ""));
                break;
//
            case R.id.setting_advice:
                fragmentCallBack.onFragmentChange(new AdviceFragment());
                break;
//            case R.id.setting_workexpe:
//                fragmentCallBack.onFragmentChange(new WorkExepSettingFragment());
//                break;
            case R.id.setting_aboutus:
                Intent toWeb = new Intent(getContext(), WebActivity.class);
                toWeb.putExtra("url", Configs.Server + "/aboutus/?oem="+getString(R.string.oem_tag));
                startActivity(toWeb);
                break;
//            case R.id.setting_comfirm:
//                fragmentCallBack.onFragmentChange(new RecordFragment());
//                break;
//            case R.id.setting_logout:
//
//                Intent it = new Intent(getActivity(), MainActivity.class);
//                it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                it.putExtra(MainActivity.ACTION, MainActivity.LOGOUT);
//                startActivity(it);
//                break;
//
            case R.id.setting_modifyphone:
                fragmentCallBack.onFragmentChange(new ModifyPhoneFragment());
                break;
            case R.id.setting_calsync:
                fragmentCallBack.onFragmentChange(CalSyncFragment.newInstance("", ""));
                break;
            default:
                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.setting_share, R.id.setting_update, R.id.setting_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_share:
                break;
            case R.id.setting_update:
                break;
            case R.id.setting_logout:
                Intent it = new Intent(getActivity(), Main2Activity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                it.putExtra(Main2Activity.ACTION, Main2Activity.LOGOUT);
                startActivity(it);
                break;
        }
    }
}
