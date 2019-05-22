package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.event.EventBatchPayCard;
import cn.qingchengfit.saasbase.cards.item.BatchCardChooseTypeCountItem;
import cn.qingchengfit.saasbase.cards.item.BatchPayCardHeaderItem;
import cn.qingchengfit.saasbase.cards.item.BatchPayCardItem;
import cn.qingchengfit.saasbase.cards.presenters.CardTypeListPresenter;
import cn.qingchengfit.saasbase.course.batch.bean.CardTplBatchShip;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.views.UseStaffAppFragmentFragment;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.DialogList;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "card", path = "/card/batch/choose/") public class BatchPayCardFragment
    extends SaasBaseFragment
    implements CardTypeListPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

  @Need ArrayList<Rule> rules;
  @Need ArrayList<CardTplBatchShip> cardTplBatchShips;
  @Need Integer maxCount = 1;
  @Need Boolean multiPrice;
  @Need Boolean isPrivate;//true- 私教， false 团课
  @Need Boolean isAdd; //true -添加，false 编辑

  @Inject IPermissionModel permissionModel;
  TextView toolbarTitile;
  Toolbar toolbar;
  RecyclerView recyclerview;
  SwipeRefreshLayout srl;
  @Inject CardTypeListPresenter presenter;
  private HashMap<String, HashMap<Integer, String>> cardCost = new HashMap();
  private List<AbstractFlexibleItem> mDatas = new ArrayList<>();
  private CommonFlexAdapter commonFlexAdapter;
  private LinearLayout llBottom;
  private TextView textBottom;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
    if (rules != null && rules.size() > 0) {
      cardCost.clear();
      for (int i = 0; i < rules.size(); i++) {
        if (!rules.get(i).channel.equals(Configs.CHANNEL_CARD)) continue;
        Rule rule = rules.get(i);
        if (cardCost.get(rule.card_tpl_id) != null) {
          cardCost.get(rule.card_tpl_id).put(rule.from_number, rule.cost);
        } else {
          HashMap<Integer, String> c = new HashMap<>();
          c.put(rule.from_number, rule.cost);
          cardCost.put(rule.card_tpl_id, c);
        }
      }
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_saas_batch_pay_by_cards, container, false);
    llBottom = view.findViewById(R.id.ll_bottom_add);
    textBottom = view.findViewById(R.id.tv_bottom_content);
    textBottom.setText("+ 添加会员卡种类");
    delegatePresenter(presenter, this);
    toolbar = view.findViewById(R.id.toolbar);
    toolbarTitile = view.findViewById(R.id.toolbar_title);
    recyclerview = view.findViewById(R.id.recyclerview);
    srl = view.findViewById(R.id.srl);
    srl.setRefreshing(true);
    srl.setOnRefreshListener(this);
    initToolbar(toolbar);
    if (commonFlexAdapter == null) {
      commonFlexAdapter = new CommonFlexAdapter(mDatas, this);
      commonFlexAdapter.setAutoCollapseOnExpand(false).setAutoScrollOnExpand(true);
    }
    recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
    recyclerview.setAdapter(commonFlexAdapter);
    textBottom.setOnClickListener(v -> {
      if (AppUtils.getCurApp(getContext()) == 0) {
        UseStaffAppFragmentFragment.newInstance().show(getChildFragmentManager(), "");
      } else {
        new DialogList(getContext()).list(getResources().getStringArray(R.array.cardtype_category),
            (parent, view1, position, id) -> onMenuAdd(position)).show();
      }
    });
    llBottom.setVisibility(View.VISIBLE);
    onRefresh();
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("设置会员卡约课价格");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        save();
        return false;
      }
    });
  }

  public void onMenuAdd(int position) {
    if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE)) {
      routeTo("/cardtpl/add/",
          new cn.qingchengfit.saasbase.cards.views.CardtplAddParams().cardCategory(position + 1)
              .build());
    } else {
      DialogUtils.showAlert(getContext(),
          getResources().getString(R.string.add_cardtpl_no_permission));
    }
  }

  /**
   * 保存
   */
  private void save() {
    ArrayList<Rule> rules = new ArrayList<>();
    int count = 0;
    for (int i = 0; i < commonFlexAdapter.getExpandedItems().size(); i++) {
      BatchPayCardHeaderItem item =
          (BatchPayCardHeaderItem) commonFlexAdapter.getExpandedItems().get(i);
      if (item.isExpanded()) {
        count++;
        if (item.getCardtype() == Configs.CATEGORY_DATE) {
          Rule rrr = new Rule();
          rrr.card_tpl_name = item.getCardname();
          rrr.card_tpl_id = item.getCardId();
          rrr.channel = Configs.CHANNEL_CARD;
          rrr.from_number = 1;
          rrr.to_number = maxCount + 1;
          rules.add(rrr);
        } else {
          //非期限卡
          if (multiPrice) {//私教
            List<Rule> rg = new ArrayList<>();
            for (int j = 0; j < item.getSubItems().size(); j++) {
              Rule rrr = new Rule();
              rrr.card_tpl_name = item.getCardname();
              rrr.card_tpl_id = item.getCardId();
              rrr.channel = Configs.CHANNEL_CARD;
              rrr.from_number = j + 1;
              rrr.to_number = j + 2;
              rrr.cost = item.getSubItems().get(j).getCost();
              if (TextUtils.isEmpty(rrr.cost)) {
                ToastUtils.show(item.getCardname() + "未填写完整");
                return;
              }

              rg.add(rrr);
            }
            rules.addAll(rg);
          } else {
            Rule rp = new Rule();
            rp.channel = Configs.CHANNEL_CARD;
            rp.from_number = 1;
            rp.to_number = maxCount + 1;
            rp.card_tpl_id = item.getCardId();
            rp.card_tpl_name = item.getCardname();
            if (item.getSubItems() != null && item.getSubItems().size() > 0) {
              rp.cost = item.getSubItems().get(0).getCost();
            }
            if (TextUtils.isEmpty(rp.cost)) {
              ToastUtils.show(item.getCardname() + "未填写完整");
              return;
            }
            rules.add(rp);
          }
        }
      }
    }
    RxBus.getBus().post(new EventBatchPayCard(rules));
    popBack();
  }

  @Override public String getFragmentName() {
    return BatchPayCardFragment.class.getName();
  }

  public void onGetData(List<CardTpl> card_tpls) {
    srl.setRefreshing(false);
    mDatas.clear();
    totalCount = new int[] { 0, 0, 0, 0 };
    openCount = new int[] { 0, 0, 0, 0 };
    commonFlexAdapter.clear();
    if (card_tpls != null && !card_tpls.isEmpty()) {
      if (cardTplBatchShips != null) {
        for (int i = 0; i < cardTplBatchShips.size(); i++) {
          CardTplBatchShip cardTplBatchShip = cardTplBatchShips.get(i);
          if (card_tpls.contains(cardTplBatchShip)) {
            continue;
          } else {
            CardTpl cardTpl =
                new CardTpl(cardTplBatchShip.name, cardTplBatchShip.type, "", cardTplBatchShip.id,
                    "");
            cardTpl.is_enable = false;
            card_tpls.add(cardTpl);
          }
        }
      }
      Collections.sort(card_tpls, new Comparator<CardTpl>() {
        @Override public int compare(CardTpl o1, CardTpl o2) {
          int result = o1.getType() - o2.getType();
          if (result == 0) {
            if (cardCost.containsKey(o1.getId()) && cardCost.containsKey(o2.getId())) {
              return 0;
            } else if (cardCost.containsKey(o1.getId())) {
              return -1;
            } else if (cardCost.containsKey(o2.getId())) {
              return 1;
            }
          }
          return result;
        }
      });

      for (int i = 0; i < card_tpls.size(); i++) {
        CardTpl cardTpl = card_tpls.get(i);
        BatchPayCardHeaderItem itemData = new BatchPayCardHeaderItem(cardTpl);
        switch (cardTpl.getType()) {
          case 1:
          case 2:
            if (multiPrice) {
              for (int j = 0; j < maxCount; j++) {
                if (cardCost.containsKey(cardTpl.getId())) {
                  try {
                    String money = cardCost.get(cardTpl.getId()).get(j + 1);
                    itemData.addChild(new BatchPayCardItem(money, j, cardTpl, multiPrice));
                  } catch (Exception e) {
                  }
                } else {
                  itemData.addChild(new BatchPayCardItem("", j, cardTpl, multiPrice));
                }
              }
              if (cardTplBatchShips != null
                  && cardTplBatchShips.contains(cardTpl)
                  && cardTplBatchShips.get(cardTplBatchShips.indexOf(cardTpl)).status == 2) {
                itemData.setHasOrdered(true);
              }
            } else {
              String cost = "";
              if (cardCost.containsKey(cardTpl.getId())) {
                cost = cardCost.get(cardTpl.getId()).get(1);
              }
              itemData.addChild(new BatchPayCardItem(cost, 0, cardTpl, multiPrice));

              if (cardTplBatchShips != null
                  && cardTplBatchShips.contains(cardTpl)
                  && cardTplBatchShips.get(cardTplBatchShips.indexOf(cardTpl)).status == 2) {
                itemData.setHasOrdered(true);
              }
            }
            break;
          case 3:
            itemData.addChild(new BatchPayCardItem("", 0, cardTpl, multiPrice));
            if (cardTplBatchShips != null
                && cardTplBatchShips.contains(cardTpl)
                && cardTplBatchShips.get(cardTplBatchShips.indexOf(cardTpl)).status == 2) {
              itemData.setHasOrdered(true);
            }
            break;
          default:
            break;
        }
        if (cardCost.containsKey(cardTpl.getId())) {
          itemData.setExpanded(true);
          openCount[cardTpl.getType()]++;
        }
        totalCount[cardTpl.getType()]++;
        mDatas.add(itemData);
      }
      if (mDatas.size() == 0) {
        commonFlexAdapter.addItem(new CommonNoDataItem(R.drawable.vd_card_empty, "您没有可用的会员卡"));
      } else {
        for (int i = 0; i < mDatas.size(); i++) {
          if (mDatas.get(i) instanceof BatchPayCardHeaderItem) {
            BatchPayCardHeaderItem item = (BatchPayCardHeaderItem) mDatas.get(i);
            if (i == 0
                || ((BatchPayCardHeaderItem) mDatas.get(i - 1)).getCardtype()
                != item.getCardtype()) {
              item.setHeader(new BatchCardChooseTypeCountItem(item.getCardtype(),
                  totalCount[item.getCardtype()], openCount[item.getCardtype()]));
            }
          }
        }
      }
      commonFlexAdapter.updateDataSet(mDatas);
    } else {
      List<CommonNoDataItem> commonNoDataItems = new ArrayList<>();
      commonNoDataItems.add(
          new CommonNoDataItem(R.drawable.vd_img_empty_universe, "没有可用的会员卡种类", "请先添加会员卡种类"));
      commonFlexAdapter.updateDataSet(commonNoDataItems);
    }
  }

  private int totalCount[] = new int[] { 0, 0, 0, 0 };
  private int openCount[] = new int[] { 0, 0, 0, 0 };

  public void notifyTypeItemOpenCount(int type) {
    List headerItems = commonFlexAdapter.getHeaderItems();
    for (int i = 0; i < headerItems.size(); i++) {
      Object o = headerItems.get(i);
      if (o instanceof BatchCardChooseTypeCountItem
          && ((BatchCardChooseTypeCountItem) o).getType() == type) {

        ((BatchCardChooseTypeCountItem) o).setOpenCount(
            openCount[((BatchCardChooseTypeCountItem) o).getType()]);
        commonFlexAdapter.notifyItemChanged(commonFlexAdapter.getGlobalPositionOf((IFlexible) o));
      }
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item instanceof BatchPayCardHeaderItem) {
      int cardtype = ((BatchPayCardHeaderItem) item).getCardtype();
      if (((BatchPayCardHeaderItem) item).isExpanded()) {
        openCount[cardtype]++;
      } else {
        openCount[cardtype]--;
      }
      notifyTypeItemOpenCount(cardtype);
      commonFlexAdapter.notifyItemChanged(position);
    }
    return false;
  }

  @Override public void onDoneCardtplList() {
    List<CardTpl> cardTplByType = presenter.getCardTplByType(0);
    List<CardTpl> filterCardTplEnable = new ArrayList<>();
    if (!cardTplByType.isEmpty()) {
      for (CardTpl tpl : cardTplByType) {
        if (tpl.is_enable) {
          filterCardTplEnable.add(tpl);
        }
      }
    }
    onGetData(filterCardTplEnable);
  }

  @Override public void onRefresh() {
    presenter.queryCardTypeNoNeedPermission(isPrivate, isAdd);
  }
}