package cn.qingchengfit.staffkit.views.statement;

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
import cn.qingchengfit.model.responese.SigninFilter;
import cn.qingchengfit.model.responese.SigninReportDetail;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.CardTypeEvent;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.statement.filter.CardTypeChooseDialogFragment;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;

/****
 *
 */
public class SigninFilterActivity extends BaseActivity {
    public static final String FILTER = "filter";
    public static final String CARD_TYPE = "card_type";
    public static final String TRADE_TYPE = "card_type";
    public static final String SALER = "saler";
    public static final String PAY_TYPE = "pay_type";
    public static final String STUDENT = "student";
    public static final String START = "start";
    public static final String END = "end";

    @BindView(R.id.signin_type) CommonInputView signinType;
    @BindView(R.id.student) CommonInputView student;
    @BindView(R.id.custom_statment_generate) Button customStatmentGenerate;
    @BindView(R.id.rootview) LinearLayout rootview;
    @BindView(R.id.start_day) CommonInputView startDay;
    @BindView(R.id.end_day) CommonInputView endDay;
    @BindView(R.id.card_type) CommonInputView cardType;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private SigninFilter mSaleFilter;
    private String mStart;
    private String mEnd;

    /**
     * 报表中出现的数据
     */
    private ArrayList<SigninReportDetail.CheckinsBean.CardBean> mFilterCardTpl = new ArrayList<>();//可筛选的会员卡种类
    private ArrayList<SigninReportDetail.CheckinsBean.UserBean> mFilterStudents = new ArrayList<>();//可筛选的会员
    private Observable<CardTpl> obCardtpl;
    private Observable<CardTypeEvent> obCardType;
    private TimeDialogWindow pwTime;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filter_signin);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_cross_blace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        mSaleFilter = getIntent().getParcelableExtra("filter");

        mFilterCardTpl = getIntent().getParcelableArrayListExtra("card_tpl");
        mFilterStudents = getIntent().getParcelableArrayListExtra("student");
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
                cardType.setContent(mSaleFilter.card.getName());
            } else {
                if (mSaleFilter.card_category == Configs.CATEGORY_DATE) {
                    cardType.setContent(getString(R.string.cardtype_date));
                } else if (mSaleFilter.card_category == Configs.CATEGORY_VALUE) {
                    cardType.setContent(getString(R.string.cardtype_value));
                } else if (mSaleFilter.card_category == Configs.CATEGORY_TIMES) cardType.setContent(getString(R.string.cardtype_times));
            }

            if (mSaleFilter.student != null) {
                student.setContent(mSaleFilter.student.getUsername());
            }
            switch (mSaleFilter.status) {
                case -1:
                    signinType.setContent("不限");
                    break;
                case 0:
                    signinType.setContent("已签到");
                    break;
                case 1:
                    signinType.setContent("已撤销");
                    break;
                case 4:
                    signinType.setContent("已签出");
                    break;
            }
        }

        obCardtpl = RxBus.getBus().register(CardTpl.class);
        obCardtpl.subscribe(new Action1<CardTpl>() {
            @Override public void call(CardTpl card_tpl) {
                cardType.setContent(card_tpl.getName());
                SigninReportDetail.CheckinsBean.CardBean c = new SigninReportDetail.CheckinsBean.CardBean();
                c.setCard_tpl_id(card_tpl.getId());
                c.setName(card_tpl.getName());
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

    @OnClick({ R.id.start_day, R.id.end_day, R.id.card_type, R.id.signin_type, R.id.student, R.id.custom_statment_generate })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_day:
              if (pwTime == null) {
                pwTime = new TimeDialogWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
              }
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
              if (pwTime == null) {
                pwTime = new TimeDialogWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
              }
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

                CardTypeChooseDialogFragment cardTypeChooseDialogFragment =
                    CardTypeChooseDialogFragment.newInstance(BusinessUtils.card2Card_tpl2(mFilterCardTpl));
                cardTypeChooseDialogFragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.signin_type://签到状态
                final List<String> trades = new ArrayList<>();
                trades.add(getString(R.string.no_limit));
                trades.add("已签到");
                trades.add("已签出");
                trades.add("已撤销");
                new DialogList(this).list(trades, new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        signinType.setContent(trades.get(position));
                        switch (position) {
                            case 0:
                                mSaleFilter.status = -1;
                                break;
                            case 1:
                                mSaleFilter.status = 0;
                                break;
                            case 2:
                                mSaleFilter.status = 4;
                                break;
                            case 3:
                                mSaleFilter.status = 1;
                                break;
                        }
                    }
                }).title("状态").show();
                break;
            case R.id.student:
                final List<String> stus = new ArrayList<>();
                stus.add(getString(R.string.all_students));
                stus.addAll(BusinessUtils.userBean2strs(mFilterStudents));

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
