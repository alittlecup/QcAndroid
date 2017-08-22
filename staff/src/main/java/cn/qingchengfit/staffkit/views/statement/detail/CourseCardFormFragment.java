package cn.qingchengfit.staffkit.views.statement.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.CourseCardForm;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.utils.StringUtils;
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
 * Created by Paper on 16/6/29 2016.
 */
public class CourseCardFormFragment extends BaseFragment {

    @BindView(R.id.data_server_num) TextView dataServerNum;
    @BindView(R.id.times_server_num) TextView timesServerNum;
    @BindView(R.id.times_course_income) TextView timesCourseIncome;
    @BindView(R.id.times_real_incomes) TextView timesRealIncomes;
    @BindView(R.id.value_server_num) TextView valueServerNum;
    @BindView(R.id.value_course_income) TextView valueCourseIncome;
    @BindView(R.id.value_real_income) TextView valueRealIncome;
    @BindView(R.id.total_server_num) TextView totalServerNum;
    @BindView(R.id.total_course_income) TextView totalCourseIncome;
    @BindView(R.id.total_real_income) TextView totalRealIncome;
    @BindView(R.id.online_server_num) TextView onlineServerNum;
    @BindView(R.id.online_course_income) TextView onlineCourseIncome;
    @BindView(R.id.online_real_income) TextView onlineRealIncome;

    public static CourseCardFormFragment newInstance(List<CourseCardForm> cardglances) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("form", (ArrayList<CourseCardForm>) cardglances);
        CourseCardFormFragment fragment = new CourseCardFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_card_form, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            ArrayList<CourseCardForm> gs = getArguments().getParcelableArrayList("form");
            if (gs != null) {
                for (int i = 0; i < gs.size(); i++) {
                    CourseCardForm g = gs.get(i);
                    switch (g.card_type) {
                        case Configs.CATEGORY_VALUE:
                            valueServerNum.setText(String.format(Locale.CHINA, getString(R.string.menber_times), g.server_count));
                            valueCourseIncome.setText(StringUtils.getBalance(getContext(), g.course_income, Configs.CATEGORY_VALUE));
                            valueRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(g.real_income)));
                            break;
                        case Configs.CATEGORY_TIMES:
                            timesServerNum.setText(String.format(Locale.CHINA, getString(R.string.menber_times), g.server_count));
                            timesCourseIncome.setText(StringUtils.getBalance(getContext(), g.course_income, Configs.CATEGORY_TIMES));
                            timesRealIncomes.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(g.real_income)));
                            break;
                        case Configs.CATEGORY_DATE:
                            dataServerNum.setText(String.format(Locale.CHINA, getString(R.string.menber_times), g.server_count));

                            break;
                        case 0:
                            onlineServerNum.setText(String.format(Locale.CHINA, getString(R.string.menber_times), g.server_count));
                            onlineCourseIncome.setText(StringUtils.getBalance(getContext(), g.course_income, Configs.CATEGORY_VALUE));
                            onlineRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(g.real_income)));
                            break;
                        default:
                            totalServerNum.setText(String.format(Locale.CHINA, getString(R.string.menber_times), g.server_count));
                            totalRealIncome.setText(String.format(Locale.CHINA, "¥%s", StringUtils.getFloatDot2(g.real_income)));
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
