package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.event.EventCustomOption;
import cn.qingchengfit.saasbase.cards.event.PayEvent;
import cn.qingchengfit.saasbase.cards.item.CardTplCustomOptionItem;
import cn.qingchengfit.saasbase.cards.item.CardTplOptionForBuy;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionOhterItem;
import cn.qingchengfit.saasbase.cards.presenters.CardBuyPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.student.views.ChooseAndSearchStudentParams;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.views.fragments.TipTextDialogFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandTextView;
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
 * Created by Paper on 2017/9/26.
 */
@Leaf(module = "card", path = "/pay/") public class CardBuyFragment extends SaasBaseFragment
    implements FlexibleAdapter.OnItemClickListener, CardBuyPresenter.MVPView,
    CardTplOptionForBuy.OnCustomCardOptionListener {

  public static final int TYPE_CARD_BUY_CARD_NO = 1001;
  public static final int TYPE_CARD_BUY_REMARK = 1002;

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
  @BindView(R2.id.tv_card_expand_desc) ExpandTextView tvCardExpandDesc;

  @BindView(R2.id.civ_real_card_num) CommonInputView civRealCardNum;
  @BindView(R2.id.civ_mark) CommonInputView civMark;
  @BindView(R2.id.civ_pay_method) CommonInputView civPayMethod;
  @BindView(R2.id.lo_input_money) LinearLayout loInputMoney;
  @BindView(R2.id.tv_card_append) TextView tvCardAppend;

  @Inject public CardBuyPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject IPermissionModel permissionModel;
  @Need public CardTpl cardTpl;
  @BindView(R2.id.layout_validate) LinearLayout layoutValidate;
  @BindView(R2.id.tv_card_validate_total) TextView tvCardValidateTotal;
  @BindView(R2.id.card_protocol) CommonInputView cardProtocol;

  protected CardTplOption cardOptionCustom = new CardTplOption();
  private List<CardTplOption> optionList = new ArrayList<>();
  public int patType;
  private int selectPos = 0;
  private Observable<PayEvent> ob;

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
    initBus();
    if (commonFlexAdapter == null) {
      commonFlexAdapter = new CommonFlexAdapter(new ArrayList(), this);
      commonFlexAdapter.setMode(SelectableAdapter.Mode.SINGLE);
    }
    elAutoOpen.setIconClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        TipTextDialogFragment.newInstance(getResources().getString(R.string.tips_auto_open_card),
            "知道了", "提示").show(getFragmentManager(), null);
      }
    });
    elAutoOpen.setVisibility(View.GONE);
    //civSaler.setContent(loginStatus.staff_name());

    civStartTime.setNoSaved();
    civEndTime.setNoSaved();

    civEndTime.setClickable(false);
    if (cardTpl.has_service_term) {
      cardProtocol.setVisibility(View.VISIBLE);
    } else {
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

  protected SpannableString setTimeFormat(String content) {
    SpannableString s = new SpannableString(content + " 修改");
    s.setSpan(
        new ForegroundColorSpan(getResources().getColor(cn.qingchengfit.widgets.R.color.text_dark)),
        0, 10, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    return s;
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
              if (optionList.size() > 0 && TextUtils.isEmpty(
                  optionList.get(optionList.size() - 1).id)) {
                optionList.remove(optionList.size() - 1);
              }
              optionList.add(eventCustomOption.getCardOptionCustom());
              commonFlexAdapter.removeItem(commonFlexAdapter.getItemCount() - 1);
              commonFlexAdapter.notifyItemRemoved(commonFlexAdapter.getItemCount() - 1);
              commonFlexAdapter.addItem(commonFlexAdapter.getItemCount(),
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

  private void initBus() {
    ob = RxBus.getBus().register(PayEvent.class);
    ob.compose(this.<PayEvent>bindToLifecycle()).subscribe(new Action1<PayEvent>() {
      @Override public void call(PayEvent payEvent) {
        if (payEvent != null && payEvent.getPayMethod() != null) {
          patType = payEvent.getPayMethod().payType;
          civPayMethod.setContent(payEvent.getPayMethod().name);
          selectPos = payEvent.getPosition();
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
    if (optionList.size() == 0) {
      DialogUtils.showAlert(getContext(), "请至少选择一种会员卡规格");
      return;
    }
    presenter.buyCard();
  }

  /**
   * 选择规格
   */
  @Override public boolean onItemClick(int position) {
    if (position < optionList.size()) {
      //已有规格 展示价格
      cardOptionCustom = optionList.get(position);
      showInputMoney(false, cardOptionCustom, cardOptionCustom.limit_days);
      setPayMoney(cardOptionCustom.price);
    } else {
      cardOptionCustom = null;
      showInputMoney(true, cardOptionCustom, false);
    }
    presenter.setmChosenOption(cardOptionCustom);
    commonFlexAdapter.toggleSelection(position);
    commonFlexAdapter.notifyDataSetChanged();
    return true;
  }

  @OnClick(R2.id.card_protocol) public void onCardProrocol() {
    if (cardTpl.card_tpl_service_term != null) {
      CardProtocolActivity.startWeb(cardTpl.card_tpl_service_term.content_link, getContext(),
          false);
    }
  }

  @Override public void onGetOptions(List<CardTplOption> options) {
    commonFlexAdapter.clear();
    if (options.size() > 0) {
      optionList.clear();
      optionList.addAll(options);
    }
    for (CardTplOption option : options) {
      commonFlexAdapter.addItem(new CardTplOptionForBuy(option, cardTpl.type, this));
    }
    if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {
      commonFlexAdapter.addItem(new CardtplOptionOhterItem());
    }
    if (options.size() > 0) {
      onItemClick(0);
    } else {
      layoutValidate.setVisibility(View.GONE);
    }
  }

  @Override public void onGetCardTpl(CardTpl cardTpl) {
    tvCardtplName.setText(cardTpl.getName());
    tvCardId.setText(cardTpl.getId());
    tvCardTplType.setText(CardBusinessUtils.getCardTypeCategoryStrHead(cardTpl.type, getContext()));
    tvGymName.setText(cardTpl.getShopNames());
    cardview.setBackground(
        DrawableUtils.generateBg(16, CardBusinessUtils.getDefaultCardbgColor(cardTpl.type)));
    tvCardAppend.setText(cardTpl.getLimit());
    if (TextUtils.isEmpty(cardTpl.getDescription())) {
      tvCardExpandDesc.setContent("简介：无");
    } else {
      tvCardExpandDesc.setContent("简介: " + cardTpl.getDescription());
    }
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
            if (date.after(new Date())) {
              elAutoOpen.setVisibility(View.VISIBLE);
            } else {
              elAutoOpen.setVisibility(View.GONE);
            }
            civStartTime.setContent(setTimeFormat(DateUtils.Date2YYYYMMDD(date)));
            checkValidate(date);
          }
        });
  }

  public void checkValidate(Date date) {
    civEndTime.setContent(DateUtils.Date2YYYYMMDD(DateUtils.addDay(date,
        cardTpl.getType() == Configs.CATEGORY_DATE ? (int) Float.parseFloat(
            cardOptionCustom.getCharge()) - 1 : cardOptionCustom.getDays() - 1)));
  }

  @OnClick(R2.id.civ_mark) public void onCivMarkClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("会员卡备注")
            .content(presenter.getRemarks())
            .type(TYPE_CARD_BUY_REMARK)
            .build());
  }

  @OnClick(R2.id.civ_real_card_num) public void onClickCardId() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("添加实体卡号")
            .content(presenter.getRealCardNo())
            .type(TYPE_CARD_BUY_CARD_NO)
            .build());
  }

  @OnClick(R2.id.civ_pay_method) public void onSelectPayMethod() {
    BottomPayDialog f = BottomPayDialog.newInstance(presenter.hasEditPermission(), selectPos);
    f.show(getFragmentManager(), "");
  }

  @Override public void showInputMoney(boolean other, CardTplOption option, boolean validDay) {
    cardOptionCustom = option;
    elAutoOpen.setVisibility(View.GONE);
    if (cardOptionCustom == null) {
      setPayMoney(0f);
    } else {
      setPayMoney(cardOptionCustom.getPrice());
    }
    if (other) {
      if (cardTpl.getType() == Configs.CATEGORY_DATE) {
        routeTo("card", "/custom/option", null);
      } else {
        routeTo("card", "/custom/all/option",
            new TotalCustomCardOptionParams().cardTplId(cardTpl.type).build());
      }
    } else if (validDay) {
      layoutValidate.setVisibility(View.VISIBLE);
      civStartTime.setContent(setTimeFormat(DateUtils.Date2YYYYMMDD(new Date())));
      civEndTime.setContent(
          DateUtils.Date2YYYYMMDD(DateUtils.addDay(new Date(), option.getDays() - 1)));
    } else {
      layoutValidate.setVisibility(View.GONE);
    }
    if (!other && cardTpl.getType() == Configs.CATEGORY_DATE) {
      layoutValidate.setVisibility(View.VISIBLE);
      civStartTime.setContent(setTimeFormat(DateUtils.Date2YYYYMMDD(new Date())));
      civEndTime.setContent(DateUtils.Date2YYYYMMDD(
          DateUtils.addDay(new Date(), (int) (Float.parseFloat(option.charge) - 1))));
    }
  }

  @Override public void bindStudent(String student) {
    civBindMenbers.setContent(student);
  }

  @Override public void bindSaler(String saler) {
    civSaler.setContent(saler);
  }

  @Override public void remark(boolean remark) {
    if (civMark != null) {
      civMark.setHint(remark ? "已填写" : getString(R.string.please_input));
    }
  }

  @Override public void onBusinessOrder(JsonObject payBusinessResponse) {
  }

  @Override public void setPayMoney(float s) {
    tvPayMoney.setText("¥" + CmStringUtils.getMoneyStr(s));
  }

  @Override public void realCardNum(boolean isRealCard) {
    if (civRealCardNum != null) {
      civRealCardNum.setHint(isRealCard ? "已填写" : getString(R.string.please_input));
    }
  }

  @Override public String realMoney() {
    return String.valueOf(cardOptionCustom.getPrice());
  }

  @Override public String chargeMoney() {
    return String.valueOf(cardOptionCustom.getCharge());
  }

  @Override public int payMethod() {
    return patType == 0 ? 7 : patType;
  }

  @Override public boolean openValidDay() {
    return cardOptionCustom.isLimit_days();
  }

  @Override public String startDay() {
    return civStartTime.getContent().split(" ")[0];
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
                ((CardTplOptionForBuy) commonFlexAdapter.getItem(position)).getOption())
                .unUse(0)
                .build());
      } else {
        routeTo(AppUtils.getRouterUri(getContext(), "/card/custom/all/option"),
            new TotalCustomCardOptionParams().cardOptionCustom(
                ((CardTplOptionForBuy) commonFlexAdapter.getItem(position)).getOption())
                .cardTplId(cardTpl.type)
                .build());
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (ob != null) {
      RxBus.getBus().unregister(PayEvent.class.getName(), ob);
    }
  }
}