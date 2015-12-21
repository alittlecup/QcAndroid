package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paper.paperbaselibrary.utils.AppUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.component.DialogSheet;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends BaseSettingFragment {


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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            })
//                    .addButton("退出应用", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    logoutSheet.dismiss();
//                    Intent it = new Intent(getActivity(), MainActivity.class);
//                    it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    it.putExtra(MainActivity.ACTION, MainActivity.FINISH);
//                    startActivity(it);
//                }
//            })
            ;

        }
        logoutSheet.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(R.menu.menu_logout, 0, getActivity().getString(R.string.setting_title));
        fragmentCallBack.onToolbarClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showDialog();
                return true;
            }
        });
        version.setText("v"+ AppUtils.getAppVer(getActivity()));
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e(this.getTag()+"  requestCode:"+requestCode+" resultCode:"+resultCode);
    }

    @Override
    public void onResume() {
        super.onResume();
//        fragmentCallBack.onToolbarMenu(0, 0, getActivity().getString(R.string.setting_title));
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
            case R.id.settting_modifyinfo:
                fragmentCallBack.onFragmentChange(ModifyInfoFragment.newInstance("", ""));
                break;

            case R.id.setting_modifypw:
                fragmentCallBack.onFragmentChange(ModifyPwFragment.newInstance("", ""));
                break;

            case R.id.setting_advice:
                fragmentCallBack.onFragmentChange(new AdviceFragment());

//                mFragmentManager.beginTransaction()
//                        .replace(R.id.settting_fraglayout,.newInstance("",""))
//                        .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out)
//                        .commit();
                break;
            case R.id.setting_workexpe:
                fragmentCallBack.onFragmentChange(new WorkExepSettingFragment());
                break;
            case R.id.setting_aboutus:
                fragmentCallBack.onFragmentChange(WebFragment.newInstance(Configs.Server + "/aboutus/", true));
                fragmentCallBack.onToolbarMenu(0, 0, "关于我们");
                break;
            case R.id.setting_comfirm:
                fragmentCallBack.onFragmentChange(new RecordFragment());
                break;
            case R.id.setting_logout:

                Intent it = new Intent(getActivity(), MainActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                it.putExtra(MainActivity.ACTION, MainActivity.LOGOUT);
                startActivity(it);
                break;

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
        ButterKnife.unbind(this);
    }
}
