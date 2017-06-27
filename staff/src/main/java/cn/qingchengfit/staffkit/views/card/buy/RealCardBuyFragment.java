package cn.qingchengfit.staffkit.views.card.buy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.inject.model.CardTypeWrapper;
import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.rxbus.event.BuyCardNextEvent;
import cn.qingchengfit.staffkit.views.adapter.StandardAdapter;
import cn.qingchengfit.staffkit.views.card.BuyCardActivity;
import cn.qingchengfit.staffkit.views.card.charge.RealValueCardChargeView;
import cn.qingchengfit.staffkit.views.custom.EqualSpaceItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/18 2016.
 */
public class RealCardBuyFragment extends BaseFragment implements RealValueCardChargeView {

    @BindView(R.id.cardname) TextView cardname;
    @BindView(R.id.card_period) TextView cardPeriod;
    @BindView(R.id.students) TextView students;
    @BindView(R.id.card) CardView card;
    @BindView(R.id.recycleview_standard) RecyclerView recycleviewStandard;
    @BindView(R.id.charge_sum) CommonInputView chargeSum;
    @BindView(R.id.imcome_sun) CommonInputView imcomeSun;
    @BindView(R.id.switch_name) TextView name;
    @BindView(R.id.switcher) SwitchCompat switcher;
    @BindView(R.id.switcher_layout) RelativeLayout switcherLayout;
    @BindView(R.id.starttime) CommonInputView starttime;
    @BindView(R.id.endtime) CommonInputView endtime;
    @BindView(R.id.extra_period) LinearLayout extraPeriod;
    @BindView(R.id.datecard_starttime) CommonInputView datecardStarttime;
    @BindView(R.id.datecard_endtime) CommonInputView datecardEndtime;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.real_card_no) CommonInputView realCardNo;
    @BindView(R.id.card_bg) LinearLayout cardBg;

    List<CardStandard> datas = new ArrayList<>();

    @Inject RealValueCardBuyPresenter presenter;
    @Inject CardTypeWrapper card_tpl;

    CreateCardBody createCardBody;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
    private StandardAdapter adapter;
    private TimeDialogWindow pwTime;
    //private String str_stu;

    private String mPostionStr = "";
    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
            if (adapter.getItemCount() == 0) {
                return true;
            }
            if (!adapter.isChosen()) {
                showAlert("请选择购卡规则");
                return true;
            }

            //规格选择其他
            if (adapter.getChooseItem() == adapter.getItemCount() - 1) {

                if (TextUtils.isEmpty(imcomeSun.getContent())) {
                    ToastUtils.show("请填写实收金额");
                    return true;
                }
                createCardBody.price = imcomeSun.getContent();

                switch (card_tpl.type()) {
                    case Configs.CATEGORY_VALUE:
                        if (TextUtils.isEmpty(chargeSum.getContent())) {
                            ToastUtils.show("请填写" + chargeSum.getLable());
                            return true;
                        }
                        createCardBody.account = chargeSum.getContent();
                        break;
                    case Configs.CATEGORY_TIMES:
                        if (TextUtils.isEmpty(chargeSum.getContent())) {
                            ToastUtils.show("请填写" + chargeSum.getLable());
                            return true;
                        }
                        createCardBody.times = chargeSum.getContent();
                        break;
                    case Configs.CATEGORY_DATE:
                        createCardBody.start = datecardStarttime.getContent();
                        createCardBody.end = datecardEndtime.getContent();
                        if (TextUtils.isEmpty(datecardEndtime.getContent()) || TextUtils.isEmpty(datecardStarttime.getContent())) {
                            ToastUtils.show("请填写开始日期和结束日期");
                            return true;
                        }
                        break;
                }

                //有效期
                if (switcher.isChecked()) {
                    if (TextUtils.isEmpty(starttime.getContent())) {
                        ToastUtils.show("请填写开始日期");
                        return true;
                    }
                    if (TextUtils.isEmpty(endtime.getContent())) {
                        ToastUtils.show("请填写结束日期");
                        return true;
                    }

                    createCardBody.check_valid = true;
                    createCardBody.valid_from = starttime.getContent();
                    createCardBody.valid_to = endtime.getContent();
                } else {
                    createCardBody.check_valid = false;
                }
            } else {//选择规格
                CardStandard cardStandard = datas.get(adapter.getChooseItem());
                switch (card_tpl.type()) {
                    case Configs.CATEGORY_VALUE:
                        createCardBody.account = cardStandard.getContent();
                        if (cardStandard.getLimitDay() > 0) {
                            createCardBody.check_valid = true;
                            if (TextUtils.isEmpty(starttime.getContent())) {
                                ToastUtils.show("请填写开始日期");
                                return true;
                            }

                            createCardBody.valid_from = starttime.getContent();
                            createCardBody.valid_to = DateUtils.Date2YYYYMMDD(new Date(
                                DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime()
                                    + DateUtils.DAY_TIME * cardStandard.getLimitDay()));
                        } else {
                            createCardBody.check_valid = false;
                        }
                        break;
                    case Configs.CATEGORY_TIMES:
                        createCardBody.times = cardStandard.getContent();
                        if (cardStandard.getLimitDay() > 0) {
                            createCardBody.check_valid = true;
                            if (TextUtils.isEmpty(starttime.getContent())) {
                                ToastUtils.show("请填写开始日期");
                                return true;
                            }

                            createCardBody.valid_from = starttime.getContent();
                            createCardBody.valid_to = DateUtils.Date2YYYYMMDD(new Date(
                                DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime()
                                    + DateUtils.DAY_TIME * cardStandard.getLimitDay()));
                        } else {
                            createCardBody.check_valid = false;
                        }
                        break;
                    case Configs.CATEGORY_DATE:
                        createCardBody.check_valid = true;
                        createCardBody.start = datecardStarttime.getContent();

                        Float f = (Float.parseFloat(cardStandard.getCharge()));
                        int out = f.intValue();
                        createCardBody.end = DateUtils.Date2YYYYMMDD(new Date(
                            DateUtils.formatDateFromYYYYMMDD(datecardStarttime.getContent()).getTime() + DateUtils.DAY_TIME * (out - 1)));
                        break;
                }

                createCardBody.price = cardStandard.getIncome() + "";
            }

            createCardBody.card_no = realCardNo.getContent();
            RxBus.getBus().post(new BuyCardNextEvent(2));
            return true;
        }
    };

    public static RealCardBuyFragment newInstance(String s) {
        Bundle args = new Bundle();
        args.putString("stu", s);
        RealCardBuyFragment fragment = new RealCardBuyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        if (getArguments() != null) {
        }

        if (createCardBody == null && getActivity() instanceof BuyCardActivity) {
            createCardBody = ((BuyCardActivity) getActivity()).getBuyCardBody();
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realcard_charge_value, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        adapter = new StandardAdapter(getContext(), datas, true, card_tpl.isEnable());
        recycleviewStandard.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recycleviewStandard.addItemDecoration(new EqualSpaceItemDecoration(MeasureUtils.dpToPx(8f, getResources())));
        recycleviewStandard.setAdapter(adapter);
        adapter.setListener(new OnRecycleItemClickListener() {
            @Override public void onItemClick(View v, int pos) {
                //选择充值规格
                if (!TextUtils.isEmpty(datas.get(pos).getReal_income())) {
                    int lPos = adapter.getChooseItem();
                    adapter.setChooseItem(pos);
                    adapter.notifyItemChanged(lPos);
                    adapter.notifyItemChanged(pos);
                    onInitView(datas.get(pos).getLimitDay(), pos == datas.size() - 1);
                } else {
                    if (presenter.hasEditPermission()) {
                        adapter.setChosen(true);
                        int lPos = adapter.getChooseItem();
                        adapter.setChooseItem(pos);
                        adapter.notifyItemChanged(lPos);
                        adapter.notifyItemChanged(pos);
                        onInitView(0, true);
                    } else {
                        showAlert(getString(R.string.alert_cardtype_use_other, mPostionStr));
                    }
                }
            }
        });
        switcher.setChecked(false);
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    starttime.setVisibility(View.VISIBLE);
                    endtime.setVisibility(View.VISIBLE);
                } else {
                    starttime.setVisibility(View.GONE);
                    endtime.setVisibility(View.GONE);
                }
            }
        });
        presenter.queryStardard();
        presenter.queryPositions(App.staffId, PermissionServerUtils.CARDSETTING_CAN_CHANGE);
        name.setText("设置有效期");
        realCardNo.setVisibility(View.VISIBLE);
        onCardInfo();
        return view;
    }

    @Override public void initToolbar(@NonNull Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbarLayout.setVisibility(View.VISIBLE);
        toolbarTitile.setText("选择购卡规格");
        toolbar.inflateMenu(R.menu.menu_next);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
    }

    void onInitView(int validDayCount, boolean other) {

        extraPeriod.setVisibility(View.GONE);
        datecardStarttime.setVisibility(View.GONE);
        datecardEndtime.setVisibility(View.GONE);
        chargeSum.setVisibility(View.GONE);
        imcomeSun.setVisibility(View.GONE);
        switch (card_tpl.type()) {
            case Configs.CATEGORY_VALUE:
                chargeSum.setLabel("充值金额(元)");
                //                imcomeSun.setLabel("实收金额(元)");
                if (other) {//选其他
                    chargeSum.setVisibility(View.VISIBLE);
                    imcomeSun.setVisibility(View.VISIBLE);
                    extraPeriod.setVisibility(View.VISIBLE);
                    switcherLayout.setVisibility(View.GONE);
                } else if (validDayCount > 0) {//规格有有效期
                    extraPeriod.setVisibility(View.VISIBLE);
                    switcherLayout.setVisibility(View.GONE);
                    starttime.setVisibility(View.VISIBLE);
                    endtime.setVisibility(View.GONE);
                } else {//规格无有效期
                    extraPeriod.setVisibility(View.GONE);
                }

                break;
            case Configs.CATEGORY_TIMES:

                chargeSum.setLabel("充值次数(次)");
                if (other) {//选其他
                    chargeSum.setVisibility(View.VISIBLE);
                    imcomeSun.setVisibility(View.VISIBLE);
                    extraPeriod.setVisibility(View.VISIBLE);
                    switcherLayout.setVisibility(View.VISIBLE);
                } else if (validDayCount > 0) {//规格有有效期
                    extraPeriod.setVisibility(View.VISIBLE);
                    switcherLayout.setVisibility(View.GONE);
                    starttime.setVisibility(View.VISIBLE);
                    endtime.setVisibility(View.GONE);
                } else {//规格无有效期
                    extraPeriod.setVisibility(View.GONE);
                }
                break;
            case Configs.CATEGORY_DATE:
                //                chargeSum.setLabel("充值天数(天)");
                //                imcomeSun.setLabel("实收金额(元)");
                datecardStarttime.setVisibility(View.VISIBLE);
                datecardStarttime.setContent(DateUtils.Date2YYYYMMDD(new Date()));
                if (other) {//选其他
                    datecardStarttime.setVisibility(View.VISIBLE);
                    datecardEndtime.setVisibility(View.VISIBLE);
                    imcomeSun.setVisibility(View.VISIBLE);
                } else {//规格无有效期
                    datecardStarttime.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override public void onDestroyView() {
        presenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onCardInfo() {
        //        card.setCardBackgroundColor(ColorUtils.parseColor(card_tpl.getColor()));
        CompatUtils.setBg(cardBg, ColorUtils.parseColor(card_tpl.color(), 200));
        card.setCardBackgroundColor(ColorUtils.parseColor(card_tpl.color(), 200).getColor());
        cardname.setText(card_tpl.name());
        cardPeriod.setVisibility(View.GONE);
        students.setText("绑定会员:" + createCardBody.user_name);
        balance.setVisibility(View.GONE);
    }

    @Override public void onPositionStr(String s) {
        mPostionStr = s;
    }

    @Override public void onStandard(List<CardStandard> standards) {
        datas.clear();
        datas.addAll(standards);
        datas.add(new CardStandard("其他", "", "", ""));

        if (datas.size() > 1) {
            onInitView(datas.get(0).getLimitDay(), false);
        } else {
            adapter.setChosen(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override public String getFragmentName() {
        return RealCardBuyFragment.class.getName();
    }

    @OnClick({ R.id.starttime, R.id.endtime, R.id.datecard_starttime, R.id.datecard_endtime }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.starttime:
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        starttime.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.datecard_starttime:
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        datecardStarttime.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.datecard_endtime:
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        datecardEndtime.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.endtime:
                pwTime.setRange(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - 10,
                    Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) + 10);
                pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
                    @Override public void onTimeSelect(Date date) {
                        endtime.setContent(DateUtils.Date2YYYYMMDD(date));
                    }
                });
                pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
                break;
        }
    }
}
