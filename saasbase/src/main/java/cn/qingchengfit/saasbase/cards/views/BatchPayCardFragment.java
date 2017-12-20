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
import android.widget.TextView;
import butterknife.ButterKnife;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.event.EventBatchPayCard;
import cn.qingchengfit.saasbase.cards.item.BatchPayCardHeaderItem;
import cn.qingchengfit.saasbase.cards.item.BatchPayCardItem;
import cn.qingchengfit.saasbase.cards.presenters.CardTypeListPresenter;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.course.batch.bean.CardTplBatchShip;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "card", path = "/card/batch/choose/") public class BatchPayCardFragment
  extends SaasBaseFragment
  implements CardTypeListPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
  SwipeRefreshLayout.OnRefreshListener {

  @Need ArrayList<Rule> rules;
  @Need ArrayList<CardTplBatchShip> cardTplBatchShips;
  @Need int maxCount = 1;
  @Need boolean multiPrice;

  TextView toolbarTitile;
  Toolbar toolbar;
  RecyclerView recyclerview;
  SwipeRefreshLayout srl;
  @Inject CardTypeListPresenter presenter;
  private HashMap<String, HashMap<Integer, String>> cardCost = new HashMap();
  private List<BatchPayCardHeaderItem> mDatas = new ArrayList<>();
  private CommonFlexAdapter commonFlexAdapter;

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
    unbinder = ButterKnife.bind(this, view);
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
    onRefresh();
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitile.setText("会员卡结算");
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        save();
        return false;
      }
    });
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
    //ret.putExtra("rules", rules);
    //ret.putExtra("count", count);
    //getActivity().setResult(Activity.RESULT_OK, ret);
    //getActivity().finish();
  }

  @Override public String getFragmentName() {
    return BatchPayCardFragment.class.getName();
  }

  public void onGetData(List<CardTpl> card_tpls) {
    srl.setRefreshing(false);
    mDatas.clear();
    commonFlexAdapter.clear();
    if (card_tpls != null) {
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

      for (int i = 0; i < card_tpls.size(); i++) {
        CardTpl cardTpl = card_tpls.get(i);
        switch (cardTpl.getType()) {
          case 1:
          case 2:
            if (multiPrice) {
              BatchPayCardHeaderItem itemData = new BatchPayCardHeaderItem(cardTpl);

              for (int j = 0; j < maxCount; j++) {
                Rule r = null;
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

              if (cardCost.containsKey(cardTpl.getId())) {
                mDatas.add(0, itemData);
              } else {
                mDatas.add(itemData);
              }
            } else {
              BatchPayCardHeaderItem itemData = new BatchPayCardHeaderItem(cardTpl);

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
              if (cardCost.containsKey(cardTpl.getId())) {
                mDatas.add(0, itemData);
              } else {
                mDatas.add(itemData);
              }
            }
            break;
          case 3:
            BatchPayCardHeaderItem itemData = new BatchPayCardHeaderItem(cardTpl);

            itemData.addChild(new BatchPayCardItem("", 0, cardTpl, multiPrice));

            if (cardTplBatchShips != null
              && cardTplBatchShips.contains(cardTpl)
              && cardTplBatchShips.get(cardTplBatchShips.indexOf(cardTpl)).status == 2) {
              itemData.setHasOrdered(true);
            }
            if (cardCost.containsKey(cardTpl.getId())) {
              mDatas.add(0, itemData);
            } else {
              mDatas.add(itemData);
            }

            break;
          default:
            break;
        }
      }
      if (mDatas.size() == 0) {
        commonFlexAdapter.addItem(new CommonNoDataItem(R.drawable.vd_card_empty, "您没有可用的会员卡"));
      }
      commonFlexAdapter.updateDataSet(mDatas);

      for (int i = 0; i < commonFlexAdapter.getHeaderItems().size(); i++) {
        if (commonFlexAdapter.getHeaderItems().get(i) instanceof BatchPayCardHeaderItem) {
          BatchPayCardHeaderItem item =
            (BatchPayCardHeaderItem) commonFlexAdapter.getHeaderItems().get(i);
          int pos = commonFlexAdapter.getGlobalPositionOf(item);
          if (cardCost.containsKey(item.getCardId())) {
            commonFlexAdapter.expand(pos);
          }
        }
      }
    }
  }

  @Override public boolean onItemClick(int position) {
    if (commonFlexAdapter.getItem(position) instanceof BatchPayCardHeaderItem) {
      //if (((BatchPayCardHeaderItem)commonFlexAdapter.getItem(position)).getSubItems().size() == 0)
      //    ((BatchPayCardHeaderItem)commonFlexAdapter.getItem(position)).toggleExpaned();
      commonFlexAdapter.notifyItemChanged(position);
    }
    //return true;
    return false;
  }

  @Override public void onDoneCardtplList() {
    onGetData(presenter.getCardTplByType(0));
  }

  @Override public void onRefresh() {
    presenter.queryCardTypeNoNeedPermission();
  }
}