package com.qingchengfit.fitcoach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcReportGlanceResponse;
import java.util.Date;
import java.util.HashMap;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatementGlanceFragment extends Fragment {
    public static final String TAG = StatementGlanceFragment.class.getName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    //@BindView(R.id.spinner_nav)
    Spinner spinnerNav;
    @BindView(R.id.statment_glance_month_title) TextView statmentGlanceMonthTitle;
    @BindView(R.id.statment_glance_month_data) TextView statmentGlanceMonthData;
    @BindView(R.id.statment_glance_week_title) TextView statmentGlanceWeekTitle;
    @BindView(R.id.statment_glance_week_data) TextView statmentGlanceWeekData;
    @BindView(R.id.statment_glance_today_title) TextView statmentGlanceTodayTitle;
    @BindView(R.id.statment_glance_today_data) TextView statmentGlanceTodayData;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    //    private ArrayAdapter<SpinnerBean> adapter;
    //    private ArrayList<SpinnerBean> spinnerBeans;
    private QcReportGlanceResponse response;
    private String curModel;
    private String curSystemId;
    private String mTitle;
    private Unbinder unbinder;

    public StatementGlanceFragment() {
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statement_glance, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        mTitle = getString(R.string.statement_course);
        toolbarTitle.setText(mTitle);
        if (getActivity() instanceof FragActivity) {
            if (((FragActivity) getActivity()).getCoachService() != null) {
                CoachService coachService = ((FragActivity) getActivity()).getCoachService();
                toolbarTitle.setText(coachService.getName());
                curModel = coachService.model;
                curSystemId = coachService.getId();
            }
        }
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(refresh.getViewTreeObserver(), this);
                refresh.setRefreshing(true);
                freshData();
            }
        });
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                freshData();
            }
        });

        return view;
    }


    public void freshData() {
        HashMap<String, Object> prams = new HashMap<>();
        prams.put("id", curSystemId + "");
        prams.put("model", curModel);
        QcCloudClient.getApi().getApi.qcGetCoachReportGlance(App.coachid, prams)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.newThread())
            .subscribe(qcReportGlanceResponse -> {
                response = qcReportGlanceResponse;
                handleReponse(qcReportGlanceResponse);
            }, throwable -> {
            }, () -> {
            });
    }

    public void handleReponse(QcReportGlanceResponse qcReportGlanceResponse) {
        if (qcReportGlanceResponse == null) return;
        //List<QcReportGlanceResponse.glanceservice> systems = qcReportGlanceResponse.data.services;
        StringBuffer monthTitle = new StringBuffer();
        StringBuffer weekTitle = new StringBuffer();
        StringBuffer dayTitle = new StringBuffer();
        monthTitle.append("本月");
        weekTitle.append("本周");
        dayTitle.append("今日");
        monthTitle.append("(")
            .append(DateUtils.getStartDayOfMonth(new Date()))
            .append("至")
            .append(DateUtils.getEndDayOfMonth(new Date()))
            .append(")");
        weekTitle.append("(")
            .append(DateUtils.getMondayOfThisWeek(new Date()))
            .append("至")
            .append(DateUtils.getSundayOfThisWeek(new Date()))
            .append(")");
        dayTitle.append("(").append(DateUtils.formatToServer(new Date())).append(")");

        StringBuffer monthContent = new StringBuffer();
        StringBuffer weekContent = new StringBuffer();
        StringBuffer dayContent = new StringBuffer();
        monthContent.append(qcReportGlanceResponse.data.month.course_count)
            .append("节课程,服务")
            .append(qcReportGlanceResponse.data.month.user_count)
            .append("人次");
        weekContent.append(qcReportGlanceResponse.data.week.course_count)
            .append("节课程,服务")
            .append(qcReportGlanceResponse.data.week.user_count)
            .append("人次");
        dayContent.append(qcReportGlanceResponse.data.today.course_count)
            .append("节课程,服务")
            .append(qcReportGlanceResponse.data.today.user_count)
            .append("人次");

        getActivity().runOnUiThread(() -> {
            statmentGlanceMonthTitle.setText(monthTitle.toString());
            statmentGlanceWeekTitle.setText(weekTitle.toString());
            statmentGlanceTodayTitle.setText(dayTitle.toString());
            statmentGlanceMonthData.setText(monthContent.toString());
            statmentGlanceWeekData.setText(weekContent.toString());
            statmentGlanceTodayData.setText(dayContent.toString());
            refresh.setRefreshing(false);
        });
    }
    //public void handleReponse(QcReportGlanceResponse qcReportGlanceResponse) {
    //     if (qcReportGlanceResponse == null)
    //         return;
    //     List<QcReportGlanceResponse.glanceservice> systems = qcReportGlanceResponse.data.services;
    //     StringBuffer monthTitle = new StringBuffer();
    //     StringBuffer weekTitle = new StringBuffer();
    //     StringBuffer dayTitle = new StringBuffer();
    //     monthTitle.append("本月");
    //     weekTitle.append("本周");
    //     dayTitle.append("今日");
    //     int monthClassNum = 0, weekClassNum = 0, dayClassNum = 0,
    //             monthOrderNum = 0, weekOrderNum = 0, dayOrderNum = 0,
    //             monthServerNum = 0, weekServerNum = 0, dayServerNum = 0;
    //
    //     for (int i = 0; i < systems.size(); i++) {
    //         QcReportGlanceResponse.glanceservice system = systems.get(i);
    //         if (system.service == null)
    //             continue;
    //         if (i == 0) {
    //             monthTitle.append("(").append(system.stat.month.from_date).append("至").append(system.stat.month.to_date).append(")");
    //             weekTitle.append("(").append(system.stat.week.from_date).append("至").append(system.stat.week.to_date).append(")");
    //             dayTitle.append("(").append(system.stat.today.from_date).append("至").append(system.stat.today.to_date).append(")");
    //         }
    //
    //         if (curSystemId != 0 && (curSystemId != system.service.id || !system.service.model.equals(curModel)))
    //             continue;
    //         if (system.stat!= null && system.stat.month != null) {
    //
    //             monthClassNum += system.stat.month.course_count;
    //             monthOrderNum += system.stat.month.order_count;
    //             monthServerNum += system.stat.month.user_count;
    //             weekClassNum += system.stat.week.course_count;
    //             weekOrderNum += system.stat.week.order_count;
    //             weekServerNum += system.stat.week.user_count;
    //             dayClassNum += system.stat.today.course_count;
    //             dayOrderNum = +system.stat.today.order_count;
    //             dayServerNum += system.stat.today.user_count;
    //         }
    //     }
    //
    //     StringBuffer monthContent = new StringBuffer();
    //     StringBuffer weekContent = new StringBuffer();
    //     StringBuffer dayContent = new StringBuffer();
    //     monthContent.append(monthClassNum).append("节课程,服务").append(monthServerNum).append("人次");
    //     weekContent.append(weekClassNum).append("节课程,服务").append(weekServerNum).append("人次");
    //     dayContent.append(dayClassNum).append("节课程,服务").append(dayServerNum).append("人次");
    //
    //     getActivity().runOnUiThread(() -> {
    //         statmentGlanceMonthTitle.setText(monthTitle.toString());
    //         statmentGlanceWeekTitle.setText(weekTitle.toString());
    //         statmentGlanceTodayTitle.setText(dayTitle.toString());
    //         statmentGlanceMonthData.setText(monthContent.toString());
    //         statmentGlanceWeekData.setText(weekContent.toString());
    //         statmentGlanceTodayData.setText(dayContent.toString());
    //         refresh.setRefreshing(false);
    //     });
    // }

    @OnClick(R.id.statement_glance_month) public void onClickMonth() {
        getFragmentManager().beginTransaction()
            .add(R.id.web_frag_layout, StatementDetailFragment.newInstance(2))
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_glance_week) public void onClickWeek() {
        getFragmentManager().beginTransaction()
            .add(R.id.web_frag_layout, StatementDetailFragment.newInstance(1))
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_glance_today) public void onClickToday() {
        getFragmentManager().beginTransaction()
            .add(R.id.web_frag_layout, StatementDetailFragment.newInstance(0))
            .addToBackStack(null)
            .commit();
    }

    @OnClick(R.id.statement_glance_custom) public void onClickCustom() {
        getFragmentManager().beginTransaction().add(R.id.web_frag_layout, new CustomStatmentFragment()).addToBackStack(null).commit();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0 && requestCode == 501) {
            toolbarTitle.setText(data.getStringExtra("name"));
            curModel = data.getStringExtra("model");
            curSystemId = data.getStringExtra("id");
            LogUtil.e("curModel:" + curModel + "   id:" + curSystemId);

            handleReponse(response);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
