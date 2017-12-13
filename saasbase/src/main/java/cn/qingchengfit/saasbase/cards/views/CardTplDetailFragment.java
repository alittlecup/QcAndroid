package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.item.AddCardtplStantardItem;
import cn.qingchengfit.saasbase.cards.item.CardtplOptionItem;
import cn.qingchengfit.saasbase.cards.presenters.CardTplDetailPresenter;
import cn.qingchengfit.saasbase.common.views.CommonInputParams;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.DialogList;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

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
  @BindView(R2.id.civ_input_card_name) protected CommonInputView civInputCardname;

  @Inject public CardTplDetailPresenter presenter;
  @Need public CardTpl cardTpl;
  CommonFlexAdapter comonAdapter;
  @BindView(R2.id.civ_input_card_desc) CommonInputView civInputCardDesc;
  @BindView(R2.id.expand_setting_limit) ExpandedLayout expandSettingLimit;
  @BindView(R2.id.expand_card_protocol) ExpandedLayout expandCardProtocol;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    comonAdapter = new CommonFlexAdapter(new ArrayList(), this);
    RxBus.getBus()
        .register(EventSaasFresh.CardList.class)
        .compose(this.<EventSaasFresh.CardList>bindToLifecycle())
        .compose(this.<EventSaasFresh.CardList>doWhen(FragmentEvent.CREATE_VIEW))
        .subscribe(new BusSubscribe<EventSaasFresh.CardList>() {
          @Override public void onNext(EventSaasFresh.CardList cardList) {
            onRefresh();
          }
        });
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cardtpl_detail, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    presenter.setCardTpl(cardTpl);
    initToolbar(toolbar);
    GridLayoutManager layoutManager =
        new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_item_count));
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return 1;
      }
    });
    recycleview.setLayoutManager(layoutManager);
    recycleview.addItemDecoration(new FlexibleItemDecoration(getContext()).withOffset(20)
        .withBottomEdge(true)
        .withRightEdge(true));
    recycleview.setAdapter(comonAdapter);
    onRefresh();
    return view;
  }


  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("会员卡种类详情");
    toolbar.inflateMenu(R.menu.menu_flow);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .subscribe(new BusSubscribe<Void>() {
          @Override public void onNext(Void aVoid) {
            showBottomList();
          }
        });
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
                  routeTo("common", "/input/",
                      new CommonInputParams().content(presenter.getCardName())
                          .title("编辑会员卡种类名称")
                          .hint("填写会员卡种类名称")
                          .build());
                } else if (position == 1) {
                  if (presenter.isCardTplEnable()) {
                    alertDisableCardtpl();
                  } else {
                    alertEnableCardtpl();
                  }
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
    tvCardAppend.setText(card_tpl.getLimit());
    tvGymName.setText(card_tpl.getShopNames());
    cardStatus.setVisibility(cardTpl.is_enable ? View.GONE : View.VISIBLE);
    cardStatus.setBackground(DrawableUtils.generateCardStatusBg(R.color.red, getContext()));

    //limit.setText(card_tpl.getLimit());
    //intro.setText(card_tpl.getDescription());
    //type.setText(CardBusinessUtils.getCardTypeCategoryStr(card_tpl.getType(), getContext()));
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

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = comonAdapter.getItem(i);

    if (item instanceof CardtplOptionItem) {

      //会员卡价格修改
      routeTo("/cardtpl/option/",
          new cn.qingchengfit.saasbase.cards.views.CardTplOptionParams().cardTplOption(
              ((CardtplOptionItem) item).getOption()).build());
    } else if (item instanceof AddCardtplStantardItem) {
      //新增会员卡价格
      routeTo("/cardtpl/option/add/",
          new cn.qingchengfit.saasbase.cards.views.CardtplOptionAddParams().cardTplId(
              presenter.getCardtplId()).cardCate(presenter.getCardCate()).build());
    }
    return true;
  }
}
