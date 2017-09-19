package cn.qingchengfit.staffkit.views.batch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.CardTplBatchShip;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.CardTplType;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.BatchPayCardHeaderItem;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.BatchPayCardItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.cardtype.list.CardTypeListPresenter;
import cn.qingchengfit.staffkit.views.cardtype.list.CardTypeListView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Created by Paper on 2017/3/21.
 */
@FragmentWithArgs public class BatchPayCardFragment extends BaseFragment implements CardTypeListView, FlexibleAdapter.OnItemClickListener {

    @Arg(required = false) ArrayList<Rule> rules;
    @Arg(required = false) ArrayList<CardTplBatchShip> cardTplBatchShips;
    @Arg int maxCount = 1;
    @Arg boolean isPrivate;

    @BindView(R.id.toolbar_title) TextView toolbarTitile;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
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

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_batch_pay_by_cards, container, false);
        unbinder = ButterKnife.bind(this, view);
        delegatePresenter(presenter, this);
        initToolbar(toolbar);
        if (commonFlexAdapter == null) {
            commonFlexAdapter = new CommonFlexAdapter(mDatas, this);
            commonFlexAdapter.setAutoCollapseOnExpand(false).setAutoScrollOnExpand(true);
        }
        recyclerview.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        recyclerview.setAdapter(commonFlexAdapter);
        recyclerview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                CompatUtils.removeGlobalLayout(recyclerview.getViewTreeObserver(), this);
                presenter.queryCardTypeNoNeedPermission();
            }
        });

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
        Intent ret = new Intent();
        ArrayList<Rule> rules = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < commonFlexAdapter.getExpandedItems().size(); i++) {
            BatchPayCardHeaderItem item = (BatchPayCardHeaderItem) commonFlexAdapter.getExpandedItems().get(i);
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
                    if (isPrivate) {//私教
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
        ret.putExtra("rules", rules);
        ret.putExtra("count", count);
        getActivity().setResult(Activity.RESULT_OK, ret);
        getActivity().finish();
    }

    @Override public String getFragmentName() {
        return BatchPayCardFragment.class.getName();
    }

    @Override public void onGetData(List<CardTpl> card_tpls) {
        if (card_tpls != null) {
            if (cardTplBatchShips != null) {
                for (int i = 0; i < cardTplBatchShips.size(); i++) {
                    CardTplBatchShip cardTplBatchShip = cardTplBatchShips.get(i);
                    if (card_tpls.contains(cardTplBatchShip)) {
                        continue;
                    } else {
                        CardTpl cardTpl = new CardTpl(cardTplBatchShip.name, cardTplBatchShip.type, "", cardTplBatchShip.id, "");
                        cardTpl.is_enable = false;
                        card_tpls.add(cardTpl);
                    }
                }
            }

            for (int i = 0; i < card_tpls.size(); i++) {
                CardTpl cardTpl = card_tpls.get(i);
                switch (cardTpl.getType()) {
                    case CardTplType.VALUE:
                    case CardTplType.TIMES:
                        if (isPrivate) {
                            BatchPayCardHeaderItem itemData = new BatchPayCardHeaderItem(cardTpl);

                            for (int j = 0; j < maxCount; j++) {
                                Rule r = null;
                                if (cardCost.containsKey(cardTpl.getId())) {
                                    try {
                                        String money = cardCost.get(cardTpl.getId()).get(j + 1);
                                        itemData.addChild(new BatchPayCardItem(money, j, cardTpl, isPrivate));
                                    } catch (Exception e) {

                                    }
                                } else {
                                    itemData.addChild(new BatchPayCardItem("", j, cardTpl, isPrivate));
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
                            itemData.addChild(new BatchPayCardItem(cost, 0, cardTpl, isPrivate));

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
                    case CardTplType.DATA:
                        BatchPayCardHeaderItem itemData = new BatchPayCardHeaderItem(cardTpl);

                        itemData.addChild(new BatchPayCardItem("", 0, cardTpl, isPrivate));

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
                commonFlexAdapter.addItem(new CommonNoDataItem(R.drawable.no_cardtype, "您没有可用的会员卡"));
            }
            commonFlexAdapter.notifyDataSetChanged();

            for (int i = 0; i < commonFlexAdapter.getHeaderItems().size(); i++) {
                if (commonFlexAdapter.getHeaderItems().get(i) instanceof BatchPayCardHeaderItem) {
                    BatchPayCardHeaderItem item = (BatchPayCardHeaderItem) commonFlexAdapter.getHeaderItems().get(i);
                    int pos = commonFlexAdapter.getGlobalPositionOf(item);
                    if (cardCost.containsKey(item.getCardId())) {
                        commonFlexAdapter.expand(pos);
                    }
                }
            }
        }
    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

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
}
