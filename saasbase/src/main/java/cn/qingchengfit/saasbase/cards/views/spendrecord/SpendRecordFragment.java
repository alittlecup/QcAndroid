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
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qingchengfit.saasbase.R;

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

  TextView addupCharge;
  TextView addupSpend;
  TextView balance;
  ImageView minusYear;
  TextView year;
  ImageView addYear;
  LinearLayout linearLayout;
  PagerSlidingTabStrip monthTab;
  ViewPager viewpager;

  @Need Card card;
  Toolbar toolbar;
  TextView toolbarTitle;
  private int mCurYear, mCurMonth;
  private FragmentAdapter mFragmentAdapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_spendrecord, container, false);
    addupCharge = (TextView) view.findViewById(R.id.addup_charge);
    addupSpend = (TextView) view.findViewById(R.id.addup_spend);
    balance = (TextView) view.findViewById(R.id.balance);
    minusYear = (ImageView) view.findViewById(R.id.minus_year);
    year = (TextView) view.findViewById(R.id.year);
    addYear = (ImageView) view.findViewById(R.id.add_year);
    monthTab = (PagerSlidingTabStrip) view.findViewById(R.id.month_tab);
    linearLayout = view.findViewById(R.id.ll_container);
    viewpager = (ViewPager) view.findViewById(R.id.viewpager);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    view.findViewById(R.id.minus_year).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SpendRecordFragment.this.onClick(v);
      }
    });
    view.findViewById(R.id.add_year).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SpendRecordFragment.this.onClick(v);
      }
    });

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
    if (card.getType() == 3) {
      linearLayout.setVisibility(View.GONE);
    }
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

  public void onClick(View view) {
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
      SpendRecordListFragment spendRecordListFragment =
          SpendRecordListFragment.newInstance(card.getId(), mCurYear, position);
      return spendRecordListFragment;
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
