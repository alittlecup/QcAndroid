package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.widgets.utils.DateUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.SaleCompare;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.activity.FragActivity;
import com.qingchengfit.fitcoach.bean.SaleBean;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcSaleDetailRespone;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaleDetailFragment extends Fragment {
    public static final String TAG = SaleDetailFragment.class.getName();
    public static final int TYPE_MONTH = 2;
    public static final int TYPE_WEEK = 1;
    public static final int TYPE_DAY = 0;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.statement_detail_time)
    TextView statementDetailTime;
    @BindView(R.id.item_statement_detail_content)
    TextView itemStatementDetailContent;

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
    private List<SaleBean> statementBeans = new ArrayList<>();
    private List<Integer> mSystemsId = new ArrayList<>();
    private HashMap<Integer, Integer> mTotalCost = new HashMap<>();
    private HashMap<Integer, Integer> mTotalAccount = new HashMap<>();
    private HashMap<Integer, List<SaleBean>> mAllHistory = new HashMap<>();

    /**
     * 初始化 spinner
     */
//    private ArrayAdapter<SpinnerBean> spinnerBeanArrayAdapter;
//    private ArrayList<SpinnerBean> spinnerBeans;
    private int curSystemId = 0;
    /**
     * 报表参数
     */

    private String start;
    private String end;
    private String card_id = "";
    private int mDividerType = 0;
    private Calendar curCalendar;

    private MaterialDialog loadingDialog;
    private String card_name;
    private Unbinder unbinder;
    private String curModel;

    public SaleDetailFragment() {

    }

    public static SaleDetailFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        SaleDetailFragment fragment = new SaleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SaleDetailFragment newInstance(int type, String starttime, String endtime, int sysId, String cardId, String cardname) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("start", starttime);
        args.putString("end", endtime);
        args.putInt("system", sysId);
        args.putString("card", cardId);
        args.putString("cardname", cardname);

        SaleDetailFragment fragment = new SaleDetailFragment();
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
                card_id = getArguments().getString("card");
                card_name = getArguments().getString("cardname");
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statement_detail, container, false);
        view.setOnTouchListener((v, event) -> {
            return true;
        });
        unbinder=ButterKnife.bind(this, view);
        setupToolbar();
        if (getActivity() instanceof FragActivity){
            if (((FragActivity) getActivity()).getCoachService() != null){
                CoachService coachService = ((FragActivity) getActivity()).getCoachService();

                curModel = coachService.model;
                curSystemId = (int) coachService.getId();
            }
        }

        mStatementDetailAdapter = new StatementDetailAdapter(statementBeans);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mStatementDetailAdapter);
        if (mDividerType == 3) {
            statementDetailLess.setVisibility(View.GONE);
            statementDetailMore.setVisibility(View.GONE);
            statementDetailFilter.setVisibility(View.VISIBLE);
            statementDetailFilter.setText(card_name);
            statementDetailChange.setVisibility(View.VISIBLE);
            statementDetailChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        } else {
            statementDetailLess.setVisibility(View.VISIBLE);
            statementDetailMore.setVisibility(View.VISIBLE);
            statementDetailFilter.setVisibility(View.GONE);
        }

        statementDetailMore.setEnabled(false);
        refresh.setColorSchemeResources(R.color.primary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshData();
            }
        });
        refreshNodata.setColorSchemeResources(R.color.primary);
        refreshNodata.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshData();
            }
        });
        refresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refresh.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                freshData();
            }
        });

        return view;
    }

    /**
     * 初始化toolbar
     */
    public void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbarTitle.setText("销售报表");
        //toolbarTitle.setVisibility(View.GONE);
    }

