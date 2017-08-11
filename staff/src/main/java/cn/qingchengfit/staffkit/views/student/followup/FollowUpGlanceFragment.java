package cn.qingchengfit.staffkit.views.student.followup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.responese.FollowUpDataStatistic;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.event.EventRouter;
import cn.qingchengfit.utils.LogUtil;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

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
 * Created by Paper on 2017/5/4.
 */
@FragmentWithArgs public class FollowUpGlanceFragment extends BaseFragment {

    @Arg int type;
    @Arg FollowUpDataStatistic.NewCreateUsersBean data;

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_data_today) TextView tvDataToday;
    @BindView(R.id.tv_data_7day) TextView tvData7day;
    @BindView(R.id.tv_data_30day) TextView tvData30day;
    FollowUpDataStatisticsBaseFragment fragment;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_followup_glance, container, false);
        unbinder = ButterKnife.bind(this, view);
        try {

            switch (type) {
                case 1:
                    tvTitle.setText("新增跟进");
                    fragment = new FollowUpDataStatistics1Fragment();
                    break;
                case 2:
                    tvTitle.setText("新增会员");
                    fragment = new FollowUpDataStatistics2Fragment();
                    break;
                default:
                    tvTitle.setText("新增注册");
                    fragment = new FollowUpDataStatistics0Fragment();
                    break;
            }

            tvDataToday.setText(data.today_count + "");
            tvData7day.setText(data.week_count + "");
            tvData30day.setText(data.month_count + "");
            fragment.touchable = true;
            getChildFragmentManager().beginTransaction().replace(R.id.frag_regist, fragment).commitNowAllowingStateLoss();
        } catch (Exception e) {
          LogUtil.e(e.getMessage());
        }
        return view;
    }

    @Override protected void onFinishAnimation() {
        fragment.setData(data, 30);
    }

    @OnClick({ R.id.layout_title, R.id.layout_data }) public void clickTitle() {
        EventRouter eventRouter = new EventRouter(RouterFollowUp.NEW_REGISTE_TODAY);
        switch (type) {
            case 1:
                eventRouter.setPath(RouterFollowUp.FOLLOWUPING_TODAY);
                break;
            case 2:
                eventRouter.setPath(RouterFollowUp.STUDENT_TODAY);
                break;
            default:
                break;
        }
        RxBus.getBus().post(eventRouter);
    }

    @Override public String getFragmentName() {
        return FollowUpGlanceFragment.class.getName();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }
}
