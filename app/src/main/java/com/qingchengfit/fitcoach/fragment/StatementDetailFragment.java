package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.StatementCompare;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.bean.StatementBean;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.LoopView;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystem;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
import com.qingchengfit.fitcoach.http.bean.QcStatementDetailRespone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatementDetailFragment extends Fragment {
    public static final String TAG = StatementDetailFragment.class.getName();
    public static final int TYPE_MONTH = 2;
    public static final int TYPE_WEEK = 1;
    public static final int TYPE_DAY = 0;
    @Bind(R.id.spinner_nav)
    Spinner spinnerNav;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.statement_detail_time)
    TextView statementDetailTime;
    @Bind(R.id.item_statement_detail_content)
    TextView itemStatementDetailContent;
    Observer<QcStatementDetailRespone> mHttpStatement = new Observer<QcStatementDetailRespone>() {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(QcStatementDetailRespone qcStatementDetailRespone) {

        }
    };
    @Bind(R.id.statement_detail_less)
    ImageView statementDetailLess;
    @Bind(R.id.statement_detail_more)
    ImageView statementDetailMore;

    private StatementDetailAdapter mStatementDetailAdapter;
    private List<StatementBean> statementBeans = new ArrayList<>();
    private List<Integer> mSystemsId = new ArrayList<>();
    private HashMap<Integer, Integer> mCourseNum = new HashMap<>();
    private HashMap<Integer, Integer> mOrderNum = new HashMap<>();
    private HashMap<Integer, Integer> mServerNum = new HashMap<>();
    private HashMap<Integer, List<StatementBean>> mAllStatemet = new HashMap<>();

    /**
     * 初始化 spinner
     */
    private ArrayAdapter<SpinnerBean> spinnerBeanArrayAdapter;
    private ArrayList<SpinnerBean> spinnerBeans;
    private int curSystemId = 0;
    /**
     * 报表参数
     */

    private String start;
    private String end;
    private int course_id = 0;
    private int user_id = 0;
    private int mDividerType = 0;
    private Calendar curCalendar;

    public StatementDetailFragment() {

    }

    public static StatementDetailFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        StatementDetailFragment fragment = new StatementDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static StatementDetailFragment newInstance(int type, String starttime, String endtime, int sysId, int userId, int courseId) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("start", starttime);
        args.putString("end", endtime);
        args.putInt("system", sysId);
        args.putInt("course", courseId);
        args.putInt("user", userId);

        StatementDetailFragment fragment = new StatementDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDividerType = getArguments().getInt("type");
        }
        curCalendar = Calendar.getInstance();
        start = DateUtils.getServerDateDay(new Date());
        switch (mDividerType) {
            case 0:
                end = start;
                break;
            case 1:
                start = DateUtils.getMondayOfThisWeek(curCalendar.getTime());
                end = DateUtils.getSundayOfThisWeek(curCalendar.getTime());

                break;
            case 2:
                start = DateUtils.getStartDayOfMonth(curCalendar.getTime());
                end = DateUtils.getEndDayOfMonth(curCalendar.getTime());
                break;
            case 3:
                start = getArguments().getString("start");
                end = getArguments().getString("end");
                curSystemId = getArguments().getInt("system");
                course_id = getArguments().getInt("course");
                user_id = getArguments().getInt("user");
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statement_detail, container, false);
        ButterKnife.bind(this, view);
        setupToolbar();
        setUpNaviSpinner();
        mStatementDetailAdapter = new StatementDetailAdapter(statementBeans);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mStatementDetailAdapter);
        if (mDividerType == 3) {
            statementDetailLess.setVisibility(View.GONE);
            statementDetailMore.setVisibility(View.GONE);
        } else {
            statementDetailLess.setVisibility(View.VISIBLE);
            statementDetailMore.setVisibility(View.VISIBLE);
        }

        return view;
    }

    /**
     * 初始化toolbar
     */
    public void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    public void setUpNaviSpinner() {
        spinnerBeans = new ArrayList<>();
        spinnerBeans.add(new SpinnerBean("", "全部预约报表", true));
        spinnerBeanArrayAdapter = new ArrayAdapter<SpinnerBean>(getContext(), R.layout.spinner_checkview, spinnerBeans) {
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
        spinnerBeanArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerNav.setAdapter(spinnerBeanArrayAdapter);
        spinnerNav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curSystemId = spinnerBeanArrayAdapter.getItem(position).id;
                showData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //获取用户拥有系统信息
        QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid).subscribeOn(Schedulers.newThread())
                .subscribe(qcCoachSystemResponse -> {
                    List<QcCoachSystem> systems = qcCoachSystemResponse.date.systems;
                    mSystemsId.clear();
                    for (int i = 0; i < systems.size(); i++) {
                        QcCoachSystem system = systems.get(i);
                        spinnerBeans.add(new SpinnerBean(system.color, system.name, system.id));
                        mSystemsId.add(system.id);
                    }
                    getActivity().runOnUiThread(spinnerBeanArrayAdapter::notifyDataSetChanged);
                    queryStatement();
                });
    }

    private void queryStatement() {
        if (mSystemsId.size() == 0)
            return;
        mOrderNum.clear();
        mServerNum.clear();
        mAllStatemet.clear();
        mCourseNum.clear();

        Observable.from(mSystemsId)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Integer, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Integer integer) {
                        return QcCloudClient.getApi().getApi.qcGetStatementDatail(App.coachid, getParams(integer))
                                .flatMap(qcStatementDetailRespone -> {
                                    mCourseNum.put(integer, qcStatementDetailRespone.data.stat.course_count);
                                    mOrderNum.put(integer, qcStatementDetailRespone.data.stat.order_count);
                                    mServerNum.put(integer, qcStatementDetailRespone.data.stat.user_count);
                                    List<QcScheduleBean> beans = qcStatementDetailRespone.data.schedules;
                                    List<StatementBean> statementBeans = new ArrayList<StatementBean>();
                                    for (int i = 0; i < beans.size(); i++) {
                                        QcScheduleBean b = beans.get(i);
                                        StatementBean bean = new StatementBean();
                                        bean.title = b.course.name;
                                        bean.picture = b.course.photo;
                                        bean.date = DateUtils.formatDateFromServer(b.start);
                                        StringBuffer sb = new StringBuffer();
                                        sb.append(DateUtils.getTimeHHMM(bean.date)).append("-").append(DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(b.end)))
                                                .append("  ").append(b.users).append("  ").append(b.count).append("人");
                                        bean.content = sb.toString();
                                        statementBeans.add(bean);
                                    }
                                    mAllStatemet.put(integer, statementBeans);
                                    return Observable.just(true);
                                });


                    }
                })
                .last()
                .subscribe(aBoolean -> {
                    showData();
                });


    }

    public void showData() {
        int orderC = 0;
        int serverC = 0;
        int courseC = 0;
        statementBeans.clear();
        for (Integer key : mAllStatemet.keySet()) {
            if (curSystemId != 0 && curSystemId != key)
                continue;
            orderC += mOrderNum.get(key);
            serverC += mServerNum.get(key);
            courseC += mCourseNum.get(key);
            statementBeans.addAll(mAllStatemet.get(key));
        }

        Collections.sort(statementBeans, new StatementCompare());
        StringBuffer sb1 = new StringBuffer();
        sb1.append(start).append("至").append(end);
        StringBuffer sb2 = new StringBuffer();
        sb2.append(courseC).append("节课程  ").append(orderC).append("人预约  服务").append(serverC).append("人次");

        getActivity().runOnUiThread(() -> {
            mStatementDetailAdapter.notifyDataSetChanged();
            if (statementBeans.size() > 0) {
                recyclerview.setVisibility(View.VISIBLE);
            } else recyclerview.setVisibility(View.GONE);

            statementDetailTime.setText(sb1.toString());
            itemStatementDetailContent.setText(sb2.toString());
        });
    }

    private HashMap<String, String> getParams(int system_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("system_id", Integer.toString(system_id));
        if (course_id != 0)
            params.put("course_id", Integer.toString(course_id));
        if (user_id != 0)
            params.put("user_id", Integer.toString(user_id));
        return params;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.statement_detail_less)
    public void onClickLess() {
        changeCalendar(-1);

    }

    @OnClick(R.id.statement_detail_more)
    public void onClickMore() {
        changeCalendar(1);
    }

    /**
     * 增加或者减少 +1 -1
     *
     * @param symbol
     */
    private void changeCalendar(int symbol) {
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .map(s1 -> {
                    switch (mDividerType) {
                        case 0:
                            curCalendar.add(Calendar.DAY_OF_YEAR, symbol);
                            start = DateUtils.getServerDateDay(curCalendar.getTime());
                            end = start;
                            break;
                        case 1:
                            curCalendar.add(Calendar.WEEK_OF_YEAR, symbol);
                            start = DateUtils.getMondayOfThisWeek(curCalendar.getTime());
                            end = DateUtils.getSundayOfThisWeek(curCalendar.getTime());
                            break;
                        case 2:
                            curCalendar.add(Calendar.MONTH, symbol);
                            start = DateUtils.getStartDayOfMonth(curCalendar.getTime());
                            end = DateUtils.getEndDayOfMonth(curCalendar.getTime());
                            break;
                        default:
                            break;
                    }

                    return s1;
                })
                .subscribe(s -> queryStatement());
    }


    /**
     * recycle view
     */
    class StatementDetailVH extends RecyclerView.ViewHolder {
        @Bind(R.id.item_statement_detail_bottomdivier)
        View itemStatementDetailBottomdivier;
        @Bind(R.id.item_statement_detail_headerdivier)
        View itemStatementDetailHeaderdivier;
        @Bind(R.id.item_statement_detail_day)
        TextView itemStatementDetailDay;
        @Bind(R.id.item_statement_detail_month)
        TextView itemStatementDetailMonth;
        @Bind(R.id.item_statement_detail_date)
        LinearLayout itemStatementDetailDate;
        @Bind(R.id.item_statement_detail_pic)
        ImageView itemStatementDetailPic;
        @Bind(R.id.item_statement_detail_name)
        TextView itemStatementDetailName;
        @Bind(R.id.item_statement_detail_content)
        TextView itemStatementDetailContent;

        public StatementDetailVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class StatementDetailAdapter extends RecyclerView.Adapter<StatementDetailVH> implements View.OnClickListener {

        private List<StatementBean> datas;
        private String day = "";
        private OnRecycleItemClickListener listener;

        public StatementDetailAdapter(List<StatementBean> data) {
            this.datas = data;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }


        @Override
        public StatementDetailVH onCreateViewHolder(ViewGroup parent, int viewType) {
            StatementDetailVH holder = new StatementDetailVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statement_detail, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(StatementDetailVH holder, int position) {
            holder.itemView.setTag(position);
            StatementBean bean = datas.get(position);
            String now = DateUtils.getOnlyDay(bean.date);

            if (position == 0 || !now.equalsIgnoreCase(DateUtils.getOnlyDay(datas.get(position - 1).date))) {
                holder.itemStatementDetailHeaderdivier.setVisibility(View.VISIBLE);
                holder.itemStatementDetailDay.setVisibility(View.VISIBLE);
                holder.itemStatementDetailMonth.setVisibility(View.VISIBLE);
            } else {
                holder.itemStatementDetailHeaderdivier.setVisibility(View.INVISIBLE);
                holder.itemStatementDetailDay.setVisibility(View.INVISIBLE);
                holder.itemStatementDetailMonth.setVisibility(View.INVISIBLE);
            }

            if (position == getItemCount() - 1) {
                holder.itemStatementDetailBottomdivier.setVisibility(View.VISIBLE);
            } else
                holder.itemStatementDetailBottomdivier.setVisibility(View.GONE);


            holder.itemStatementDetailName.setText(bean.title);
            holder.itemStatementDetailContent.setText(bean.content);
            holder.itemStatementDetailDay.setText(now.substring(3, 5));
            holder.itemStatementDetailMonth.setText(now.substring(0, 2) + "月");

            Glide.with(App.AppContex).load(bean.picture).asBitmap().into(new CircleImgWrapper(holder.itemStatementDetailPic, App.AppContex));

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(v, (int) v.getTag());
        }
    }
}
