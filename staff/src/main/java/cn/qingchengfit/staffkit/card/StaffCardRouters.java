package cn.qingchengfit.staffkit.card;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.routers.cardImpl;
import cn.qingchengfit.staffkit.card.view.StaffCardBuyFragment;
import cn.qingchengfit.staffkit.card.view.StaffCardChargeFragment;
import cn.qingchengfit.staffkit.card.view.StaffCardListHomeFragment;

/**
 * Created by fb on 2017/12/20.
 */

public class StaffCardRouters extends cardImpl {

  @Override public Fragment toCardBuyFragment(Bundle args) {
    StaffCardBuyFragment fragment = new StaffCardBuyFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public Fragment toCardListHomeFragment(Bundle args) {
    StaffCardListHomeFragment fragment = new StaffCardListHomeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public Fragment toNewCardChargeFragment(Bundle args) {
    StaffCardChargeFragment fragment = new StaffCardChargeFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
