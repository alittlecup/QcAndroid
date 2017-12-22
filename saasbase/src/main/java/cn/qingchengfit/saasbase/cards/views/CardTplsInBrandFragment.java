package cn.qingchengfit.saasbase.cards.views;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import cn.qingchengfit.saasbase.cards.presenters.CardTplListInBrandPresenter;
import com.anbillon.flabellum.annotations.Leaf;
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
 * Created by Paper on 2017/12/4.
 */
@Leaf(module = "card", path = "/brand/cardtpl/list/") public class CardTplsInBrandFragment
  extends CardTplsHomeInGymFragment {

  @Inject CardTplListInBrandPresenter presenterBrand;

  @Override void initFragments() {
    if (fragmentList.size() == 0) {
      for (int i = 0; i < 3; i++) {
        fragmentList.add(new CardTplListFragment());
      }
    }
  }

  @Override public CardTplListInBrandPresenter getPresenter() {
    return presenterBrand;
  }

  @Override public void onMenuAdd(int position) {
    routeTo("/cardtpl/add/brand",
        new cn.qingchengfit.saasbase.cards.views.CardtplAddParams().cardCategory(
            position + 1).build());
  }

  @Override void initVp() {
    pageAdapter = new CardViewpagerAdapterInBrand(getChildFragmentManager());
    viewpager.setAdapter(pageAdapter);
    tab.setupWithViewPager(viewpager);
    viewpager.setOffscreenPageLimit(fragmentList.size());
    viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      //页面变化
      @Override public void onPageSelected(int position) {
        cardCount.setText(fragmentList.get(position).getItemCount() + "");
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }

  public class CardViewpagerAdapterInBrand extends CardViewpagerAdapter {

    public CardViewpagerAdapterInBrand(FragmentManager fm) {
      super(fm);
    }

    @Override public int getCount() {
      return 3;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 1:
          return "多场馆通卡";
        case 2:
          return "单场馆卡";
        default:
          return "全部";
      }
    }
  }
}
