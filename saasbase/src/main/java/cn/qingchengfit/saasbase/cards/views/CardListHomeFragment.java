package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.bubble.BubblePopupView;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.cards.presenters.CardListPresenter;
import cn.qingchengfit.saasbase.utils.SharedPreferenceUtils;
import cn.qingchengfit.saascommon.events.EventSaasFresh;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.anbillon.flabellum.annotations.Leaf;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.Arrays;
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
 * Created by Paper on 2017/9/28.
 */
@Leaf(module = "card", path = "/list/home/") public class CardListHomeFragment
  extends SaasBaseFragment
  implements CardListPresenter.MVPView, FlexibleAdapter.OnItemClickListener,
  SwipeRefreshLayout.OnRefreshListener, FlexibleAdapter.EndlessScrollListener {

 public CardListFragment cardListFragment;
  CardListFilterFragment filterFragment;
  @Inject public CardListPresenter presenter;
  @Inject IPermissionModel serPermisAction;
	Toolbar toolbar;
	TextView toolbarTitle;
	ViewGroup tl;
	CompatTextView tvCardBalance;
	FrameLayout btnCardBalance;
	FrameLayout btnOutport;
	QcFilterToggle filterTpl;
	QcFilterToggle filterStatus;
	protected LinearLayout layoutCardOperate;
	TextView tvCardCount;
	protected RelativeLayout cardListLayout;
    private BubblePopupView bubblePopupView;

    private SharedPreferenceUtils sharedPreferenceUtils;
    private Handler popupHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean isFirst = sharedPreferenceUtils.IsFirst("cardList");
            if(isFirst) {
                bubblePopupView = new BubblePopupView(getContext());
                bubblePopupView.show(toolbar, "点击管理会员卡种类", 90, 400);
                sharedPreferenceUtils.saveFlag("cardList", false);
            }
        }
    };

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
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_saas_card_list, container, false);

    popupHandler.sendEmptyMessageDelayed(0, 1000);
    sharedPreferenceUtils = new SharedPreferenceUtils(getContext());

    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    tl = (ViewGroup) view.findViewById(R.id.toolbar_layout);
    tvCardBalance = (CompatTextView) view.findViewById(R.id.tv_card_balance);
    btnCardBalance = (FrameLayout) view.findViewById(R.id.btn_card_balance);
    btnOutport = (FrameLayout) view.findViewById(R.id.btn_outport);
    filterTpl = (QcFilterToggle) view.findViewById(R.id.filter_tpl);
    filterStatus = (QcFilterToggle) view.findViewById(R.id.filter_status);
    layoutCardOperate = (LinearLayout) view.findViewById(R.id.layout_card_operate);
    tvCardCount = (TextView) view.findViewById(R.id.tv_card_count);
    cardListLayout = (RelativeLayout) view.findViewById(R.id.layout_card_list);
    view.findViewById(R.id.btn_card_balance).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnCardBalanceClicked();
      }
    });
    view.findViewById(R.id.btn_outport).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBtnOutportClicked();
      }
    });
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

    initToolbar(toolbar);
    delegatePresenter(presenter, this);
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("会员卡");
    toolbar.getMenu().clear();
    toolbar.inflateMenu(R.menu.menu_search_flow_searchview);
    RxMenuItem.clicks(toolbar.getMenu().getItem(0))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          showSearch(tl);
        }
      });

    RxMenuItem.clicks(toolbar.getMenu().getItem(1))
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<Void>() {
        @Override public void onNext(Void aVoid) {
          showSelectSheet(null, Arrays.asList("会员卡种类管理"), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              if (!serPermisAction.check(PermissionServerUtils.CARDSETTING)) {
                showAlert(R.string.alert_permission_forbid);
                return;
              }
              routeTo("card", "/cardtpl/list/", null);
            }
          });
        }
      });
    initSearch(tl, "输入会员姓名或手机号查找会员卡");
  }

  @Override public void onTextSearch(String text) {
    presenter.queryKeyworkd(text);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    filterFragment.setListener(new CardListFilterFragment.CardlistFilterListener() {
      @Override public void onFilterResult(CardTpl cardTpl, int status) {
        filterTpl.setChecked(false);
        filterStatus.setChecked(false);
        cardListFragment.initLoadMore(100, CardListHomeFragment.this);
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

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
    Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof CardListFragment) {

    }
  }

  @Override public String getFragmentName() {
    return CardListHomeFragment.class.getName();
  }

  /**
   * 余额不足卡提醒
   */
 public void onBtnCardBalanceClicked() {
    if (!serPermisAction.check(PermissionServerUtils.CARDBALANCE)) {
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    routeTo("/balance/", null);
  }

  /**
   * 会员卡导出
   */
 public void onBtnOutportClicked() {
    if (!serPermisAction.check(PermissionServerUtils.CARD_EXPORT) && !serPermisAction.check(
      PermissionServerUtils.CARD_IMPORT)) {
      showAlert(R.string.sorry_for_no_permission);
      return;
    }
    routeTo("export", "/card/", null);
  }

  /**
   * 按卡种类筛选
   */
 public void onFilterTplClicked() {
    toggleFilter(0);
  }

  /**
   * 按状态筛选
   */
 public void onFilterStatusClicked() {
    toggleFilter(1);
  }

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

  @Override public void onRefresh() {
    cardListFragment.initLoadMore(100, this);
    presenter.initpage();
    presenter.queryAllCards();
    presenter.queryBalanceCount();
  }

  @Override public void onCardCount(int count) {
    tvCardCount.setText(getString(R.string.card_total_count_d, count));
    cardListFragment.initLoadMore(count, this);
  }

  @Override public void onGetBalanceCount(int count) {
    tvCardBalance.setText("续卡提醒(" + count + ")");
  }

  @Override public void noMoreLoad(int newItemsSize) {

  }

  @Override public void onLoadMore(int lastPosition, int currentPage) {
    presenter.queryAllCards();
  }
}
