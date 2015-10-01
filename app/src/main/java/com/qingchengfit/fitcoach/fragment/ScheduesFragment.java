package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.DateSegmentLayout;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.Coach;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduesFragment extends Fragment {
    public static final String TAG = ScheduesFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_radiogroup)
    DateSegmentLayout drawerRadiogroup;
    @Bind(R.id.schedule_calendar)
    RelativeLayout scheduleCalendar;
    @Bind(R.id.schedule_rv)
    RecyclerView scheduleRv;
    @Bind(R.id.schedule_no_img)
    ImageView scheduleNoImg;
    @Bind(R.id.schedule_no_tv)

    TextView scheduleNoTv;
    public ScheduesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedues, container, false);
        ButterKnife.bind(this, view);
        drawerRadiogroup.setDate(new Date());
        Gson gson = new Gson();
        String id = PreferenceUtils.getPrefString(getActivity(), "coach", "");
        if (TextUtils.isEmpty(id)) {
        }
        Coach coach = gson.fromJson(id, Coach.class);
        QcCloudClient.getApi().getApi.qcGetCoachSchedule(Integer.parseInt(coach.id)).subscribe();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
