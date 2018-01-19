package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.events.EventCommonFilter;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;
import cn.qingchengfit.views.fragments.FilterListFragment;

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
 * Created by Paper on 2017/9/27.
 */

public class CardListFilterFragment extends BaseFilterFragment {

  CardTpl mCardTpl = new CardTpl("全部会员卡",0,null,null,null);
  int mStatus;

  CardlistFilterListener listener;

  public CardlistFilterListener getListener() {
    return listener;
  }

  public void setListener(CardlistFilterListener listener) {
    this.listener = listener;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    RxBusAdd(CardTpl.class).subscribe(new BusSubscribe<CardTpl>() {
      @Override public void onNext(CardTpl cardTpl) {
        mCardTpl = cardTpl;
        dismiss();

      }
    });
    RxBusAdd(EventCommonFilter.class).subscribe(new BusSubscribe<EventCommonFilter>() {
      @Override public void onNext(EventCommonFilter eventCommonFilter) {
        mStatus = eventCommonFilter.getPos();
        dismiss();

      }
    });

    return view;
  }

  @Override public void dismiss() {
    if (listener != null)
      listener.onFilterResult(mCardTpl, mStatus);
    super.dismiss();
  }

  @Override protected String[] getTags() {
    return getResources().getStringArray(R.array.card_filter_tags);
  }

  @Override protected Fragment getFragmentByTag(String tag) {
    if (tag.equalsIgnoreCase(getTags()[0])) {
      return new CardFilterTplFragment();
    } else if (tag.equalsIgnoreCase(getTags()[1])) {
      return FilterListFragment.newInstance(R.array.card_filter_status);
    }
    return new EmptyFragment();
  }

  public interface CardlistFilterListener {
    void onFilterResult(CardTpl cardTpl, int status);
  }
}
