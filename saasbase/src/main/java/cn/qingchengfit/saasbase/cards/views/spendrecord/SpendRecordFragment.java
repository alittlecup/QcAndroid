package cn.qingchengfit.saasbase.cards.views.spendrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.utils.StringUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.Calendar;
import java.util.Locale;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/4/16 2016.
 */
@Leaf(module = "card", path = "/consumption/record/") public class SpendRecordFragment
    extends SaasBaseFragment {

  @BindView(R2.id.addup_charge) TextView addupCharge;
  @BindView(R2.id.addup_spend) TextView addupSpend;
  @BindView(R2.id.balance) TextView balance;
  @BindView(R2.id.minus_year) ImageView minusYear;
  @BindView(R2.id.year) TextView year;
  @BindView(R2.id.add_year) ImageView addYear;
  @BindView(R2.id.month_tab) PagerSlidingTabStrip monthTab;
  @BindView(R2.id.viewpager) ViewPager viewpager;

  @Need Card card;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  private int mCurYear, mCurMonth;
  private FragmentAdapter mFragmentAdapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_spendrecord, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    toolbarTitle.setText("消费记录");

    mCurYear = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
    mCurMonth = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH);
    mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
    viewpager.setAdapter(mFragmentAdapter);
    viewpager.setOffscreenPageLimit(1);
    viewpager.setCurrentItem(mCurMonth, false);
    monthTab.setViewPager(viewpager);
    balance.setText(getCardBlance(card));
    if (card.getType() == Configs.CATEGORY_DATE) {
      addupCharge.setVisibility(View.GONE);
      addupSpend.setVisibility(View.GONE);
    } else {
      addupCharge.setVisibility(View.VISIBLE);
      addupSpend.setVisibility(View.VISIBLE);
    }
    addupCharge.setText(
        card.getTotal_account() + StringUtils.getUnit(getContext(), card.getType()));
    addupSpend.setText(card.getTotal_cost() + "元");
    year.setText(mCurYear + "年");
    return view;
  }

  private String getCardBlance(Card card) {
    switch (card.getType()) {
      case 2:
        return ((Float) card.getBalance()).intValue() + "次";
      case 3:
        return ((Float) card.getBalance()).intValue() + "天";
      default:
        return StringUtils.getFloatDot2(card.getBalance()) + "元";
    }
  }

  @Override public String getFragmentName() {
    return null;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @OnClick({ R2.id.minus_year, R2.id.add_year }) public void onClick(View view) {
    int i = view.getId();
    if (i == R.id.minus_year) {
      mCurYear--;
    } else if (i == R.id.add_year) {
      mCurYear++;
    }
    year.setText(mCurYear + "年");
    mFragmentAdapter.notifyDataSetChanged();
  }

  public class FragmentAdapter extends FragmentStatePagerAdapter {

    public FragmentAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      return SpendRecordListFragment.newInstance(card.getId(), mCurYear, position);
    }

    @Override public int getCount() {
      return 12;
    }

    @Override public CharSequence getPageTitle(int position) {
      return (position + 1) + "月";
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }
  }
}
