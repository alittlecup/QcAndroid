package com.qingchengfit.fitcoach.fragment;


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

import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcSaleGlanceResponse;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaleGlanceFragment extends Fragment {
    public static final String TAG = SaleGlanceFragment.class.getName();
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
    private QcSaleGlanceResponse response;
    private int curSystem = 0;

    public SaleGlanceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statement_glance, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        view.setOnTouchListener((v, event) -> {
            return true;
        });
        toolbar.setTitle("销售报表");
        toolbarTitle.setVisibility(View.GONE);
//        spinnerBeans = new ArrayList<>();
//        spinnerBeans.add(new SpinnerBean("", "全部销售报表", true));
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
            }
        });
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshData();
            }
        });
        statmentGlanceTodayTitle.setText("今日(" + DateUtils.getServerDateDay(new Date()) + ")");
        statmentGlanceWeekTitle.setText("本周("+DateUtils.getMondayOfThisWeek(new Date())+"至"+DateUtils.getSundayOfThisWeek(new Date())+")");
        statmentGlanceMonthTitle.setText("本月("+DateUtils.getStartDayOfMonth(new Date())+"至"+DateUtils.getEndDayOfMonth(new Date())+")");
        freshData();
        return view;
    }

    public void freshData() {
        QcCloudClient.getApi().getApi.qcGetCoachSaleGlance(App.coachid).subscribeOn(Schedulers.newThread())
                .subscribe(qcSaleGlanceResponse -> {
                    response = qcSaleGlanceResponse;
                    handleReponse(qcSaleGlanceResponse);
                }, throwable -> {
                }, () -> {
                });
    }


    public void handleReponse(QcSaleGlanceResponse qcReportGlanceResponse) {
        if (qcReportGlanceResponse == null)
            return;
        List<QcSaleGlanceResponse.System> systems = qcReportGlanceResponse.data.systems;
//        StringBuffer monthTitle = new StringBuffer();
//        StringBuffer weekTitle = new StringBuffer();
//        StringBuffer dayTitle = new StringBuffer();
//        monthTitle.append("本月");
//        weekTitle.append("本周");
//        dayTitle.append("今日");
        int monthServerNum = 0, weekServerNum = 0, dayServerNum = 0;

        for (int i = 0; i < systems.size(); i++) {
            QcSaleGlanceResponse.System system = systems.get(i);
            if (system.system == null)
                continue;
//            if (i == 0) {
//                monthTitle.append("(").append(system.month.from_date).append("至").append(system.month.to_date).append(")");
//                weekTitle.append("(").append(system.week.from_date).append("至").append(system.week.to_date).append(")");
//                dayTitle.append("(").append(system.today.from_date).append("至").append(system.today.to_date).append(")");
//            }

            if (curSystem != 0 && curSystem != system.system.id)
                continue;
            monthServerNum += system.month.total_cost;
            weekServerNum += system.week.total_cost;
            dayServerNum += system.today.total_cost;
        }

        StringBuffer monthContent = new StringBuffer();
        StringBuffer weekContent = new StringBuffer();
        StringBuffer dayContent = new StringBuffer();
        monthContent.append("实收金额¥").append(monthServerNum);
        weekContent.append("实收金额¥").append(weekServerNum);
        dayContent.append("实收金额¥").append(dayServerNum);

        getActivity().runOnUiThread(() -> {
//            statmentGlanceMonthTitle.setText(monthTitle);
//            statmentGlanceWeekTitle.setText(weekTitle);
//            statmentGlanceTodayTitle.setText(dayTitle);
            statmentGlanceMonthData.setText(monthContent.toString());
            statmentGlanceWeekData.setText(weekContent.toString());
            statmentGlanceTodayData.setText(dayContent.toString());
            refresh.setRefreshing(false);
        });
    }


    @OnClick(R.id.statement_glance_month)
    public void onClickMonth() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, SaleDetailFragment.newInstance(2))
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.statement_glance_week)
    public void onClickWeek() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, SaleDetailFragment.newInstance(1))
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.statement_glance_today)
    public void onClickToday() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, SaleDetailFragment.newInstance(0))
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.statement_glance_custom)
    public void onClickCustom() {
        getFragmentManager().beginTransaction()
                .add(R.id.web_frag_layout, new CustomSaleFragment())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
