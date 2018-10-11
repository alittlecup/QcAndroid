package cn.qingchengfit.staffkit.views.statement.detail;

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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.SigninFilter;
import cn.qingchengfit.model.responese.SigninReportDetail;
import cn.qingchengfit.model.responese.SigninReportForm;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.RxNetWorkEvent;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.staffkit.views.statement.SigninFilterActivity;
import cn.qingchengfit.staffkit.views.statement.excel.OutExcelFragment;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.SensorsUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class SigninReportFragment extends BaseFragment implements SigninReportPresenter.PresenterView {
    public static final String TAG = SigninReportFragment.class.getName();
    public static final int RESULT_FILTER = 2;

	ImageButton statementDetailLess;
	ImageButton statementDetailMore;
	TextView statementDetailTime;
	TextView statementDetailFilter;
	TextView itemStatementDetailContent;
	RecycleViewWithNoImg recyclerview;
	Button statementDetailChange;
	ViewPager viewpager;
    @Inject SigninReportPresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
	Toolbar toolbar;
	TextView toolbarTitile;
	ImageView down;

    private ReportDetailAdapter mStatementDetailAdapter;
    private List<SigninReportDetail.CheckinsBean> statementBeans = new ArrayList<>();
    private List<SigninReportDetail.CheckinsBean> mAllstatementBeans = new ArrayList<>();
    /**
     * 报表中出现的数据
     */
    private ArrayList<SigninReportDetail.CheckinsBean.CardBean> mFilterCardTpl = new ArrayList<>();//可筛选的会员卡种类
    private ArrayList<SigninReportDetail.CheckinsBean.UserBean> mFilterStudents = new ArrayList<>();//可筛选的会员
    /**
     * 报表参数
     */

    private String start;
    private String end;
    private String card_id;
    private String card_extra;//卡类型
    private int mDividerType = 0;
    private int mDividerDay = 0;
    private Calendar curCalendar;
    private String mChooseShopId;
    private Observable<RxNetWorkEvent> mNetOb;
    private ArrayList<Fragment> fs = new ArrayList<>();
    private FragmentAdapter fragmentAdapter;

    private SigninFilter mSaleFilter = new SigninFilter();//筛选参数
    private Toolbar.OnMenuItemClickListener MenuClick = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            mSaleFilter.startDay = start;
            mSaleFilter.endDay = end;
            getFragmentManager().beginTransaction()
                .replace(mCallbackActivity.getFragId(), OutExcelFragment.newInstance(mSaleFilter, mChooseShopId))
                .addToBackStack(getFragmentName())
                .commit();
            return true;
        }
    };

    public SigninReportFragment() {

    }

    public static SigninReportFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        SigninReportFragment fragment = new SigninReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SigninReportFragment newInstance(int type, String starttime, String endtime, String cardId, String card_extra,
        SigninFilter filter) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("start", starttime);
        args.putString("end", endtime);
        args.putString("card", cardId);
        args.putString("card_extra", card_extra);
        args.putParcelable("filter", filter);
        SigninReportFragment fragment = new SigninReportFragment();
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
                mDividerDay = DateUtils.interval(start, end);
                mSaleFilter = getArguments().getParcelable("filter");

                break;
            default:
                break;
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin_report_detail, container, false);
      statementDetailLess = (ImageButton) view.findViewById(R.id.statement_detail_less);
      statementDetailMore = (ImageButton) view.findViewById(R.id.statement_detail_more);
      statementDetailTime = (TextView) view.findViewById(R.id.statement_detail_time);
      statementDetailFilter = (TextView) view.findViewById(R.id.statement_detail_filter);
      itemStatementDetailContent = (TextView) view.findViewById(R.id.item_statement_time_shop);
      recyclerview = (RecycleViewWithNoImg) view.findViewById(R.id.recyclerview);
      statementDetailChange = (Button) view.findViewById(R.id.statement_detail_change);
      viewpager = (ViewPager) view.findViewById(R.id.viewpager);
      toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      toolbarTitile = (TextView) view.findViewById(R.id.toolbar_title);
      down = (ImageView) view.findViewById(R.id.down);

      view.findViewById(R.id.statement_detail_less).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickLess();
        }
      });
      view.findViewById(R.id.statement_detail_more).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onClickMore();
        }
      });
      view.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onBtnFilter();
        }
      });
      view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        delegatePresenter(presenter, this);
        initView();
        initToolbar(toolbar);
        mNetOb = RxBus.getBus().register(RxNetWorkEvent.class);
        mNetOb.subscribe(new Action1<RxNetWorkEvent>() {
            @Override public void call(RxNetWorkEvent rxNetWorkEvent) {
                if (rxNetWorkEvent.status == -1) {
                    hideLoading();
                }
            }
        });
        SensorsUtils.trackScreen(      this.getClass().getCanonicalName());
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (gymWrapper.inBrand()) {
            toolbarTitile.setText(R.string.all_gyms);
            toolbar.inflateMenu(R.menu.menu_out_excel);
            toolbar.setOnMenuItemClickListener(MenuClick);
        } else {
            toolbarTitile.setText(R.string.statement_signin);
            toolbar.inflateMenu(R.menu.menu_out_excel);
            toolbar.setOnMenuItemClickListener(MenuClick);
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                if (TextUtils.isEmpty(mChooseShopId)) {
                    toolbarTitile.setText(R.string.all_gyms);
                } else {
                    toolbarTitile.setText(IntentUtils.getIntentString(data, 0));
                }
                showLoading();
                presenter.queryReportDetail(mChooseShopId, start, end, card_id, card_extra);
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
        mStatementDetailAdapter = new ReportDetailAdapter(statementBeans);
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
        presenter.queryReportDetail(mChooseShopId, start, end, card_id, card_extra);
    }

    @Override public void onDestroyView() {
        RxBus.getBus().unregister(RxNetWorkEvent.class.getName(), mNetOb);
        presenter.unattachView();
        super.onDestroyView();
    }

 public void onClickLess() {
        changeCalendar(-1);
    }

 public void onClickMore() {
        changeCalendar(1);
    }

 public void onBtnFilter() {
        Intent toFilter = new Intent(getActivity(), SigninFilterActivity.class);
        if (TextUtils.isEmpty(mSaleFilter.startDay)) mSaleFilter.startDay = start;
        if (TextUtils.isEmpty(mSaleFilter.endDay)) mSaleFilter.endDay = end;

        toFilter.putExtra("start", start);
        toFilter.putExtra("end", end);
        toFilter.putExtra("filter", mSaleFilter);
        toFilter.putParcelableArrayListExtra("card_tpl", mFilterCardTpl);
        toFilter.putParcelableArrayListExtra("student", mFilterStudents);
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

    @Override public void onSuccess(SigninReportDetail signinDetail) {
        hideLoading();
        mAllstatementBeans.clear();
        mAllstatementBeans.addAll(signinDetail.getCheckins());

        mFilterCardTpl.clear();
        mFilterStudents.clear();
        mSaleFilter.startDay = start;
        mSaleFilter.endDay = end;
        Staff nosaler = new Staff();
        nosaler.id = "-1";
        nosaler.username = "无销售";
        for (int i = 0; i < mAllstatementBeans.size(); i++) {
            SigninReportDetail.CheckinsBean history = mAllstatementBeans.get(i);

            //可筛选数据
            if (!mFilterCardTpl.contains(history.getCard())) mFilterCardTpl.add(history.getCard());
            for (int j = 0; j < signinDetail.getCheckins().size(); j++) {//学生
                if (!mFilterStudents.contains(signinDetail.getCheckins().get(j).getUser())) {
                    mFilterStudents.add(signinDetail.getCheckins().get(j).getUser());
                }
            }
        }

        Filter();
    }

    public void Filter() {
        statementBeans.clear();

        /**
         * user_count : 99              总的签到人次
         * total_real_price : 2481.6    总的实际收入
         *
         * value_real_price : 2481.6    储值卡实际收入
         * times_real_price : 0         次卡实际收入
         *
         * time_card_count : 14         期限卡签到人数
         * times_card_count : 3         次卡签到人数
         * value_card_count:            储值卡签到人数
         */

        HashMap<String, Float> caculator = new HashMap<>();
        caculator.put("time_card_count", 0f);
        caculator.put("times_card_count", 0f);
        caculator.put("value_card_count", 0f);


        caculator.put("value_real_price", 0f);
        caculator.put("times_real_price", 0f);

        caculator.put("value_cost", 0f);
        caculator.put("times_cost", 0f);



        caculator.put("user_count", 0f);
        caculator.put("total_real_price", 0f);
        caculator.put("total_cost", 0f);

        for (int i = 0; i < mAllstatementBeans.size(); i++) {
            SigninReportDetail.CheckinsBean history = mAllstatementBeans.get(i);

            if (((mSaleFilter.status == -1)//全部状态
                || (mSaleFilter.status == 0 && (history.getStatus() == 0 || history.getStatus() == 3))//已签到
                || (mSaleFilter.status == history.getStatus()))//已签出、一撤销
                && ((mSaleFilter.card == null && (mSaleFilter.card_category == 0 || mSaleFilter.card_category == history.getCard()
                .getCard_tpl_type()))//会员卡类型
                || (mSaleFilter.card != null && mSaleFilter.card.getCard_tpl_id().equals(history.getCard().getCard_tpl_id())))//筛选会员卡种类
                && (mSaleFilter.student == null || history.getUser().getId() == mSaleFilter.student.getId())//筛选学员
                && (DateUtils.formatDateFromYYYYMMDD(mSaleFilter.startDay).getTime() <= DateUtils.formatDateFromServer(
                history.getCreated_at()).getTime()
                && DateUtils.formatDateFromServer(history.getCreated_at()).getTime() < (DateUtils.formatDateFromYYYYMMDD(mSaleFilter.endDay)
                .getTime() + DateUtils.DAY_TIME))) {
                statementBeans.add(history);

                /**
                 * 会员签到/签出状态：
                 * status:0 已签到
                 * status:1 已撤销
                 * status:2 待签到
                 * status:3 待签出
                 * status:4 已签出
                 */
                // 制表 排除已撤销的
                if (history.getStatus() != 1) {
                    switch (history.getCard().getCard_tpl_type()) {
                        case Configs.CATEGORY_VALUE://储值卡
                            caculator.put("value_card_count", caculator.get("value_card_count") + 1);//签到人次
                            caculator.put("value_real_price", caculator.get("value_real_price") + history.getReal_price());//实际收入
                            caculator.put("value_cost",
                                caculator.get("value_cost") + history.getCost());

                            caculator.put("user_count", caculator.get("user_count") + 1);//总签到人次
                            caculator.put("total_real_price", caculator.get("total_real_price") + history.getReal_price());//总实际收入
                            caculator.put("total_cost",
                                caculator.get("total_cost") + history.getCost());
                            break;
                        case Configs.CATEGORY_TIMES://次卡
                            caculator.put("times_card_count", caculator.get("times_card_count") + 1);//签到人次
                            caculator.put("times_real_price", caculator.get("times_real_price") + history.getReal_price());//实际收入
                            caculator.put("times_cost",
                                caculator.get("times_cost") + history.getCost());//实际收入

                            caculator.put("user_count", caculator.get("user_count") + 1);//总签到人次
                            caculator.put("total_real_price", caculator.get("total_real_price") + history.getReal_price());//总实际收入
                            caculator.put("total_cost",
                                caculator.get("total_cost") + history.getCost());
                            break;
                        case Configs.CATEGORY_DATE://期限卡
                            caculator.put("time_card_count", caculator.get("time_card_count") + 1);//签到人次

                            caculator.put("user_count", caculator.get("user_count") + 1);//总签到人次
                            break;
                    }
                }
            }
        }

        List<SigninReportForm> signinReportForms = new ArrayList<>();

        SigninReportForm signinReportForm1 = new SigninReportForm(Configs.CATEGORY_VALUE,
            caculator.get("value_card_count").intValue(), caculator.get("value_real_price"),
            caculator.get("value_cost"));
        SigninReportForm signinReportForm2 = new SigninReportForm(Configs.CATEGORY_TIMES,
            caculator.get("times_card_count").intValue(), caculator.get("times_real_price"),
            caculator.get("times_cost"));
        SigninReportForm signinReportForm3 =
            new SigninReportForm(Configs.CATEGORY_DATE, caculator.get("time_card_count").intValue(),
                0, 0);
        SigninReportForm signinReportForm4 =
            new SigninReportForm(0, caculator.get("user_count").intValue(),
                caculator.get("total_real_price"), caculator.get("total_cost"));
        signinReportForms.add(signinReportForm1);
        signinReportForms.add(signinReportForm2);
        signinReportForms.add(signinReportForm3);
        signinReportForms.add(signinReportForm4);

        SigninReportFormFragment formFragment2 = SigninReportFormFragment.newInstance(signinReportForms);

        // 刷新报表
        fs.clear();

        fs.add(formFragment2);
        fragmentAdapter.notifyDataSetChanged();

        mStatementDetailAdapter.notifyDataSetChanged();
        recyclerview.setNoData(statementBeans.size() == 0);
    }

    @Override public void onFailed(String e) {
        hideLoading();
        ToastUtils.show(e);
        recyclerview.setFresh(false);
    }

    @Override public String getFragmentName() {
        return SigninReportFragment.class.getName();
    }

    /**
     * recycle view
     */
    class StatementDetailVH extends RecyclerView.ViewHolder {

	View itemStatementDetailBottomdivier;
	View itemStatementDetailHeaderdivier;
	TextView itemStatementDetailDay;
	TextView itemStatementDetailMonth;
	LinearLayout itemStatementDetailDate;

	ImageView itemStatementDetailPic;
	TextView itemStatementDetailName;
	TextView itemStatementDetailStatus;
	TextView itemStatementTimeShop;
	TextView itemCardCost;
	TextView itemCheckoutTime;

        public StatementDetailVH(View view) {
            super(view);
          itemStatementDetailBottomdivier =
              (View) view.findViewById(R.id.item_statement_detail_bottomdivier);
          itemStatementDetailHeaderdivier =
              (View) view.findViewById(R.id.item_statement_detail_headerdivier);
          itemStatementDetailDay = (TextView) view.findViewById(R.id.item_statement_detail_day);
          itemStatementDetailMonth = (TextView) view.findViewById(R.id.item_statement_detail_month);
          itemStatementDetailDate = (LinearLayout) view.findViewById(R.id.item_statement_detail_date);
          itemStatementDetailPic = (ImageView) view.findViewById(R.id.item_statement_detail_pic);
          itemStatementDetailName = (TextView) view.findViewById(R.id.item_statement_detail_name);
          itemStatementDetailStatus = (TextView) view.findViewById(R.id.item_statement_detail_status);
          itemStatementTimeShop = (TextView) view.findViewById(R.id.item_statement_time_shop);
          itemCardCost = (TextView) view.findViewById(R.id.item_card_cost);
          itemCheckoutTime = (TextView) view.findViewById(R.id.item_checkout_time);

        }
    }

    class ReportDetailAdapter extends RecyclerView.Adapter<StatementDetailVH> {

        private List<SigninReportDetail.CheckinsBean> datas;

        public ReportDetailAdapter(List<SigninReportDetail.CheckinsBean> data) {
            this.datas = data;
        }

        @Override public StatementDetailVH onCreateViewHolder(ViewGroup parent, int viewType) {
            StatementDetailVH holder =
                new StatementDetailVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signin_report_detail, parent, false));
            return holder;
        }

        @Override public void onBindViewHolder(StatementDetailVH holder, int position) {
            holder.itemView.setTag(position);
            SigninReportDetail.CheckinsBean bean = datas.get(position);
            Date date = DateUtils.formatDateFromServer(bean.getCreated_at());
            String now = DateUtils.Date2MMDD(date);

            if (position == 0 || !now.equalsIgnoreCase(
                DateUtils.Date2MMDD(DateUtils.formatDateFromServer(datas.get(position - 1).getCreated_at())))) {
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
            } else {
                holder.itemStatementDetailBottomdivier.setVisibility(View.GONE);
            }

            holder.itemStatementDetailName.setText(bean.getUser().getUsername());
            holder.itemStatementTimeShop.setText(DateUtils.getTimeHHMM(date) + "  " + bean.getShop().getName());
            // 日期
            holder.itemStatementDetailDay.setText(now.substring(3, 5));
            holder.itemStatementDetailMonth.setText(now.substring(0, 2) + "月");

            String cardCost = "";
            if (bean.getCard().getCard_tpl_type() == Configs.CATEGORY_VALUE) {
                cardCost =
                    TextUtils.concat(bean.getCard().getName(), "  ", String.valueOf(bean.getCost()),
                        "元", "  实收：", CmStringUtils.getMoneyStr(bean.getReal_price()), "元")
                        .toString();
            } else if (bean.getCard().getCard_tpl_type() == Configs.CATEGORY_TIMES) {
                cardCost =
                    TextUtils.concat(bean.getCard().getName(), "  ", String.valueOf(bean.getCost()),
                        "次", "  实收：", CmStringUtils.getMoneyStr(bean.getReal_price()), "元")
                        .toString();
            } else {
                cardCost = TextUtils.concat(bean.getCard().getName(), "  实收：",
                    CmStringUtils.getMoneyStr(bean.getReal_price()), "元").toString();
            }
            holder.itemCardCost.setText(cardCost);

            String status = "";
            switch (bean.getStatus()) {
                case 0://已签到
                case 3://暂未签出
                    status = "暂未签出";
                    break;
                case 1://已撤销
                    status = "已撤销";
                    break;
                case 4://已签出
                    status = "已签出";
                    break;
            }
            holder.itemStatementDetailStatus.setText(status);
            holder.itemCheckoutTime.setText("签出时间: " + (TextUtils.isEmpty(bean.getCheckout_at()) ? ""
                : DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(bean.getCheckout_at()))));
            if (!TextUtils.isEmpty(bean.getUser().getCheckin_avatar())) {
                Glide.with(getContext())
                    .load(PhotoUtils.getSmall(bean.getUser().getCheckin_avatar()))
                    .asBitmap()
                    .into(holder.itemStatementDetailPic);
            } else {
                Glide.with(getContext()).load(R.drawable.img_default_checkinphoto).asBitmap().into(holder.itemStatementDetailPic);
            }
        }

        @Override public int getItemCount() {
            return datas.size();
        }
    }
}
