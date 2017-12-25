package cn.qingchengfit.shop.ui.home;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Pair;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopHomePageAdapter extends FragmentStatePagerAdapter {
  List<Pair<String, Fragment>> fragments;

  public ShopHomePageAdapter(FragmentManager manager, List<Pair<String, Fragment>> fragments) {
    super(manager);
    this.fragments = fragments;
  }

  @Nullable @Override public CharSequence getPageTitle(int position) {
    return fragments.get(position).first;
  }

  @Override public Fragment getItem(int position) {
    return fragments.get(position).second;
  }

  @Override public int getCount() {
    return fragments.size();
  }
}
