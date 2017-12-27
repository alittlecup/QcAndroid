package cn.qingchengfit.saasbase.cards.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.event.EventCustomOption;
import cn.qingchengfit.saasbase.cards.item.CardTplCustomOptionItem;
import cn.qingchengfit.saasbase.cards.item.CardTplOptionForBuy;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionOhterItem;
import cn.qingchengfit.saasbase.cards.presenters.CardBuyPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.student.views.ChooseAndSearchStudentParams;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.views.fragments.TipTextDialogFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.JsonObject;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 2017/9/26.
 */
@Leaf(module = "card", path = "/pay/") public class CardBuyFragment extends SaasBaseFragment
    implements FlexibleAdapter.OnItemClickListener, CardBuyPresenter.MVPView,
    CardTplOptionForBuy.OnCustomCardOptionListener {

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.tv_card_tpl_type) TextView tvCardTplType;
  @BindView(R2.id.tv_cardtpl_name) TextView tvCardtplName;
  @BindView(R2.id.tv_gym_name) TextView tvGymName;
  @BindView(R2.id.tv_card_id) TextView tvCardId;
  @BindView(R2.id.cardview) RelativeLayout cardview;

  @BindView(R2.id.rv) RecyclerView rv;
  @BindView(R2.id.tv_money_label) protected TextView tvMoneyLabel;
  @BindView(R2.id.tv_pay_money) protected TextView tvPayMoney;
  @BindView(R2.id.btn_pay) Button btnPay;
  @BindView(R2.id.civ_bind_menbers) protected CommonInputView civBindMenbers;
  @BindView(R2.id.civ_saler) protected CommonInputView civSaler;
  @BindView(R2.id.civ_start_time) protected CommonInputView civStartTime;
  @BindView(R2.id.civ_end_time) protected CommonInputView civEndTime;
  @BindView(R2.id.el_auto_open) protected ExpandedLayout elAutoOpen;

  @BindView(R2.id.civ_real_card_num) CommonInputView civRealCardNum;
  @BindView(R2.id.civ_mark) CommonInputView civMark;
  @BindView(R2.id.civ_pay_method) CommonInputView civPayMethod;
  @BindView(R2.id.lo_input_money) LinearLayout loInputMoney;
  @BindView(R2.id.tv_card_append) TextView tvCardAppend;

  @Inject public CardBuyPresenter presenter;
  @Need public CardTpl cardTpl;
  @BindView(R2.id.layout_validate) LinearLayout layoutValidate;
  @BindView(R2.id.tv_card_validate_total) TextView tvCardValidateTotal;
  @BindView(R2.id.card_protocol) CommonInputView cardProtocol;

  private CardTplOption cardOptionCustom = new CardTplOption();
  private List<CardTplOption> optionList = new ArrayList<>();
  public int patType;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initCustomOption();
  }

  private CommonFlexAdapter commonFlexAdapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_buy_card, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    setCardInfo();
    if (commonFlexAdapter == null) {
      commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
      commonFlexAdapter.setMode(SelectableAdapter.Mode.SINGLE);
    }
    elAutoOpen.setLeftClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        TipTextDialogFragment.newInstance(getResources().getString(R.string.tips_auto_open_card),
            "知道了", "提示").show(getFragmentManager(), null);
      }
    });
    civSaler.setContent("本人");
    civEndTime.setClickable(false);
    if (cardTpl.has_service_term) {
      cardProtocol.setVisibility(View.VISIBLE);
    }else{
      cardProtocol.setVisibility(View.GONE);
    }
    SmoothScrollLinearLayoutManager layoutManager =
        new SmoothScrollLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    rv.setNestedScrollingEnabled(false);
    rv.addItemDecoration(new FlexibleItemDecoration(getContext()).withOffset(6)
        .withRightEdge(true)
        .withBottomEdge(true));
    rv.setLayoutManager(layoutManager);
    rv.setAdapter(commonFlexAdapter);
    presenter.getCardTplDetail();
    //civRealMoney.addTextWatcher(new TextWatcher() {
    //  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    //
    //  }
    //
    //  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    //    setPayMoney(s.toString());
    //  }
    //
    //  @Override public void afterTextChanged(Editable s) {
    //
    //  }
    //});

    return view;
  }

  public void setCardInfo() {
    presenter.setmCardTplId(cardTpl.getId());
    presenter.setCardCate(cardTpl.getType());
  }

  private void initCustomOption() {
    RxBus.getBus()
        .register(EventCustomOption.class)
        .compose(this.<EventCustomOption>bindToLifecycle())
        .compose(this.<EventCustomOption>doWhen(FragmentEvent.CREATE_VIEW))
        .subscribe(new Action1<EventCustomOption>() {
          @Override public void call(EventCustomOption eventCustomOption) {
            if (eventCustomOption != null) {
              cardOptionCustom = eventCustomOption.getCardOptionCustom();
              optionList.add(eventCustomOption.getCardOptionCustom());
              commonFlexAdapter.removeItem(commonFlexAdapter.getItemCount() - 1);
              commonFlexAdapter.notifyItemRemoved(commonFlexAdapter.getItemCount() - 1);
              commonFlexAdapter.addItem(commonFlexAdapter.getItemCount() ,
                  new CardTplCustomOptionItem(eventCustomOption.getCardOptionCustom(), cardTpl.type,
                      CardBuyFragment.this));
              for (int i = 0; i < commonFlexAdapter.getItemCount(); i++) {
                commonFlexAdapter.removeSelection(i);
              }
              commonFlexAdapter.addSelection(commonFlexAdapter.getItemCount() - 1);
              commonFlexAdapter.notifyDataSetChanged();
              showInputMoney(false, cardOptionCustom, cardOptionCustom.isLimit_days());
            }
          }
        });
  }


  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("开卡详情");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryOption();
  }

  @Override public int getLayoutRes() {
    return R.id.frag_charge_content;
  }

  @Override public String getFragmentName() {
    return CardBuyFragment.class.getName();
  }

  /**
   * 点击支付
   */
  @OnClick(R2.id.btn_pay) public void onViewClicked() {
    onConfirmPay();
  }

  public void onConfirmPay() {
    presenter.buyCard();
  }

  /**
   * 选择规格
   */
  @Override public boolean onItemClick(int position) {
    if (position < optionList.size()){
      //已有规格 展示价格
      cardOptionCustom = optionList.get(position);
      showInputMoney(false, cardOptionCustom, cardOptionCustom.limit_days);
      setPayMoney(cardOptionCustom.price + "元");
    }else {
      cardOptionCustom = null;
      showInputMoney(true, cardOptionCustom, false);
    }
    presenter.setmChosenOption(cardOptionCustom);
    commonFlexAdapter.toggleSelection(position);
    commonFlexAdapter.notifyDataSetChanged();
    return true;
  }

  @OnClick(R2.id.card_protocol)
  public void onCardProrocol(){
    if (cardTpl.card_tpl_service_term != null){
      CardProtocolActivity.startWeb(cardTpl.card_tpl_service_term.content_link, getContext(), false);
    }
  }

  @Override public void onGetOptions(List<CardTplOption> options) {
    commonFlexAdapter.clear();
    if (options.size() > 0){
      optionList.clear();
      optionList.addAll(options);
    }
    for (CardTplOption option : options) {
      commonFlexAdapter.addItem(new CardTplOptionForBuy(option, cardTpl.type, this));
    }
    // TODO: 2017/9/30 判断权限
    commonFlexAdapter.addItem(new CardtplOptionOhterItem());
    onItemClick(0);
  }

  @Override public void onGetCardTpl(CardTpl cardTpl) {
    tvCardtplName.setText(cardTpl.getName());
    tvCardId.setText(cardTpl.getId());
    tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(cardTpl.type, getContext()));
    tvGymName.setText(cardTpl.getShopNames());
    cardview.setBackground(
        DrawableUtils.generateBg(16, CardBusinessUtils.getDefaultCardbgColor(cardTpl.type)));
    tvCardAppend.setText(cardTpl.getLimit());
  }

  @OnClick(R2.id.civ_bind_menbers) public void onCivBindMenbersClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/student/choose/student/"),
        new ChooseAndSearchStudentParams().studentIdList(presenter.getChoseStuIds()).build());
  }

  @OnClick(R2.id.civ_saler) public void onCivSalerClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/staff/choose/saler/"), null);
  }

  @OnClick(R2.id.civ_start_time) public void onCivStartTimeClicked() {
    choosTime(TimePopupWindow.Type.YEAR_MONTH_DAY, 0, 0, new Date(), civStartTime,
        new TimeDialogWindow.OnTimeSelectListener() {
          @Override public void onTimeSelect(Date date) {
            civStartTime.setContent(DateUtils.Date2YYYYMMDD(date));
            civEndTime.setContent(
                DateUtils.Date2YYYYMMDD(DateUtils.addDay(date, cardOptionCustom.getDays())));
          }
        });
  }

  @OnClick(R2.id.civ_mark) public void onCivMarkClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("添加备注").hint(presenter.getRemarks()).build());
  }

  @OnClick(R2.id.civ_real_card_num) public void onClickCardId() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("添加实体账号").hint(realCardNum()).build());
  }

  @OnClick(R2.id.civ_pay_method) public void onSelectPayMethod() {
    BottomPayDialog f = BottomPayDialog.newInstance(presenter.hasEditPermission());
    f.setTargetFragment(this, 3);
    f.show(getFragmentManager(), "");
  }

  @Override public void showInputMoney(boolean other, CardTplOption option, boolean validDay) {
    cardOptionCustom = option;
    if (cardOptionCustom == null){
      setPayMoney("0元");
    }else{
      setPayMoney(cardOptionCustom.getPrice());
    }
    if (other) {
      if (cardTpl.getType() == Configs.CATEGORY_DATE) {
        routeTo("card", "/custom/option", null);
      } else {
        routeTo("card", "/custom/all/option", null);
      }
    } else if (validDay) {
      layoutValidate.setVisibility(View.VISIBLE);
      civStartTime.setContent(DateUtils.Date2YYYYMMDD(new Date()));
      civEndTime.setContent(
          DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), option.getDays())));
    } else {
      layoutValidate.setVisibility(View.GONE);
    }
    if (!other && cardTpl.getType() == Configs.CATEGORY_DATE) {
      layoutValidate.setVisibility(View.VISIBLE);
      civStartTime.setContent(DateUtils.Date2YYYYMMDD(
          TextUtils.isEmpty(option.created_at) ? new Date()
              : DateUtils.formatDateFromServer(option.created_at)));
      civEndTime.setContent(DateUtils.Date2YYYYMMDD(
          TextUtils.isEmpty(option.created_at) ? DateUtils.addDay(new Date(), (option.days))
              : DateUtils.addDay(DateUtils.formatDateFromServer(option.created_at),
                  ((int) Float.parseFloat(option.charge)))));
    }
  }

  //TODO 修改回调方式
  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case 3:
          int p = Integer.parseInt(IntentUtils.getIntentString(data));
          civPayMethod.setContent(getResources().getStringArray(R.array.pay_method)[p]);
          switch (p) {
            case 0:
              patType = 7;
              break;
            case 1:
              patType = 6;
              break;
            case 2:
              patType = 1;
              break;
            case 3:
              patType = 2;
              break;
            case 4:
              patType = 3;
              break;
            case 5:
              patType = 4;
              break;
          }
          break;
      }
    }
  }

  @Override public void bindStudent(String student) {
    civBindMenbers.setContent(student);
  }

  @Override public void bindSaler(String saler) {
    civSaler.setContent(saler);
  }

  @Override public void remark(boolean remark) {
    civMark.setHint(remark ? "已填写" : getString(R.string.please_input));
  }

  @Override public void onBusinessOrder(JsonObject payBusinessResponse) {
  }

  @Override public void setPayMoney(String s) {
    tvPayMoney.setText(s);
  }

  @Override public String realCardNum() {
    return civRealCardNum.getContent();
  }

  @Override public String realMoney() {
    return String.valueOf(cardOptionCustom.getPrice());
  }

  @Override public String chargeMoney() {
    return String.valueOf(cardOptionCustom.getDays());
  }

  @Override public int payMethod() {
    return patType == 0 ? 7 : patType;
  }

  @Override public boolean openValidDay() {
    return cardOptionCustom.isLimit_days();
  }

  @Override public String startDay() {
    return civStartTime.getContent();
  }

  @Override public String endDay() {
    return civEndTime.getContent();
  }

  @Override public boolean autoOpen() {
    return elAutoOpen.isExpanded();
  }

  @Override public void onCustomCard(int position) {
    //TODO 传参
    if (commonFlexAdapter.getItem(position) instanceof CardTplOptionForBuy) {
      if (cardTpl.getType() == Configs.CATEGORY_DATE) {
        routeTo(AppUtils.getRouterUri(getContext(), "/card/custom/option"),
            new TimeCardOptionParams().cardOptionCustom(
                ((CardTplOptionForBuy) commonFlexAdapter.getItem(position)).getOption()).unUse(0).build());
      } else {
        routeTo(AppUtils.getRouterUri(getContext(), "/card/custom/all/option"),
            new TotalCustomCardOptionParams().cardOptionCustom(
                ((CardTplOptionForBuy) commonFlexAdapter.getItem(position)).getOption()).unuse(0).build());
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
