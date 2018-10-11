package cn.qingchengfit.saasbase.cards.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardLimit;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.event.EventLimitBuyCount;
import cn.qingchengfit.saasbase.cards.item.AddCardtplStantardItem;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionItem;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.ShopsBody;
import cn.qingchengfit.saasbase.cards.presenters.CardTplDetailPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saascommon.events.EventSaasFresh;
import cn.qingchengfit.saasbase.network.model.Shop;
import cn.qingchengfit.saascommon.qrcode.model.QrEvent;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.ExpandTextView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
 * Created by Paper on 2017/8/23.
 */
@Leaf(module = "card", path = "/cardtpl/detail/") public class CardTplDetailFragment
  extends SaasBaseFragment
  implements CardTplDetailPresenter.MVPView, FlexibleAdapter.OnItemClickListener {

  private final static String PARAMS_KEY = "uuid=";

	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;
	TextView tvCardTplType;
	TextView tvCardtplName;
	TextView tvGymName;
	TextView tvCardId;
	TextView cardStatus;
	RelativeLayout cardview;
	RecyclerView recycleview;
	TextView btnDel;
	TextView tvCardAppend;
	ExpandTextView tvCardExpandDesc;
	protected CommonInputView civInputCardname;

  @Inject public CardTplDetailPresenter presenter;
  @Inject GymWrapper gymWrapper;
  @Inject public IPermissionModel permissionModel;
  @Need public CardTpl cardTpl;
  CommonFlexAdapter comonAdapter;
	CommonInputView civInputCardDesc;
	ExpandedLayout expandSettingLimit;
	ExpandedLayout expandCardProtocol;
	CommonInputView preOrderCount;
	CommonInputView duringCount;
	CommonInputView limitBugCount;
	CommonInputView supportGyms;
  protected CardLimit cardLimit = new CardLimit();
	LinearLayout layoutCardValueDesc;
	LinearLayout layoutCardOption;
	CommonInputView inputCardProtocol;
  public String desc;
  protected String supportShopStr;
	protected LinearLayout layoutCardDetail;
  private CardtplBody body = new CardtplBody();
  private boolean isShouldSave;
  private StringBuilder sb = new StringBuilder();
  private Observable<EventTxT> obEvent;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    comonAdapter = new CommonFlexAdapter(new ArrayList(), this);

    RxBus.getBus()
      .register(EventSaasFresh.CardTplList.class)
      .compose(this.<EventSaasFresh.CardTplList>bindToLifecycle())
      .subscribe(new BusSubscribe<EventSaasFresh.CardTplList>() {
        @Override public void onNext(EventSaasFresh.CardTplList cardList) {
          if (getActivity() != null) {
            getActivity().onBackPressed();
          }
        }
      });

    obEvent = RxBus.getBus().register(EventTxT.class);
    obEvent.subscribe(new BusSubscribe<EventTxT>() {
      @Override public void onNext(EventTxT eventTxT) {
        body.description = eventTxT.txt;
        desc = eventTxT.txt;
        if (tvCardExpandDesc != null) {
          tvCardExpandDesc.setContent(desc);
        }
        if (civInputCardDesc != null) {
          civInputCardDesc.setContent("查看");
        }
      }
    });
    initBus();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cardtpl_detail, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    tvCardTplType = (TextView) view.findViewById(R.id.tv_card_tpl_type);
    tvCardtplName = (TextView) view.findViewById(R.id.tv_cardtpl_name);
    tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
    tvCardId = (TextView) view.findViewById(R.id.tv_card_id);
    cardStatus = (TextView) view.findViewById(R.id.img_stutus);
    cardview = (RelativeLayout) view.findViewById(R.id.cardview);
    recycleview = (RecyclerView) view.findViewById(R.id.recycleview);
    btnDel = (TextView) view.findViewById(R.id.btn_del);
    tvCardAppend = (TextView) view.findViewById(R.id.tv_card_append);
    tvCardExpandDesc = (ExpandTextView) view.findViewById(R.id.tv_card_expand_desc);
    civInputCardname = (CommonInputView) view.findViewById(R.id.civ_input_card_name);
    civInputCardDesc = (CommonInputView) view.findViewById(R.id.civ_input_card_desc);
    expandSettingLimit = (ExpandedLayout) view.findViewById(R.id.expand_setting_limit);
    expandCardProtocol = (ExpandedLayout) view.findViewById(R.id.expand_card_protocol);
    preOrderCount = (CommonInputView) view.findViewById(R.id.pre_order_count);
    duringCount = (CommonInputView) view.findViewById(R.id.during_count);
    limitBugCount = (CommonInputView) view.findViewById(R.id.limit_bug_count);
    supportGyms = (CommonInputView) view.findViewById(R.id.support_gyms);
    layoutCardValueDesc = (LinearLayout) view.findViewById(R.id.layout_card_value_desc);
    layoutCardOption = (LinearLayout) view.findViewById(R.id.layout_card_option);
    inputCardProtocol = (CommonInputView) view.findViewById(R.id.input_card_protocol);
    layoutCardDetail = (LinearLayout) view.findViewById(R.id.layout_card_detail);
    view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDeleteCardTpl();
      }
    });
    view.findViewById(R.id.input_card_protocol).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onOpenProtocol();
      }
    });
    view.findViewById(R.id.civ_input_card_desc).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDesc();
      }
    });
    view.findViewById(R.id.expand_card_protocol).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onProtocol();
      }
    });
    view.findViewById(R.id.support_gyms).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onSupportGyms();
      }
    });
    view.findViewById(R.id.pre_order_count).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onPreOrderCount();
      }
    });
    view.findViewById(R.id.during_count).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onDuringCount();
      }
    });
    view.findViewById(R.id.limit_bug_count).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onLimitCard();
      }
    });

    delegatePresenter(presenter, this);
    presenter.setCardTpl(cardTpl);
    setToolbar();
    SmoothScrollLinearLayoutManager layoutManager =
      new SmoothScrollLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    initBuyLimit();
    if (cardTpl != null) {
      initView();
    }
    recycleview.setLayoutManager(layoutManager);
    recycleview.addItemDecoration(new FlexibleItemDecoration(getContext()).withOffset(10)
      .withLeftEdge(true)
      .withRightEdge(true));
    recycleview.setAdapter(comonAdapter);
    expandCardProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          inputCardProtocol.setVisibility(View.VISIBLE);
          initCardProtocol();
        } else {
          inputCardProtocol.setVisibility(View.GONE);
        }
        editInfoListener(false);
      }
    });
    expandSettingLimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        tvCardAppend.setVisibility(b ? View.VISIBLE : View.GONE);
        cardLimit.is_limit=b;
        editInfoListener(false);
      }
    });
    expandSettingLimit.setOnClickListener(view1 -> {
      hasAddPermission();
    });
    onRefresh();
    return view;
  }

  private void initBus() {
    RxBus.getBus()
      .register(QrEvent.class)
      .compose(this.<QrEvent>doWhen(FragmentEvent.RESUME))
      .compose(this.<QrEvent>bindToLifecycle())
      .subscribe(new BusSubscribe<QrEvent>() {
        @Override public void onNext(QrEvent cardList) {
          Uri toUri;
          if (!gymWrapper.inBrand()) {
            toUri = AppUtils.getRouterUri(getContext(), "card/cardtpl/list/");
          } else {
            toUri = AppUtils.getRouterUri(getContext(), "card/brand/cardtpl/list/");
          }
          //getActivity().getSupportFragmentManager().popBackStack(null, 1);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!getActivity().isDestroyed()) {
              getActivity().finish();
            }
          }
          routeTo(toUri, null);
        }
      });
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    civInputCardname.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvCardtplName.setText(s.toString());
      }

      @Override public void afterTextChanged(Editable s) {

      }
    });
  }

  private void initView() {

    layoutCardValueDesc.setVisibility(View.VISIBLE);
    layoutCardOption.setVisibility(View.VISIBLE);
    civInputCardname.setVisibility(View.VISIBLE);

    civInputCardDesc.setVisibility(View.VISIBLE);
    expandSettingLimit.setVisibility(View.VISIBLE);
    expandCardProtocol.setVisibility(View.VISIBLE);
    btnDel.setVisibility(View.VISIBLE);
    if (cardTpl.is_enable) {
      btnDel.setText(getResources().getString(R.string.stop_card_tpl));
      isEnable(true);
    } else {
      btnDel.setText(getResources().getString(R.string.resume_card_tpl));
      isEnable(false);
    }
    onCheckPermission();
    civInputCardname.setContent(cardTpl.getName());
    body.description = cardTpl.getDescription();
    civInputCardDesc.setContent(TextUtils.isEmpty(cardTpl.getDescription()) ? "选填" : "查看");
    expandSettingLimit.setExpanded(cardTpl.is_limit());
    expandCardProtocol.setExpanded(cardTpl.is_open_service_term);
    if (cardTpl.is_limit) {
      preOrderCount.setContent(String.valueOf(cardTpl.getPre_times()));
      if (cardTpl.getMonth_times() > 0) {
        duringCount.setContent("每月," + cardTpl.getMonth_times() + "节");
        cardLimit.month_times = cardTpl.getMonth_times();
      } else if (cardTpl.getWeek_times() > 0) {
        duringCount.setContent("每周," + cardTpl.getWeek_times() + "节");
        cardLimit.week_times = cardTpl.getWeek_times();
      } else {
        duringCount.setContent("每天," + cardTpl.getDay_times() + "节");
        cardLimit.day_times = cardTpl.getDay_times();
      }
      limitBugCount.setContent(cardTpl.getBuy_limit() == 0 ? getString(R.string.buy_card_no_limit)
        : getString(R.string.card_count_unit, cardTpl.getBuy_limit()));
    }
  }

  public void onCheckPermission() {
    if (cardTpl.getShopIds().size() > 1) {
      isEnable(false);
    } else {
      if (!permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE)) {
        isEnable(false);
      }
    }
  }


  public boolean hasAddPermission() {
    boolean ret = true;
    if (cardTpl == null){
      ret = permissionModel.checkAllGym(PermissionServerUtils.CARDSETTING_CAN_WRITE);
      if (!ret) {
        showAlert("您没有会员卡新增权限");
      }
      return ret;
    }
    if (cardTpl.getShopIds().size() > 1) {
      showAlert(getString(R.string.alert_edit_cardtype_link_manage));
      return false;
    }
    ret = permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE);
    if (!ret) {
      showAlert("您没有会员卡新增权限");
    }
    return ret;
  }
