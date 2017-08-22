package com.qingchengfit.fitcoach.fragment.statement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.StudentBean;
import com.qingchengfit.fitcoach.fragment.statement.model.CardTpl;
import com.qingchengfit.fitcoach.fragment.statement.model.QcResponseSaleDetail;
import com.qingchengfit.fitcoach.fragment.statement.model.SaleFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/6/30 2016.
 */
public class SaleFilterActivity extends BaseActivity {
    public static final String FILTER = "filter";
    public static final String CARD_TYPE = "card_type";
    public static final String TRADE_TYPE = "card_type";
    public static final String SALER = "saler";
    public static final String PAY_TYPE = "pay_type";
    public static final String STUDENT = "student";
    public static final String START = "start";
    public static final String END = "end";

    @BindView(R.id.trade_type) CommonInputView tradeType;
    @BindView(R.id.pay_method) CommonInputView payMethod;
    @BindView(R.id.student) CommonInputView student;
    @BindView(R.id.custom_statment_generate) Button customStatmentGenerate;
    @BindView(R.id.rootview) LinearLayout rootview;
    @BindView(R.id.start_day) CommonInputView startDay;
    @BindView(R.id.end_day) CommonInputView endDay;
    @BindView(R.id.card_type) CommonInputView cardType;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private SaleFilter mSaleFilter;
    private String mStart;
    private String mEnd;

