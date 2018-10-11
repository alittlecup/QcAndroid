package cn.qingchengfit.saasbase.cards.views;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.item.CardTplItem;
import cn.qingchengfit.saasbase.cards.presenters.CardTypeListPresenter;
import cn.qingchengfit.saascommon.events.EventSaasFresh;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.DialogList;
import com.anbillon.flabellum.annotations.Leaf;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
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
 * Created by Paper on 2017/8/14.
 */

@Leaf(module = "card", path = "/cardtpl/list/") public class CardTplsHomeInGymFragment
    extends SaasBaseFragment
    implements CardTypeListPresenter.MVPView, SwipeRefreshLayout.OnRefreshListener,
    FlexibleAdapter.OnItemClickListener {

 public TabLayout tab;
  public ViewPager viewpager;
  Toolbar toolbar;
  public TextView toolbarTitle;
  TextView cardCount;
  TextView cardDisable;

  @Inject CardTypeListPresenter presenter;
  @Inject IPermissionModel permissionModel;

  int childCount = 0;
  protected List<CardTplListFragment> fragmentList = new ArrayList<>();
  protected CardViewpagerAdapter pageAdapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initFragments();
    RxBus.getBus()
        .register(EventSaasFresh.CardTplList.class)
        .compose(this.<EventSaasFresh.CardTplList>bindToLifecycle())
        .compose(this.<EventSaasFresh.CardTplList>doWhen(FragmentEvent.CREATE_VIEW))
        .subscribe(new BusSubscribe<EventSaasFresh.CardTplList>() {
          @Override public void onNext(EventSaasFresh.CardTplList cardTplList) {
            onRefresh();
          }
        });
  }

  void initFragments() {
    if (fragmentList.size() == 0) {
      for (int i = 0; i < 4; i++) {
        fragmentList.add(new CardTplListFragment());
      }
    }
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cardtype_home, container, false);
    tab = (TabLayout) view.findViewById(R.id.tab);
    viewpager = (ViewPager) view.findViewById(R.id.viewpager);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    cardCount = (TextView) view.findViewById(R.id.card_count);
    cardDisable = (TextView) view.findViewById(R.id.card_disable);
    view.findViewById(R.id.card_disable).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickCardDisable();
      }
    });
    view.findViewById(R.id.card_disable_status).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickCardDisable();
      }
    });
    super.onCreateView(inflater, container, savedInstanceState);

    delegatePresenter(getPresenter(), this);
    initToolbar(toolbar);
    initVp();
    return view;
  }

  public CardTypeListPresenter getPresenter() {
    return presenter;
  }

  void initVp() {
    pageAdapter = new CardViewpagerAdapter(getChildFragmentManager());
    viewpager.setAdapter(pageAdapter);
    tab.setupWithViewPager(viewpager);
    viewpager.setOffscreenPageLimit(fragmentList.size());
    viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      //页面变化
      @Override public void onPageSelected(int position) {
        cardCount.setText(fragmentList.get(position).getItemCount() + "");
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText(R.string.title_cardtpl_types);
    toolbar.inflateMenu(R.menu.menu_add_card);
    //toolbar.getMenu().clear();
    //toolbar.getMenu().add("新增会员卡种类")
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        new DialogList(getContext()).list(getResources().getStringArray(R.array.cardtype_category),
            new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMenuAdd(position);
              }
            }).show();
        return true;
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

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof CardTplListFragment) {
      childCount++;
      if (childCount == fragmentList.size()) onRefresh();
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
  }

  @Override public String getFragmentName() {
    return CardTplsHomeInGymFragment.class.getName();
  }

  public void onClickCardDisable() {
    final String[] status = getResources().getStringArray(R.array.cardtype_disable_stauts);
    DialogList.builder(getContext()).list(status, new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cardDisable.setText(status[position]);
        getPresenter().setEnable(position == 0);
      }
    }).show();
  }

  /**
   * 获取后端数据完成
   */
  @Override public void onDoneCardtplList() {
    cardCount.setText(getPresenter().getCardTplByType(viewpager.getCurrentItem()).size() + "");
    int i = 0;
    for (CardTplListFragment fragment : fragmentList) {
      fragment.setCardtpls(getPresenter().getCardTplByType(i));
      fragment.initListener(this);
      i++;
    }
  }

  @Override public void onRefresh() {
    getPresenter().queryCardtypeList();
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = fragmentList.get(viewpager.getCurrentItem()).getItem(i);
    if (item instanceof CardTplItem) {
      getPresenter().chooseOneCardTpl(((CardTplItem) item).getCardTpl());
      routeTo("/cardtpl/detail/",
          new cn.qingchengfit.saasbase.cards.views.CardTplDetailParams().cardTpl(
              ((CardTplItem) item).getCardTpl()).build());
    }
    return true;
  }

  class CardViewpagerAdapter extends FragmentStatePagerAdapter {

    public CardViewpagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      if (position < fragmentList.size()) {
        return fragmentList.get(position);
      } else {
        return new Fragment();
      }
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public int getCount() {
      return 4;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return getString(R.string.cardtype_all);
        case 1:
          return getString(R.string.cardtype_value);
        case 2:
          return getString(R.string.cardtype_times);
        case 3:
          return getString(R.string.cardtype_date);
        default:
          return getString(R.string.cardtype_all);
      }
    }
  }
}
