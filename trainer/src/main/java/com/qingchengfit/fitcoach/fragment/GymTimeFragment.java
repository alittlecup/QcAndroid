package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymTimeFragment extends Fragment {
    public static final String TAG = GymTimeFragment.class.getName();
	Toolbar toolbar;
	CommonInputView gymtimeMon;
	CommonInputView gymtimeTus;
	CommonInputView gymtimeWen;
	CommonInputView gymtimeThu;
	CommonInputView gymtimeFri;
	CommonInputView gymtimeSat;
	CommonInputView gymtimeSun;
    TimePeriodChooser timeDialogWindow;


    public GymTimeFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gym_time, container, false);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      gymtimeMon = (CommonInputView) view.findViewById(R.id.gymtime_mon);
      gymtimeTus = (CommonInputView) view.findViewById(R.id.gymtime_tus);
      gymtimeWen = (CommonInputView) view.findViewById(R.id.gymtime_wen);
      gymtimeThu = (CommonInputView) view.findViewById(R.id.gymtime_thu);
      gymtimeFri = (CommonInputView) view.findViewById(R.id.gymtime_fri);
      gymtimeSat = (CommonInputView) view.findViewById(R.id.gymtime_sat);
      gymtimeSun = (CommonInputView) view.findViewById(R.id.gymtime_sun);
      view.findViewById(R.id.gymtime_mon).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          GymTimeFragment.this.onClick();
        }
      });

      toolbar.setTitle("营业时间");
        toolbar.setNavigationIcon(R.drawable.ic_cross_white);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        timeDialogWindow = new TimePeriodChooser(getContext(), TimePopupWindow.Type.HOURS_MINS);
        return view;
    }

 public void onClick() {
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

    }
}
