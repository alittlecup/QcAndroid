package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.events.EventRecycleClick;
import cn.qingchengfit.items.ActionDescItem;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.item.CardActionsItem;
import cn.qingchengfit.saasbase.cards.item.CardTplItem;
import cn.qingchengfit.saasbase.cards.presenters.CardDetailPresenter;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.views.fragments.BaseListFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
 * Created by Paper on 2017/9/29.
 */
@Leaf(module = "card", path = "/detail/") public class CardDetailFragment extends BaseListFragment
    implements CardDetailPresenter.MVPView, FlexibleAdapter.OnItemClickListener {
  @Need
  String cardid;

  public static CardDetailFragment newInstance(String id) {
    Bundle args = new Bundle();
    args.putString("id", id);
    CardDetailFragment fragment = new CardDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null)
      cardid = getArguments().getString("id");
  }

  @Inject CardDetailPresenter presenter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    delegatePresenter(presenter,this);
    presenter.setCardId(cardid);
    RxBusAdd(EventRecycleClick.class)
        .subscribe(new BusSubscribe<EventRecycleClick>() {
          @Override public void onNext(EventRecycleClick eventRecycleClick) {
            if (eventRecycleClick.viewId == R.id.btn_charge){
              Bundle b = new Bundle();
              b.putParcelable("card",presenter.getmCard());
              routeTo("/charge/",b);
            }
          }
        });
    return v;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryCardDetail();
  }

  @Override public String getFragmentName() {
    return CardDetailFragment.class.getName();
  }

  @Override public int getNoDataIconRes() {
    return 0;
  }

  @Override public String getNoDataStr() {
    return null;
  }

  @Override public void onCardDetail(Card card) {
    List<AbstractFlexibleItem> items = new ArrayList<>();
    items.add(new CardTplItem(card.getCard_tpl()));
    items.add(new CardActionsItem(card));
    items.add(new ActionDescItem.Builder().action(1)
        .action(R.drawable.vd_add_batch)
        .title("绑定会员")
        .desc(card.getBundleUsers())
        .build());
    items.add(new ActionDescItem.Builder().action(2)
        .action(R.drawable.vd_add_batch)
        .title("适用场馆")
        .desc(card.getSupportGyms())
        .build());
    items.add(new ActionDescItem.Builder().action(3)
        .action(R.drawable.vd_add_batch)
        .title("消费记录")
        .desc("todo")
        .build());
    items.add(new ActionDescItem.Builder().action(4)
        .action(R.drawable.vd_add_batch)
        .title("实体卡号")
        .desc(card.getId())
        .build());
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = commonFlexAdapter.getItem(position);
    if (item instanceof ActionDescItem) {
      switch (((ActionDescItem) item).getAction()) {
        case 1://绑定会员
          routeTo(AppUtils.getRouterUri(getContext(),"/student/choose/student/"),new BundleBuilder().adds("ids",(ArrayList<String>)presenter.getmCard().getUserIds()).build());
          break;
        case 2://适用场馆
          break;

        case 3://消费记录

          break;

        case 4://实体卡号
          break;
      }
    }

    return true;
  }
}
