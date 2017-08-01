package cn.qingchengfit.staffkit.views.statement.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.SigninReportForm;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 签到报表详情上部表格
 */
public class SigninReportFormFragment extends BaseFragment {

  @BindView(R.id.tv_signin_report_date_count) TextView tv_signin_report_date_count;
  @BindView(R.id.tv_signin_report_date_income) TextView tvSigninReportDateIncome;
  @BindView(R.id.tv_siginin_report_times_count) TextView tv_siginin_report_times_count;
  @BindView(R.id.tv_siginin_report_value_count) TextView tv_siginin_report_value_count;
  @BindView(R.id.tv_siginin_report_value_income) TextView tv_siginin_report_value_income;
  @BindView(R.id.tv_siginin_report_total_count) TextView tv_siginin_report_total_count;
  @BindView(R.id.tv_siginin_report_total_income) TextView tv_siginin_report_total_income;
  @BindView(R.id.tv_siginin_report_times_incomes) TextView tv_siginin_report_times_incomes;
  @BindView(R.id.tv_signin_report_date_cost) TextView tvSigninReportDateCost;
  @BindView(R.id.tv_siginin_report_times_cost) TextView tvSigininReportTimesCost;
  @BindView(R.id.tv_siginin_report_value_cost) TextView tvSigininReportValueCost;
  @BindView(R.id.tv_siginin_report_total_cost) TextView tvSigininReportTotalCost;

  public static SigninReportFormFragment newInstance(List<SigninReportForm> ts) {

    Bundle args = new Bundle();
    args.putParcelableArrayList("form", (ArrayList<SigninReportForm>) ts);
    SigninReportFormFragment fragment = new SigninReportFormFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_report_form, container, false);
    unbinder = ButterKnife.bind(this, view);
    if (getArguments() != null) {
      ArrayList<SigninReportForm> ts = getArguments().getParcelableArrayList("form");
      if (ts != null) {
        for (int i = 0; i < ts.size(); i++) {
          SigninReportForm t = ts.get(i);
          switch (t.card_type) {
            case Configs.CATEGORY_VALUE:
              tv_siginin_report_value_count.setText(
                  String.format(Locale.CHINA, getString(R.string.menber_times), t.signin_count));
              tv_siginin_report_value_income.setText(
                  String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.real_income))));
              tvSigininReportValueCost.setText(
                  String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.cost))));
              break;
            case Configs.CATEGORY_TIMES:
              tv_siginin_report_times_count.setText(
                  String.format(Locale.CHINA, getString(R.string.menber_times), t.signin_count));
              tv_siginin_report_times_incomes.setText(
                  String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.real_income))));
              tvSigininReportTimesCost.setText(
                  String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.cost))));
              break;
            case Configs.CATEGORY_DATE:
              tv_signin_report_date_count.setText(
                  String.format(Locale.CHINA, getString(R.string.menber_times), t.signin_count));
              break;
            default:
              tv_siginin_report_total_count.setText(
                  String.format(Locale.CHINA, getString(R.string.menber_times), t.signin_count));
              tv_siginin_report_total_income.setText(
                  String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.real_income))));
              tvSigininReportTotalCost.setText(
                  String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2((t.cost))));
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