    /**
     * 报表中出现的数据
     */
    private ArrayList<QcResponseSaleDetail.Card> mFilterCardTpl = new ArrayList<>();//可筛选的会员卡种类
    private ArrayList<Integer> mFilterTradeType = new ArrayList<>();//可筛选的交易类型
    private ArrayList<Staff> mFilterSalers = new ArrayList<>();//可筛选的销售
    private ArrayList<Integer> mFilterPayMethod = new ArrayList<>();//可筛选的支付类型
    private ArrayList<StudentBean> mFilterStudents = new ArrayList<>();//可筛选的会员
    private Observable<CardTpl> obCardtpl;
    private Observable<CardTypeEvent> obCardType;
    private TimeDialogWindow pwTime;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filter_sale);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_cross_blace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        mSaleFilter = getIntent().getParcelableExtra("filter");

        mFilterCardTpl = getIntent().getParcelableArrayListExtra("card_tpl");
        mFilterSalers = getIntent().getParcelableArrayListExtra("sale");
        mFilterStudents = getIntent().getParcelableArrayListExtra("student");
        mFilterPayMethod = getIntent().getIntegerArrayListExtra("pay");
        mFilterTradeType = getIntent().getIntegerArrayListExtra("card_type");
        mStart = getIntent().getStringExtra("start");
        mEnd = getIntent().getStringExtra("end");
        /**
         *  填充筛选条件
         */
        if (mSaleFilter != null) {
            if (!TextUtils.isEmpty(mSaleFilter.startDay)) {
                startDay.setContent(mSaleFilter.startDay);
            }

            if (!TextUtils.isEmpty(mSaleFilter.endDay)) endDay.setContent(mSaleFilter.endDay);
            if (mSaleFilter.card != null) {
                cardType.setContent(mSaleFilter.card.name);
            } else {
                if (mSaleFilter.card_category == Configs.CATEGORY_DATE) {
                    cardType.setContent(getString(R.string.cardtype_date));
                } else if (mSaleFilter.card_category == Configs.CATEGORY_VALUE) {
                    cardType.setContent(getString(R.string.cardtype_value));
                } else if (mSaleFilter.card_category == Configs.CATEGORY_TIMES) cardType.setContent(getString(R.string.cardtype_times));
            }
            if (mSaleFilter.student != null) student.setContent(mSaleFilter.student.username);
            if (mSaleFilter.tradeType > 0) {
                tradeType.setContent(BusinessUtils.getTradeType(this, mSaleFilter.tradeType));
            }
            if (mSaleFilter.payMethod > 0) {
                payMethod.setContent(BusinessUtils.getPayMethod(this, mSaleFilter.payMethod));
            }
        }

        //        startDay.setContent(getIntent().getStringExtra(START));
        //        endDay.setContent(getIntent().getStringExtra(END));

        obCardtpl = RxBus.getBus().register(CardTpl.class);
        obCardtpl.subscribe(new Action1<CardTpl>() {
            @Override public void call(CardTpl card_tpl) {
                cardType.setContent(card_tpl.getName());
                QcResponseSaleDetail.Card c = new QcResponseSaleDetail.Card();
                c.card_tpl_id = card_tpl.getId();
                c.name = card_tpl.getName();
                mSaleFilter.card = c;

                mSaleFilter.card_category = -1;
            }
        });
        obCardType = RxBus.getBus().register(CardTypeEvent.class);
        obCardType.subscribe(new Action1<CardTypeEvent>() {
            @Override public void call(CardTypeEvent cardTypeEvent) {
                mSaleFilter.card_category = cardTypeEvent.cardtype;
                mSaleFilter.card = null;
                switch (cardTypeEvent.cardtype) {
                    case 0:
                        cardType.setContent(getString(R.string.cardtype_all));
                        break;
                    case Configs.CATEGORY_VALUE:
                        cardType.setContent(getString(R.string.all_cardtype_value));
                        break;
                    case Configs.CATEGORY_TIMES:
                        cardType.setContent(getString(R.string.all_cardtype_times));
                        break;
                    case Configs.CATEGORY_DATE:
                        cardType.setContent(getString(R.string.all_cardtype_date));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        RxBus.getBus().unregister(CardTpl.class.getName(), obCardtpl);
        RxBus.getBus().unregister(CardTypeEvent.class.getName(), obCardType);
    }

    @OnClick({
        R.id.start_day, R.id.end_day, R.id.card_type, R.id.trade_type, R.id.pay_method, R.id.student, R.id.custom_statment_generate
    }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_day:
                if (pwTime == null) pwTime = new TimeDialogWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(1900, 2100);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        try {

                            if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(mStart).getTime()
                                || date.getTime() > DateUtils.formatDateFromYYYYMMDD(mEnd).getTime()) {
                                ToastUtils.show(R.drawable.ic_share_fail, "必须在范围内筛选");
                            } else {
                                pwTime.dismiss();
                                startDay.setContent(DateUtils.Date2YYYYMMDD(date));
                                mSaleFilter.startDay = DateUtils.Date2YYYYMMDD(date);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Date date = new Date();
                try {
                    date = DateUtils.formatDateFromYYYYMMDD(startDay.getContent());
                } catch (Exception e) {
                }
                pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);
                break;
            case R.id.end_day:
                if (pwTime == null) pwTime = new TimeDialogWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
                pwTime.setRange(1900, 2100);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        try {

                            if (date.getTime() < DateUtils.formatDateFromYYYYMMDD(mStart).getTime()
                                || date.getTime() > DateUtils.formatDateFromYYYYMMDD(mEnd).getTime()) {
                                ToastUtils.show(R.drawable.ic_share_fail, "必须在范围内筛选");
                            } else {
                                pwTime.dismiss();
                                endDay.setContent(DateUtils.Date2YYYYMMDD(date));
                                mSaleFilter.endDay = DateUtils.Date2YYYYMMDD(date);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Date date2 = new Date();
                try {
                    date2 = DateUtils.formatDateFromYYYYMMDD(startDay.getContent());
                } catch (Exception e) {
                }
                pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date2);
                break;
            case R.id.card_type:

                CardTypeChooseDialogFragment cardTypeChooseDialog =
                    CardTypeChooseDialogFragment.newInstance(BusinessUtils.card2Card_tpl(mFilterCardTpl));
                cardTypeChooseDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.trade_type://交易类型
                final List<String> trades = new ArrayList<>();
                trades.add(getString(R.string.no_limit));
                trades.addAll(BusinessUtils.getTradeTypes(this, mFilterTradeType));
                new DialogList(this).list(trades, new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tradeType.setContent(trades.get(position));
                        if (position == 0) {
                            mSaleFilter.tradeType = 0;
                        } else {
                            mSaleFilter.tradeType = mFilterTradeType.get(position - 1);
                        }
                    }
                }).title(getString(R.string.trade_type)).show();
                break;
            case R.id.pay_method:

                final List<String> paystringList = new ArrayList<>();
                paystringList.add(getString(R.string.no_limit));
                paystringList.addAll(BusinessUtils.getPayMethods(this, mFilterPayMethod));
                new DialogList(this).list(paystringList, new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        payMethod.setContent(paystringList.get(position));
                        if (position == 0) {
                            mSaleFilter.payMethod = 0;
                        } else {
                            mSaleFilter.payMethod = mFilterPayMethod.get(position - 1);
                        }
                    }
                }).title(getString(R.string.pay_method)).show();
                break;
            case R.id.student:
                final List<String> stus = new ArrayList<>();
                stus.add(getString(R.string.all_students));
                stus.addAll(BusinessUtils.students2strs(mFilterStudents));

                new DialogList(this).list(stus, new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        student.setContent(stus.get(position));
                        if (position == 0) {
                            mSaleFilter.student = null;
                        } else {
                            mSaleFilter.student = mFilterStudents.get(position - 1);
                        }
                    }
                }).title(getString(R.string.choose_student)).show();
                break;
            case R.id.custom_statment_generate:
                if (DateUtils.formatDateFromYYYYMMDD(startDay.getContent()).getTime() > DateUtils.formatDateFromYYYYMMDD(
                    endDay.getContent()).getTime()) {
                    ToastUtils.show("开始时间必须小于结束时间");
                    return;
                }

                Intent result = new Intent();
                result.putExtra("filter", mSaleFilter);
                setResult(Activity.RESULT_OK, result);
                this.finish();
                break;
        }
    }
}
