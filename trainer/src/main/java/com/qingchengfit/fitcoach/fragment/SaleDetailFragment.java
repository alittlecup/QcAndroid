package com.qingchengfit.fitcoach.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.FragmentAdapter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.RecycleViewWithNoImg;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import cn.qingchengfit.bean.StudentBean;
import com.qingchengfit.fitcoach.component.CircleIndicator;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import com.qingchengfit.fitcoach.fragment.statement.RxNetWorkEvent;
import com.qingchengfit.fitcoach.fragment.statement.SaleCardTypeView;
import com.qingchengfit.fitcoach.fragment.statement.SaleFilterActivity;
import com.qingchengfit.fitcoach.fragment.statement.fragment.SaleCardTypeFragment;
import com.qingchengfit.fitcoach.fragment.statement.fragment.SaleTradeTypeFormFragment;
import com.qingchengfit.fitcoach.fragment.statement.model.QcResponseSaleDetail;
import com.qingchengfit.fitcoach.fragment.statement.model.SaleCardForm;
import com.qingchengfit.fitcoach.fragment.statement.model.SaleFilter;
import com.qingchengfit.fitcoach.fragment.statement.model.SaleTradeForm;
import com.qingchengfit.fitcoach.fragment.statement.presenter.SaleDetailPresenter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaleDetailFragment extends BaseFragment implements SaleCardTypeView {
    public static final String TAG = SaleDetailFragment.class.getName();
    public static final int TYPE_MONTH = 2;
    public static final int TYPE_WEEK = 1;
    public static final int TYPE_DAY = 0;
    public static final int RESULT_FILTER = 2;

    @BindView(R.id.statement_detail_less) ImageButton statementDetailLess;
    @BindView(R.id.statement_detail_more) ImageButton statementDetailMore;
    @BindView(R.id.statement_detail_time) TextView statementDetailTime;
    @BindView(R.id.statement_detail_filter) TextView statementDetailFilter;
    @BindView(R.id.item_statement_time_shop) TextView itemStatementDetailContent;
    @BindView(R.id.recyclerview) RecycleViewWithNoImg recyclerview;
    @BindView(R.id.statement_detail_change) Button statementDetailChange;
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.indicator) CircleIndicator indicator;
    @Inject SaleDetailPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    private StatementDetailAdapter mStatementDetailAdapter;
    private List<QcResponseSaleDetail.History> statementBeans = new ArrayList<>();
    private List<QcResponseSaleDetail.History> mAllstatementBeans = new ArrayList<>();
    /**
     * 报表中出现的数据
     */
    private ArrayList<QcResponseSaleDetail.Card> mFilterCardTpl = new ArrayList<>();//可筛选的会员卡种类
    private ArrayList<Integer> mFilterTradeType = new ArrayList<>();//可筛选的交易类型
    private ArrayList<Staff> mFilterSalers = new ArrayList<>();//可筛选的销售
    private ArrayList<Integer> mFilterPayMethod = new ArrayList<>();//可筛选的支付类型
    private ArrayList<StudentBean> mFilterStudents = new ArrayList<>();//可筛选的会员
    /**
     * 报表参数
     */

    private String start;
    private String end;
    private String card_id;
    private String card_extra;
    private String seller_id;
    private int mTradType;
    private int mPayType;
    private int mDividerType = 0;
    private int mDividerDay = 0;
    private Calendar curCalendar;
    private String mChooseShopId;
    private Observable<RxNetWorkEvent> mNetOb;
    private ArrayList<Fragment> fs = new ArrayList<>();
    private FragmentAdapter fragmentAdapter;

    private SaleFilter mSaleFilter = new SaleFilter();//筛选参数

    public SaleDetailFragment() {

    }

    public static SaleDetailFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        SaleDetailFragment fragment = new SaleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SaleDetailFragment newInstance(int type, String starttime, String endtime, String cardId, String card_extra,
        String seller_id, int mTradType, int mPayType, SaleFilter filter) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("start", starttime);
        args.putString("end", endtime);
        //        args.putInt("system", sysId);
        args.putString("card", cardId);
        args.putString("card_extra", card_extra);
        args.putString("seller_id", seller_id);
        args.putInt("tradetype", mTradType);
        args.putInt("mPayType", mPayType);
        args.putParcelable("filter", filter);
        SaleDetailFragment fragment = new SaleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
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
                card_id = getArguments().getString("card");
                card_extra = getArguments().getString("card_extra");
                seller_id = getArguments().getString("seller_id");
                mTradType = getArguments().getInt("tradetype");
                mPayType = getArguments().getInt("mPayType");
                mDividerDay = DateUtils.interval(start, end);
                mSaleFilter = getArguments().getParcelable("filter");

                break;
            default:
                break;
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_detail, container, false);
        delegatePresenter(presenter, this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        unbinder = ButterKnife.bind(this, view);
        initToolbar(toolbar);
        initView();
        mNetOb = RxBus.getBus().register(RxNetWorkEvent.class);
        mNetOb.subscribe(new Action1<RxNetWorkEvent>() {
            @Override public void call(RxNetWorkEvent rxNetWorkEvent) {
                if (rxNetWorkEvent.status == -1) {
                    hideLoading();
                }
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (gymWrapper.inBrand()) {
            toolbarTitle.setText(R.string.all_gyms);
        } else {
            toolbarTitle.setText(R.string.statement_sale);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                if (TextUtils.isEmpty(mChooseShopId)) {
                    toolbarTitle.setText(R.string.all_gyms);
                    //mCallbackActivity.setBar(new ToolbarBean(getString(R.string.all_gyms), true, new View.OnClickListener() {
                    //    @Override public void onClick(View v) {
                    //        ChooseGymActivity.start(SaleDetailFragment.this, 1, PermissionServerUtils.SALES_REPORT,
                    //            getString(R.string.choose_gym), mChooseShopId);
                    //    }
                    //}, R.menu.menu_out_excel, MenuClick));
                } else {
                    toolbarTitle.setText(IntentUtils.getIntentString(data, 0));
                    //mCallbackActivity.setBar(new ToolbarBean(IntentUtils.getIntentString(data, 0), true, new View.OnClickListener() {
                    //    @Override public void onClick(View v) {
                    //        ChooseGymActivity.start(SaleDetailFragment.this, 1, PermissionServerUtils.SALES_REPORT,
                    //            getString(R.string.choose_gym), mChooseShopId);
                    //    }
                    //}, R.menu.menu_out_excel, MenuClick));
                }
                showLoading();
                presenter.querySaleDetail(start, end);
            } else if (requestCode == 2) {
                //筛选条件
                mSaleFilter = data.getParcelableExtra("filter");

                if (mSaleFilter.startDay.equalsIgnoreCase(mSaleFilter.endDay)) {
                    statementDetailTime.setText(mSaleFilter.startDay);
                } else {
                    statementDetailTime.setText(mSaleFilter.startDay + "至" + mSaleFilter.endDay);
                }

                if (mSaleFilter.startDay.equals(start) && mSaleFilter.endDay.equals(end)) {
                    statementDetailMore.setEnabled(true);
                    statementDetailLess.setEnabled(true);
                } else {
                    statementDetailMore.setEnabled(false);
                    statementDetailLess.setEnabled(false);
                }

                Filter();
            }
        }
    }

    private void initView() {
        recyclerview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                freshData();
            }
        });
        mStatementDetailAdapter = new StatementDetailAdapter(statementBeans);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mStatementDetailAdapter);

        if (mDividerType == 3) {
            statementDetailMore.setEnabled(DateUtils.interval(end, DateUtils.Date2YYYYMMDD(new Date())) > mDividerDay);
        } else {

            statementDetailLess.setVisibility(View.VISIBLE);
            statementDetailMore.setVisibility(View.VISIBLE);
            statementDetailFilter.setVisibility(View.GONE);
            statementDetailMore.setEnabled(false);
        }

        recyclerview.stopLoading();
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fs);
        viewpager.setAdapter(fragmentAdapter);

        statementDetailTime.setText(start.equals(end) ? start : start + "至" + end);

        freshData();
    }

    public void freshData() {
        showLoading();
        //获取用户拥有系统信息
        presenter.querySaleDetail(start, end);
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(RxNetWorkEvent.class.getName(), mNetOb);
        presenter.unattachView();
        super.onDestroyView();
    }

    @OnClick(R.id.statement_detail_less) public void onClickLess() {
        changeCalendar(-1);
    }

    @OnClick(R.id.statement_detail_more) public void onClickMore() {
        changeCalendar(1);
    }

    @OnClick(R.id.btn_filter) public void onBtnFilter() {
        Intent toFilter = new Intent(getActivity(), SaleFilterActivity.class);
        if (TextUtils.isEmpty(mSaleFilter.startDay)) mSaleFilter.startDay = start;
        if (TextUtils.isEmpty(mSaleFilter.endDay)) mSaleFilter.endDay = end;

        toFilter.putExtra("start", start);
        toFilter.putExtra("end", end);
        toFilter.putExtra("filter", mSaleFilter);
        toFilter.putParcelableArrayListExtra("card_tpl", mFilterCardTpl);
        toFilter.putParcelableArrayListExtra("sale", mFilterSalers);
        toFilter.putParcelableArrayListExtra("student", mFilterStudents);
        toFilter.putIntegerArrayListExtra("pay", mFilterPayMethod);
        toFilter.putIntegerArrayListExtra("card_type", mFilterTradeType);
        startActivityForResult(toFilter, RESULT_FILTER);
    }

    /**
     * 增加或者减少 +1 -1
     */
    private void changeCalendar(int symbol) {
      Observable.just(symbol)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .throttleFirst(500, TimeUnit.MILLISECONDS)
          .map(new Func1<Integer, Integer>() {
            @Override public Integer call(Integer symbol) {
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
                        if (symbol < 0) {
                            end = DateUtils.minusDay(DateUtils.formatDateFromYYYYMMDD(start), 1);
                            start = DateUtils.minusDay(DateUtils.formatDateFromYYYYMMDD(end), mDividerDay);
                        } else {
                            start = DateUtils.minusDay(DateUtils.formatDateFromYYYYMMDD(end), -1);
                            end = DateUtils.minusDay(DateUtils.formatDateFromYYYYMMDD(start), -mDividerDay);
                        }
                        break;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        if (mDividerType < 3) {
                            if (DateUtils.Date2YYYYMMDD(curCalendar.getTime()).equalsIgnoreCase(DateUtils.Date2YYYYMMDD(new Date()))) {
                                statementDetailMore.setEnabled(false);
                            } else {
                                statementDetailMore.setEnabled(true);
                            }
                        } else {
                            statementDetailMore.setEnabled(DateUtils.interval(end, DateUtils.Date2YYYYMMDD(new Date())) > mDividerDay);
                        }
                        statementDetailTime.setText(start.equals(end) ? start : start + "至" + end);
                    }
                });

                return symbol;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override public void call(Object o) {
                freshData();
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {

            }
        });
    }

    @Override public void onSuccess(List<QcResponseSaleDetail.History> list) {
        hideLoading();
        mAllstatementBeans.clear();
        mAllstatementBeans.addAll(list);

        mFilterCardTpl.clear();
        mFilterStudents.clear();
        mFilterTradeType.clear();
        mFilterPayMethod.clear();
        mFilterSalers.clear();
        mSaleFilter.startDay = start;
        mSaleFilter.endDay = end;
        Staff nosaler = new Staff();
        nosaler.id = "-1";
        nosaler.username = "无销售";
        for (int i = 0; i < mAllstatementBeans.size(); i++) {
            QcResponseSaleDetail.History history = mAllstatementBeans.get(i);

            //可筛选数据
            if (!mFilterCardTpl.contains(history.card)) mFilterCardTpl.add(history.card);
            if (!mFilterPayMethod.contains(history.charge_type))//充值方式
            {
                mFilterPayMethod.add(history.charge_type);
            }
            if (history.saler.id == null) {
                if (!mFilterSalers.contains(nosaler)) {
                    mFilterSalers.add(nosaler);
                }
            } else if (!mFilterSalers.contains(history.saler)) mFilterSalers.add(history.saler);
            if (!mFilterTradeType.contains(history.type))//交易方式
            {
                mFilterTradeType.add(history.type);
            }
            for (int j = 0; j < history.students.size(); j++) {//学生
                if (!mFilterStudents.contains(history.students.get(j))) {
                    mFilterStudents.add(history.students.get(j));
                }
            }
        }

        Filter();
    }

    public void Filter() {
        statementBeans.clear();

        HashMap<String, Float> caculator = new HashMap<>();
        caculator.put("trade_charge", 0f);
        caculator.put("trade_charge_realincome", 0f);
        caculator.put("trade_charge_first", 0f);
        caculator.put("trade_charge_first_realincome", 0f);
        caculator.put("trade_refund", 0f);
        caculator.put("trade_refund_realincome", 0f);
        caculator.put("trade_present", 0f);
        caculator.put("trade_present_realincome", 0f);
        caculator.put("value_trade", 0f);
        caculator.put("value_charge", 0f);
        caculator.put("value_income", 0f);
        caculator.put("times_trade", 0f);
        caculator.put("times_charge", 0f);
        caculator.put("times_income", 0f);
        caculator.put("date_trade", 0f);
        caculator.put("date_charge", 0f);
        caculator.put("date_income", 0f);
        for (int i = 0; i < mAllstatementBeans.size(); i++) {
            QcResponseSaleDetail.History history = mAllstatementBeans.get(i);

            if ((mSaleFilter.payMethod == 0 || mSaleFilter.payMethod == history.charge_type)
                //筛选充值类型
                && (mSaleFilter.tradeType == 0 || mSaleFilter.tradeType == history.type)
                //筛选交易类型
                && (mSaleFilter.saler == null || TextUtils.equals(mSaleFilter.saler.id, history.saler.id))
                //筛选销售
                && ((mSaleFilter.card == null && (mSaleFilter.card_category == 0 || mSaleFilter.card_category == history.card.card_type))
                || (mSaleFilter.card != null && TextUtils.equals(mSaleFilter.card.card_tpl_id, history.card.card_tpl_id)))
                //筛选会员卡种类
                && (mSaleFilter.student == null || history.getStudentIds().contains(mSaleFilter.student.id))
                //筛选学员
                && (DateUtils.formatDateFromYYYYMMDD(mSaleFilter.startDay).getTime() <= DateUtils.formatDateFromServer(history.created_at)
                .getTime() && DateUtils.formatDateFromServer(history.created_at).getTime() < (DateUtils.formatDateFromYYYYMMDD(
                mSaleFilter.endDay).getTime() + DateUtils.DAY_TIME))) {
                statementBeans.add(history);
                //制表
                switch (history.type) {
                    case Configs.TRADE_CHARGE:
                        caculator.put("trade_charge", caculator.get("trade_charge") + 1);
                        caculator.put("trade_charge_realincome", caculator.get("trade_charge_realincome") + (history.cost));
                        break;
                    case Configs.TRADE_CHARGE_FIRST:
                        caculator.put("trade_charge_first", caculator.get("trade_charge_first") + 1);
                        caculator.put("trade_charge_first_realincome", caculator.get("trade_charge_first_realincome") + (history.cost));
                        break;
                    case Configs.TRADE_REFUND:
                        caculator.put("trade_refund", caculator.get("trade_refund") + 1);
                        caculator.put("trade_refund_realincome", caculator.get("trade_refund_realincome") + (history.cost));

                        break;
                    case Configs.TRADE_PRESENT:
                        caculator.put("trade_present", caculator.get("trade_present") + 1);
                        caculator.put("trade_present_realincome", caculator.get("trade_present_realincome") + (history.cost));
                        break;
                }
                switch (history.card.card_type) {
                    case Configs.CATEGORY_VALUE:
                        caculator.put("value_trade", caculator.get("value_trade") + 1);
                        caculator.put("value_charge", caculator.get("value_charge") + (-history.account));//充值
                        caculator.put("value_income", caculator.get("value_income") + (history.cost));//实收
                        break;
                    case Configs.CATEGORY_TIMES:
                        caculator.put("times_trade", caculator.get("times_trade") + 1);
                        caculator.put("times_charge", caculator.get("times_charge") + (-history.account));//充值
                        caculator.put("times_income", caculator.get("times_income") + (history.cost));//实收

                        break;
                    case Configs.CATEGORY_DATE:
                        caculator.put("date_trade", caculator.get("date_trade") + 1);
                        caculator.put("date_charge", caculator.get("date_charge") + (-history.account));//充值
                        caculator.put("date_income", caculator.get("date_income") + (history.cost));//实收
                        break;
                }
            }
        }
        List<SaleCardForm> saleCardForms = new ArrayList<>();
        SaleCardForm saleCardForm1 =
            new SaleCardForm(Configs.CATEGORY_VALUE, caculator.get("value_trade").intValue(), caculator.get("value_charge"),
                caculator.get("value_income"));
        SaleCardForm saleCardForm2 =
            new SaleCardForm(Configs.CATEGORY_TIMES, caculator.get("times_trade").intValue(), caculator.get("times_charge"),
                caculator.get("times_income"));
        SaleCardForm saleCardForm3 =
            new SaleCardForm(Configs.CATEGORY_DATE, caculator.get("date_trade").intValue(), caculator.get("date_charge"),
                caculator.get("date_income"));
        SaleCardForm saleCardForm = new SaleCardForm(0, saleCardForm1.trade_count + saleCardForm2.trade_count + saleCardForm3.trade_count,
            saleCardForm1.charge + saleCardForm2.charge + saleCardForm3.charge,
            saleCardForm1.real_income + saleCardForm2.real_income + saleCardForm3.real_income);
        saleCardForms.add(saleCardForm1);
        saleCardForms.add(saleCardForm2);
        saleCardForms.add(saleCardForm3);
        saleCardForms.add(saleCardForm);

        List<SaleTradeForm> saleTradeForms = new ArrayList<>();
        SaleTradeForm saleTradeForm1 = new SaleTradeForm(Configs.TRADE_CHARGE_FIRST, caculator.get("trade_charge_first").intValue(),
            caculator.get("trade_charge_first_realincome"));
        SaleTradeForm saleTradeForm2 =
            new SaleTradeForm(Configs.TRADE_CHARGE, caculator.get("trade_charge").intValue(), caculator.get("trade_charge_realincome"));
        SaleTradeForm saleTradeForm3 =
            new SaleTradeForm(Configs.TRADE_PRESENT, caculator.get("trade_present").intValue(), caculator.get("trade_present_realincome"));
        SaleTradeForm saleTradeForm4 =
            new SaleTradeForm(Configs.TRADE_REFUND, caculator.get("trade_refund").intValue(), caculator.get("trade_refund_realincome"));
        SaleTradeForm saleTradeForm = new SaleTradeForm(0,
            saleTradeForm1.trade_count + saleTradeForm2.trade_count + saleTradeForm3.trade_count + saleTradeForm4.trade_count,
            saleTradeForm1.real_income + saleTradeForm2.real_income + saleTradeForm3.real_income + saleTradeForm4.real_income);
        saleTradeForms.add(saleTradeForm);
        saleTradeForms.add(saleTradeForm1);
        saleTradeForms.add(saleTradeForm2);
        saleTradeForms.add(saleTradeForm3);
        saleTradeForms.add(saleTradeForm4);

        SaleCardTypeFragment fragment1 = SaleCardTypeFragment.newInstance(saleCardForms);
        SaleTradeTypeFormFragment formFragment2 = SaleTradeTypeFormFragment.newInstance(saleTradeForms);

        // 刷新报表
        fs.clear();

        fs.add(formFragment2);
        fs.add(fragment1);
        fragmentAdapter.notifyDataSetChanged();
        indicator.setViewPager(viewpager);

        mStatementDetailAdapter.notifyDataSetChanged();
        recyclerview.setNoData(statementBeans.size() == 0);
    }

    @Override public void onFailed(String e) {
        hideLoading();
        ToastUtils.show(e);
        recyclerview.setFresh(false);
    }

    @Override public String getFragmentName() {
        return SaleDetailFragment.class.getName();
    }

    /**
     * recycle view
     */
    class StatementDetailVH extends RecyclerView.ViewHolder {
        @BindView(R.id.item_statement_detail_bottomdivier) View itemStatementDetailBottomdivier;
        @BindView(R.id.item_statement_detail_headerdivier) View itemStatementDetailHeaderdivier;
        @BindView(R.id.item_statement_detail_day) TextView itemStatementDetailDay;
        @BindView(R.id.item_statement_detail_month) TextView itemStatementDetailMonth;
        @BindView(R.id.item_statement_detail_date) LinearLayout itemStatementDetailDate;
        @BindView(R.id.item_statement_detail_name) TextView itemStatementDetailName;
        @BindView(R.id.item_statement_time_shop) TextView itemStatementTimeShop;
        @BindView(R.id.item_statement_detail_price) TextView itemStatementDetailPrice;
        @BindView(R.id.trade_type) TextView tradeType;
        @BindView(R.id.pay_method) TextView payMethod;
        @BindView(R.id.pay_money) TextView payMoney;
        @BindView(R.id.account) TextView account;
        @BindView(R.id.users) TextView users;

        public StatementDetailVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class StatementDetailAdapter extends RecyclerView.Adapter<StatementDetailVH> implements View.OnClickListener {

        private List<QcResponseSaleDetail.History> datas;
        private String day = "";
        private OnRecycleItemClickListener listener;

        public StatementDetailAdapter(List<QcResponseSaleDetail.History> data) {
            this.datas = data;
        }

        public OnRecycleItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnRecycleItemClickListener listener) {
            this.listener = listener;
        }

        @Override public StatementDetailVH onCreateViewHolder(ViewGroup parent, int viewType) {
            StatementDetailVH holder =
                new StatementDetailVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale_detail, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        }

        @Override public void onBindViewHolder(StatementDetailVH holder, int position) {
            holder.itemView.setTag(position);
            QcResponseSaleDetail.History bean = datas.get(position);
            Date date = DateUtils.formatDateFromServer(bean.created_at);
            String now = DateUtils.Date2MMDD(date);

            if (position == 0 || !now.equalsIgnoreCase(
                DateUtils.Date2MMDD(DateUtils.formatDateFromServer(datas.get(position - 1).created_at)))) {
                holder.itemStatementDetailHeaderdivier.setVisibility(View.VISIBLE);
                holder.itemStatementDetailDay.setVisibility(View.VISIBLE);
                holder.itemStatementDetailMonth.setVisibility(View.VISIBLE);
            } else {
                holder.itemStatementDetailHeaderdivier.setVisibility(View.INVISIBLE);
                holder.itemStatementDetailDay.setVisibility(View.INVISIBLE);
                holder.itemStatementDetailMonth.setVisibility(View.INVISIBLE);
            }
            holder.itemStatementDetailName.setText(bean.card.name + " (" + bean.card.id + ")");
            //            holder.itemStatementTimeShop.setText("销售: "+  bean.saler==null?"无销售":bean.saler.username);
            String sa = "销售: ";
            if (bean.saler == null || bean.saler.username == null) {
                holder.itemStatementTimeShop.setText(sa + "无销售");
            } else {
                holder.itemStatementTimeShop.setText(sa + bean.saler.username);
            }

            holder.itemStatementDetailDay.setText(now.substring(3, 5));
            holder.itemStatementDetailMonth.setText(now.substring(0, 2) + "月");

            if (position == getItemCount() - 1) {
                holder.itemStatementDetailBottomdivier.setVisibility(View.VISIBLE);
            } else {
                holder.itemStatementDetailBottomdivier.setVisibility(View.GONE);
            }

            switch (bean.type) {
                case Configs.TRADE_CHARGE:
                    holder.tradeType.setText(getString(R.string.charge));
                    holder.tradeType.setTextColor(CompatUtils.getColor(getContext(), R.color.colorPrimary));
                    holder.tradeType.setBackgroundResource(R.drawable.ic_charge_bg);
                    holder.account.setText(getString(R.string.charge).concat(StringUtils.getFloatDot2(-bean.account))
                        .concat(StringUtils.getUnit(getContext(), bean.card.card_type)));
                    holder.payMoney.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(bean.cost)));

                    break;
                case Configs.TRADE_CHARGE_FIRST:
                    holder.tradeType.setText(R.string.new_buy_card);
                    holder.tradeType.setTextColor(CompatUtils.getColor(getContext(), R.color.text_orange));
                    holder.tradeType.setBackgroundResource(R.drawable.ic_newbuy_bg);
                    holder.account.setText(getString(R.string.charge).concat(StringUtils.getFloatDot2(-bean.account))
                        .concat(StringUtils.getUnit(getContext(), bean.card.card_type)));
                    holder.payMoney.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(bean.cost)));

                    break;
                case Configs.TRADE_REFUND:
                    holder.tradeType.setText(R.string.spend_money);
                    holder.tradeType.setTextColor(CompatUtils.getColor(getContext(), R.color.red));
                    holder.tradeType.setBackgroundResource(R.drawable.ic_refund_bg);
                    holder.account.setText(getString(R.string.refund).concat(StringUtils.getFloatDot2(bean.account))
                        .concat(StringUtils.getUnit(getContext(), bean.card.card_type)));
                    holder.payMoney.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(-bean.cost)));

                    break;
                case Configs.TRADE_PRESENT:
                    holder.tradeType.setText(R.string.present);
                    holder.tradeType.setTextColor(CompatUtils.getColor(getContext(), R.color.blue));
                    holder.tradeType.setBackgroundResource(R.drawable.ic_present_bg);
                    holder.account.setText(getString(R.string.charge).concat(StringUtils.getFloatDot2(-bean.account))
                        .concat(StringUtils.getUnit(getContext(), bean.card.card_type)));
                    holder.payMoney.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(bean.cost)));

                    break;
            }
            holder.users.setText(BusinessUtils.students2str(bean.students));
            //由于计算问题，暂时先去掉期限卡充值 天数
            holder.account.setVisibility(bean.card.card_type == Configs.CATEGORY_DATE ? View.GONE : View.VISIBLE);

            if (bean.type == Configs.TRADE_REFUND) {
                holder.payMethod.setText(R.string.return_money);
                holder.payMoney.setTextColor(CompatUtils.getColor(getContext(), R.color.red));
            } else if (bean.type == Configs.TRADE_PRESENT) {
                holder.payMethod.setText(R.string.present);
                holder.payMoney.setTextColor(CompatUtils.getColor(getContext(), R.color.colorPrimary));
            } else {
                holder.payMoney.setTextColor(CompatUtils.getColor(getContext(), R.color.colorPrimary));
                switch (bean.charge_type) {
                    case Configs.CHARGE_MODE_CASH:
                        holder.payMethod.setText(R.string.cash_pay);
                        break;
                    case Configs.CHARGE_MODE_OTHER:
                        holder.payMethod.setText(R.string.other);
                        break;
                    case Configs.CHARGE_MODE_TRANSFER:
                        holder.payMethod.setText(R.string.transit_pay);
                        break;
                    case Configs.CHARGE_MODE_WEIXIN:
                        holder.payMethod.setText(R.string.wechat_pay);
                        break;
                    case Configs.CHARGE_MODE_WEIXIN_QRCODE:
                        holder.payMethod.setText(R.string.wechat_scan);
                        break;
                    case Configs.CHARGE_MODE_CARD:
                        holder.payMethod.setText(R.string.credit_pay);
                        break;
                }
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }

        @Override public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, (int) v.getTag());
        }
    }
}
