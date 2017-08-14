package cn.qingchengfit.saasbase.cards.cardtypes.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.cardtypes.item.CardTplItem;
import cn.qingchengfit.saasbase.cards.cardtypes.presenters.ChooseCardtplPresenter;
import cn.qingchengfit.saasbase.events.EventChooseCardTpl;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.BaseFragment;
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
 * Created by Paper on 2017/8/15.
 */
public class ChooseCardTplFragment extends BaseFragment implements
    ChooseCardtplPresenter.MVPView,FlexibleAdapter.OnItemClickListener{

  @Inject ChooseCardtplPresenter presenter;

  CardTplListFragment cardTplListFragment;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cardTplListFragment = new CardTplListFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_choose_cardtpl, container, false);
    delegatePresenter(presenter,this);
    return view;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(cardTplListFragment);
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    super.onChildViewCreated(fm, f, v, savedInstanceState);
    if (f instanceof CardTplListFragment){
      presenter.queryCardtplsNoPermission();
      cardTplListFragment.initListener(this);
    }
  }

  @Override public String getFragmentName() {
    return ChooseCardTplFragment.class.getName();
  }

  @Override public int getLayoutRes() {
    return R.id.frag_choose_cardtpl;
  }

  @Override public void onCardtplListDone(List<CardTpl> cardtpls) {
    if (cardTplListFragment != null && cardTplListFragment.isAdded()){
      cardTplListFragment.setCardtpls(cardtpls);
    }else LogUtil.e("卡模板列表页面未加载");
  }

  @Override public boolean onItemClick(int i) {
    IFlexible item = cardTplListFragment.getItem(i);
    if (item instanceof CardTplItem){
      RxBus.getBus().post(new EventChooseCardTpl(((CardTplItem) item).getCardTpl()));
      getActivity().finish();
    }
    return true;
  }
}
