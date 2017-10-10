package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.events.EventFilterParams;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.item.CardItem;
import cn.qingchengfit.saasbase.cards.presenters.CardListPresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
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
 * Created by Paper on 2017/9/28.
 */
@Leaf(module = "card" ,path = "/list/home/")
public class CardListHomeFragment extends BaseFragment implements CardListPresenter.MVPView
  ,FlexibleAdapter.OnItemClickListener{

  CardListFragment cardListFragment;
  CardListFilterFragment filterFragment;
  @Inject CardListPresenter presenter;


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater,container,savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_saas_card_list, container, false);
    cardListFragment = new CardListFragment();
    cardListFragment.initListener(this);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter,this);
    RxBusAdd(EventFilterParams.class)
        .subscribe(new BusSubscribe<EventFilterParams>() {
          @Override public void onNext(EventFilterParams eventFilterParams) {

            if (filterFragment != null && filterFragment.isAdded()){
              getChildFragmentManager().beginTransaction().hide(filterFragment).commitAllowingStateLoss();
            }
            presenter.setFilter((int)eventFilterParams.getValue("type"),(String)eventFilterParams.getValue("id")
            ,(int)eventFilterParams.getValue("status"));
          }
        });
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(cardListFragment);
    filterFragment = new CardListFilterFragment();
    stuff(R.id.frag_card_filter,filterFragment);
    hideChild(filterFragment);
  }

  @Override public int getLayoutRes() {
    return R.id.frag_card_list;
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof CardListFragment) {
      presenter.queryAllCards();
    }
  }

  @Override public String getFragmentName() {
    return CardListHomeFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  /**
   * 余额不足卡提醒
   */
  @OnClick(R2.id.btn_card_balance) public void onBtnCardBalanceClicked() {

  }

  /**
   * 会员卡导出
   */
  @OnClick(R2.id.btn_outport) public void onBtnOutportClicked() {

  }

  /**
   * 按卡种类筛选
   */
  @OnClick(R2.id.filter_tpl) public void onFilterTplClicked() {
    toggleFilter(0);
  }
  /**
   * 按状态筛选
   */
  @OnClick(R2.id.filter_status) public void onFilterStatusClicked() {
    toggleFilter(1);
  }

  private void toggleFilter(int index){
    if (filterFragment == null){

    }else {
      if (filterFragment.isVisible()){
        if (filterFragment.getCurIndex() == index){
          hideChild(filterFragment);
        }else filterFragment.showPage(index);
      }else {
        showChild(filterFragment);
        filterFragment.showPage(index);
      }
    }

  }

  @Override public void onCardList(List<Card> cards) {
    if (cardListFragment != null && cardListFragment.isAdded()){
      cardListFragment.setCardtpls(cards);
    }
  }

  @Override public boolean onItemClick(int position) {
    IFlexible iFlexible = cardListFragment.getItem(position);
    if (iFlexible instanceof CardItem){
      Bundle b = new Bundle(1);
      b.putString("id",((CardItem) iFlexible).getRealCard().getId());
      routeTo("/detail/",b);
    }
    return true;
  }
}
