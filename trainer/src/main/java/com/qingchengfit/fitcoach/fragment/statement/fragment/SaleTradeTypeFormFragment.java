package com.qingchengfit.fitcoach.fragment.statement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.statement.model.SaleTradeForm;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/6/30 2016.
 */
public class SaleTradeTypeFormFragment extends BaseFragment {

	TextView newBuyTradeTimes;
	TextView newBuyRealincome;
	TextView chargeTradeTimes;
	TextView presentTradeTimes;
	TextView presentRealIncome;
	TextView fefundTradeTimes;
	TextView refundRealIncome;
	TextView totalTradeTimes;
	TextView totalRealIncome;
	TextView chargeRealIncomes;

    public static SaleTradeTypeFormFragment newInstance(List<SaleTradeForm> ts) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("form", (ArrayList<SaleTradeForm>) ts);
        SaleTradeTypeFormFragment fragment = new SaleTradeTypeFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_trade_form, container, false);
      newBuyTradeTimes = (TextView) view.findViewById(R.id.new_buy_trade_times);
      newBuyRealincome = (TextView) view.findViewById(R.id.new_buy_realincome);
      chargeTradeTimes = (TextView) view.findViewById(R.id.charge_trade_times);
      presentTradeTimes = (TextView) view.findViewById(R.id.present_trade_times);
      presentRealIncome = (TextView) view.findViewById(R.id.present_real_income);
      fefundTradeTimes = (TextView) view.findViewById(R.id.fefund_trade_times);
      refundRealIncome = (TextView) view.findViewById(R.id.refund_real_income);
      totalTradeTimes = (TextView) view.findViewById(R.id.total_trade_times);
      totalRealIncome = (TextView) view.findViewById(R.id.total_real_income);
      chargeRealIncomes = (TextView) view.findViewById(R.id.charge_real_incomes);

      if (getArguments() != null) {
            ArrayList<SaleTradeForm> ts = getArguments().getParcelableArrayList("form");
            if (ts != null) {
                for (int i = 0; i < ts.size(); i++) {
                    SaleTradeForm t = ts.get(i);
                    switch (t.trade_type) {
                        case Configs.TRADE_CHARGE_FIRST:
                            newBuyTradeTimes.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            newBuyRealincome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.real_income))));
                            break;
                        case Configs.TRADE_CHARGE:
                            chargeTradeTimes.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            chargeRealIncomes.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.real_income))));
                            break;
                        case Configs.TRADE_REFUND:
                            fefundTradeTimes.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            refundRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.real_income))));

                            break;
                        case Configs.TRADE_PRESENT:
                            presentTradeTimes.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            presentRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.real_income))));

                            break;
                        default:
                            totalTradeTimes.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            totalRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.real_income))));

                            break;
                    }
                }
            }
        }

        return view;
    }

    @Override public String getFragmentName() {
        return null;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
