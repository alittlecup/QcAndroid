package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.MainActivity;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, getActivity().getString(R.string.setting_title));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentCallBack.onToolbarMenu(0, 0, getActivity().getString(R.string.setting_title));
    }

    @OnClick({R.id.setting_aboutus,
            R.id.setting_advice,
            R.id.setting_modifypw,
            R.id.settting_modifyinfo,
            R.id.setting_comfirm,
            R.id.setting_workexpe,
            R.id.setting_logout,
            R.id.setting_modifyphone,

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
                fragmentCallBack.onFragmentChange(WebFragment.newInstance(Configs.Server + "/aboutus/"));
                fragmentCallBack.onToolbarMenu(0, 0, "关于我们");
//
//  mFragmentManager.beginTransaction()
//                        .replace(R.id.settting_fraglayout,.newInstance("",""))
//                        .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out)
//                        .commit();
                break;
            case R.id.setting_comfirm:
                fragmentCallBack.onFragmentChange(new RecordFragment());
                break;
            case R.id.setting_logout:

                Intent it = new Intent(getActivity(), MainActivity.class);
                it.putExtra(MainActivity.ACTION, MainActivity.LOGOUT);
                startActivity(it);
                break;

            case R.id.setting_modifyphone:
                fragmentCallBack.onFragmentChange(new ModifyPhoneFragment());
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
