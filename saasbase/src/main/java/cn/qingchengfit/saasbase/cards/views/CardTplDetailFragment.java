package cn.qingchengfit.saasbase.cards.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardLimit;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.event.EventLimitBuyCount;
import cn.qingchengfit.saasbase.cards.item.AddCardtplStantardItem;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionItem;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.presenters.CardTplDetailPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saasbase.network.model.Shop;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.saasbase.utils.StringUtils;
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
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
 * Created by Paper on 2017/8/23.
 */
@Leaf(module = "card", path = "/cardtpl/detail/") public class CardTplDetailFragment
    extends SaasBaseFragment
    implements CardTplDetailPresenter.MVPView, FlexibleAdapter.OnItemClickListener {

  private final static String PARAMS_KEY = "uuid=";

  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.tv_card_tpl_type) TextView tvCardTplType;
  @BindView(R2.id.tv_cardtpl_name) TextView tvCardtplName;
  @BindView(R2.id.tv_gym_name) TextView tvGymName;
  @BindView(R2.id.tv_card_id) TextView tvCardId;
  @BindView(R2.id.img_stutus) TextView cardStatus;
  @BindView(R2.id.cardview) RelativeLayout cardview;
  @BindView(R2.id.recycleview) RecyclerView recycleview;
  @BindView(R2.id.btn_del) TextView btnDel;
  @BindView(R2.id.tv_card_append) TextView tvCardAppend;
  @BindView(R2.id.tv_card_expand_desc) ExpandTextView tvCardExpandDesc;
  @BindView(R2.id.civ_input_card_name) protected CommonInputView civInputCardname;

  @Inject public CardTplDetailPresenter presenter;
  @Inject GymWrapper gymWrapper;
  //@Inject SerPermisAction serPermisAction;
  @Inject IPermissionModel permissionModel;
  @Need public CardTpl cardTpl;
  CommonFlexAdapter comonAdapter;
  @BindView(R2.id.civ_input_card_desc) CommonInputView civInputCardDesc;
  @BindView(R2.id.expand_setting_limit) ExpandedLayout expandSettingLimit;
  @BindView(R2.id.expand_card_protocol) ExpandedLayout expandCardProtocol;
  @BindView(R2.id.pre_order_count) CommonInputView preOrderCount;
  @BindView(R2.id.during_count) CommonInputView duringCount;
  @BindView(R2.id.limit_bug_count) CommonInputView limitBugCount;
  @BindView(R2.id.support_gyms) CommonInputView supportGyms;
  protected CardLimit cardLimit = new CardLimit();
  @BindView(R2.id.layout_card_value_desc) LinearLayout layoutCardValueDesc;
  @BindView(R2.id.layout_card_option) LinearLayout layoutCardOption;
  @BindView(R2.id.input_card_protocol) CommonInputView inputCardProtocol;
  public String desc;
  protected String supportShopStr;
  private CardtplBody body = new CardtplBody();
  private boolean isShouldSave;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    comonAdapter = new CommonFlexAdapter(new ArrayList(), this);

    RxBus.getBus()
        .register(EventSaasFresh.CardTplList.class)
        .subscribe(new BusSubscribe<EventSaasFresh.CardTplList>() {
          @Override public void onNext(EventSaasFresh.CardTplList cardList) {
            if (getActivity() != null) {
              getActivity().onBackPressed();
            }
          }
        });

    RxBus.getBus()
        .register(EventTxT.class)
        .subscribe(new BusSubscribe<EventTxT>() {
          @Override public void onNext(EventTxT eventTxT) {
            body.description = eventTxT.txt;
            desc = eventTxT.txt;
            civInputCardDesc.setContent("查看");
          }
        });
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cardtpl_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    presenter.setCardTpl(cardTpl);
    setToolbar();
    SmoothScrollLinearLayoutManager layoutManager =
        new SmoothScrollLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    //layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
    //  @Override public int getSpanSize(int position) {
    //    return 1;
    //  }
    //});
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
        editInfoListener(false);
      }
    });
    onRefresh();
    return view;
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

  private void initView(){

    layoutCardValueDesc.setVisibility(View.VISIBLE);
    layoutCardOption.setVisibility(View.VISIBLE);
    civInputCardname.setVisibility(View.VISIBLE);
    civInputCardDesc.setVisibility(View.VISIBLE);
    expandSettingLimit.setVisibility(View.VISIBLE);
    expandCardProtocol.setVisibility(View.VISIBLE);
    btnDel.setVisibility(View.VISIBLE);
    if(cardTpl.is_enable){
      btnDel.setText(getResources().getString(R.string.stop_card_tpl));
      isEnable(true);
    }else{
      btnDel.setText(getResources().getString(R.string.resume_card_tpl));
      isEnable(false);
    }
    if (!permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE)){
      isEnable(false);
    }
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

  //无权限或停用时所有选项不能点击
  protected void isEnable(boolean isEnable){
    civInputCardname.setEnable(isEnable);
    civInputCardDesc.setEnable(isEnable);
    civInputCardname.setClickable(isEnable);
    civInputCardDesc.setClickable(isEnable);

    expandSettingLimit.setEnabled(isEnable);
    expandCardProtocol.setEnabled(isEnable);

    for (int i = 0; i < comonAdapter.getItemCount(); i++) {
      comonAdapter.getItem(i).setEnabled(isEnable);
    }
    comonAdapter.notifyDataSetChanged();
  }

  private void editInfoListener(boolean isEdit){

    if (cardTpl == null){
      return;
    }
    isShouldSave = isEdit
        || !civInputCardname.getContent().equals(cardTpl.getName())
        || expandSettingLimit.isExpanded() != cardTpl.is_limit
        || expandCardProtocol.isExpanded() != cardTpl.is_open_service_term;
    if (isShouldSave && toolbar.getMenu().size() <= 0) {
      toolbar.inflateMenu(R.menu.menu_save);
      toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          body.name = civInputCardname.getContent();
          cardLimit.is_limit = expandSettingLimit.isExpanded();
          if (cardLimit.is_limit){
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

  //@OnClick(R2.id.civ_input_card_name)
  //public void onName(){
  //  routeTo("common", "/input/",
  //      new CommonInputParams().content(presenter.getCardName())
  //          .title("编辑会员卡种类名称")
  //          .hint("填写会员卡种类名称")
  //          .build());
  //}

  public void initCardProtocol(){
    if (cardTpl.has_service_term) {
      inputCardProtocol.setLabel(getResources().getString(R.string.card_protocol_content));
    }else{
      inputCardProtocol.setVisibility(View.GONE);
      Intent intent = new Intent(getActivity(), QRActivity.class);
      if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE, cardTpl.getShopIds())) {
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
      }
    });
  }

  @OnClick(R2.id.btn_del)
  public void onDeleteCardTpl(){
    if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_DELETE)){
      if (presenter.isCardTplEnable()) {
        alertDisableCardtpl();
      } else {
        alertEnableCardtpl();
      }
    }else{
      DialogUtils.showAlert(getContext(), getResources().getString(R.string.delete_cardtpl_no_permission));
    }
  }

  public void setToolbar() {
    toolbar.setNavigationIcon(cn.qingchengfit.widgets.R.drawable.vd_navigate_before_white_24dp);
    if (!CompatUtils.less21() && toolbar.getParent() instanceof ViewGroup  && isfitSystemPadding()) {
      ((ViewGroup) toolbar.getParent()).setPadding(0,
          MeasureUtils.getStatusBarHeight(getContext()), 0, 0);
    }
    toolbar.setSaveEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (isShouldSave){
          DialogUtils.instanceDelDialog(getContext(), "", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              if (which == DialogAction.POSITIVE){
                cardLimit.is_limit = expandSettingLimit.isExpanded();
                if (cardLimit.is_limit){
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
            }
          }).show();
        }else{
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
            new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//编辑
                  //presenter.editCardTpl();
                  routeTo(AppUtils.getRouterUri(getContext(), "card/cardtpl/edit"),
                      new EditCardTplParams().cardTpl(cardTpl).build());
                } else if (position == 1) {

                }
              }
            })
        .show();
  }

  public void alertDisableCardtpl() {
    DialogUtils.instanceDelDialog(getContext(), "是否确认停用",
        "1.停用后，该会员卡种类无法用于开卡\n2.停用后，该会员卡种类无法用于排课支付\n3.不影响已发行会员卡的使用和显示",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            presenter.disable();
          }
        }).show();
  }

  public void alertEnableCardtpl() {
    DialogUtils.instanceDelDialog(getContext(), "是否确认恢复",
        new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            presenter.enable();
          }
        }).show();
  }

  /**
   * 获取会员卡基本信息
   */
  @Override public void onGetCardTypeInfo(CardTpl card_tpl) {
    tvCardId.setText("ID:" + card_tpl.getId());
    tvCardtplName.setText(card_tpl.getName());
    tvCardTplType.setText(
        CardBusinessUtils.getCardTypeCategoryStrHead(card_tpl.getType(), getContext()));
    cardview.setBackground(
        DrawableUtils.generateBg(8, CardBusinessUtils.getDefaultCardbgColor(card_tpl.getType())));
    if (TextUtils.isEmpty(card_tpl.getDescription())){
      tvCardExpandDesc.setContent("简介：无");
    }else {
      tvCardExpandDesc.setContent(getResources().getString(R.string.cardtpl_description, card_tpl.getDescription()));
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
    if (presenter.isCardTplEnable() ) {
      comonAdapter.addItem(new AddCardtplStantardItem());
    }
  }

  @Override public void onDelSucceess() {
    onFinishAnimation();
    hideLoading();
    ToastUtils.show("已停卡");
  }

  @Override public void onResumeOk() {
    onFinishAnimation();
    hideLoading();
    ToastUtils.show("已恢复");
  }

  @Override public void onStashSuccessed(String uuid) {
    Intent intent = new Intent(getActivity(), QRActivity.class);
    if (!gymWrapper.inBrand()) {
      intent.putExtra(QRActivity.LINK_MODULE,
          QRActivity.MODULE_ADD_CARD_PROTOCOL + "?" + PARAMS_KEY + uuid);
    }else{
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
          break;
        case 4:
          String ids = "";
          ArrayList<Shop> shops = data.getParcelableArrayListExtra(IntentUtils.RESULT);
          if (shops != null) {
            for (int i = 0; i < shops.size(); i++) {
              if (i < shops.size() - 1) {
                ids = TextUtils.concat(ids, shops.get(i).id, ",").toString();
              } else {
                ids = TextUtils.concat(ids, shops.get(i).id).toString();
              }
            }
            supportShopStr = ids;
            //TODO 调这个接口什么意思
            //if (mType != 0) presenter.FixGyms(App.staffId, card_tpl.getId(), ids);
            supportGyms.setContent(shops.size() + "家");
          }
          break;
      }
    }
  }

  @OnClick({ R2.id.pre_order_count, R2.id.during_count, R2.id.limit_bug_count })
  public void onLimit(View v) {
    int i = v.getId();
    if (i == R.id.pre_order_count) {
      onPreOrderCount();
    } else if (i == R.id.during_count) {
      onDuringCount();
    } else if (i == R.id.limit_bug_count) {
      onLimitCard();
    }
  }

  @OnClick({R2.id.input_card_protocol}) public void onOpenProtocol() {
    if (cardTpl != null && cardTpl.has_service_term) {
      CardProtocolActivity.startWeb(cardTpl.card_tpl_service_term.content_link, getContext(), true,
          "", cardTpl);
    }
  }

  @OnClick(R2.id.civ_input_card_desc) public void onDesc() {
    routeTo("common", "/input/",
        new CommonInputParams().content(body.description)
            .title("填加简介")
            .hint("填写会员卡简介")
            .build());
    editInfoListener(true);
  }

  @OnClick(R2.id.expand_card_protocol) public void onProtocol() {

  }

  @OnClick(R2.id.support_gyms) public void onSupportGyms() {
    if (cardTpl != null && cardTpl.getShopIds() != null) {
      MutiChooseGymFragment.start(CardTplDetailFragment.this, false,
          (ArrayList<String>) cardTpl.getShopIds(), PermissionServerUtils.CARDSETTING_CAN_CHANGE,
          4);
    } else if (!TextUtils.isEmpty(supportShopStr)) {
      MutiChooseGymFragment.start(CardTplDetailFragment.this, false,
          (ArrayList<String>) StringUtils.Str2List(supportShopStr),
          PermissionServerUtils.CARDSETTING_CAN_CHANGE, 4);
    } else {
      MutiChooseGymFragment.start(CardTplDetailFragment.this, false, null,
          PermissionServerUtils.CARDSETTING_CAN_WRITE, 4);
    }
  }

  //可提前预约课程数
  public void onPreOrderCount() {
    List<String> preList = new ArrayList<>();
    int i = 1;
    while (i < 100){
     preList.add(String.valueOf(i));
     i++;
    }
    SimpleScrollPicker simpleScrollPicker = new SimpleScrollPicker(getContext());
    simpleScrollPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        preOrderCount.setContent(Integer.toString(pos + 1));
        cardLimit.pre_times = pos + 1;
      }
    });

    simpleScrollPicker.show(preList, cardLimit.pre_times > 0  ? cardLimit.pre_times - 1 : 0);
  }

  //单位时间可上课程数
  public void onDuringCount() {
    ClassLimitBottomFragment.start(this, 3);
  }

  //每个会员限购张数
  public void onLimitCard() {
    BottomBuyLimitFragment.newInstance(cardLimit.buy_limit).show(getFragmentManager(), "");
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = comonAdapter.getItem(i);
    if (!civInputCardname.isEnabled()){
      return false;
    }
    if (item instanceof CardtplOptionItem) {

      //会员卡价格修改
      routeTo("/cardtpl/option/",
          new CardTplOptionParams().cardTplOption(((CardtplOptionItem) item).getOption()).build());
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
    super.onDestroyView();
  }
}
