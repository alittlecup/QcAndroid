package cn.qingchengfit.staffkit.views.statement.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.QcResponseSaleDetail;
import cn.qingchengfit.model.responese.SaleFilter;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.CardTypeEvent;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.statement.detail.SaleDetailFragment;
import cn.qingchengfit.staffkit.views.statement.filter.CardTypeChooseDialogFragment;
import cn.qingchengfit.staffkit.views.statement.filter.SalerChooseDialogFragment;
import cn.qingchengfit.utils.BusinessUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomSaleFragment extends BaseFragment implements CustomSaleView {

    public static final String TAG = CustomSaleFragment.class.getName();
    @BindView(R.id.custom_statment_start) CommonInputView customStatmentStart;
    @BindView(R.id.custom_statment_end) CommonInputView customStatmentEnd;
    @BindView(R.id.rootview) LinearLayout rootview;

    @Inject CustomSalePresenter presenter;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @BindView(R.id.custom_statment_cardtype) CommonInputView customStatmentCardtype;
    @BindView(R.id.trade_type) CommonInputView tradeType;
    @BindView(R.id.sale_menber) CommonInputView saleMenber;
    @BindView(R.id.pay_method) CommonInputView payMethod;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    private Calendar date;

    private TimeDialogWindow pwTime;
    private String mChooseShopId;
    private String startTime, endTime;
    private String mShopStr;
    private String card_extra;
    private String card_id;
    private String mSeller_id;
    private int mPaytype;
    private int mTradetype;

    private SaleFilter mSaleFilter = new SaleFilter();

    public CustomSaleFragment() {

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = Calendar.getInstance();
        //获取用户拥有系统信息
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_sale, container, false);
        unbinder = ButterKnife.bind(this, view);
        //initDI
        //
        initView();
        initToolbar(toolbar);
        delegatePresenter(presenter, this);

        RxBusAdd(CardTpl.class).subscribe(new Action1<CardTpl>() {
            @Override public void call(CardTpl card_tpl) {
                customStatmentCardtype.setContent(card_tpl.getName());
                card_id = card_tpl.getId();
                QcResponseSaleDetail.Card card = new QcResponseSaleDetail.Card();
                card.name = card_tpl.getName();
                card.card_tpl_id = card_tpl.getId();
                mSaleFilter.card = card;
            }
        });
        RxBusAdd(CardTypeEvent.class).subscribe(new Action1<CardTypeEvent>() {
            @Override public void call(CardTypeEvent cardTypeEvent) {

                mSaleFilter.card_category = cardTypeEvent.cardtype;
                mSaleFilter.card = null;
                card_id = null;
                switch (cardTypeEvent.cardtype) {
                    case 0:
                        customStatmentCardtype.setContent(getString(R.string.cardtype_all));
                        card_extra = null;

                        break;
                    case Configs.CATEGORY_VALUE:
                        customStatmentCardtype.setContent(getString(R.string.all_cardtype_value));
                        card_extra = "all_value";
                        break;
                    case Configs.CATEGORY_TIMES:
                        customStatmentCardtype.setContent(getString(R.string.all_cardtype_times));
                        card_extra = "all_times";
                        break;
                    case Configs.CATEGORY_DATE:
                        customStatmentCardtype.setContent(getString(R.string.all_cardtype_date));
                        card_extra = "all_time";
                        break;
                    default:
                        break;
                }
            }
        });
        RxBusAdd(StudentBean.class).subscribe(new Action1<StudentBean>() {
            @Override public void call(StudentBean studentBean) {
                if (studentBean.username != null) {
                    saleMenber.setContent(studentBean.username);
                    mSeller_id = studentBean.id;
                    Staff saler = new Staff();
                    saler.username = studentBean.username;
                    saler.id = studentBean.id;
                    mSaleFilter.saler = saler;
                } else {
                    saleMenber.setContent(getString(R.string.no_limit));
                    mSeller_id = null;
                    mSaleFilter.saler = null;
                }
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initView() {
        //mCallbackActivity.setToolbar("自定义销售报表", false, null, 0, null);
        if (TextUtils.isEmpty(startTime)) {
            customStatmentStart.setContent(DateUtils.Date2YYYYMMDD(new Date()));
            startTime = customStatmentStart.getContent();
        } else {
            customStatmentStart.setContent(startTime);
        }
        if (TextUtils.isEmpty(endTime)) {
            customStatmentEnd.setContent(DateUtils.Date2YYYYMMDD(new Date()));
            endTime = customStatmentEnd.getContent();
        } else {
            customStatmentEnd.setContent(endTime);
        }
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("自定义销售报表");
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                CardTpl card_tpl = data.getParcelableExtra(Configs.EXTRA_CARD_TYPE);
                presenter.selectCard(card_tpl.getId());
            } else if (requestCode == 2) {
                mChooseShopId = IntentUtils.getIntentString(data, 1);
                if (TextUtils.isEmpty(mChooseShopId)) {
                    mCallbackActivity.setToolbar(getString(R.string.all_gyms), true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            ChooseGymActivity.start(CustomSaleFragment.this, 2, getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, 0, null);
                } else {
                    mShopStr = IntentUtils.getIntentString(data, 0);
                    mCallbackActivity.setToolbar(IntentUtils.getIntentString(data, 0), true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            ChooseGymActivity.start(CustomSaleFragment.this, 2, getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, 0, null);
                }
                if (TextUtils.isEmpty(mChooseShopId)) {
                    presenter.selectShopid("0");
                } else {
                    presenter.selectShopid(mChooseShopId);
                }
            }
        }
    }

    @OnClick(R.id.custom_statment_end) public void onClickEnd() {
      if (pwTime == null) {
        pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
      }
        pwTime.setRange(1900, 2100);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                try {
                    pwTime.dismiss();
                    customStatmentEnd.setContent(DateUtils.Date2YYYYMMDD(date));
                    presenter.selectEndTime(customStatmentEnd.getContent());
                    endTime = customStatmentEnd.getContent();
                    mSaleFilter.endDay = endTime;
                    //                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Date date = new Date();
        try {
            date = DateUtils.formatDateFromYYYYMMDD(customStatmentEnd.getContent());
        } catch (Exception e) {

        }
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);
    }

    @OnClick(R.id.custom_statment_start) public void onClickStart() {
      if (pwTime == null) {
        pwTime = new TimeDialogWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
      }
        pwTime.setRange(1900, 2100);
        pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
            @Override public void onTimeSelect(Date date) {
                try {
                    pwTime.dismiss();
                    customStatmentStart.setContent(DateUtils.Date2YYYYMMDD(date));
                    startTime = customStatmentStart.getContent();
                    presenter.selectStartTime(customStatmentStart.getContent());
                    mSaleFilter.startDay = startTime;
                    //                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Date date = new Date();
        try {
            date = DateUtils.formatDateFromYYYYMMDD(customStatmentStart.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        pwTime.showAtLocation(rootview, Gravity.BOTTOM, 0, 0, date);
    }

    @OnClick(R.id.custom_statment_generate) public void onClickGenerate() {
        if (DateUtils.formatDateFromYYYYMMDD(customStatmentStart.getContent()).getTime() > DateUtils.formatDateFromYYYYMMDD(
            customStatmentEnd.getContent()).getTime()) {
            ToastUtils.show("开始时间不能小于结束时间");
            return;
        }
        if (DateUtils.formatDateFromYYYYMMDD(customStatmentEnd.getContent()).getTime() - DateUtils.formatDateFromYYYYMMDD(
            customStatmentStart.getContent()).getTime() > DateUtils.MONTH_TIME) {
            ToastUtils.show(R.drawable.ic_share_fail, "自定义时间不能超过一个月");
            return;
        }
        //        presenter.completedCustom(getFragmentManager(), mCallbackActivity.getFragId());
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(),
                SaleDetailFragment.newInstance(3, startTime, endTime, card_id, card_extra, mSeller_id, mTradetype, mPaytype, mSaleFilter))
            .addToBackStack(getFragmentName())
            .commit();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @Override public void onGetCards(List<CardTpl> cardtpls) {
        CardTypeChooseDialogFragment dialog = CardTypeChooseDialogFragment.newInstance(cardtpls);
        dialog.show(getFragmentManager(), "");
    }

    @Override public String getFragmentName() {
        return CustomSaleFragment.class.getName();
    }

    @OnClick({ R.id.trade_type, R.id.sale_menber, R.id.pay_method, R.id.custom_statment_cardtype }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trade_type:
                List<String> tm = new ArrayList<>();
                tm.add(getString(R.string.no_limit));
                tm.addAll(Arrays.asList(getResources().getStringArray(R.array.trade_types)));
                new DialogList(getContext()).list(tm, new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            tradeType.setContent(getResources().getStringArray(R.array.trade_types)[position - 1]);
                            mTradetype = BusinessUtils.getTradeTypeServer(position - 1);
                        } else {
                            tradeType.setContent(getString(R.string.no_limit));
                            mTradetype = 0;
                        }
                        mSaleFilter.tradeType = mTradetype;
                    }
                }).title(getString(R.string.trade_type)).show();
                break;
            case R.id.sale_menber:
                SalerChooseDialogFragment salerChooseDialogFragment = new SalerChooseDialogFragment();
                salerChooseDialogFragment.show(getFragmentManager(), "");
                break;
            case R.id.pay_method:
                List<String> pm = new ArrayList<>();
                pm.add(getString(R.string.no_limit));
                pm.addAll(Arrays.asList(getResources().getStringArray(R.array.pay_method)));

                new DialogList(getContext()).list(pm, new AdapterView.OnItemClickListener() {
                    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (position > 0) {
                            mPaytype = BusinessUtils.getPayMethodServer(position - 1);
                            payMethod.setContent(getResources().getStringArray(R.array.pay_method)[position - 1]);
                        } else {
                            mPaytype = 0;
                            payMethod.setContent(getString(R.string.no_limit));
                        }
                        mSaleFilter.payMethod = mPaytype;
                    }
                }).title(getString(R.string.pay_method)).show();
                break;
            case R.id.custom_statment_cardtype:
                presenter.queryCardTpl();
                break;
        }
    }
}
