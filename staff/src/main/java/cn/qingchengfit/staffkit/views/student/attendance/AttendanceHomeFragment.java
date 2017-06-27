package cn.qingchengfit.staffkit.views.student.attendance;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.items.FilterCommonLinearItem;
import cn.qingchengfit.model.responese.AttendanceCharDataBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.FilterFragment;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * Created by Paper on 2017/2/27.
 */
public class AttendanceHomeFragment extends BaseFragment
    implements FilterFragment.OnSelectListener, AttendanceChartPresenter.AttendanceView {

    @BindView(R.id.btn_absentee) ViewGroup btnAbsentete;
    @BindView(R.id.btn_attendence) ViewGroup btnAttendence;
    @BindView(R.id.rl_absence_top_filter) RelativeLayout rlAbsenceTopFilter;
    @BindView(R.id.text_recent_condition) TextView textRecentCondition;
    @BindView(R.id.image_up) ImageView imgUp;
    @BindView(R.id.attendance_list_shadow) View shadow;
    @BindView(R.id.filter_fragment) ViewGroup filterLayout;

    @BindColor(R.color.white) int white;
    @BindColor(R.color.bg_grey) int grey;
    @Inject AttendanceChartPresenter chartPresenter;
    private List<AbstractFlexibleItem> filterList = new ArrayList<>();
    private FilterFragment filterFragment;
    private AttendanceStaticFragment staticFragment;

    private boolean isThirty;
    private boolean isShow = false;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCallbackActivity.setToolbar("会员出勤", false, null, 0, null);
        delegatePresenter(chartPresenter, this);

        rlAbsenceTopFilter.setTag(false);

        initFilterData();
        filterFragment = new FilterFragment();
        filterFragment.setItemList(filterList);
        filterFragment.setOnSelectListener(this);

        getChildFragmentManager().beginTransaction().replace(R.id.filter_fragment, filterFragment).commit();

        toggleViewState();

        staticFragment = new AttendanceStaticFragmentBuilder().build();
        staticFragment.setAttendance(true);
        staticFragment.touchable = true;
        getChildFragmentManager().beginTransaction().replace(R.id.frag_attendance_home, staticFragment).commit();

        chartPresenter.queryChartData("", "");
        return view;
    }

    private void toggleViewState() {
        rlAbsenceTopFilter.setAlpha(isShow ? 1f : 0.4f);
        rlAbsenceTopFilter.setBackgroundColor(isShow ? white : grey);
        imgUp.setImageResource(isShow ? R.drawable.vector_arrow_up_grey : R.drawable.vector_arrow_down_grey);
        shadow.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (isShow) {
            filterFragment.setFilterAnimation(filterLayout, isShow);
        } else if (filterLayout.getHeight() > 0) {
            filterFragment.setFilterAnimation(filterLayout, isShow);
        }
    }

    @OnClick({ R.id.btn_absentee, R.id.btn_attendence, R.id.rl_absence_top_filter }) public void onJump(View view) {
        switch (view.getId()) {
            case R.id.btn_absentee:
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_attendance_frag, new AbsenceStuentListFragment())
                    .addToBackStack(null)
                    .commit();
                break;

            case R.id.btn_attendence:
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_attendance_frag, new AttendanceRankFragment())
                    .addToBackStack(null)
                    .commit();
                break;

            case R.id.rl_absence_top_filter:
                isShow = !isShow;
                toggleViewState();
                break;
        }
    }

    @OnClick(R.id.attendance_list_shadow) public void onDismiss() {
        isShow = false;
        toggleViewState();
    }

    private void initFilterData() {
        filterList.clear();
        filterList.add(new FilterCommonLinearItem("最近7天 (" + (DateUtils.minusDay(new Date(), 6)) + "至" + DateUtils.getStringToday() + ")"));
        filterList.add(
            new FilterCommonLinearItem("最近30天 (" + (DateUtils.minusDay(new Date(), 29)) + "至" + DateUtils.getStringToday() + ")"));
    }

    @Override public String getFragmentName() {
        return AttendanceHomeFragment.class.getName();
    }

    @Override public void onSelectItem(int position) {
        textRecentCondition.setText(((FilterCommonLinearItem) filterList.get(position)).getData().split(" ")[0]);
        switch (position) {
            case 0:
                isThirty = false;
                chartPresenter.refreshData("", "");
                break;
            case 1:
                isThirty = true;
                chartPresenter.refreshData(DateUtils.minusDay(new Date(), 29), DateUtils.getStringToday());
                break;
        }
        isShow = false;
        toggleViewState();
    }

    @Override public void onAbsence(AttendanceCharDataBean statistic) {
        if (isThirty) {
            staticFragment.setData(statistic, 30);
        } else {
            staticFragment.setData(statistic, 7);
        }
    }

    @Override public void onNoMore() {

    }

    @Override public void clearDatas() {

    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }
}
