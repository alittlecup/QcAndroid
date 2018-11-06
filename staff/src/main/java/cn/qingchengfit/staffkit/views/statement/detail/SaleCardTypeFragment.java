package cn.qingchengfit.staffkit.views.statement.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.model.responese.SaleCardForm;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
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
public class SaleCardTypeFragment extends BaseFragment {

	TextView dataTradeTimes;
	TextView dataCharge;
	TextView dataRealIncome;
	TextView timesTradeTimes;
	TextView timesCharge;
	TextView timesRealIncomes;
	TextView valueTradeTimes;
	TextView valueCharge;
	TextView valueRealIncome;
	TextView totalServerNum;
	TextView totalRealIncome;

    public static SaleCardTypeFragment newInstance(List<SaleCardForm> t) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("form", (ArrayList<SaleCardForm>) t);
        SaleCardTypeFragment fragment = new SaleCardTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_card_form, container, false);
      dataTradeTimes = (TextView) view.findViewById(R.id.data_trade_times);
      dataCharge = (TextView) view.findViewById(R.id.data_charge);
      dataRealIncome = (TextView) view.findViewById(R.id.data_real_income);
      timesTradeTimes = (TextView) view.findViewById(R.id.times_trade_times);
      timesCharge = (TextView) view.findViewById(R.id.times_charge);
      timesRealIncomes = (TextView) view.findViewById(R.id.times_real_incomes);
      valueTradeTimes = (TextView) view.findViewById(R.id.value_trade_times);
      valueCharge = (TextView) view.findViewById(R.id.value_charge);
      valueRealIncome = (TextView) view.findViewById(R.id.value_real_income);
      totalServerNum = (TextView) view.findViewById(R.id.total_server_num);
      totalRealIncome = (TextView) view.findViewById(R.id.total_real_income);

      if (getArguments() != null) {
            ArrayList<SaleCardForm> type = getArguments().getParcelableArrayList("form");
            if (type != null) {
                for (int i = 0; i < type.size(); i++) {
                    SaleCardForm t = type.get(i);
                    switch (t.card_type) {
                        case Configs.CATEGORY_DATE:
                            dataTradeTimes.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            dataCharge.setText(StringUtils.getBalance(getContext(), t.charge, Configs.CATEGORY_DATE));
                            dataRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(t.real_income)));
                            break;
                        case Configs.CATEGORY_TIMES:
                            timesTradeTimes.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            timesCharge.setText(StringUtils.getBalance(getContext(), t.charge, Configs.CATEGORY_TIMES));
                            timesRealIncomes.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(t.real_income)));

                            break;
                        case Configs.CATEGORY_VALUE:
                            valueTradeTimes.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            //                            valueCharge.setText(StringUtils.getBalance(getContext(), t.charge, Configs.CATEGORY_VALUE));
                            valueCharge.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(t.charge)));
                            valueRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(t.real_income)));

                            break;
                        default:
                            totalServerNum.setText(String.format(Locale.CHINA, getString(R.string.trade_unit), t.trade_count));
                            totalRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(t.real_income)));

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
