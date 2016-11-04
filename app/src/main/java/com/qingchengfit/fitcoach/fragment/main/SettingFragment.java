package com.qingchengfit.fitcoach.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.AppUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.component.DialogSheet;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 *   setting view
 *
 */
public class SettingFragment extends Fragment {


    @Bind(R.id.settting_modifyinfo)
    RelativeLayout setttingModifyinfo;
    @Bind(R.id.setting_modifypw)
    RelativeLayout settingModifypw;
    @Bind(R.id.setting_advice)
    RelativeLayout settingAdvice;
    @Bind(R.id.setting_aboutus)
    RelativeLayout settingAboutus;
    FragmentManager mFragmentManager;
    DialogSheet logoutSheet;
    @Bind(R.id.version)
    TextView version;

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
        ButterKnife.bind(this, view);
//        fragmentCallBack.onToolbarMenu(R.menu.menu_logout, 0, getActivity().getString(R.string.setting_title));
//        fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                showDialog();
//                return true;
//            }
//        });
        version.setText("v"+ AppUtils.getAppVer(getActivity()));
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
//            case R.id.setting_modifypw:
//                fragmentCallBack.onFragmentChange(ModifyPwFragment.newInstance("", ""));
//                break;
//
//            case R.id.setting_advice:
//                fragmentCallBack.onFragmentChange(new AdviceFragment());
//                break;
//            case R.id.setting_workexpe:
//                fragmentCallBack.onFragmentChange(new WorkExepSettingFragment());
//                break;
//            case R.id.setting_aboutus:
//                Intent toWeb = new Intent(getContext(), WebActivity.class);
//                toWeb.putExtra("url", Configs.Server + "/aboutus/?oem="+getString(R.string.oem_tag));
//                startActivity(toWeb);
//                break;
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
//            case R.id.setting_modifyphone:
//                fragmentCallBack.onFragmentChange(new ModifyPhoneFragment());
//                break;
//            case R.id.setting_calsync:
//                fragmentCallBack.onFragmentChange(CalSyncFragment.newInstance("", ""));
//                break;
            default:
                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
