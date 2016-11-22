package com.qingchengfit.fitcoach.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.StatementCompare;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.ChooseGymActivity;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.bean.StatementBean;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcScheduleBean;
import com.qingchengfit.fitcoach.http.bean.QcStatementDetailRespone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import cn.qingchengfit.widgets.utils.LogUtil;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
    @BindView(R.id.spinner_nav)
    Spinner spinnerNav;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.statement_detail_time)
    TextView statementDetailTime;
    @BindView(R.id.item_statement_detail_content)
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
    @BindView(R.id.statement_detail_less)
    ImageButton statementDetailLess;
    @BindView(R.id.statement_detail_more)
    ImageButton statementDetailMore;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.refresh_nodata)
    SwipeRefreshLayout refreshNodata;
    @BindView(R.id.statement_detail_change)
    Button statementDetailChange;
    @BindView(R.id.statement_detail_filter)
    TextView statementDetailFilter;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    private StatementDetailAdapter mStatementDetailAdapter;
    private List<StatementBean> statementBeans = new ArrayList<>();
    private List<Pair<Integer, String>> mSystemsId = new ArrayList<>();
    private HashMap<Pair<Integer,String>, Integer> mCourseNum = new HashMap<>();
    private HashMap<Pair<Integer,String>, Integer> mOrderNum = new HashMap<>();
    private HashMap<Pair<Integer,String>, Integer> mServerNum = new HashMap<>();
    private HashMap<Pair<Integer,String>, List<StatementBean>> mAllStatemet = new HashMap<>();

    /**
     * 初始化 spinner
     */
    private int curSystemId = 0;    //当前健身房id

    /**
     * 报表参数
     */

    private String start;
    private String end;
    private int course_id = 0;
    private int user_id = 0;
    private int mDividerType = 0;
    private Calendar curCalendar;
    private MaterialDialog loadingDialog;
    private String user_name;
    private String course_name;
    private String mTitle;
    private String curModel;
    private Unbinder unbinder;

    public StatementDetailFragment() {

    }

    public static StatementDetailFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        StatementDetailFragment fragment = new StatementDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static StatementDetailFragment newInstance(int type, String starttime, String endtime,String model, int sysId, int userId, int courseId, String userName, String courseName) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("start", starttime);
        args.putString("end", endtime);
        args.putInt("system", sysId);
        args.putInt("course", courseId);
        args.putInt("user", userId);
        args.putString("userName", userName);
        args.putString("courseName", courseName);
        args.putString("model", model);

        StatementDetailFragment fragment = new StatementDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new MaterialDialog.Builder(getActivity())
                    .content("请稍后")
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        loadingDialog.show();
    }

    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDividerType = getArguments().getInt("type");
        }
        curCalendar = Calendar.getInstance();
        start = DateUtils.Date2YYYYMMDD(new Date());
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
                user_name = getArguments().getString("userName");
                course_name = getArguments().getString("courseName");
                curModel = getArguments().getString("model");
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statement_detail, container, false);
        unbinder=ButterKnife.bind(this, view);
        setupToolbar();
