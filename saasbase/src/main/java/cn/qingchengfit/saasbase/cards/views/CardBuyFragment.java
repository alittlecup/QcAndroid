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
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.event.EventCustomOption;
import cn.qingchengfit.saasbase.cards.event.PayEvent;
import cn.qingchengfit.saasbase.cards.item.CardTplCustomOptionItem;
import cn.qingchengfit.saasbase.cards.item.CardTplOptionForBuy;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionOhterItem;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.presenters.CardBuyPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	Toolbar toolbar;
	TextView toolbarTitle;
	TextView tvCardTplType;
	TextView tvCardtplName;
	TextView tvGymName;
	TextView tvCardId;
	RelativeLayout cardview;

	RecyclerView rv;
	protected TextView tvMoneyLabel;
	protected TextView tvPayMoney;
	Button btnPay;
	protected CommonInputView civBindMenbers;
	protected CommonInputView civSaler;
	protected CommonInputView civStartTime;
	protected CommonInputView civEndTime;
	protected ExpandedLayout elAutoOpen;
	ExpandTextView tvCardExpandDesc;

	CommonInputView civRealCardNum;
	CommonInputView civMark;
	CommonInputView civPayMethod;
	LinearLayout loInputMoney;
	TextView tvCardAppend;

  @Inject public CardBuyPresenter presenter;
  @Inject public LoginStatus loginStatus;
  @Inject IPermissionModel permissionModel;
  @Need public CardTpl cardTpl;
  @Need QcStudentBean qcStudentBean;
	LinearLayout layoutValidate;
	TextView tvCardValidateTotal;
	CommonInputView cardProtocol;

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
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    tvCardTplType = (TextView) view.findViewById(R.id.tv_card_tpl_type);
    tvCardtplName = (TextView) view.findViewById(R.id.tv_cardtpl_name);
    tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
    tvCardId = (TextView) view.findViewById(R.id.tv_card_id);
    cardview = (RelativeLayout) view.findViewById(R.id.cardview);
    rv = (RecyclerView) view.findViewById(R.id.rv);
    tvMoneyLabel = (TextView) view.findViewById(R.id.tv_money_label);
    tvPayMoney = (TextView) view.findViewById(R.id.tv_pay_money);
    btnPay = (Button) view.findViewById(R.id.btn_pay);
    civBindMenbers = (CommonInputView) view.findViewById(R.id.civ_bind_menbers);
    civSaler = (CommonInputView) view.findViewById(R.id.civ_saler);
    civStartTime = (CommonInputView) view.findViewById(R.id.civ_start_time);
    civEndTime = (CommonInputView) view.findViewById(R.id.civ_end_time);
    elAutoOpen = (ExpandedLayout) view.findViewById(R.id.el_auto_open);
    tvCardExpandDesc = (ExpandTextView) view.findViewById(R.id.tv_card_expand_desc);
    civRealCardNum = (CommonInputView) view.findViewById(R.id.civ_real_card_num);
    civMark = (CommonInputView) view.findViewById(R.id.civ_mark);
    civPayMethod = (CommonInputView) view.findViewById(R.id.civ_pay_method);
    loInputMoney = (LinearLayout) view.findViewById(R.id.lo_input_money);
    tvCardAppend = (TextView) view.findViewById(R.id.tv_card_append);
    layoutValidate = (LinearLayout) view.findViewById(R.id.layout_validate);
    tvCardValidateTotal = (TextView) view.findViewById(R.id.tv_card_validate_total);
    cardProtocol = (CommonInputView) view.findViewById(R.id.card_protocol);
    view.findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onViewClicked();
      }
    });
    view.findViewById(R.id.card_protocol).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCardProrocol();
      }
    });
    view.findViewById(R.id.civ_bind_menbers).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivBindMenbersClicked();
      }
    });
    view.findViewById(R.id.civ_saler).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivSalerClicked();
      }
    });
    view.findViewById(R.id.civ_start_time).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivStartTimeClicked();
      }
    });
    view.findViewById(R.id.civ_mark).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCivMarkClicked();
      }
    });
    view.findViewById(R.id.civ_real_card_num).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickCardId();
      }
    });
    view.findViewById(R.id.civ_pay_method).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSelectPayMethod();
      }
    });

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

    if (qcStudentBean != null){
      civBindMenbers.setContent(qcStudentBean.username());
      presenter.setUserIds(qcStudentBean.id());
    }

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
 public void onViewClicked() {
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
      presenter.setOtherOption(false);
      showInputMoney(false, cardOptionCustom, cardOptionCustom.limit_days);
      setPayMoney(cardOptionCustom.price);
    } else {
      cardOptionCustom = null;
      presenter.setOtherOption(true);
      showInputMoney(true, cardOptionCustom, false);
    }
    presenter.setmChosenOption(cardOptionCustom);
    commonFlexAdapter.toggleSelection(position);
    commonFlexAdapter.notifyDataSetChanged();
    return true;
  }

 public void onCardProrocol() {
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

 public void onCivBindMenbersClicked() {
    //routeTo(AppUtils.getRouterUri(getContext(), "/student/choose/student/"),
    //    new ChooseAndSearchStudentParams().studentIdList(presenter.getChoseStuIds()).build());

   Map<String,Object> map=new HashMap<>();
   map.put("studentIdList",presenter.getChoseStuIds());
   QcRouteUtil.setRouteOptions(new RouteOptions("student").setActionName("/choose/student/")
       .addParams(map)).call();
  }

 public void onCivSalerClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/staff/choose/saler/"), null);
  }

 public void onCivStartTimeClicked() {
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

 public void onCivMarkClicked() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("会员卡备注")
            .content(presenter.getRemarks())
            .type(TYPE_CARD_BUY_REMARK)
            .build());
  }

 public void onClickCardId() {
    routeTo(AppUtils.getRouterUri(getContext(), "/common/input/"),
        new CommonInputParams().title("添加实体卡号")
            .content(presenter.getRealCardNo())
            .type(TYPE_CARD_BUY_CARD_NO)
            .build());
  }

 public void onSelectPayMethod() {
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

  @Override public void setDefineSeller(Staff seller) {

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

  @Override public boolean checkCardBuyBody(CardBuyBody cardBuyBody) {
    if(cardBuyBody.checkData()>0){
      showAlert(cardBuyBody.checkData());
      return true;
    }
    return false;
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
