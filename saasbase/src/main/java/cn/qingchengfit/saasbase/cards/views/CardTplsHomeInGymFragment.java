package cn.qingchengfit.saasbase.cards.views;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.item.CardTplItem;
import cn.qingchengfit.saasbase.cards.presenters.CardTypeListPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.DialogList;
import com.anbillon.flabellum.annotations.Leaf;
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

@Leaf(module = "card",path = "/cardtpl/list/" )
public class CardTplsHomeInGymFragment extends BaseFragment implements
    CardTypeListPresenter.MVPView,SwipeRefreshLayout.OnRefreshListener,
    FlexibleAdapter.OnItemClickListener{

  @BindView(R2.id.tab) TabLayout tab;
  @BindView(R2.id.viewpager) ViewPager viewpager;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  @BindView(R2.id.card_count) TextView cardCount;
  @BindView(R2.id.card_disable) TextView cardDisable;

  @Inject CardTypeListPresenter presenter;

  int childCount = 0;
  protected List<CardTplListFragment> fragmentList = new ArrayList<>();
  private CardViewpagerAdapter pageAdapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    for (int i = 0; i < 4; i++) {
      fragmentList.add(new CardTplListFragment());
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cardtype_home, container, false);
    super.onCreateView(inflater,container,savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter,this);
    initToolbar(toolbar);
    toolbarTitle.setText(R.string.title_cardtpl_types);
    pageAdapter = new CardViewpagerAdapter(getChildFragmentManager());
    viewpager.setAdapter(pageAdapter);
    tab.setupWithViewPager(viewpager);
    viewpager.setOffscreenPageLimit(4);
    viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      //页面变化
      @Override public void onPageSelected(int position) {
        cardCount.setText(fragmentList.get(position).getItemCount()+"");
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_add);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        new DialogList(getContext()).list(getResources().getStringArray(R.array.cardtype_category), new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            presenter.createCardCate(position+1);
            routeTo("/cardtpl/add/",new CardtplAddParams().build());
          }
        }).show();
        return true;
      }
    });
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof CardTplListFragment){
      childCount++;
      if (childCount == 4)
        onRefresh();
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
  }

  @Override public String getFragmentName() {
    return CardTplsHomeInGymFragment.class.getName();
  }


  @OnClick({ R2.id.card_disable,R2.id.card_disable_status})
  public void onClickCardDisable(){
    final String[] status =  getResources().getStringArray(R.array.cardtype_disable_stauts);
    DialogList.builder(getContext())
        .list(status, new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cardDisable.setText(status[position]);
            presenter.setEnable(position == 0);
          }
        }).show();
  }

  /**
   * 获取后端数据完成
   */
  @Override public void onDoneCardtplList() {
    if (viewpager.getCurrentItem() == 0){
      cardCount.setText(presenter.getCardTplByType(0).size()+"");
    }
    int i = 0;
    for (CardTplListFragment fragment : fragmentList) {
      fragment.setCardtpls(presenter.getCardTplByType(i));
      fragment.initListener(this);
      i++;
    }
  }

  @Override public void onRefresh() {
    presenter.queryCardtypeList();
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item =fragmentList.get(viewpager.getCurrentItem()).getItem(i);
    if (item instanceof CardTplItem){
      presenter.chooseOneCardTpl(((CardTplItem) item).getCardTpl());
      routeTo("/cardtpl/detail/",new CardTplDetailParams()
        .cardTpl(((CardTplItem) item).getCardTpl())
        .build());
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
