package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyBodyTestFragment extends Fragment {


    @Bind(R.id.test_date)
    CommonInputView testDate;
    @Bind(R.id.height)
    CommonInputView height;
    @Bind(R.id.weight)
    CommonInputView weight;
    @Bind(R.id.bmi)
    CommonInputView bmi;
    @Bind(R.id.body_fat_rate)
    CommonInputView bodyFatRate;
    @Bind(R.id.upper)
    CommonInputView upper;
    @Bind(R.id.chest)
    CommonInputView chest;
    @Bind(R.id.waistline)
    CommonInputView waistline;
    @Bind(R.id.hipline)
    CommonInputView hipline;
    @Bind(R.id.thigh)
    CommonInputView thigh;
    @Bind(R.id.calf)
    CommonInputView calf;

    private QcBodyTestTemplateRespone.Base mBase;
    public static ModifyBodyTestFragment newInstance(QcBodyTestTemplateRespone.Base base) {

        Bundle args = new Bundle();
        args.putParcelable("base",base);
        ModifyBodyTestFragment fragment = new ModifyBodyTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBase = getArguments().getParcelable("base");
    }

    public ModifyBodyTestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_body_test, container, false);
        ButterKnife.bind(this, view);
        if (mBase.show_bmi)
            bmi.setVisibility(View.VISIBLE);
        else
            bmi.setVisibility(View.GONE);
        if (mBase.show_body_fat_rate)
            bodyFatRate.setVisibility(View.VISIBLE);
        else bodyFatRate.setVisibility(View.GONE);
        if (mBase.show_circumference_of_calf)
            calf.setVisibility(View.VISIBLE);
        else calf.setVisibility(View.GONE);
        chest.setVisibility(mBase.show_circumference_of_chest?View.VISIBLE:View.GONE);
        thigh.setVisibility(mBase.show_circumference_of_thigh?View.VISIBLE:View.GONE);
        upper.setVisibility(mBase.show_circumference_of_upper?View.VISIBLE:View.GONE);
        height.setVisibility(mBase.show_height?View.VISIBLE:View.GONE);
        hipline.setVisibility(mBase.show_hipline?View.VISIBLE:View.GONE);
        waistline.setVisibility(mBase.show_waistline?View.VISIBLE:View.GONE);
        weight.setVisibility(mBase.show_weight?View.VISIBLE:View.GONE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
