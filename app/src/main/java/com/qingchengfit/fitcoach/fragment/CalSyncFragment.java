package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.DialogList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.PreferenceUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalSyncFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalSyncFragment extends BaseSettingFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.cal_sync_switch)
    Switch calSyncSwitch;
    @BindView(R.id.cal_sync_switch_layout)
    RelativeLayout calSyncSwitchLayout;
    @BindView(R.id.cal_sync_time)
    TextView calSyncTime;
    @BindView(R.id.cal_sync_time_layout)
    RelativeLayout calSyncTimeLayout;
    private String[] mTimeArray = {"不提醒", "15分钟提醒", "30分钟提醒", "1小时提醒", "2小时提醒", "24小时提醒"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Unbinder unbinder;


    public CalSyncFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalSyncFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalSyncFragment newInstance(String param1, String param2) {
        CalSyncFragment fragment = new CalSyncFragment();
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
        View view = inflater.inflate(R.layout.fragment_cal_sync, container, false);
        unbinder=ButterKnife.bind(this, view);
        fragmentCallBack.onToolbarMenu(0, 0, getActivity().getString(R.string.setting_cal_sync_title));
        calSyncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calSyncTimeLayout.setVisibility(View.VISIBLE);
                    PreferenceUtils.setPrefBoolean(getContext(), App.coachid + "cal_sync", true);

                } else {
                    calSyncTimeLayout.setVisibility(View.GONE);
                    PreferenceUtils.setPrefBoolean(getContext(), App.coachid + "cal_sync", false);

                }
            }
        });
        boolean isSync = PreferenceUtils.getPrefBoolean(getContext(), App.coachid + "cal_sync", true);
        calSyncSwitch.setChecked(isSync);
        int time = PreferenceUtils.getPrefInt(getContext(), App.coachid + "cal_sync_time", 60);
        if (time < 0) {
            calSyncTime.setText(mTimeArray[0]);
        } else if (time == 15) {
            calSyncTime.setText(mTimeArray[1]);
        } else if (time == 30) {
            calSyncTime.setText(mTimeArray[2]);
        } else if (time == 60) {
            calSyncTime.setText(mTimeArray[3]);
        } else if (time == 120) {
            calSyncTime.setText(mTimeArray[4]);
        } else if (time == 1440) {
            calSyncTime.setText(mTimeArray[5]);
        }
        return view;
    }

    @OnClick(R.id.cal_sync_switch_layout)
    public void onSwitch() {
        calSyncSwitch.toggle();
    }

    @OnClick(R.id.cal_sync_time_layout)
    public void chooseTime() {

        DialogList dialogList = new DialogList(getContext());
        dialogList.title("请选择提醒时间");
        dialogList.list(mTimeArray, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogList.dismiss();
                if (position == 0) {
                    PreferenceUtils.setPrefInt(getContext(), App.coachid + "cal_sync_time", -1);
                } else if (position == 1) {
                    PreferenceUtils.setPrefInt(getContext(), App.coachid + "cal_sync_time", 15);
                } else if (position == 2) {
                    PreferenceUtils.setPrefInt(getContext(), App.coachid + "cal_sync_time", 30);
                } else if (position == 3) {
                    PreferenceUtils.setPrefInt(getContext(), App.coachid + "cal_sync_time", 60);
                } else if (position == 4) {
                    PreferenceUtils.setPrefInt(getContext(), App.coachid + "cal_sync_time", 120);
                } else if (position == 5) {
                    PreferenceUtils.setPrefInt(getContext(), App.coachid + "cal_sync_time", 1440);
                }
                calSyncTime.setText(mTimeArray[position]);

            }
        });
        dialogList.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