//    public void setUpNaviSpinner() {
//        spinnerBeans = new ArrayList<>();
//        spinnerBeans.add(new SpinnerBean("", "全部销售报表", true));
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
//        freshData();
//
//    }

    public void freshData() {
        //获取用户拥有系统信息
        //QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid).subscribeOn(Schedulers.newThread())
        //        .subscribe(qcCoachSystemResponse -> {
        //            List<QcCoachSystem> systems = qcCoachSystemResponse.date.systems;
        //            mSystemsId.clear();
        //            for (int i = 0; i < systems.size(); i++) {
        //                QcCoachSystem system = systems.get(i);
        //                mSystemsId.add(system.id);
        //            }
        //            queryStatement();
        //        }, throwable -> {
        //        }, () -> {
        //        });
        queryStatement();
    }

    private void queryStatement() {
        mSystemsId.add(curSystemId);
        if (mSystemsId.size() == 0)
            return;
        mTotalAccount.clear();

        mAllHistory.clear();
        mTotalCost.clear();
        getActivity().runOnUiThread(() -> {
            if (!refresh.isRefreshing() && !refreshNodata.isRefreshing())
                showLoading();
        });
        Observable.from(mSystemsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Integer, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Integer integer) {
                        return QcCloudClient.getApi().getApi.qcGetSaleDatail(App.coachid, getParams(integer))
                            .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .flatMap(qcSaleDetailRespone -> {
                                    mTotalCost.put(integer, qcSaleDetailRespone.data.total_cost);
                                    mTotalAccount.put(integer, qcSaleDetailRespone.data.total_account);
                                    List<QcSaleDetailRespone.History> beans = qcSaleDetailRespone.data.histories;
                                    List<SaleBean> statementBeans = new ArrayList<SaleBean>();
                                    for (int i = 0; i < beans.size(); i++) {
                                        QcSaleDetailRespone.History b = beans.get(i);
                                        SaleBean bean = new SaleBean();
                                        bean.date = DateUtils.formatDateFromServer(b.created_at);
                                        bean.card = b.card == null ? "":b.card.card_name;
                                        bean.price = b.account;

                                        StringBuffer sb = new StringBuffer();
                                        for (int j = 0; j < b.users.size(); j++) {
                                            if (!TextUtils.isEmpty(sb.toString())){
                                                sb.append("、");
                                            }
                                            sb.append(b.users.get(j).username);
                                        }
                                        bean.title = sb.toString();
                                        statementBeans.add(bean);
                                    }
                                    mAllHistory.put(integer, statementBeans);
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
                                    ToastUtils.show(R.drawable.ic_share_fail, getString(R.string.err_server));
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

        int totalsale = 0;
        statementBeans.clear();
        for (Integer key : mAllHistory.keySet()) {
            if (curSystemId != 0 && curSystemId != key)
                continue;
            totalsale += mTotalAccount.get(key);


            statementBeans.addAll(mAllHistory.get(key));
        }

        Collections.sort(statementBeans, new SaleCompare());
        StringBuffer sb1 = new StringBuffer();
        sb1.append(start).append("至").append(end);
        StringBuffer sb2 = new StringBuffer();
        sb2.append("总销售额").append(totalsale).append("元");
        if (getActivity() != null)
            getActivity().runOnUiThread(() -> {
                refresh.setRefreshing(false);
                refreshNodata.setRefreshing(false);
                hideLoading();
                mStatementDetailAdapter.notifyDataSetChanged();
                if (statementBeans.size() > 0) {
                    refreshNodata.setVisibility(View.GONE);
                    refresh.setVisibility(View.VISIBLE);
                } else {
                    refresh.setVisibility(View.GONE);
                    refreshNodata.setVisibility(View.VISIBLE);
                }

                statementDetailTime.setText(sb1.toString());
                itemStatementDetailContent.setText(sb2.toString());
            });
    }

    private HashMap<String, String> getParams(int system_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("id", curSystemId+"");
        params.put("model",curModel);
        if (!TextUtils.isEmpty(card_id) && !card_id.equalsIgnoreCase("0"))
            params.put("card_tpl_id", card_id);
        return params;
    }

    @Override
    public void onDestroyView() {
        if (loadingDialog!=null)
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
                .subscribe(s -> queryStatement());
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
        @BindView(R.id.item_statement_detail_name)
        TextView itemStatementDetailName;
        @BindView(R.id.item_statement_detail_content)
        TextView itemStatementDetailContent;
        @BindView(R.id.item_statement_detail_price)
        TextView itemStatementDetailPrice;

        public StatementDetailVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class StatementDetailAdapter extends RecyclerView.Adapter<StatementDetailVH> implements View.OnClickListener {


        private List<SaleBean> datas;
        private String day = "";
        private OnRecycleItemClickListener listener;

        public StatementDetailAdapter(List<SaleBean> data) {
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
            StatementDetailVH holder = new StatementDetailVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale_detail, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(StatementDetailVH holder, int position) {
            holder.itemView.setTag(position);
            SaleBean bean = datas.get(position);
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
            holder.itemStatementDetailContent.setText(bean.card);
            holder.itemStatementDetailDay.setText(now.substring(3, 5));
            holder.itemStatementDetailMonth.setText(now.substring(0, 2) + "月");
            holder.itemStatementDetailPrice.setText("¥" + bean.price);


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
