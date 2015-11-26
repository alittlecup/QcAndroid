package com.qingchengfit.fitcoach.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.paper.paperbaselibrary.utils.DateUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.SaleCompare;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.bean.SaleBean;
import com.qingchengfit.fitcoach.bean.SpinnerBean;
import com.qingchengfit.fitcoach.component.LoopView;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystem;
import com.qingchengfit.fitcoach.http.bean.QcSaleDetailRespone;

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

    @Bind(R.id.statement_detail_less)
    ImageButton statementDetailLess;
    @Bind(R.id.statement_detail_more)
    ImageButton statementDetailMore;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.refresh_nodata)
    SwipeRefreshLayout refreshNodata;
    @Bind(R.id.statement_detail_change)
    Button statementDetailChange;
    @Bind(R.id.statement_detail_filter)
    TextView statementDetailFilter;

    private StatementDetailAdapter mStatementDetailAdapter;
    private List<SaleBean> statementBeans = new ArrayList<>();
    private List<Integer> mSystemsId = new ArrayList<>();
    private HashMap<Integer, Integer> mTotalCost = new HashMap<>();
    private HashMap<Integer, Integer> mTotalAccount = new HashMap<>();
    private HashMap<Integer, List<SaleBean>> mAllHistory = new HashMap<>();

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
    private int card_id = 0;
    private int mDividerType = 0;
    private Calendar curCalendar;

    private MaterialDialog loadingDialog;
    private String card_name;

    public SaleDetailFragment() {

    }

    public static SaleDetailFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        SaleDetailFragment fragment = new SaleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SaleDetailFragment newInstance(int type, String starttime, String endtime, int sysId, int cardId, String cardname) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("start", starttime);
        args.putString("end", endtime);
        args.putInt("system", sysId);
        args.putInt("card", cardId);
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
                card_id = getArguments().getInt("card");
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
        ButterKnife.bind(this, view);
        setupToolbar();
        setUpNaviSpinner();
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
        spinnerBeans.add(new SpinnerBean("", "全部销售报表", true));
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
        freshData();

    }

    public void freshData() {
        //获取用户拥有系统信息
        QcCloudClient.getApi().getApi.qcGetCoachSystem(App.coachid).subscribeOn(Schedulers.newThread())
                .subscribe(qcCoachSystemResponse -> {
                    List<QcCoachSystem> systems = qcCoachSystemResponse.date.systems;
                    mSystemsId.clear();
                    spinnerBeans.clear();
                    spinnerBeans.add(new SpinnerBean("", "全部课程报表", true));
                    for (int i = 0; i < systems.size(); i++) {
                        QcCoachSystem system = systems.get(i);
                        spinnerBeans.add(new SpinnerBean(system.color, system.name, system.id));
                        mSystemsId.add(system.id);
                    }
                    getActivity().runOnUiThread(spinnerBeanArrayAdapter::notifyDataSetChanged);
                    queryStatement();
                }, throwable -> {
                }, () -> {
                });
    }

    private void queryStatement() {
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
                                        bean.card = b.card;
                                        bean.price = b.account;

                                        StringBuffer sb = new StringBuffer();
                                        sb.append(b.username).append(b.remarks);
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
        params.put("system_id", Integer.toString(system_id));
        if (card_id != 0)
            params.put("card_tpl_id", Integer.toString(card_id));
        return params;
    }

    @Override
    public void onDestroyView() {

        loadingDialog.dismiss();
        ButterKnife.unbind(this);
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
                    getActivity().runOnUiThread(() -> {
                        if (DateUtils.getServerDateDay(curCalendar.getTime()).equalsIgnoreCase(DateUtils.getServerDateDay(new Date()))) {
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
        @Bind(R.id.item_statement_detail_name)
        TextView itemStatementDetailName;
        @Bind(R.id.item_statement_detail_content)
        TextView itemStatementDetailContent;
        @Bind(R.id.item_statement_detail_price)
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
