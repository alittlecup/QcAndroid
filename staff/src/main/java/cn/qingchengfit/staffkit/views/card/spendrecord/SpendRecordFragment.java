package cn.qingchengfit.staffkit.views.card.spendrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.custom.PagerSlidingTabStrip;
import cn.qingchengfit.utils.RealCardUtils;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.Calendar;
import java.util.Locale;
import javax.inject.Inject;

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
public class SpendRecordFragment extends BaseFragment {

    @BindView(R.id.addup_charge) TextView addupCharge;
    @BindView(R.id.addup_spend) TextView addupSpend;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.minus_year) ImageView minusYear;
    @BindView(R.id.year) TextView year;
    @BindView(R.id.add_year) ImageView addYear;
    @BindView(R.id.month_tab) PagerSlidingTabStrip monthTab;
    @BindView(R.id.viewpager) ViewPager viewpager;

    @Inject RealcardWrapper realCard;
    private int mCurYear, mCurMonth;
    private FragmentAdapter mFragmentAdapter;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spendrecord, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCallbackActivity.setToolbar("消费记录", false, null, 0, null);

        mCurYear = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
        mCurMonth = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH);
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        viewpager.setAdapter(mFragmentAdapter);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setCurrentItem(mCurMonth, false);
        monthTab.setViewPager(viewpager);
        balance.setText(RealCardUtils.getCardBlance(realCard.getRealCard()));
        if (realCard.type() == Configs.CATEGORY_DATE) {
            addupCharge.setVisibility(View.GONE);
            addupSpend.setVisibility(View.GONE);
        } else {
            addupCharge.setVisibility(View.VISIBLE);
            addupSpend.setVisibility(View.VISIBLE);
        }
        addupCharge.setText(realCard.getRealCard().getTotal_account() + StringUtils.getUnit(getContext(), realCard.type()));
        addupSpend.setText(realCard.getRealCard().getTotal_cost() + "元");
        year.setText(mCurYear + "年");
        return view;
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({ R.id.minus_year, R.id.add_year }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.minus_year:
                mCurYear--;
                break;
            case R.id.add_year:
                mCurYear++;
                break;
        }
        year.setText(mCurYear + "年");
        mFragmentAdapter.notifyDataSetChanged();
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            return SpendRecordListFragment.newInstance(mCurYear, position);
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
