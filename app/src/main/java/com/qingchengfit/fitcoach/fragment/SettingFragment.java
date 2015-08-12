package com.qingchengfit.fitcoach.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qingchengfit.fitcoach.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.settting_modifyinfo)
    RelativeLayout setttingModifyinfo;
    @Bind(R.id.setting_modifypw)
    RelativeLayout settingModifypw;
    @Bind(R.id.setting_advice)
    RelativeLayout settingAdvice;
    @Bind(R.id.setting_aboutus)
    RelativeLayout settingAboutus;
    FragmentManager mFragmentManager;
    FragmentCallBack fragmentCallBack;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle(getActivity().getString(R.string.setting_title));

        return view;
    }

    @OnClick({R.id.setting_aboutus,
            R.id.setting_advice,
            R.id.setting_modifypw,
            R.id.settting_modifyinfo

    })
    public void onClickUs(View view) {
        switch (view.getId()) {
            case R.id.settting_modifyinfo:
                mFragmentManager.beginTransaction()
                        .replace(R.id.settting_fraglayout, ModifyInfoFragment.newInstance("", ""))
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
                        .commit();
                break;
            case R.id.setting_modifypw:
                mFragmentManager.beginTransaction()
                        .replace(R.id.settting_fraglayout, ModifyPwFragment.newInstance("", ""))
                        .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
                        .commit();
                break;
            case R.id.setting_advice:
//                mFragmentManager.beginTransaction()
//                        .replace(R.id.settting_fraglayout,.newInstance("",""))
//                        .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out)
//                        .commit();
                break;
            case R.id.setting_aboutus:
//                mFragmentManager.beginTransaction()
//                        .replace(R.id.settting_fraglayout,.newInstance("",""))
//                        .setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out)
//                        .commit();
                break;
            default:
                break;

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentCallBack = (FragmentCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentCallBack = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
