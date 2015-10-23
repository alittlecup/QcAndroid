package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.TimePopupWindow;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymTimeFragment extends Fragment {
    public static final String TAG = GymTimeFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gymtime_mon)
    CommonInputView gymtimeMon;
    @Bind(R.id.gymtime_tus)
    CommonInputView gymtimeTus;
    @Bind(R.id.gymtime_wen)
    CommonInputView gymtimeWen;
    @Bind(R.id.gymtime_thu)
    CommonInputView gymtimeThu;
    @Bind(R.id.gymtime_fri)
    CommonInputView gymtimeFri;
    @Bind(R.id.gymtime_sat)
    CommonInputView gymtimeSat;
    @Bind(R.id.gymtime_sun)
    CommonInputView gymtimeSun;
    TimePeriodChooser timeDialogWindow;

    public GymTimeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gym_time, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("营业时间");
        toolbar.setNavigationIcon(R.drawable.ic_cross_white);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        timeDialogWindow = new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS);
        return view;
    }

    @OnClick(R.id.gymtime_mon)
    public void onClick() {
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeMon.setContent(sb.toString());
            }
        });
        timeDialogWindow.showAtLocation(new Date());
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
