package cn.qingchengfit.staffkit.card;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.routers.cardImpl;
import cn.qingchengfit.staffkit.card.view.StaffCardBuyFragment;

/**
 * Created by fb on 2017/12/20.
 */

public class StaffCardRouters extends cardImpl {

  @Override public Fragment toCardBuyFragment(Bundle args) {
    StaffCardBuyFragment fragment = new StaffCardBuyFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
