package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.qingchengfit.fitcoach.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyPwFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyPwFragment extends BaseSettingFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.modifypw_ori_pw)
    EditText modifypwOriPw;
    @Bind(R.id.modifypw_new_pw)
    EditText modifypwNewPw;
    @Bind(R.id.modifypw_comfirm_pw)
    EditText modifypwComfirmPw;
    @Bind(R.id.modifypw_comfirm_btn)
    Button modifypwComfirmBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ModifyPwFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifyPwFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyPwFragment newInstance(String param1, String param2) {
        ModifyPwFragment fragment = new ModifyPwFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modify_pw, container, false);
        ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, "修改密码");
        
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
