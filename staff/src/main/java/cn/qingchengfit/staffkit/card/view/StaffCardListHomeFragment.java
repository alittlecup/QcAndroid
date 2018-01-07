package cn.qingchengfit.staffkit.card.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.cards.views.CardListHomeFragment;
import cn.qingchengfit.staffkit.R;

/**
 * Created by fb on 2018/1/4.
 */

public class StaffCardListHomeFragment extends CardListHomeFragment{

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    cardListLayout.setBackgroundColor(getResources().getColor(R.color.white));
    return view;
  }
}
