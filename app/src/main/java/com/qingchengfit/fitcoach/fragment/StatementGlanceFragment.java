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

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.activity.ChooseGymActivity;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcReportGlanceResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.widgets.utils.LogUtil;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatementGlanceFragment extends Fragment {
    public static final String TAG = StatementGlanceFragment.class.getName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.spinner_nav)
    Spinner spinnerNav;
    @Bind(R.id.statment_glance_month_title)
    TextView statmentGlanceMonthTitle;
    @Bind(R.id.statment_glance_month_data)
    TextView statmentGlanceMonthData;
    @Bind(R.id.statment_glance_week_title)
    TextView statmentGlanceWeekTitle;
    @Bind(R.id.statment_glance_week_data)
    TextView statmentGlanceWeekData;
    @Bind(R.id.statment_glance_today_title)
    TextView statmentGlanceTodayTitle;
    @Bind(R.id.statment_glance_today_data)
    TextView statmentGlanceTodayData;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
//    private ArrayAdapter<SpinnerBean> adapter;
//    private ArrayList<SpinnerBean> spinnerBeans;
    private QcReportGlanceResponse response;
    private String curModel;
    private int curSystemId = 0;
    private String mTitle;

    public StatementGlanceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statement_glance, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        mTitle = getString(R.string.statement_course);
        toolbarTitle.setText(mTitle);
        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosegym =new Intent(getContext(), ChooseGymActivity.class);
                
                choosegym.putExtra("model",curModel);
                LogUtil.e("curSystemid:"+curSystemId);
                choosegym.putExtra("id",curSystemId);
                choosegym.putExtra("title",mTitle);
                startActivityForResult(choosegym,501);
            }
        });
//        spinnerBeans = new ArrayList<>();
//        spinnerBeans.add(new SpinnerBean("", "全部课程报表", true));
//        adapter = new ArrayAdapter<SpinnerBean>(getContext(), R.layout.spinner_checkview, spinnerBeans) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_checkview, parent, false);
//                }
//                ((TextView) convertView).setText(spinnerBeans.get(position).text);
//                return convertView;
//            }
//
//            @Override
//            public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
//                }
//                SpinnerBean bean = getItem(position);
//                ((TextView) convertView.findViewById(R.id.spinner_tv)).setText(bean.text);
//                if (bean.isTitle) {
//                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.GONE);
//                    ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.VISIBLE);
//                } else {
//                    ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.GONE);
//                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.VISIBLE);
//                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setImageDrawable(new LoopView(bean.color));
//                }
//                return convertView;
//            }
//        };
//        adapter.setDropDownViewResource(R.layout.spinner_item);
//        spinnerNav.setAdapter(adapter);
//        spinnerNav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                curSystem = adapter.getItem(position).id;
//                handleReponse(response);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refresh.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                refresh.setRefreshing(true);
                freshData();
            }
        });
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshData();
            }
        });

        return view;
    }

    public void freshData() {
        QcCloudClient.getApi().getApi.qcGetCoachReportGlance(App.coachid).subscribeOn(Schedulers.newThread())
                .subscribe(qcReportGlanceResponse -> {
                    response = qcReportGlanceResponse;
                    handleReponse(qcReportGlanceResponse);
                }, throwable -> {
                }, () -> {
                });
    }

    public void handleReponse(QcReportGlanceResponse qcReportGlanceResponse) {
        if (qcReportGlanceResponse == null)
            return;
        List<QcReportGlanceResponse.glanceservice> systems = qcReportGlanceResponse.data.services;
        StringBuffer monthTitle = new StringBuffer();
        StringBuffer weekTitle = new StringBuffer();
        StringBuffer dayTitle = new StringBuffer();
        monthTitle.append("本月");
        weekTitle.append("本周");
        dayTitle.append("今日");
        int monthClassNum = 0, weekClassNum = 0, dayClassNum = 0,
                monthOrderNum = 0, weekOrderNum = 0, dayOrderNum = 0,
                monthServerNum = 0, weekServerNum = 0, dayServerNum = 0;

        for (int i = 0; i < systems.size(); i++) {
            QcReportGlanceResponse.glanceservice system = systems.get(i);
            if (system.service == null)
                continue;
            if (i == 0) {
                monthTitle.append("(").append(system.stat.month.from_date).append("至").append(system.stat.month.to_date).append(")");
                weekTitle.append("(").append(system.stat.week.from_date).append("至").append(system.stat.week.to_date).append(")");
                dayTitle.append("(").append(system.stat.today.from_date).append("至").append(system.stat.today.to_date).append(")");
            }

            if (curSystemId != 0 && (curSystemId != system.service.id || !system.service.model.equals(curModel)))
                continue;
            if (system.stat!= null && system.stat.month != null) {

                monthClassNum += system.stat.month.course_count;
                monthOrderNum += system.stat.month.order_count;
                monthServerNum += system.stat.month.user_count;
                weekClassNum += system.stat.week.course_count;
                weekOrderNum += system.stat.week.order_count;
                weekServerNum += system.stat.week.user_count;
                dayClassNum += system.stat.today.course_count;
                dayOrderNum = +system.stat.today.order_count;
                dayServerNum += system.stat.today.user_count;
            }
        }

        StringBuffer monthContent = new StringBuffer();
        StringBuffer weekContent = new StringBuffer();
        StringBuffer dayContent = new StringBuffer();
        monthContent.append(monthClassNum).append("节课程,服务").append(monthServerNum).append("人次");
        weekContent.append(weekClassNum).append("节课程,服务").append(weekServerNum).append("人次");
        dayContent.append(dayClassNum).append("节课程,服务").append(dayServerNum).append("人次");

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


    @OnClick(R.id.statement_glance_month)
    public void onClickMonth() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, StatementDetailFragment.newInstance(2))
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.statement_glance_week)
    public void onClickWeek() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, StatementDetailFragment.newInstance(1))
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.statement_glance_today)
    public void onClickToday() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, StatementDetailFragment.newInstance(0))
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.statement_glance_custom)
    public void onClickCustom() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, new CustomStatmentFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0 && requestCode == 501) {
            toolbarTitle.setText(data.getStringExtra("name"));
            curModel = data.getStringExtra("model");
            curSystemId = Integer.parseInt(data.getStringExtra("id"));
            LogUtil.e("curModel:" + curModel + "   id:" + curSystemId);

            handleReponse(response);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
