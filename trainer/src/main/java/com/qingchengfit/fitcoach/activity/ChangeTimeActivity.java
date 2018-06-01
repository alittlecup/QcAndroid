package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.TimePeriodChooser;
import com.qingchengfit.fitcoach.http.bean.QcPrivateGymReponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChangeTimeActivity extends BaseActivity {

    public static final String TAG = ChangeTimeActivity.class.getName();

	Toolbar toolbar;
	CommonInputView gymtimeMon;
	CommonInputView gymtimeTus;
	CommonInputView gymtimeWen;
	CommonInputView gymtimeThu;
	CommonInputView gymtimeFri;
	CommonInputView gymtimeSat;
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

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gym_time);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      gymtimeMon = (CommonInputView) findViewById(R.id.gymtime_mon);
      gymtimeTus = (CommonInputView) findViewById(R.id.gymtime_tus);
      gymtimeWen = (CommonInputView) findViewById(R.id.gymtime_wen);
      gymtimeThu = (CommonInputView) findViewById(R.id.gymtime_thu);
      gymtimeFri = (CommonInputView) findViewById(R.id.gymtime_fri);
      gymtimeSat = (CommonInputView) findViewById(R.id.gymtime_sat);
      gymtimeSun = (CommonInputView) findViewById(R.id.gymtime_sun);
      findViewById(R.id.gymtime_mon).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClick1();
        }
      });
      findViewById(R.id.gymtime_tus).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClick2();
        }
      });
      findViewById(R.id.gymtime_wen).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClick3();
        }
      });
      findViewById(R.id.gymtime_thu).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClick4();
        }
      });
      findViewById(R.id.gymtime_fri).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClick5();
        }
      });
      findViewById(R.id.gymtime_sat).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClick6();
        }
      });
      findViewById(R.id.gymtime_sun).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClick7();
        }
      });
      findViewById(R.id.gymtime_comfirm).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          OnComfirm();
        }
      });

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

 public void onClick1() {
        setDialogTime(gymtimeMon);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date start, Date end) {
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

 public void onClick2() {
        setDialogTime(gymtimeTus);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date start, Date end) {
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

 public void onClick3() {
        setDialogTime(gymtimeWen);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date start, Date end) {
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

 public void onClick4() {
        setDialogTime(gymtimeThu);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date start, Date end) {
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

 public void onClick5() {
        setDialogTime(gymtimeFri);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date start, Date end) {
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

 public void onClick6() {
        setDialogTime(gymtimeSat);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date start, Date end) {
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

 public void onClick7() {
        setDialogTime(gymtimeSun);
        timeDialogWindow.setOnTimeSelectListener(new TimePeriodChooser.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date start, Date end) {
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

    @Override public void onBackPressed() {
        setResult(-1);
        finish();
        //        overridePendingTransition(R.anim.slide_hold, R.anim.timepicker_anim_exit_bottom);
    }
}
