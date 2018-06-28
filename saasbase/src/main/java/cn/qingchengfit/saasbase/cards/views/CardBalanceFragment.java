package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.BalanceDetail;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.cards.network.body.CardBalanceNotifyBody;
import cn.qingchengfit.saasbase.cards.presenters.CardBalancePresenter;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.anbillon.flabellum.annotations.Leaf;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

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
 * Created by Paper on 2017/11/24.
 */
@Leaf(module = "card", path = "/balance/") public class CardBalanceFragment extends SaasBaseFragment
  implements FlexibleAdapter.OnItemClickListener, CardBalancePresenter.MVPView,
  SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.EndlessScrollListener {

  CardListFragment cardListFragment;
  CardListFilterFragment filterFragment;
	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;
	TextView textFilterTips;
	TextView textFilterCondition;
	TextView textChangeButton;
	LinearLayout llBalanceCondition;
	QcFilterToggle filterTpl;
	QcFilterToggle filterStatus;
	TextView tvCardCount;
	FrameLayout fragCardList;
	FrameLayout fragCardFilter;

  @Inject CardBalancePresenter presenter;
  private PopupWindow popupWindow;
  private EditText storeEdit, secondEdit, timeEdit;
  private TextView textFilterReset, textFilterSure;
  private HashMap<String, String> idMap = new HashMap<>();
  protected CardTpl card_tpl = new CardTpl();
  private int cardStatus;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cardListFragment = new CardListFragment();
    cardListFragment.initListener(this);
    filterFragment = CardListFilterFragment.newFragmentWithOutStopCardFilter();
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
    View view = inflater.inflate(R.layout.fragment_saas_card_balance, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    textFilterTips = (TextView) view.findViewById(R.id.text_filter_tips);
    textFilterCondition = (TextView) view.findViewById(R.id.text_filter_condition);
    textChangeButton = (TextView) view.findViewById(R.id.text_change_button);
    llBalanceCondition = (LinearLayout) view.findViewById(R.id.ll_balance_condition);
    filterTpl = (QcFilterToggle) view.findViewById(R.id.filter_tpl);
    filterStatus = (QcFilterToggle) view.findViewById(R.id.filter_status);
    tvCardCount = (TextView) view.findViewById(R.id.tv_card_count);
    fragCardList = (FrameLayout) view.findViewById(R.id.frag_card_list);
    fragCardFilter = (FrameLayout) view.findViewById(R.id.frag_card_filter);
    view.findViewById(R.id.filter_tpl).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onFilterTplClicked();
      }
    });
    view.findViewById(R.id.filter_status).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onFilterStatusClicked();
      }
    });
    view.findViewById(R.id.text_change_button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onFilter(v);
      }
    });

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    presenter.queryBalanceCondition();
    initPopView();
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("续卡提醒");
    toolbar.getMenu().add("自动提醒").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          routeTo("/autonotify/", null);
        }
      });
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    filterFragment.setListener(new CardListFilterFragment.CardlistFilterListener() {
      @Override public void onFilterResult(CardTpl cardTpl, int status) {
        card_tpl = cardTpl;
        cardStatus = status;
        filterTpl.setChecked(false);
        filterStatus.setChecked(false);
        cardListFragment.initLoadMore(100, CardBalanceFragment.this);
        presenter.setFilter(cardTpl.type, cardTpl.getId(), status);
      }
    });
    stuff(cardListFragment);
    stuff(R.id.frag_card_filter, filterFragment);
    hideChild(filterFragment);
    onRefresh();
  }

  private void initPopView() {
    View mPopView =
      LayoutInflater.from(getContext()).inflate(R.layout.layout_balance_setting, null);
    if (popupWindow == null) {
      popupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
        MeasureUtils.dpToPx(214.0f, getResources()), true);
      storeEdit = (EditText) mPopView.findViewById(R.id.edit_store_money);
      secondEdit = (EditText) mPopView.findViewById(R.id.edit_second_money);
      timeEdit = (EditText) mPopView.findViewById(R.id.edit_remain_time);
      textFilterReset = (TextView) mPopView.findViewById(R.id.tv_student_filter_reset);
      textFilterSure = (TextView) mPopView.findViewById(R.id.tv_student_filter_confirm);

      textFilterSure.setOnClickListener(view -> confirm());
      textFilterReset.setOnClickListener(view -> reset());
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

  public void dismissPopupWindow() {
    //cardListShadow.setVisibility(View.GONE);
  }

  void confirm() {
    if (TextUtils.isEmpty(storeEdit.getText())) {
      DialogUtils.showAlert(getContext(), "请填写储值卡余额小于多少元");
      return;
    }
    if (Float.valueOf(storeEdit.getText().toString()) == 0) {
      DialogUtils.showAlert(getContext(), "储值卡余额不可小于0元");
      return;
    }
    if (TextUtils.isEmpty(secondEdit.getText())) {
      DialogUtils.showAlert(getContext(), "请填写次卡余额小于多少次");
      return;
    }
    if (Float.valueOf(secondEdit.getText().toString()) == 0) {
      DialogUtils.showAlert(getContext(), "次卡余额不可小于0次");
      return;
    }
    if (TextUtils.isEmpty(timeEdit.getText())) {
      DialogUtils.showAlert(getContext(), "请填写剩余有效期少于多少天");
      return;
    }
    if (Float.valueOf(timeEdit.getText().toString()) == 0) {
      DialogUtils.showAlert(getContext(), "剩余有效期不可小于0天");
      return;
    }
    List<CardBalanceNotifyBody.ConfigsBean> configsBeanList = new ArrayList<>();
    CardBalanceNotifyBody.ConfigsBean configsBean1 = new CardBalanceNotifyBody.ConfigsBean();
    configsBean1.setId(idMap.get(presenter.QUERY_STORE_BALANCE));
    configsBean1.setValue(Integer.valueOf(storeEdit.getText().toString()));
    configsBeanList.add(configsBean1);
    CardBalanceNotifyBody.ConfigsBean configsBean2 = new CardBalanceNotifyBody.ConfigsBean();
    configsBean2.setId(idMap.get(presenter.QUERY_SECOND_BALANCE));
    configsBean2.setValue(Integer.valueOf(secondEdit.getText().toString()));
    configsBeanList.add(configsBean2);
    CardBalanceNotifyBody.ConfigsBean configsBean3 = new CardBalanceNotifyBody.ConfigsBean();
    configsBean3.setId(idMap.get(presenter.QUERY_DAYS_BALANCE));
    configsBean3.setValue(Integer.valueOf(timeEdit.getText().toString()));
    configsBeanList.add(configsBean3);

    presenter.putBalanceRemindCondition(configsBeanList);
    delayRefreshData();
  }

  void reset() {

    List<CardBalanceNotifyBody.ConfigsBean> configsBeanList = new ArrayList<>();
    CardBalanceNotifyBody.ConfigsBean configsBean1 = new CardBalanceNotifyBody.ConfigsBean();
    configsBean1.setId(idMap.get(presenter.QUERY_STORE_BALANCE));
    configsBean1.setValue(500);
    configsBeanList.add(configsBean1);
    CardBalanceNotifyBody.ConfigsBean configsBean2 = new CardBalanceNotifyBody.ConfigsBean();
    configsBean2.setId(idMap.get(presenter.QUERY_SECOND_BALANCE));
    configsBean2.setValue(5);
    configsBeanList.add(configsBean2);
    CardBalanceNotifyBody.ConfigsBean configsBean3 = new CardBalanceNotifyBody.ConfigsBean();
    configsBean3.setId(idMap.get(presenter.QUERY_DAYS_BALANCE));
    configsBean3.setValue(5);
    configsBeanList.add(configsBean3);
    presenter.putBalanceRemindCondition(configsBeanList);
    delayRefreshData();

  }

  private void delayRefreshData() {
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        presenter.initpage();
        presenter.setFilter(card_tpl.type, card_tpl.getId(), cardStatus);
      }
    }, 500);
  }

  @Override public int getLayoutRes() {
    return R.id.frag_card_list;
  }

  @Override public String getFragmentName() {
    return CardBalanceFragment.class.getName();
  }

  @Override public void noMoreLoad(int newItemsSize) {
  }

  @Override public void onLoadMore(int lastPosition, int currentPage) {
    presenter.queryAllCards();
  }

  @Override public void onRefresh() {
    cardListFragment.initLoadMore(100, this);
    presenter.initpage();
    presenter.queryAllCards();
  }

  @Override public void onCardCount(int count) {
    tvCardCount.setText(getString(R.string.card_total_count_d, count));
    cardListFragment.initLoadMore(count, this);
  }

  @Override public void onGetBalance(List<BalanceDetail> balanceDetailList) {
    if (popupWindow != null) popupWindow.dismiss();
    int storeValue=0, secondValue=0, timeValue=0;

    for (BalanceDetail balanceDetail : balanceDetailList) {

      if (balanceDetail.key.equalsIgnoreCase(presenter.QUERY_STORE_BALANCE)) {
        storeValue = (int)balanceDetail.value;
        storeEdit.setText(storeValue + "");
        idMap.put(presenter.QUERY_STORE_BALANCE, balanceDetail.id);
      }else if (balanceDetail.key.equalsIgnoreCase(presenter.QUERY_SECOND_BALANCE)) {
        secondValue = (int)balanceDetail.value;
        secondEdit.setText(secondValue + "");
        idMap.put(presenter.QUERY_SECOND_BALANCE, balanceDetail.id);
      }else if(balanceDetail.key.equalsIgnoreCase(presenter.QUERY_DAYS_BALANCE)){
        timeValue = (int)balanceDetail.value;
        timeEdit.setText(timeValue + "");
        idMap.put(presenter.QUERY_DAYS_BALANCE, balanceDetail.id);
      }

    }
    if (textFilterCondition != null) {
      textFilterCondition.setText(
        "储值卡<" + storeValue + "元， 次卡<" + secondValue + "次， 有效期<" + timeValue + "天");
    }
  }

  @Override public void onCardList(List<Card> cards, int page) {
    if (cardListFragment != null && cardListFragment.isAdded()) {
      cardListFragment.setCardtpls(cards, page);
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible iFlexible = cardListFragment.getItem(position);
    if (iFlexible instanceof CardItem) {
      routeTo("/detail/", new cn.qingchengfit.saasbase.cards.views.CardDetailParams().cardid(
        ((CardItem) iFlexible).getRealCard().getId()).build());
    }
    return true;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

 public void onFilterTplClicked() {
    toggleFilter(0);
  }

 public void onFilterStatusClicked() {
    toggleFilter(1);
  }
 public void onFilter(View view) {
    showAsDropDown(popupWindow, view);
  }
  public void showAsDropDown(PopupWindow mPopupWindow, View view) {
    if (mPopupWindow != null && !mPopupWindow.isShowing()) {
      if (mPopupWindow == popupWindow) {
        mPopupWindow.showAtLocation(view, Gravity.TOP, 0, MeasureUtils.dpToPx(75.0f, getResources()));
      } else {
        mPopupWindow.showAsDropDown(view);
      }
      //cardListShadow.setVisibility(View.VISIBLE);
    } else if (mPopupWindow.isShowing()) {
      mPopupWindow.dismiss();
    }
  }

  /**
   * 展示筛选
   */
  private void toggleFilter(int index) {
    if (filterFragment == null) {

    } else {
      if (filterFragment.isVisible()) {
        if (filterFragment.getCurIndex() == index) {
          hideChild(filterFragment);
        } else {
          filterFragment.showPage(index);
        }
      } else {
        getFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out)
          .show(filterFragment)
          .commit();
        filterFragment.showPage(index);
      }
    }
  }

}