public boolean hasAddPermission(boolean toast) {
    boolean ret = true;
    if (cardTpl == null){
      ret = permissionModel.checkAllGym(PermissionServerUtils.CARDSETTING_CAN_WRITE);
      if (!ret && toast) {
        showAlert("您没有会员卡新增权限");
      }
      return ret;
    }
    if (cardTpl.getShopIds().size() > 1 && toast) {
      showAlert(getString(R.string.alert_edit_cardtype_link_manage));
      return false;
    }
    ret = permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE);
    if (!ret && toast) {
      showAlert("您没有会员卡新增权限");
    }
    return ret;
  }

  //无权限或停用时所有选项不能点击
  protected void isEnable(boolean isEnable) {
    civInputCardname.setEnable(isEnable);
    civInputCardname.setCanClick(!isEnable);
    civInputCardDesc.setEnable(isEnable);

    preOrderCount.setEnable(isEnable);
    limitBugCount.setEnable(isEnable);
    duringCount.setEnable(isEnable);

    expandSettingLimit.setEnabled(isEnable);
    expandCardProtocol.setEnabled(hasAddPermission(false));

    for (int i = 0; i < comonAdapter.getItemCount(); i++) {
      comonAdapter.getItem(i).setEnabled(isEnable);
    }
    comonAdapter.notifyDataSetChanged();
  }

  private void editInfoListener(boolean isEdit) {

    if (cardTpl == null) {
      return;
    }
    isShouldSave = isEdit
      || !civInputCardname.getContent().equals(cardTpl.getName())
      || expandSettingLimit.isExpanded() != cardTpl.is_limit
      || expandCardProtocol.isExpanded() != cardTpl.is_open_service_term;
    if (isShouldSave && toolbar.getMenu().size() <= 0) {
      toolbar.getMenu().clear();
      toolbar.inflateMenu(R.menu.menu_save);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          if (TextUtils.isEmpty(civInputCardname.getContent())) {
            showAlert(R.string.e_card_name_empty);
            return false;
          }
          body.name = civInputCardname.getContent();
          cardLimit.is_limit = expandSettingLimit.isExpanded();
          if (cardLimit.is_limit) {
            body.is_limit = cardLimit.is_limit;
            body.day_times = cardLimit.day_times;
            body.buy_limit = cardLimit.buy_limit;
            body.pre_times = cardLimit.pre_times;
            body.week_times = cardLimit.week_times;
            body.month_times = cardLimit.month_times;
          }
          body.is_open_service_term = expandCardProtocol.isExpanded();
          presenter.editCardTpl(body);
          return false;
        }
      });
    }
  }


  public void initCardProtocol() {
    if (cardTpl.has_service_term) {
      inputCardProtocol.setLabel(getResources().getString(R.string.card_protocol_content));
    } else {
      inputCardProtocol.setVisibility(View.GONE);
      Intent intent = new Intent(getActivity(), QRActivity.class);
      if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE,
        cardTpl.getShopIds())) {
        if (!gymWrapper.inBrand()) {
          intent.putExtra(QRActivity.LINK_MODULE,
            getResources().getString(R.string.qr_code_2web_add_card_term, cardTpl.id));
        } else {
          intent.putExtra(QRActivity.LINK_MODULE,
            getResources().getString(R.string.qr_code_2web_multi_card_add, gymWrapper.brand_id(),
              cardTpl.id));
        }
      } else {
        showAlert(R.string.alert_edit_cardtype_no_permission);
      }
      expandCardProtocol.setExpanded(false);
      getContext().startActivity(intent);
    }
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  private void initBuyLimit() {
    RxBusAdd(EventLimitBuyCount.class).subscribe(new Action1<EventLimitBuyCount>() {
      @Override public void call(EventLimitBuyCount eventLimitBuyCount) {
        cardLimit.buy_limit = eventLimitBuyCount.buy_count;
        limitBugCount.setContent(eventLimitBuyCount.text);
        syncLimit();
        editInfoListener(true);
      }
    });
  }

 public void onDeleteCardTpl() {
    if (presenter.isCardTplEnable()) {
      if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_DELETE,
        cardTpl.getShopIds())) {
        alertDisableCardtpl();
      } else {
        showAlert(
          getResources().getString(R.string.delete_cardtpl_no_permission));
      }
    } else {
      if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE,
        cardTpl.getShopIds())) {
        alertEnableCardtpl();
      } else {
        showAlert(
          getResources().getString(R.string.add_cardtpl_no_permission));
      }
    }
  }

  public void setToolbar() {
    toolbar.setNavigationIcon(cn.qingchengfit.widgets.R.drawable.vd_navigate_before_white_24dp);
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup && isfitSystemPadding()) {
      ((ViewGroup) toolbar.getParent()).setPadding(0, MeasureUtils.getStatusBarHeight(getContext()),
        0, 0);
    }
    toolbar.setSaveEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (!civInputCardname.isClickable()) {
          getActivity().onBackPressed();
          return;
        }
        if (isShouldSave) {
          new MaterialDialog.Builder(getActivity()).content(
            getResources().getString(R.string.edit_card_tpl_back_tips))
            .autoDismiss(true)
            .positiveColorRes(cn.qingchengfit.widgets.R.color.orange)
            .negativeColorRes(cn.qingchengfit.widgets.R.color.text_black)
            .negativeText(cn.qingchengfit.widgets.R.string.pickerview_cancel)
            .positiveText(cn.qingchengfit.widgets.R.string.pickerview_submit)
            .onPositive((dialog, which) -> {
              if (which == DialogAction.POSITIVE) {
                if (TextUtils.isEmpty(civInputCardname.getContent())) {
                  showAlert(R.string.e_card_name_empty);
                  return;
                }
                cardLimit.is_limit = expandSettingLimit.isExpanded();
                body.name = civInputCardname.getContent();
                if (cardLimit.is_limit) {
                  body.is_limit = cardLimit.is_limit;
                  body.day_times = cardLimit.day_times;
                  body.buy_limit = cardLimit.buy_limit;
                  body.pre_times = cardLimit.pre_times;
                  body.week_times = cardLimit.week_times;
                  body.month_times = cardLimit.month_times;
                }
                body.is_open_service_term = expandCardProtocol.isExpanded();
                presenter.editCardTpl(body);
                return;
              }
            })
            .onNegative((dialog, which) -> getActivity().onBackPressed())
            .build()
            .show();
        } else {
          getActivity().onBackPressed();
        }
      }
    });
    toolbarTitle.setText("会员卡种类详情");
  }

  public void onRefresh() {
    presenter.queryCardtpl();
    presenter.queryCardtplOption();
  }

  @Override public String getFragmentName() {
    return CardTplDetailFragment.class.getName();
  }

  /**
   * 展示底部选择框
   */
  protected void showBottomList() {
    DialogList.builder(getContext())
      .list(getResources().getStringArray(
        presenter.isCardTplEnable() ? R.array.card_tpl_flow : R.array.card_tpl_flow_resume),
        (parent, view, position, id) -> {
          if (position == 0) {//编辑
            //presenter.editCardTpl();
            routeTo(AppUtils.getRouterUri(getContext(), "card/cardtpl/edit"),
              new EditCardTplParams().cardTpl(cardTpl).build());
          } else if (position == 1) {

          }
        })
      .show();
  }

  public void alertDisableCardtpl() {
    DialogUtils.instanceDelDialog(getContext(), "是否确认停用",
      "1.停用后，该会员卡种类无法用于开卡\n2.停用后，该会员卡种类无法用于排课支付\n3.不影响已发行会员卡的使用和显示",
      (dialog, which) -> presenter.disable()).show();
  }

  public void alertEnableCardtpl() {
    DialogUtils.instanceDelDialog(getContext(), "是否确认恢复", (dialog, which) -> presenter.enable())
      .show();
  }

  /**
   * 获取会员卡基本信息
   */
  @Override public void onGetCardTypeInfo(CardTpl card_tpl) {
    cardTpl = card_tpl;
    tvCardId.setText("ID:" + card_tpl.getId());
    tvCardtplName.setText(card_tpl.getName());
    tvCardTplType.setText(
      CardBusinessUtils.getCardTypeCategoryStrHead(card_tpl.getType(), getContext()));
    cardview.setBackground(
      DrawableUtils.generateBg(8, CardBusinessUtils.getDefaultCardbgColor(card_tpl.getType())));
    if (TextUtils.isEmpty(card_tpl.getDescription())) {
      tvCardExpandDesc.setContent("简介：无");
    } else {
      tvCardExpandDesc.setContent(
        getResources().getString(R.string.cardtpl_description, card_tpl.getDescription()));
    }
    tvCardAppend.setText(card_tpl.getLimit());
    tvGymName.setText(card_tpl.getShopNames());
    cardStatus.setVisibility(cardTpl.is_enable ? View.GONE : View.VISIBLE);
    cardStatus.setText("已停用");
    cardStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.red, getContext()));
  }

  @Override public void onGetStandards(List<CardTplOption> cardStandards) {
    comonAdapter.clear();
    comonAdapter.setStatus(presenter.isCardTplEnable() ? 0 : 1);
    if (cardStandards != null) {

      for (CardTplOption cardStandard : cardStandards) {
        comonAdapter.addItem(new CardtplOptionItem(cardStandard, presenter.getCardCate()));
      }
    }
    if (presenter.isCardTplEnable()) {
      comonAdapter.addItem(new AddCardtplStantardItem());
    }
  }

  private void refreshBtnDel(boolean isStop) {
    if (!isStop) {
      btnDel.setText(getResources().getString(R.string.stop_card_tpl));
      btnDel.setTextColor(getResources().getColor(R.color.danger_red_normal));
    } else {
      btnDel.setText(getResources().getString(R.string.resume_card_tpl));
      btnDel.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
  }

  @Override public void onDelSucceess() {
    hideLoading();
    isEnable(false);
    supportGyms.setEnable(false);
    supportGyms.setClickable(false);
    refreshBtnDel(true);
    presenter.queryCardtpl();
    ToastUtils.show("已停卡");
  }

  @Override public void onResumeOk() {
    hideLoading();
    isEnable(true);
    supportGyms.setEnable(true);
    supportGyms.setClickable(true);
    refreshBtnDel(false);
    presenter.queryCardtpl();
    ToastUtils.show("已恢复");
  }

  @Override public void onStashSuccessed(String uuid) {
    Intent intent = new Intent(getActivity(), QRActivity.class);
    if (!gymWrapper.inBrand()) {
      intent.putExtra(QRActivity.LINK_MODULE,
        QRActivity.MODULE_ADD_CARD_PROTOCOL + "?" + PARAMS_KEY + uuid);
    } else {
      try {
        intent.putExtra(QRActivity.LINK_MODULE, URLEncoder.encode(QRActivity.MULTI_CARD_TPL
          + "?"
          + "brand_id="
          + gymWrapper.brand_id()
          + "&"
          + PARAMS_KEY
          + uuid, "utf-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    expandCardProtocol.setExpanded(false);
    getContext().startActivity(intent);
  }

  @Override public String getCardName() {
    return civInputCardname.getContent().trim();
  }

  @Override public List<CardTplOption> getCardTplOptions() {
    List<CardTplOption> options = new ArrayList<>();
    for (int i = 0; i < comonAdapter.getItemCount(); i++) {
      IFlexible iFlexible = comonAdapter.getItem(i);
      if (iFlexible instanceof CardtplOptionItem) {
        options.add(((CardtplOptionItem) iFlexible).getOption());
      }
    }
    return options;
  }

  @Override public CardLimit getCardLimit() {
    return cardLimit;
  }

  @Override public String getDescription() {
    return desc;
  }

  @Override public String getSupportShopId() {
    return TextUtils.isEmpty(supportShopStr) ? gymWrapper.shop_id() : supportShopStr;
  }

  @Override public boolean isOpenCardTerm() {
    return expandCardProtocol.isExpanded();
  }

  private void syncLimit() {
    sb.delete(0, sb.length());
    sb.append("限制: ");
    if (cardLimit.pre_times > 0) {
      sb.append(getResources().getString(R.string.card_limit_pre_times,
        tempChargeLimit(cardLimit.pre_times)));
      sb.append(",");
    }
    if (cardLimit.day_times > 0) {
      sb.append(getResources().getString(R.string.card_can_work_everyday, cardLimit.day_times));
      sb.append(",");
    }
    if (cardLimit.week_times > 0) {
      sb.append(getResources().getString(R.string.card_can_work_everyweek, cardLimit.week_times));
      sb.append(",");
    }
    if (cardLimit.month_times > 0) {
      sb.append(getResources().getString(R.string.card_can_work_month, cardLimit.month_times));
      sb.append(",");
    }
    if (cardLimit.buy_limit > 0) {
      sb.append(getResources().getString(R.string.buylimit, tempChargeLimit(cardLimit.buy_limit)));
    }
    tvCardAppend.setText(sb);
  }

  private String tempChargeLimit(int i) {
    return i == 0 ? "不限" : String.valueOf(i);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case 2:
          desc = IntentUtils.getIntentString(data);
          break;
        case 3:
          int circ_type = Integer.parseInt(IntentUtils.getIntentString(data, 0));
          int circ_time = Integer.parseInt(IntentUtils.getIntentString(data, 1));
          duringCount.setContent(getResources().getStringArray(R.array.class_frequent)[circ_type]
            + ","
            + circ_time
            + "次");

          if (circ_type == 0) {
            cardLimit.day_times = circ_time;
          } else if (circ_type == 1) {
            cardLimit.week_times = circ_time;
          } else if (circ_type == 2) {
            cardLimit.month_times = circ_time;
          }
          syncLimit();
          editInfoListener(true);
          break;
        case 4:
          onSelectSupportGyms(data.getParcelableArrayListExtra(IntentUtils.RESULT));
          break;
      }
    }
  }

  public void onSelectSupportGyms(List<Shop> shops) {
    String ids = "";
    if (shops != null) {
      for (int i = 0; i < shops.size(); i++) {
        if (i < shops.size() - 1) {
          ids = TextUtils.concat(ids, shops.get(i).id, ",").toString();
        } else {
          ids = TextUtils.concat(ids, shops.get(i).id).toString();
        }
      }
      supportShopStr = ids;
      supportGyms.setContent(shops.size() + "家");
      presenter.qcFixGyms(new ShopsBody.Builder().shops(supportShopStr).build());
    }
  }


 public void onOpenProtocol() {
    if (cardTpl != null && cardTpl.has_service_term) {
      CardProtocolActivity.startWeb(cardTpl.card_tpl_service_term.content_link, getContext(), true,
        "", cardTpl);
    }
  }

 public void onDesc() {
    if (!hasAddPermission()) return;
    routeTo("common", "/input/",
      new CommonInputParams().content(body.description).title("填加简介").hint("填写会员卡简介").build());
    editInfoListener(true);
  }

 public void onProtocol() {
    if (!hasAddPermission()) return;
  }

 public void onSupportGyms() {
    if (cardTpl != null && cardTpl.getShopIds() != null) {
      MutiChooseGymFragment.start(CardTplDetailFragment.this, false,
        (ArrayList<String>) cardTpl.getShopIds(), PermissionServerUtils.CARDSETTING_CAN_WRITE, 4);
    } else if (!TextUtils.isEmpty(supportShopStr)) {
      MutiChooseGymFragment.start(CardTplDetailFragment.this, false,
        (ArrayList<String>) StringUtils.Str2List(supportShopStr),
        PermissionServerUtils.CARDSETTING_CAN_WRITE, 4);
    } else {
      MutiChooseGymFragment.start(CardTplDetailFragment.this, false, null,
        PermissionServerUtils.CARDSETTING_CAN_WRITE, 4);
    }
  }

  //可提前预约课程数

  public void onPreOrderCount() {
    if (!hasAddPermission()) return;
    List<String> preList = new ArrayList<>();
    int i = 0;
    while (i < 100) {
      preList.add(String.valueOf(i));
      i++;
    }
    SimpleScrollPicker simpleScrollPicker = new SimpleScrollPicker(getContext());
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        preOrderCount.setContent(Integer.toString(pos));
        cardLimit.pre_times = pos;
        syncLimit();
        editInfoListener(true);
      }
    });

    simpleScrollPicker.show(preList, cardLimit.pre_times);
  }

  //单位时间可上课程数

  public void onDuringCount() {
    if (!hasAddPermission()) return;
    ClassLimitBottomFragment.start(this, 3);
  }

  //每个会员限购张数

  public void onLimitCard() {
    if (!hasAddPermission()) return;
    BottomBuyLimitFragment.newInstance(cardLimit.buy_limit).show(getFragmentManager(), "");
  }

  @Override public boolean onItemClick(int i) {
    if (!hasAddPermission()) return true;
    IFlexible item = comonAdapter.getItem(i);
    if (item instanceof CardtplOptionItem) {
      //会员卡价格修改
      routeTo("/cardtpl/option/",
        new CardTplOptionParams().cardTplOption(((CardtplOptionItem) item).getOption()).cardCate(presenter.getCardCate()).build());
    } else if (item instanceof AddCardtplStantardItem) {
      //新增会员卡价格
      routeTo("/cardtpl/option/add/",
        new CardtplOptionAddParams().cardTplId(presenter.getCardtplId())
          .cardCate(presenter.getCardCate())
          .build());
    }
    return true;
  }

  @Override public void onDestroyView() {
    if (obEvent != null) {
      RxBus.getBus().unregister(EventTxT.class.getName(), obEvent);
    }
    super.onDestroyView();
  }
}