//        setUpNaviSpinner();
        mTitle = getString(R.string.statement_course);
        toolbarTitle.setText(mTitle);


        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosegym = new Intent(getContext(), ChooseGymActivity.class);
                choosegym.putExtra("model", curModel);
                choosegym.putExtra("id", curSystemId);
                choosegym.putExtra("title", mTitle);
                startActivityForResult(choosegym, 501);
            }
        });

        mStatementDetailAdapter = new StatementDetailAdapter(statementBeans);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mStatementDetailAdapter);
        if (mDividerType == 3) {
            statementDetailLess.setVisibility(View.GONE);
            statementDetailMore.setVisibility(View.GONE);
            statementDetailFilter.setVisibility(View.VISIBLE);
            statementDetailFilter.setText(course_name + "  " + user_name);
            statementDetailChange.setVisibility(View.VISIBLE);
            statementDetailChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        } else {
            statementDetailFilter.setVisibility(View.GONE);
            statementDetailLess.setVisibility(View.VISIBLE);
            statementDetailMore.setVisibility(View.VISIBLE);
        }
        statementDetailMore.setEnabled(false);//初始化右键不可点
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshDate();
            }
        });
        refreshNodata.setColorSchemeResources(R.color.primary);
        refreshNodata.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshDate();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        recyclerview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                refresh.setRefreshing(true);
                freshDate();
            }
        });

        if (getActivity() instanceof FragActivity){
            if (((FragActivity) getActivity()).getCoachService() != null){
                CoachService coachService = ((FragActivity) getActivity()).getCoachService();
                toolbarTitle.setText(coachService.getName());
                curModel = coachService.model;
                curSystemId = (int) coachService.getId();
            }

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

//    public void setUpNaviSpinner() {
//        spinnerBeans = new ArrayList<>();
//        spinnerBeans.add(new SpinnerBean("", "全部课程报表", true));
//        spinnerBeanArrayAdapter = new ArrayAdapter<SpinnerBean>(getContext(), R.layout.spinner_checkview, spinnerBeans) {
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
//        spinnerBeanArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//        spinnerNav.setAdapter(spinnerBeanArrayAdapter);
//        spinnerNav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                curSystemId = spinnerBeanArrayAdapter.getItem(position).id;
//                showData();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        freshDate();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0 && requestCode == 501) {
            toolbarTitle.setText(data.getStringExtra("name"));
            curModel = data.getStringExtra("model");
            curSystemId = Integer.parseInt(data.getStringExtra("id"));
            LogUtil.e("curModel:" + curModel + "   id:" + curSystemId);
//            handleReponse(response);  TODO
            showData();

        }
    }


    public void freshDate() {
        //获取用户拥有系统信息
        QcCloudClient.getApi().getApi.qcGetCoachService(App.coachid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(qcCoachSystemResponse -> {
                    List<CoachService> systems = qcCoachSystemResponse.data.services;
                    mSystemsId.clear();
                    for (int i = 0; i < systems.size(); i++) {
                        CoachService system = systems.get(i);
                        if (curSystemId == system.id && curModel.equals(system.model)){
                            if (!TextUtils.isEmpty(curModel) && curSystemId != 0){
                                toolbar.setTitle(system.name);
                            }
                        }
                        mSystemsId.add(new Pair<Integer, String>((int) system.id, system.model));
                    }
                    queryStatement();
                }, throwable -> {
                }, () -> {
                });
    }


    private void queryStatement() {
        if (mSystemsId.size() == 0)
            return;
        mOrderNum.clear();
        mServerNum.clear();
        mAllStatemet.clear();
        mCourseNum.clear();
        getActivity().runOnUiThread(() -> {
            if (!refresh.isRefreshing() && !refreshNodata.isRefreshing())
                showLoading();
        });
        Observable.from(mSystemsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Pair<Integer, String>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Pair<Integer, String> integerStringPair) {
                        return QcCloudClient.getApi().getApi.qcGetStatementDatail(App.coachid, getParams(integerStringPair))
                                .observeOn(AndroidSchedulers.mainThread())
                                .flatMap(qcStatementDetailRespone -> {
                                    if (qcStatementDetailRespone.data.schedules != null &&
                                            qcStatementDetailRespone.data.schedules.stat != null) {
                                        mCourseNum.put(integerStringPair, qcStatementDetailRespone.data.schedules.stat.course_count);
                                        mOrderNum.put(integerStringPair, qcStatementDetailRespone.data.schedules.stat.order_count);
                                        mServerNum.put(integerStringPair, qcStatementDetailRespone.data.schedules.stat.user_count);
                                    }
                                    List<QcScheduleBean> beans = null;
                                    if (qcStatementDetailRespone.data.schedules != null) {
                                        beans = qcStatementDetailRespone.data.schedules.schedules;
                                    }
                                    List<StatementBean> statementBeans = new ArrayList<StatementBean>();
                                    if (beans != null) {
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
                                    }
                                    mAllStatemet.put(integerStringPair, statementBeans);
                                    return Observable.just(true);
                                });

                    }
                })
                .last()
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    ToastUtils.show(R.drawable.ic_share_fail, "网络错误");
                                }
                            });
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        showData();
                    }
                });


    }

    public void showData() {
        int orderC = 0;
        int serverC = 0;
        int courseC = 0;
        statementBeans.clear();
        for (Pair<Integer,String> key : mAllStatemet.keySet()) {
            if (curSystemId != 0 && (curSystemId != key.first || !curModel.equals(key.second)))
                continue;
            if (mOrderNum.get(key) != null)
                orderC += mOrderNum.get(key);
            if (mServerNum.get(key) != null)
                serverC += mServerNum.get(key);
            if (mCourseNum.get(key) != null)
                courseC += mCourseNum.get(key);
            statementBeans.addAll(mAllStatemet.get(key));
        }

        Collections.sort(statementBeans, new StatementCompare());
        StringBuffer sb1 = new StringBuffer();
        sb1.append(start).append("至").append(end);
        StringBuffer sb2 = new StringBuffer();
        sb2.append(courseC).append("节课程  ").append(orderC).append("人预约  服务").append(serverC).append("人次");
        if (getActivity() != null)
            getActivity().runOnUiThread(() -> {
                hideLoading();
                mStatementDetailAdapter.notifyDataSetChanged();
                if (statementBeans.size() > 0) {
                    refresh.setVisibility(View.VISIBLE);
                    refreshNodata.setVisibility(View.GONE);
                } else {
                    refresh.setVisibility(View.GONE);
                    refreshNodata.setVisibility(View.VISIBLE);
                }

                statementDetailTime.setText(sb1.toString());
                itemStatementDetailContent.setText(sb2.toString());
                refresh.setRefreshing(false);
                refreshNodata.setRefreshing(false);
            });
    }

    private HashMap<String, String> getParams(Pair<Integer, String> system_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("id", Integer.toString(system_id.first));
        params.put("model", system_id.second);

        if (course_id != 0)
            params.put("course_id", Integer.toString(course_id));
        if (user_id != 0)
            params.put("user_id", Integer.toString(user_id));
        return params;
    }

    @Override
    public void onDestroyView() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
        unbinder.unbind();
        super.onDestroyView();

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
                            start = DateUtils.Date2YYYYMMDD(curCalendar.getTime());
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

                    getActivity().runOnUiThread(() -> {
                        if (DateUtils.Date2YYYYMMDD(curCalendar.getTime()).equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date()))) {
                            statementDetailMore.setEnabled(false);
                        } else {
                            statementDetailMore.setEnabled(true);
                        }
                    });


                    return s1;
                })
                .subscribe(s -> queryStatement(), throwable -> {
                }, () -> {
                });
    }


    /**
     * recycle view
     */
    class StatementDetailVH extends RecyclerView.ViewHolder {
        @BindView(R.id.item_statement_detail_bottomdivier)
        View itemStatementDetailBottomdivier;
        @BindView(R.id.item_statement_detail_headerdivier)
        View itemStatementDetailHeaderdivier;
        @BindView(R.id.item_statement_detail_day)
        TextView itemStatementDetailDay;
        @BindView(R.id.item_statement_detail_month)
        TextView itemStatementDetailMonth;
        @BindView(R.id.item_statement_detail_date)
        LinearLayout itemStatementDetailDate;
        @BindView(R.id.item_statement_detail_pic)
        ImageView itemStatementDetailPic;
        @BindView(R.id.item_statement_detail_name)
        TextView itemStatementDetailName;
        @BindView(R.id.item_statement_detail_content)
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
            String now = DateUtils.Date2MMDD(bean.date);

            if (position == 0 || !now.equalsIgnoreCase(DateUtils.Date2MMDD(datas.get(position - 1).date))) {
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
