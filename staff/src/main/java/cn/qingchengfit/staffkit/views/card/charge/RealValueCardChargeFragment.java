package cn.qingchengfit.staffkit.views.card.charge;

import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.body.ChargeBody;
import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.views.adapter.StandardAdapter;
import cn.qingchengfit.staffkit.views.custom.EqualSpaceItemDecoration;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.RealCardUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
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
public class RealValueCardChargeFragment extends BaseFragment implements RealValueCardChargeView {

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
    List<CardStandard> datas = new ArrayList<>();
    @BindView(R.id.datecard_starttime) CommonInputView datecardStarttime;
    @BindView(R.id.datecard_endtime) CommonInputView datecardEndtime;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.card_bg) LinearLayout cardBg;
    @Inject RealValueCardChargePresenter presenter;
    @Inject RealcardWrapper realCard;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StandardAdapter adapter;
    private TimeDialogWindow pwTime;
    private String shopId;
    private boolean isOther;
    private boolean isLoadStandard = false;
    private String mPositionStr = "";

    //public static RealValueCardChargeFragment newInstance(String shopId) {
    //
    //    Bundle args = new Bundle();
    //    args.putString("shopid", shopId);
    //    RealValueCardChargeFragment fragment = new RealValueCardChargeFragment();
    //    fragment.setArguments(args);
    //    return fragment;
    //}

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = gymWrapper.shop_id();
        //if (getArguments() != null) {
        //    shopId = getArguments().getString("shopid");
        //}
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realcard_charge_value, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        mCallbackActivity.setToolbar("请选择充值规格", false, null, R.menu.menu_next, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (!isLoadStandard) {
                    return true;
                }
                if (!adapter.isChosen()) {
                    showAlert("请选择购卡规则");
                    return true;
                }

                ChargeBody body = new ChargeBody();
                body.setShop_id(shopId);
                if (isOther) {
                    switch (realCard.type()) {
                        case Configs.CATEGORY_VALUE:
                            if (TextUtils.isEmpty(chargeSum.getContent())) {
                                ToastUtils.show("请填写充值金额");
                                return true;
                            }
                            body.setAccount(chargeSum.getContent());
                            break;
                        case Configs.CATEGORY_TIMES:
                            if (TextUtils.isEmpty(chargeSum.getContent())) {
                                ToastUtils.show("请填写充值次数");
                                return true;
                            }
                            body.setTimes(chargeSum.getContent());
                            break;
                        case Configs.CATEGORY_DATE:

                            if (datecardStarttime.isEmpty()) {
                                ToastUtils.show("请填写开始日期");
                                return true;
                            }
                            if (datecardEndtime.isEmpty()) {
                                ToastUtils.show("请填写结束日期");
                                return true;
                            }

                            body.setStart(datecardStarttime.getContent());
                            body.setEnd(datecardEndtime.getContent());
                            break;
                    }
                    if (TextUtils.isEmpty(imcomeSun.getContent())) {
                        ToastUtils.show("请填写实收金额");
                        return true;
                    }
                    body.setPrice(imcomeSun.getContent());
                    body.setCheck_valid(switcher.isChecked());
                    if (switcher.isChecked()) {
                        if (TextUtils.isEmpty(starttime.getContent())) {
                            ToastUtils.show("请填写开始日期");
                            return true;
                        }
                        if (TextUtils.isEmpty(endtime.getContent())) {
                            ToastUtils.show("请填写结束日期");
                            return true;
                        }
                        body.setValid_from(starttime.getContent());
                        body.setValid_to(endtime.getContent());
                    }
                } else {
                    // 选择规格
                    CardStandard cardStandard = datas.get(adapter.getChooseItem());
                    switch (realCard.type()) {
                        case Configs.CATEGORY_VALUE:
                            body.setAccount(cardStandard.getCharge());
                            if (cardStandard.getLimitDay() > 0) {
                                if (TextUtils.isEmpty(starttime.getContent())) {
                                    ToastUtils.show("请填写开始日期");
                                    return true;
                                }
                                body.setCheck_valid(true);
                                body.setValid_from(starttime.getContent());
                                body.setValid_to(DateUtils.Date2YYYYMMDD(new Date(
                                    DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime()
                                        + DateUtils.DAY_TIME * (cardStandard.getLimitDay() - 1))));
                            }
                            break;
                        case Configs.CATEGORY_TIMES:
                            body.setTimes(cardStandard.getCharge());
                            if (cardStandard.getLimitDay() > 0) {
                                if (TextUtils.isEmpty(starttime.getContent())) {
                                    ToastUtils.show("请填写开始日期");
                                    return true;
                                }
                                body.setCheck_valid(true);
                                body.setValid_from(starttime.getContent());
                                body.setValid_to(DateUtils.Date2YYYYMMDD(new Date(
                                    DateUtils.formatDateFromYYYYMMDD(starttime.getContent()).getTime()
                                        + DateUtils.DAY_TIME * (cardStandard.getLimitDay() - 1))));
                            }
                            break;
                        case Configs.CATEGORY_DATE:
                            int out = 0;
                            try {
                                Float f = Float.parseFloat(cardStandard.getCharge());
                                out = f.intValue();
                                //if (cardStandard.getCharge().contains("."))
                                // out = Integer.parseInt(cardStandard.getCharge());
                            } catch (Exception e) {
                                ToastUtils.show("充值天数不为整数,请修改此条规格");
                                return true;
                            }

                            if (realCard.getRealCard().isExpired()) {//已过期
                                if (TextUtils.isEmpty(datecardStarttime.getContent())) {
                                    ToastUtils.show("请选择开始时间");
                                    return true;
                                }
                                body.setStart(datecardStarttime.getContent());
                                body.setEnd(DateUtils.Date2YYYYMMDD(new Date(
                                    DateUtils.formatDateFromYYYYMMDD(datecardStarttime.getContent()).getTime() + DateUtils.DAY_TIME * (out
                                        - 1))));
                            } else {
                                body.setStart(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(realCard.end())));
                                body.setEnd(DateUtils.Date2YYYYMMDD(
                                    new Date(DateUtils.formatDateFromServer(realCard.end()).getTime() + DateUtils.DAY_TIME * out)));
                            }

                            break;
                    }
                    body.setPrice(cardStandard.getIncome() + "");
                }
                getFragmentManager().beginTransaction()
                    .add(mCallbackActivity.getFragId(), CompletedChargeFragment.newInstance(body))
                    .addToBackStack(null)
                    .commit();
                return true;
            }
        });
        name.setText("设置有效期");
        adapter = new StandardAdapter(getContext(), datas, true, true);
        recycleviewStandard.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recycleviewStandard.addItemDecoration(new EqualSpaceItemDecoration(MeasureUtils.dpToPx(8f, getResources())));
        //        recycleviewStandard.addItemDecoration(new SpacesItemDecoration(MeasureUtils.dpToPx(R.dimen.activity_horizontal_margin,getResources())));
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
                        showAlert(getString(R.string.alert_cardtype_use_other, mPositionStr));
                    }
                }
            }
        });
        presenter.queryPositions(App.staffId, PermissionServerUtils.CARDSETTING_CAN_CHANGE);
        presenter.queryStardard();
        onCardInfo();
        extraPeriod.setVisibility(View.GONE);
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
      pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        return view;
    }

    void onInitView(int validDayCount, boolean other) {
        isOther = other;
        extraPeriod.setVisibility(View.GONE);
        datecardStarttime.setVisibility(View.GONE);
        datecardEndtime.setVisibility(View.GONE);
        chargeSum.setVisibility(View.GONE);
        imcomeSun.setVisibility(View.GONE);
        switch (realCard.type()) {
            case Configs.CATEGORY_VALUE:
                chargeSum.setLabel("充值金额(元)");
                //                imcomeSun.setLabel("实收金额(元)");
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
                    //                    chargeSum.setVisibility(View.VISIBLE);
                    //                    chargeSum.setVisibility(View.VISIBLE);
                    //                    extraPeriod.setVisibility(View.VISIBLE);

                }

                break;
            case Configs.CATEGORY_TIMES:
                chargeSum.setLabel("充值次数(次)");
                //                imcomeSun.setLabel("实收金额(元)");
                if (other) {//选其他
                    chargeSum.setVisibility(View.VISIBLE);
                    imcomeSun.setVisibility(View.VISIBLE);
                    extraPeriod.setVisibility(View.VISIBLE);
                    switcherLayout.setVisibility(View.VISIBLE);
                } else if (validDayCount < 0) {//规格有有效期
                    extraPeriod.setVisibility(View.VISIBLE);
                    switcherLayout.setVisibility(View.GONE);
                    starttime.setVisibility(View.VISIBLE);
                    endtime.setVisibility(View.GONE);
                } else {//规格无有效期
                    extraPeriod.setVisibility(View.GONE);
                }
                break;
            case Configs.CATEGORY_DATE:
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

        CompatUtils.setBg(cardBg, ColorUtils.parseColor(realCard.getRealCard().getColor(), 200));
        card.setCardBackgroundColor(ColorUtils.parseColor(realCard.getRealCard().getColor(), 200).getColor());
        cardname.setText(realCard.name());

        switch (realCard.type()) {
            case Configs.CATEGORY_VALUE:
            case Configs.CATEGORY_TIMES:
                if (realCard.getRealCard().isCheck_valid()) {
                    cardPeriod.setText("有效期: "
                        + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(realCard.getRealCard().getValid_from()))
                        + "至"
                        + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(realCard.getRealCard().getValid_to())));
                } else {
                    cardPeriod.setText("有效期: 不限");
                }
                break;
            case Configs.CATEGORY_DATE:
                cardPeriod.setText("有效期: "
                    + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(realCard.getRealCard().getStart()))
                    + "至"
                    + DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(realCard.getRealCard().getEnd())));

                break;
        }

        students.setText("绑定会员: " + realCard.getRealCard().getUsersStr());
        balance.setText(RealCardUtils.getCardBlance(realCard.getRealCard()));
    }

    @Override public void onPositionStr(String s) {
        mPositionStr = s;
    }

    @Override public void onStandard(List<CardStandard> standards) {
        isLoadStandard = true;
        datas.clear();
        datas.addAll(standards);
        datas.add(new CardStandard("其他", "", "", ""));

        if (datas.size() > 1) {
            onInitView(datas.get(0).getLimitDay(), datas.size() == 1);
        } else {
            adapter.setChosen(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override public String getFragmentName() {
        return RealValueCardChargeFragment.class.getName();
    }

    @OnClick(R.id.switcher_layout) public void onSwitch() {
        switcher.toggle();
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
        }
    }
}
