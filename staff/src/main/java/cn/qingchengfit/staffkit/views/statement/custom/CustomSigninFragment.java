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
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.SigninFilter;
import cn.qingchengfit.model.responese.SigninReportDetail;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rxbus.event.CardTypeEvent;
import cn.qingchengfit.staffkit.views.ChooseGymActivity;
import cn.qingchengfit.staffkit.views.custom.DialogList;
import cn.qingchengfit.staffkit.views.statement.detail.SigninReportFragment;
import cn.qingchengfit.staffkit.views.statement.filter.CardTypeChooseDialogFragment;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomSigninFragment extends BaseFragment implements CustomSigninPresenter.PresenterView {

    public static final String TAG = CustomSigninFragment.class.getName();
    @BindView(R.id.custom_statment_start) CommonInputView customStatmentStart;
    @BindView(R.id.custom_statment_end) CommonInputView customStatmentEnd;
    @BindView(R.id.custom_statment_cardtype) CommonInputView customStatmentCardtype;
    @BindView(R.id.custom_statment_signin_type) CommonInputView customStatmentSignintype;
    @BindView(R.id.rootview) LinearLayout rootview;

    @Inject CustomSigninPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;

    private Calendar date;

    private TimeDialogWindow pwTime;
    private String mChooseShopId;
    private String startTime, endTime;
    private String mShopStr;
    private String card_extra;
    private String card_id;

    private SigninFilter mSaleFilter = new SigninFilter();

    public CustomSigninFragment() {

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = Calendar.getInstance();
        //获取用户拥有系统信息
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_signin, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        initView();

        RxBusAdd(CardTpl.class).subscribe(new Action1<CardTpl>() {
            @Override public void call(CardTpl card_tpl) {
                customStatmentCardtype.setContent(card_tpl.getName());
                card_id = card_tpl.getId();
                SigninReportDetail.CheckinsBean.CardBean card = new SigninReportDetail.CheckinsBean.CardBean();
                card.setName(card_tpl.getName());
                card.setCard_tpl_type(Integer.valueOf(card_tpl.getId()));
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

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarTitile.setText("自定义签到报表");
    }

    private void initView() {
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
                            ChooseGymActivity.start(CustomSigninFragment.this, 2, getString(R.string.choose_gym), mChooseShopId);
                        }
                    }, 0, null);
                } else {
                    mShopStr = IntentUtils.getIntentString(data, 0);
                    mCallbackActivity.setToolbar(IntentUtils.getIntentString(data, 0), true, new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            ChooseGymActivity.start(CustomSigninFragment.this, 2, getString(R.string.choose_gym), mChooseShopId);
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

    /**
     * 开始时间点击
     *
     * @param view view
     */
    @OnClick(R.id.custom_statment_start) public void onClickStart(View view) {
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

    /**
     * 结束时间点击
     */
    @OnClick(R.id.custom_statment_end) public void onClickEnd(View view) {
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

    /**
     * 卡种类点击
     *
     * @param view view
     */
    @OnClick({ R.id.custom_statment_cardtype }) public void onClick(View view) {
        presenter.queryCardTpl();
    }

    /**
     * 状态点击
     *
     * @param view view
     */
    @OnClick({ R.id.custom_statment_signin_type }) public void onClickStatus(View view) {
        final List<String> trades = new ArrayList<>();
        trades.add(getString(R.string.no_limit));
        trades.add("已签到");
        trades.add("已签出");
        trades.add("已撤销");
        new DialogList(getActivity()).list(trades, new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customStatmentSignintype.setContent(trades.get(position));
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
    }

    /**
     * 生成报表按钮点击
     */
    @OnClick(R.id.custom_statment_generate) public void onClickGenerate(View view) {
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
        getFragmentManager().beginTransaction()
            .add(mCallbackActivity.getFragId(), SigninReportFragment.newInstance(3, startTime, endTime, card_id, card_extra, mSaleFilter))
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
        return CustomSigninFragment.class.getName();
    }
}
