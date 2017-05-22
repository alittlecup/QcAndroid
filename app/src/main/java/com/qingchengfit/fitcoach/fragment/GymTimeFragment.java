package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.utils.DateUtils;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymTimeFragment extends Fragment {
    public static final String TAG = GymTimeFragment.class.getName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.gymtime_mon) CommonInputView gymtimeMon;
    @BindView(R.id.gymtime_tus) CommonInputView gymtimeTus;
    @BindView(R.id.gymtime_wen) CommonInputView gymtimeWen;
    @BindView(R.id.gymtime_thu) CommonInputView gymtimeThu;
    @BindView(R.id.gymtime_fri) CommonInputView gymtimeFri;
    @BindView(R.id.gymtime_sat) CommonInputView gymtimeSat;
    @BindView(R.id.gymtime_sun) CommonInputView gymtimeSun;
    TimePeriodChooser timeDialogWindow;
    private Unbinder unbinder;

    public GymTimeFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gym_time, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle("营业时间");
        toolbar.setNavigationIcon(R.drawable.ic_cross_white);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        timeDialogWindow = new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS);
        return view;
    }

    @OnClick(R.id.gymtime_mon) public void onClick() {
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeMon.setContent(sb.toString());
            }
        });
        timeDialogWindow.showAtLocation();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
