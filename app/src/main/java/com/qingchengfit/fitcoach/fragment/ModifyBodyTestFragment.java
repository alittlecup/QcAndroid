package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.TimeDialogWindow;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.http.bean.Measure;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private Measure mMeasure;
    private TimeDialogWindow pwTime;
    private View view;

    public static ModifyBodyTestFragment newInstance(QcBodyTestTemplateRespone.Base base) {

        Bundle args = new Bundle();
        args.putParcelable("base", base);
        ModifyBodyTestFragment fragment = new ModifyBodyTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ModifyBodyTestFragment newInstance(Measure measure) {

        Bundle args = new Bundle();
        args.putParcelable("measure", measure);
        ModifyBodyTestFragment fragment = new ModifyBodyTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBase = getArguments().getParcelable("base");
        mMeasure = getArguments().getParcelable("measure");
    }

    public ModifyBodyTestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_modify_body_test, container, false);
        ButterKnife.bind(this, view);
        if (mBase != null) {
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
            chest.setVisibility(mBase.show_circumference_of_chest ? View.VISIBLE : View.GONE);
            thigh.setVisibility(mBase.show_circumference_of_thigh ? View.VISIBLE : View.GONE);
            upper.setVisibility(mBase.show_circumference_of_upper ? View.VISIBLE : View.GONE);
            height.setVisibility(mBase.show_height ? View.VISIBLE : View.GONE);
            hipline.setVisibility(mBase.show_hipline ? View.VISIBLE : View.GONE);
            waistline.setVisibility(mBase.show_waistline ? View.VISIBLE : View.GONE);
            weight.setVisibility(mBase.show_weight ? View.VISIBLE : View.GONE);
        }
        if (mMeasure != null) {
            testDate.setContent(DateUtils.getServerDateDay(DateUtils.formatDateFromServer(mMeasure.created_at)));

            if (!TextUtils.isEmpty(mMeasure.bmi)) {
                bmi.setVisibility(View.VISIBLE);
                bmi.setContent(String.format("%s", mMeasure.bmi));
            }
            if (!TextUtils.isEmpty(mMeasure.weight)) {
                weight.setVisibility(View.VISIBLE);
                weight.setContent(String.format("%s", mMeasure.weight));
            }
            if (!TextUtils.isEmpty(mMeasure.height)) {
                height.setVisibility(View.VISIBLE);
                height.setContent(String.format("%s", mMeasure.height));
            }
            if (!TextUtils.isEmpty(mMeasure.body_fat_rate)) {
                bodyFatRate.setVisibility(View.VISIBLE);
                bodyFatRate.setContent(String.format("%s", mMeasure.body_fat_rate));
            }
            if (!TextUtils.isEmpty(mMeasure.circumference_of_calf)) {
                calf.setVisibility(View.VISIBLE);
                calf.setContent(String.format("%s", mMeasure.circumference_of_calf));
            }
            if (!TextUtils.isEmpty(mMeasure.circumference_of_chest)) {
                chest.setVisibility(View.VISIBLE);
                chest.setContent(String.format("%s", mMeasure.circumference_of_chest));
            }
            if (!TextUtils.isEmpty(mMeasure.circumference_of_thigh)) {
                thigh.setVisibility(View.VISIBLE);
                thigh.setContent(String.format("%s", mMeasure.circumference_of_thigh));
            }
            if (!TextUtils.isEmpty(mMeasure.circumference_of_upper)) {
                upper.setVisibility(View.VISIBLE);
                upper.setContent(String.format("%s", mMeasure.circumference_of_upper));
            }
            if (!TextUtils.isEmpty(mMeasure.hipline)) {
                hipline.setVisibility(View.VISIBLE);
                hipline.setContent(String.format("%s", mMeasure.hipline));
            }
            if (!TextUtils.isEmpty(mMeasure.waistline)) {
                waistline.setVisibility(View.VISIBLE);
                waistline.setContent(String.format("%s", mMeasure.waistline));
            }
        }
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @OnClick(R.id.test_date)
    public void onClickDate() {
        LogUtil.e("click test_data");
        RxBus.getBus().post(RxBus.BUS_CHOOSEPIC);
    }

    //    @OnClick(R.id.height)
//    public void onHeight() {
//
//    }
    public void setDate(String s) {
        testDate.setContent(s);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public Measure getMeasure() {
        Measure measure = new Measure();
        measure.hipline = hipline.getContent();
        measure.waistline = waistline.getContent();
        measure.circumference_of_upper = upper.getContent();
        measure.circumference_of_chest = chest.getContent();
        measure.circumference_of_calf = calf.getContent();
        measure.circumference_of_thigh = thigh.getContent();
        measure.bmi = bmi.getContent();
        measure.created_at = testDate.getContent();
        measure.body_fat_rate = bodyFatRate.getContent();
        measure.weight = weight.getContent();
        measure.height = height.getContent();

        return measure;
    }

}
