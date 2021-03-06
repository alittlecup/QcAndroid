package com.qingchengfit.fitcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.CustomSetmentLayout;
import com.qingchengfit.fitcoach.component.DrawerImgItem;
import com.qingchengfit.fitcoach.component.DrawerModuleItem;
import com.qingchengfit.fitcoach.component.SegmentLayout;
import com.qingchengfit.fitcoach.fragment.CoachHomeFragment;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import com.qingchengfit.fitcoach.http.bean.QcDrawerResponse;
import java.util.HashMap;
import rx.schedulers.Schedulers;

public class MyHomeActivity extends AppCompatActivity {
    public static final String TAG = MyHomeActivity.class.getName();
    private static int ids[] = {
        R.id.segmentbtn_01, R.id.segmentbtn_11, R.id.segmentbtn_21, R.id.segmentbtn_31, R.id.segmentbtn_41, R.id.segmentbtn_51,
        R.id.segmentbtn_61, R.id.segmentbtn_71, R.id.segmentbtn_81, R.id.segmentbtn_91,
    };
	ImageView headerIcon;
	TextView drawerName;
	RelativeLayout drawerHeaderview;
	CustomSetmentLayout drawerRadiogroup;
	LinearLayout drawerModules;
	DrawerLayout mainDrawerlayout;
	LinearLayout oemActs;
    private FragmentManager fragmentManager;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);
      headerIcon = (ImageView) findViewById(R.id.header_icon);
      drawerName = (TextView) findViewById(R.id.drawer_name);
      drawerHeaderview = (RelativeLayout) findViewById(R.id.drawer_headerview);
      drawerRadiogroup = (CustomSetmentLayout) findViewById(R.id.drawer_radiogroup);
      drawerModules = (LinearLayout) findViewById(R.id.drawer_modules);
      mainDrawerlayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
      oemActs = (LinearLayout) findViewById(R.id.oem_acts);
      findViewById(R.id.drawer_headerview).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          MyHomeActivity.this.onClick();
        }
      });

      fragmentManager = getSupportFragmentManager();
        //        fragmentManager.beginTransaction().replace(R.id.myhome_fraglayout, new MyHomeFragment()).commit();
        fragmentManager.beginTransaction().replace(R.id.myhome_fraglayout, new CoachHomeFragment()).commit();
        initDrawer();
    }

 public void onClick() {
        mainDrawerlayout.closeDrawers();
    }

    public void openDrawer() {
        mainDrawerlayout.openDrawer(Gravity.LEFT);
    }

    public void initDrawer() {
        SegmentLayout button = new SegmentLayout(this);
        button.setText("日程安排");
        button.setId(ids[0]);
        button.setDrawables(R.drawable.ic_drawer_schedule_normal, R.drawable.ic_drawer_schedule_checked);
        drawerRadiogroup.addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        SegmentLayout button2 = new SegmentLayout(this);
        button2.setId(ids[1]);
        button2.setText("数据报表");
        button2.setDrawables(R.drawable.ic_drawer_statistic_normal, R.drawable.ic_drawer_statistic_checked);
        drawerRadiogroup.addView(button2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        SegmentLayout button3 = new SegmentLayout(this);
        button3.setText("会议培训");
        button3.setId(ids[2]);
        button3.setDrawables(R.drawable.ic_drawer_meeting_normal, R.drawable.ic_drawer_meeting_checked);
        drawerRadiogroup.addView(button3, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));

        button.setListener(v -> {
            goPage(1);
        });

        button2.setListener(v -> {
            goPage(2);
        });
        button3.setListener(v -> goPage(3));

        DrawerModuleItem item = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
        item.setTitle("我的学员");
        item.setCount("100");
        drawerModules.addView(item, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        DrawerModuleItem item1 = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
        item1.setTitle(getString(R.string.my_course_template));
        item1.setCount("100");
        drawerModules.addView(item1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));
        DrawerModuleItem item2 = (DrawerModuleItem) LayoutInflater.from(this).inflate(R.layout.drawer_module_item, null);
        item2.setTitle("我的健身房");
        item2.setCount("100");
        drawerModules.addView(item2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            (int) getResources().getDimension(R.dimen.qc_drawer_item_height)));

        item.setOnClickListener(v -> goPage(4));
        item1.setOnClickListener(v -> goPage(5));
        item2.setOnClickListener(v -> goPage(6));
        mainDrawerlayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override public void onDrawerOpened(View drawerView) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("oem", getString(R.string.oem_tag));
                TrainerRepository.getStaticTrainerAllApi().qcGetDrawerInfo(App.coachid, params)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .subscribe(qcDrawerResponse -> {
                        runOnUiThread(() -> {
                            Glide.with(App.AppContex)
                                .load(qcDrawerResponse.data.coach.avatar)
                                .asBitmap()
                                .into(new CircleImgWrapper(headerIcon, App.AppContex));
                            drawerName.setText(qcDrawerResponse.data.coach.username);
                            item.setCount(qcDrawerResponse.data.user_count);
                            item1.setCount(qcDrawerResponse.data.plan_count);
                            item2.setCount(qcDrawerResponse.data.system_count);
                            if (qcDrawerResponse.data.activities != null) {
                                oemActs.setVisibility(View.VISIBLE);
                                oemActs.removeAllViews();
                                for (QcDrawerResponse.Activity a : qcDrawerResponse.data.activities) {
                                    oemActs.addView(new DrawerImgItem(MyHomeActivity.this, a.image, a.name, new View.OnClickListener() {

                                        @Override public void onClick(View v) {
                                            goWeb(a.link);
                                        }
                                    }));
                                }
                            } else {
                                oemActs.setVisibility(View.GONE);
                            }
                        });
                    }, throwable -> {
                    }, () -> {
                    });
            }

            @Override public void onDrawerClosed(View drawerView) {

            }

            @Override public void onDrawerStateChanged(int newState) {

            }
        });

        /**
         * load cache
         */
        String cache = PreferenceUtils.getPrefString(App.AppContex, App.coachid + "drawer_info", "");
        if (!TextUtils.isEmpty(cache)) {
            QcDrawerResponse qcDrawerResponse = new Gson().fromJson(cache, QcDrawerResponse.class);
            Glide.with(App.AppContex)
                .load(qcDrawerResponse.data.coach.avatar)
                .asBitmap()
                .into(new CircleImgWrapper(headerIcon, App.AppContex));
            drawerName.setText(qcDrawerResponse.data.coach.username);
            item.setCount(qcDrawerResponse.data.user_count);
            item1.setCount(qcDrawerResponse.data.plan_count);
            item2.setCount(qcDrawerResponse.data.system_count);
            PreferenceUtils.setPrefString(App.AppContex, App.coachid + "drawer_info", new Gson().toJson(qcDrawerResponse));
        }
    }

    private void goWeb(String url) {
        Intent it = new Intent();
        it.putExtra("url", url);
        setResult(-1, it);
        this.finish();
        overridePendingTransition(R.anim.slide_hold, R.anim.slide_right_out);
    }

    private void goPage(int page) {
        setResult(page);
        this.finish();
        overridePendingTransition(R.anim.slide_hold, R.anim.slide_right_out);
    }

    @Override public void onBackPressed() {
        if (fragmentManager.popBackStackImmediate()) {

        } else {
            goPage(1);
        }
    }
}
