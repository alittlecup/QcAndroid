package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CommonInputView;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;
import com.qingchengfit.fitcoach.http.bean.QcPrivateGymReponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeTimeActivity extends BaseAcitivity {

    public static final String TAG = ChangeTimeActivity.class.getName();

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

    List<QcPrivateGymReponse.OpenTime> openTimes = new ArrayList<>();
    QcPrivateGymReponse.OpenTime openTime1 = new QcPrivateGymReponse.OpenTime();
    QcPrivateGymReponse.OpenTime openTime2 = new QcPrivateGymReponse.OpenTime();
    QcPrivateGymReponse.OpenTime openTime3 = new QcPrivateGymReponse.OpenTime();
    QcPrivateGymReponse.OpenTime openTime4 = new QcPrivateGymReponse.OpenTime();
    QcPrivateGymReponse.OpenTime openTime5 = new QcPrivateGymReponse.OpenTime();
    QcPrivateGymReponse.OpenTime openTime6 = new QcPrivateGymReponse.OpenTime();
    QcPrivateGymReponse.OpenTime openTime7 = new QcPrivateGymReponse.OpenTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gym_time);
        ButterKnife.bind(this);
        toolbar.setTitle("营业时间");
        toolbar.setNavigationIcon(R.drawable.ic_cross_white);
        toolbar.setNavigationOnClickListener(v -> this.onBackPressed());
        timeDialogWindow = new TimePeriodChooser(this, TimePopupWindow.Type.HOURS_MINS);
        if (getIntent() != null) {
            String s = getIntent().getStringExtra("time");
            Type listType = new TypeToken<ArrayList<QcPrivateGymReponse.OpenTime>>() {
            }.getType();
            List<QcPrivateGymReponse.OpenTime> ots = new Gson().fromJson(s, listType);
            for (QcPrivateGymReponse.OpenTime o : ots) {
                if (o.day == 1) {
                    openTime1.day = 1;
                    openTime1.start = o.start;
                    openTime1.end = o.end;
                    gymtimeMon.setContent(openTime1.start + "-" + openTime1.end);
                } else if (o.day == 2) {
                    openTime2.day = 2;
                    openTime2.start = o.start;
                    openTime2.end = o.end;
                    gymtimeTus.setContent(openTime2.start + "-" + openTime2.end);
                } else if (o.day == 3) {
                    openTime3.day = 3;
                    openTime3.start = o.start;
                    openTime3.end = o.end;
                    gymtimeWen.setContent(openTime3.start + "-" + openTime3.end);
                } else if (o.day == 4) {
                    openTime4.day = 4;
                    openTime4.start = o.start;
                    openTime4.end = o.end;
                    gymtimeThu.setContent(openTime4.start + "-" + openTime4.end);
                } else if (o.day == 5) {
                    openTime5.day = 5;
                    openTime5.start = o.start;
                    openTime5.end = o.end;
                    gymtimeFri.setContent(openTime5.start + "-" + openTime5.end);
                } else if (o.day == 6) {
                    openTime6.day = 6;
                    openTime6.start = o.start;
                    openTime6.end = o.end;
                    gymtimeSat.setContent(openTime6.start + "-" + openTime6.end);
                } else if (o.day == 7) {
                    openTime7.day = 7;
                    openTime7.start = o.start;
                    openTime7.end = o.end;
                    gymtimeSun.setContent(openTime7.start + "-" + openTime7.end);
                }
            }
        }

    }

    public void setDialogTime(CommonInputView inputView) {
        try {
            if (inputView != null && !TextUtils.isEmpty(inputView.getContent())) {
                String[] time = inputView.getContent().split("-");
                if (time.length > 1) {
                    Date start = DateUtils.formatDateHHMM(time[0]);
                    Date end = DateUtils.formatDateHHMM(time[1]);
                    timeDialogWindow.setTime(start, end);
                }
            }
        } catch (Exception e) {

        }


    }


    @OnClick(R.id.gymtime_mon)
    public void onClick1() {
        setDialogTime(gymtimeMon);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeMon.setContent(sb.toString());
                openTime1.start = DateUtils.getTimeHHMM(start);
                openTime1.end = DateUtils.getTimeHHMM(end);
                openTime1.day = 1;
            }
        });
        timeDialogWindow.showAtLocation();
    }

    @OnClick(R.id.gymtime_tus)
    public void onClick2() {
        setDialogTime(gymtimeTus);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeTus.setContent(sb.toString());
                openTime2.start = DateUtils.getTimeHHMM(start);
                openTime2.end = DateUtils.getTimeHHMM(end);
                openTime2.day = 2;
            }
        });
        timeDialogWindow.showAtLocation();
    }

    @OnClick(R.id.gymtime_wen)
    public void onClick3() {
        setDialogTime(gymtimeWen);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeWen.setContent(sb.toString());
                openTime3.start = DateUtils.getTimeHHMM(start);
                openTime3.end = DateUtils.getTimeHHMM(end);
                openTime3.day = 3;
            }
        });
        timeDialogWindow.showAtLocation();
    }

    @OnClick(R.id.gymtime_thu)
    public void onClick4() {
        setDialogTime(gymtimeThu);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeThu.setContent(sb.toString());
                openTime4.start = DateUtils.getTimeHHMM(start);
                openTime4.end = DateUtils.getTimeHHMM(end);
                openTime4.day = 4;
            }
        });
        timeDialogWindow.showAtLocation();
    }

    @OnClick(R.id.gymtime_fri)
    public void onClick5() {
        setDialogTime(gymtimeFri);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeFri.setContent(sb.toString());
                openTime5.start = DateUtils.getTimeHHMM(start);
                openTime5.end = DateUtils.getTimeHHMM(end);
                openTime5.day = 5;
            }
        });
        timeDialogWindow.showAtLocation();
    }

    @OnClick(R.id.gymtime_sat)
    public void onClick6() {
        setDialogTime(gymtimeSat);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeSat.setContent(sb.toString());
                openTime6.start = DateUtils.getTimeHHMM(start);
                openTime6.end = DateUtils.getTimeHHMM(end);
                openTime6.day = 6;
            }
        });
        timeDialogWindow.showAtLocation();
    }

    @OnClick(R.id.gymtime_sun)
    public void onClick7() {
        setDialogTime(gymtimeSun);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date start, Date end) {
                StringBuffer sb = new StringBuffer();
                sb.append(DateUtils.getTimeHHMM(start)).append("-").append(DateUtils.getTimeHHMM(end));
                gymtimeSun.setContent(sb.toString());
                openTime7.start = DateUtils.getTimeHHMM(start);
                openTime7.end = DateUtils.getTimeHHMM(end);
                openTime7.day = 7;
            }
        });
        timeDialogWindow.showAtLocation();
    }


    @OnClick(R.id.gymtime_comfirm)
    public void OnComfirm() {
        Intent it = new Intent();
        openTimes.clear();
        openTimes.add(openTime1);
        openTimes.add(openTime2);
        openTimes.add(openTime3);
        openTimes.add(openTime4);
        openTimes.add(openTime5);
        openTimes.add(openTime6);
        openTimes.add(openTime7);

        it.putExtra("time", new Gson().toJson(openTimes));
        setResult(1, it);
        this.finish();
//        overridePendingTransition(R.anim.slide_hold, R.anim.timepicker_anim_exit_bottom);
    }

    @Override
    public void onBackPressed() {
        setResult(-1);
        finish();
//        overridePendingTransition(R.anim.slide_hold, R.anim.timepicker_anim_exit_bottom);
    }
}
