package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.BalanceDetail;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.cards.presenters.CardBalancePresenter;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.anbillon.flabellum.annotations.Leaf;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
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
 * Created by Paper on 2017/11/24.
 */
@Leaf(module = "card", path = "/balance/") public class CardBalanceFragment extends SaasBaseFragment
  implements FlexibleAdapter.OnItemClickListener,CardBalancePresenter.MVPView,
  SwipeRefreshLayout.OnRefreshListener,FlexibleAdapter.EndlessScrollListener {

  CardListFragment cardListFragment;
  CardListFilterFragment filterFragment;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R2.id.text_filter_tips) TextView textFilterTips;
  @BindView(R2.id.text_filter_condition) TextView textFilterCondition;
  @BindView(R2.id.text_change_button) TextView textChangeButton;
  @BindView(R2.id.ll_balance_condition) LinearLayout llBalanceCondition;
  @BindView(R2.id.filter_tpl) QcFilterToggle filterTpl;
  @BindView(R2.id.filter_status) QcFilterToggle filterStatus;
  @BindView(R2.id.tv_card_count) TextView tvCardCount;
  @BindView(R2.id.frag_card_list) FrameLayout fragCardList;
  @BindView(R2.id.frag_card_filter) FrameLayout fragCardFilter;

  @Inject CardBalancePresenter presenter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cardListFragment = new CardListFragment();
    cardListFragment.initListener(this);
    filterFragment = new CardListFilterFragment();
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
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter,this);
    initToolbar(toolbar);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("续卡提醒");

  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    filterFragment.setListener(new CardListFilterFragment.CardlistFilterListener() {
      @Override public void onFilterResult(CardTpl cardTpl, int status) {
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

  @Override public int getLayoutRes() {
    return R.id.frag_card_list;
  }



  @Override public String getFragmentName() {
    return CardBalanceFragment.class.getName();
  }

  @Override public void noMoreLoad(int newItemsSize) {}

  @Override public void onLoadMore(int lastPosition, int currentPage) {
    presenter.queryAllCards();
  }


  @Override public void onRefresh() {
    cardListFragment.initLoadMore(100,this);
    presenter.initpage();
    presenter.queryAllCards();
  }


  @Override public void onCardCount(int count) {
    tvCardCount.setText(getString(R.string.card_total_count_d,count));
    cardListFragment.initLoadMore(count,this);
  }

  @Override public void onGetBalance(List<BalanceDetail> balanceDetailList) {
    //for (BalanceDetail balanceDetail : balanceDetailList) {
    //
    //  switch (balanceDetail.key) {
    //    case presenter.QUERY_STORE_BALANCE:
    //      storeValue = balanceDetail.value;
    //      storeEdit.setText(storeValue + "");
    //      idMap.put(RealCardListPresenter.QUERY_STORE_BALANCE, balanceDetail.id);
    //      break;
    //    case presenter.QUERY_SECOND_BALANCE:
    //      secondValue = balanceDetail.value;
    //      secondEdit.setText(secondValue + "");
    //      idMap.put(RealCardListPresenter.QUERY_SECOND_BALANCE, balanceDetail.id);
    //      break;
    //    case presenter.QUERY_DAYS_BALANCE:
    //      timeValue = balanceDetail.value;
    //      timeEdit.setText(timeValue + "");
    //      idMap.put(RealCardListPresenter.QUERY_DAYS_BALANCE, balanceDetail.id);
    //      break;
    //  }
    //}
    //if (textFilterCondition != null) {
    //  textFilterCondition.setText("储值卡<" + storeValue + "元， 次卡<" + secondValue + "次， 有效期<" + timeValue + "天");
    //}
  }

  @Override public void onCardList(List<Card> cards, int page) {
    if (cardListFragment != null && cardListFragment.isAdded()) {
      cardListFragment.setCardtpls(cards, page);
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible iFlexible = cardListFragment.getItem(position);
    if (iFlexible instanceof CardItem) {
      routeTo("/detail/", new cn.qingchengfit.saasbase.cards.views.CardDetailParams()
        .cardid(((CardItem) iFlexible).getRealCard().getId())
        .build());
    }
    return true;
  }




  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick(R2.id.filter_tpl) public void onFilterTplClicked() {
    toggleFilter(0);
  }

  @OnClick(R2.id.filter_status) public void onFilterStatusClicked() {
    toggleFilter(1);
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

  @OnClick(R2.id.text_change_button) public void onViewClicked() {
    routeTo("/autonotify/",null);
  }
}
