package cn.qingchengfit.staffkit.views.card.cardlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.body.CardBalanceNotifyBody;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.BalanceDetail;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.abstractflexibleitem.CardItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.card.CardDetailActivity;
import cn.qingchengfit.staffkit.views.card.filter.FilterHeadCommonFragment;
import cn.qingchengfit.staffkit.views.custom.EndlessRecyclerOnScrollListener;
import cn.qingchengfit.staffkit.views.custom.RecycleViewWithNoImg;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by fb on 2017/2/25.
 *
 * todo 1.修改recyclerview nodata  把pullrefresh 放到外层，设置recyclerview nest
 * 2.修改filter
 */

public class BalanceCardListFragment extends FilterHeadCommonFragment
    implements RealCardListView, RealCardListPresenter.OnSettingBalanceListener, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.cardcount_lable) TextView cardcountLable;
    @BindView(R.id.recycleview) RecycleViewWithNoImg recycleview;
    @BindView(R.id.cards_total) TextView cardsTotal;
    @BindView(R.id.card_list_shadow) View cardListShadow;
    @BindView(R.id.text_change_button) TextView textChangeButton;
    @BindView(R.id.text_filter_condition) TextView textFilterCondition;

    @Inject RealCardListPresenter realCardListPresenter;
    //private List<Card> datas = new ArrayList<>();
    private List<AbstractFlexibleItem> datas = new ArrayList<>();
    private CommonFlexAdapter adapter;
    private String cardTpl_id, keyword;
    private int status_id;
    private String mChooseShopId;
    private PopupWindow popupWindow;
    private EditText storeEdit, secondEdit, timeEdit;
    private TextView textFilterReset, textFilterSure;
    private HashMap<String, Object> idMap = new HashMap<>();
    private int storeValue, secondValue, timeValue;
    private List<CardBalanceNotifyBody.ConfigsBean> configsBeanList = new ArrayList<>();
    private int card_tpl_type;
    private int card_status;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        isBalance = true;
        delegatePresenter(realCardListPresenter, this);
        filterHeadPresenter.getCardFilterType(true);
        setView(view);
        super.onCreateView(inflater, container, savedInstanceState);

        mCallbackActivity.setToolbar("余额不足", false, null, R.menu.menu_card_filter_notify, new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_auto_notify) {
                    getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.student_frag, new AutoNotifySettingFragment())
                        .addToBackStack(null)
                        .commit();
                }
                return true;
            }
        });
        delegatePresenter(realCardListPresenter, this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recycleview.setLayoutManager(manager);
        adapter = new CommonFlexAdapter(datas, this);
        recycleview.setAdapter(adapter);
        recycleview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                realCardListPresenter.initBalancePage();
                realCardListPresenter.filterBalanceCard(keyword, cardTpl_id, card_tpl_type, card_status, mChooseShopId);
            }
        });
        recycleview.addScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override public void onLoadMore() {
                Observable.just("").throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<String>() {
                    @Override public void call(String s) {
                        showLoading();
                        realCardListPresenter.filterBalanceCard(keyword, cardTpl_id, card_tpl_type, card_status, mChooseShopId);
                    }
                });
            }
        });

        realCardListPresenter.setOnSettingBalanceListener(this);
        initPopView();
        realCardListPresenter.initBalancePage();
        realCardListPresenter.filterBalanceCard(null, null, card_tpl_type, card_status, mChooseShopId);
        realCardListPresenter.queryBalanceCondition(App.staffId);
        realCardListPresenter.queryBalanceCount();
        return view;
    }

    @OnClick({ R.id.text_change_button }) public void onFilter(View view) {
        showAsDropDown(popupWindow, view);
    }

    @Override public void onConfirm() {
        if (TextUtils.isEmpty(storeEdit.getText())) {
            DialogUtils.showAlert(getContext(), "请填写储值卡余额小于多少元");
            return;
        }
        if (Integer.valueOf(storeEdit.getText().toString()) == 0) {
            DialogUtils.showAlert(getContext(), "储值卡余额不可小于0元");
            return;
        }
        if (TextUtils.isEmpty(secondEdit.getText())) {
            DialogUtils.showAlert(getContext(), "请填写次卡余额小于多少次");
            return;
        }
        if (Integer.valueOf(secondEdit.getText().toString()) == 0) {
            DialogUtils.showAlert(getContext(), "次卡余额不可小于0次");
            return;
        }
        if (TextUtils.isEmpty(timeEdit.getText())) {
            DialogUtils.showAlert(getContext(), "请填写剩余有效期少于多少天");
            return;
        }
        if (Integer.valueOf(timeEdit.getText().toString()) == 0) {
            DialogUtils.showAlert(getContext(), "剩余有效期不可小于0天");
            return;
        }
        if (storeValue != Integer.valueOf(storeEdit.getText().toString())) {
            CardBalanceNotifyBody.ConfigsBean configsBean = new CardBalanceNotifyBody.ConfigsBean();
            configsBean.setId((int) idMap.get(RealCardListPresenter.QUERY_STORE_BALANCE));
            configsBean.setValue(Integer.valueOf(storeEdit.getText().toString()));
            configsBeanList.add(configsBean);
        }
        if (secondValue != Integer.valueOf(secondEdit.getText().toString())) {
            CardBalanceNotifyBody.ConfigsBean configsBean = new CardBalanceNotifyBody.ConfigsBean();
            configsBean.setId((int) idMap.get(RealCardListPresenter.QUERY_SECOND_BALANCE));
            configsBean.setValue(Integer.valueOf(secondEdit.getText().toString()));
            configsBeanList.add(configsBean);
        }
        if (timeValue != Integer.valueOf(timeEdit.getText().toString())) {
            CardBalanceNotifyBody.ConfigsBean configsBean = new CardBalanceNotifyBody.ConfigsBean();
            configsBean.setId((int) idMap.get(RealCardListPresenter.QUERY_DAYS_BALANCE));
            configsBean.setValue(Integer.valueOf(timeEdit.getText().toString()));
            configsBeanList.add(configsBean);
        }

        realCardListPresenter.putBalanceRemindCondition(App.staffId, configsBeanList);
        delayRefreshData();

        //        textFilterCondition.setText("储值卡<" + storeEdit.getText().toString() + "元， 次卡<" +
        //                secondEdit.getText().toString() + "次， 有效期<" + timeEdit.getText().toString() + "天");
    }

    @Override public void onReset() {
        storeEdit.setText(storeValue + "");
        secondEdit.setText(secondValue + "");
        timeEdit.setText(timeValue + "");

        CardBalanceNotifyBody.ConfigsBean storeConfigsBean = new CardBalanceNotifyBody.ConfigsBean();
        storeConfigsBean.setId((int) idMap.get(RealCardListPresenter.QUERY_STORE_BALANCE));
        storeConfigsBean.setValue(500);
        configsBeanList.add(storeConfigsBean);

        CardBalanceNotifyBody.ConfigsBean secondConfigsBean = new CardBalanceNotifyBody.ConfigsBean();
        secondConfigsBean.setId((int) idMap.get(RealCardListPresenter.QUERY_SECOND_BALANCE));
        secondConfigsBean.setValue(5);
        configsBeanList.add(secondConfigsBean);

        CardBalanceNotifyBody.ConfigsBean timeConfigsBean = new CardBalanceNotifyBody.ConfigsBean();
        timeConfigsBean.setId((int) idMap.get(RealCardListPresenter.QUERY_DAYS_BALANCE));
        timeConfigsBean.setValue(15);
        configsBeanList.add(timeConfigsBean);

        realCardListPresenter.putBalanceRemindCondition(App.staffId, configsBeanList);

        delayRefreshData();
        textFilterCondition.setText("储值卡<500元， 次卡<5次， 有效期<15天");
    }

    private void delayRefreshData() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                realCardListPresenter.initBalancePage();
                realCardListPresenter.filterBalanceCard(keyword, cardTpl_id, card_tpl_type, card_status, mChooseShopId);
                realCardListPresenter.queryBalanceCondition(App.staffId);
            }
        }, 500);
    }

    private void initPopView() {
        View mPopView = LayoutInflater.from(getContext()).inflate(R.layout.layout_balance_setting, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT, MeasureUtils.dpToPx(214.0f, getResources()), true);
            storeEdit = (EditText) mPopView.findViewById(R.id.edit_store_money);
            secondEdit = (EditText) mPopView.findViewById(R.id.edit_second_money);
            timeEdit = (EditText) mPopView.findViewById(R.id.edit_remain_time);
            textFilterReset = (TextView) mPopView.findViewById(R.id.tv_student_filter_reset);
            textFilterSure = (TextView) mPopView.findViewById(R.id.tv_student_filter_confirm);

            textFilterSure.setOnClickListener(this);
            textFilterReset.setOnClickListener(this);
        }

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                dismissPopupWindow();
            }
        });
    }

    @Override public void onDestroyView() {
        realCardListPresenter.setOnSettingBalanceListener(null);
        realCardListPresenter.unattachView();
        super.onDestroyView();
    }

    @Override public void onFailed(String s) {
        hideLoading();
        recycleview.setFresh(false);
        ToastUtils.show(s);
    }

    @Override public void onSuccees(int size, int curPage, List<Card> cards) {
        hideLoading();
        if (curPage == 1) datas.clear();
        recycleview.setFresh(false);
        //datas.addAll(cards);
        //adapter.setDatas(datas);
        if (cards == null) return;
        for (int i = 0; i < cards.size(); i++) {
            datas.add(new CardItem(cards.get(i)));
        }
        adapter.notifyDataSetChanged();
        recycleview.setNoData(datas.isEmpty());
        if (cardcountLable != null) {
            cardcountLable.setText("会员卡总数： " + String.format(Locale.CHINA, "%d", size) + "张");
        }
    }

    @Override public void showAsDropDown(PopupWindow mPopupWindow, View view) {
        refreshView();
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            if (mPopupWindow == popupWindow) {
                mPopupWindow.showAtLocation(view, Gravity.TOP, 0, MeasureUtils.dpToPx(75.0f, getResources()));
            } else {
                mPopupWindow.showAsDropDown(view);
            }
            cardListShadow.setVisibility(View.VISIBLE);
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override public void refreshData(String card_tpl_id, int cardtplType, int cardStatus) {
        this.card_tpl_type = cardtplType;
        this.card_status = cardStatus;
        this.cardTpl_id = card_tpl_id;
        realCardListPresenter.initBalancePage();
        realCardListPresenter.filterBalanceCard(keyword, card_tpl_id, cardtplType, cardStatus, null);
    }

    @Override public void dismissPopupWindow() {
        cardListShadow.setVisibility(View.GONE);
    }

    @Override public String getFragmentName() {
        return BalanceCardListFragment.class.getName();
    }

    @Override public void onSettingSuccess() {
        popupWindow.dismiss();
        ToastUtils.showS("设置成功");
    }

    @Override public void onSettingFailed() {
        ToastUtils.showS("设置失败，请重试");
    }

    @Override public void onGetBalance(List<BalanceDetail> balanceDetailList) {
        for (BalanceDetail balanceDetail : balanceDetailList) {

            switch (balanceDetail.key) {
                case RealCardListPresenter.QUERY_STORE_BALANCE:
                    storeValue = balanceDetail.value;
                    storeEdit.setText(storeValue + "");
                    idMap.put(RealCardListPresenter.QUERY_STORE_BALANCE, balanceDetail.id);
                    break;
                case RealCardListPresenter.QUERY_SECOND_BALANCE:
                    secondValue = balanceDetail.value;
                    secondEdit.setText(secondValue + "");
                    idMap.put(RealCardListPresenter.QUERY_SECOND_BALANCE, balanceDetail.id);
                    break;
                case RealCardListPresenter.QUERY_DAYS_BALANCE:
                    timeValue = balanceDetail.value;
                    timeEdit.setText(timeValue + "");
                    idMap.put(RealCardListPresenter.QUERY_DAYS_BALANCE, balanceDetail.id);
                    break;
            }
        }
        if (textFilterCondition != null) {
            textFilterCondition.setText("储值卡<" + storeValue + "元， 次卡<" + secondValue + "次， 有效期<" + timeValue + "天");
        }
    }

    @Override public void onGetCardCount(int count) {
        if (cardcountLable != null) {
            cardcountLable.setText("会员卡总数： " + String.format(Locale.CHINA, "%d", count) + "张");
        }
    }

    @Override public boolean onItemClick(int position) {
        if (datas.get(position) instanceof CardItem) {
            Intent intent = new Intent(getActivity(), CardDetailActivity.class);
            intent.putExtra(Configs.EXTRA_REAL_CARD, ((CardItem) datas.get(position)).getRealCard());
            startActivity(intent);
        }
        return false;
    }
}

