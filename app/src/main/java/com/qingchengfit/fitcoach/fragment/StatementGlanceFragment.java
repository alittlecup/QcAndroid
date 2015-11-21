package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.LoopView;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcReportGlanceResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private ArrayAdapter<SpinnerBean> adapter;
    private ArrayList<SpinnerBean> spinnerBeans;
    private QcReportGlanceResponse response;
    private int curSystem = 0;

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
        spinnerBeans = new ArrayList<>();
        spinnerBeans.add(new SpinnerBean("", "全部课程报表", true));
        adapter = new ArrayAdapter<SpinnerBean>(getContext(), R.layout.spinner_checkview, spinnerBeans) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_checkview, parent, false);
                }
                ((TextView) convertView).setText(spinnerBeans.get(position).text);
                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
                }
                SpinnerBean bean = getItem(position);
                ((TextView) convertView.findViewById(R.id.spinner_tv)).setText(bean.text);
                if (bean.isTitle) {
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.GONE);
                    ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.VISIBLE);
                } else {
                    ((ImageView) convertView.findViewById(R.id.spinner_up)).setVisibility(View.GONE);
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setVisibility(View.VISIBLE);
                    ((ImageView) convertView.findViewById(R.id.spinner_icon)).setImageDrawable(new LoopView(bean.color));
                }
                return convertView;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerNav.setAdapter(adapter);
        spinnerNav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curSystem = adapter.getItem(position).id;
                handleReponse(response);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        QcCloudClient.getApi().getApi.qcGetCoachReportGlance(App.coachid).subscribeOn(Schedulers.newThread())
//                .subscribe(qcReportGlanceResponse -> {
//                    response = qcReportGlanceResponse;
//                    handleReponse(qcReportGlanceResponse);
//                });
        QcCloudClient.getApi().getApi.qcGetCoachReportGlance(App.coachid).subscribeOn(Schedulers.newThread())
                .subscribe(qcReportGlanceResponse -> {
                    response = qcReportGlanceResponse;
                    handleReponse(qcReportGlanceResponse);
                }, throwable -> {
                }, () -> {
                });
        return view;
    }

    public void handleReponse(QcReportGlanceResponse qcReportGlanceResponse) {
        if (qcReportGlanceResponse == null)
            return;
        List<QcReportGlanceResponse.System> systems = qcReportGlanceResponse.data.systems;
        spinnerBeans.clear();
        spinnerBeans.add(new SpinnerBean("", "全部课程报表", true));
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
            QcReportGlanceResponse.System system = systems.get(i);
            if (system.system == null)
                continue;
            spinnerBeans.add(new SpinnerBean(system.system.color, system.system.name, system.system.id));
            if (i == 0) {
                monthTitle.append("(").append(system.month.from_date).append("至").append(system.month.to_date).append(")");
                weekTitle.append("(").append(system.week.from_date).append("至").append(system.week.to_date).append(")");
                dayTitle.append("(").append(system.today.from_date).append("至").append(system.today.to_date).append(")");
            }

            if (curSystem != 0 && curSystem != system.system.id)
                continue;
            monthOrderNum += system.month.order_count;
            monthServerNum += system.month.user_count;
            weekOrderNum += system.week.order_count;
            weekServerNum += system.week.user_count;
            dayOrderNum = +system.today.order_count;
            dayServerNum += system.today.user_count;
        }

        StringBuffer monthContent = new StringBuffer();
        StringBuffer weekContent = new StringBuffer();
        StringBuffer dayContent = new StringBuffer();
        monthContent.append(monthOrderNum).append("次预约,服务").append(monthServerNum).append("人次");
        weekContent.append(weekOrderNum).append("次预约,服务").append(weekServerNum).append("人次");
        dayContent.append(dayOrderNum).append("次预约,服务").append(dayServerNum).append("人次");

        getActivity().runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
            statmentGlanceMonthTitle.setText(monthTitle.toString());
            statmentGlanceWeekTitle.setText(weekTitle.toString());
            statmentGlanceTodayTitle.setText(dayTitle.toString());
            statmentGlanceMonthData.setText(monthContent.toString());
            statmentGlanceWeekData.setText(weekContent.toString());
            statmentGlanceTodayData.setText(dayContent.toString());
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
