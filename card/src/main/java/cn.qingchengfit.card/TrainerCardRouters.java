package cn.qingchengfit.card;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.card.view.StaffCardBuyFragment;
import cn.qingchengfit.card.view.StaffCardChargeFragment;
import cn.qingchengfit.card.view.StaffCardListHomeFragment;
import cn.qingchengfit.card.view.TrainerCardBuyFragment;
import cn.qingchengfit.card.view.TrainerCardChargeFragment;
import cn.qingchengfit.saasbase.routers.cardImpl;

public class TrainerCardRouters extends cardImpl {
  @Override public Fragment toCardBuyFragment(Bundle args) {
    TrainerCardBuyFragment fragment = new TrainerCardBuyFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public Fragment toCardListHomeFragment(Bundle args) {
    StaffCardListHomeFragment fragment = new StaffCardListHomeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public Fragment toNewCardChargeFragment(Bundle args) {
    TrainerCardChargeFragment fragment = new TrainerCardChargeFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
