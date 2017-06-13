package cn.qingchengfit.staffkit.views.student.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.common.Attendance;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.FilterCommonLinearItem;
import cn.qingchengfit.staffkit.allocate.FilterFragment;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.student.detail.StudentsDetailActivity;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
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
 * Created by Paper on 2017/2/28.
 */

public class AttendanceRankFragment extends BaseFragment
    implements AttendanceRankPresenter.MVPView, FilterFragment.OnSelectListener, FilterCustomFragment.OnBackFilterDataListener,
    FilterSortItem.OnSortDataListener, FlexibleAdapter.OnItemClickListener, FlexibleAdapter.EndlessScrollListener {

    public static final int RANK_BY_ATTENDANCE = 0;
    public static final int RANK_BY_CHECKIN = 1;
    public static final int RANK_BY_GROUP = 2;
    public static final int RANK_BY_PRIVATE = 3;

    int rankType = 0;

    @BindView(R.id.ll_filter_attendance_account) ViewGroup llAbsenceAccount;
    @BindView(R.id.ll_filter_attendance_sort) ViewGroup llSort;
    @BindView(R.id.text_attendance_condition) TextView textAbsence;
    @BindView(R.id.text_attendance_sort) TextView textSort;
    @BindView(R.id.image_attendance_account_arrow) ImageView imageAbsenceArrow;
    @BindView(R.id.image_attendance_sort_arrow) ImageView imageSortArrow;
    @BindView(R.id.rl_attendance_account) RecyclerView rlAttendList;
    @BindView(R.id.tips_attendance_account) TextView textTipsAccount;
    @BindView(R.id.attendance_list_shadow) View shadow;
    @BindView(R.id.frag_attendance_rank) ViewGroup filteLayout;
    @BindView(R.id.frag_attendance_sort) ViewGroup sortLayout;
    @Inject AttendanceRankPresenter attendanceRankPresenter;

    private List<AbstractFlexibleItem> filterList = new ArrayList<>();
    private List<AbstractFlexibleItem> sortList = new ArrayList<>();

    private List<AbstractFlexibleItem> itemList = new ArrayList<>();
    private List<FilterSortBean> dataList = new ArrayList<>();

    private String titleText;
    private String start, end;
    private int account;
    private FilterFragment filterFragment;
    private FilterFragment sortFragment;
    private FilterCustomFragment filterCustomFragment;
    private CommonFlexAdapter commonFlexAdapter;
    private boolean isShow = false;
    private boolean isRightShow = false;
    private boolean isCustom = false;
    private boolean isRevert;
    private ViewGroup.LayoutParams params;
    private ViewGroup.LayoutParams sortParams;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_rank, container, false);
        unbinder = ButterKnife.bind(this, view);

        delegatePresenter(attendanceRankPresenter, this);

        mCallbackActivity.setToolbar("出勤排行", false, null, 0, null);

        initFilterData();
        filterCustomFragment = FilterCustomFragmentBuilder.newFilterCustomFragment("时间段");
        filterCustomFragment.setSelectTime(true);
        filterCustomFragment.setOnBackFilterDataListener(this);

        sortFragment = new FilterFragment();
        sortFragment.setItemList(sortList);

        filterFragment = new FilterFragment();
        filterFragment.setItemList(filterList);
        filterFragment.setOnSelectListener(this);

        titleText = "最近30天";

        start = DateUtils.minusDay(new Date(), 29);
        end = DateUtils.getStringToday();

        attendanceRankPresenter.setParams(start, end, 0, false);

        llAbsenceAccount.setTag(false);
        llSort.setTag(false);

        rlAttendList.setLayoutManager(new LinearLayoutManager(getContext()));
        commonFlexAdapter = new CommonFlexAdapter(itemList, this);
        //commonFlexAdapter.setEndlessScrollListener(this,new ProgressItem(getContext()));
        rlAttendList.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.divider_horizon_left_44dp, 1));
        rlAttendList.setAdapter(commonFlexAdapter);

        getChildFragmentManager().beginTransaction().replace(R.id.frag_attendance_rank, filterFragment).commit();
        getChildFragmentManager().beginTransaction().add(R.id.frag_attendance_sort, sortFragment).commit();

        params = filteLayout.getLayoutParams();
        params.height = 0;
        filteLayout.setLayoutParams(params);

        sortParams = sortLayout.getLayoutParams();
        sortParams.height = 0;
        sortLayout.setLayoutParams(sortParams);

        attendanceRankPresenter.getRank();
        toggleViewState();
        return view;
    }

    private void initFilterData() {
        filterList.clear();
        dataList.clear();
        sortList.clear();

        filterList.add(
            new FilterCommonLinearItem("最近7天 (" + (DateUtils.minusDay(new Date(), 29)) + "至" + DateUtils.getStringToday() + ")"));
        filterList.add(
            new FilterCommonLinearItem("最近30天 (" + (DateUtils.minusDay(new Date(), 6)) + "至" + DateUtils.getStringToday() + ")"));
        filterList.add(new FilterCommonLinearItem("自定义"));

        dataList.add(new FilterSortBean("出勤天数", true, false));
        dataList.add(new FilterSortBean("签到次数", false, false));
        dataList.add(new FilterSortBean("团课节数", false, false));
        dataList.add(new FilterSortBean("私教节数", false, false));

        for (FilterSortBean filterSortBean : dataList) {
            sortList.add(new FilterSortItem(filterSortBean, this));
        }
    }

    //重置选中排序状态
    private void resetSortData() {
        for (int i = 0; i < dataList.size(); i++) {
            if (i == rankType) {
                continue;
            } else {
                dataList.get(i).isHighToLow = false;
                dataList.get(i).isLowToHigh = false;
            }
        }
    }

    private void toggleViewState() {

        textAbsence.setText(titleText);
        imageAbsenceArrow.setImageResource(isShow ? R.drawable.vector_arrow_up_grey : R.drawable.vector_arrow_down_grey);
        if (isCustom) {
            textAbsence.setText(start.substring(5, start.length()) + "至" + end.substring(5, end.length()));
        }

        textSort.setText(dataList.get(rankType).name + "");

        if (dataList.get(rankType).isHighToLow) {
            imageSortArrow.setImageResource(R.drawable.ic_arrow_downward_black);
        }
        if (dataList.get(rankType).isLowToHigh) {
            imageSortArrow.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }

        shadow.setVisibility(isShow || isRightShow ? View.VISIBLE : View.GONE);

        if (isShow) {
            filterFragment.setFilterAnimation(filteLayout, isShow);
        } else if (filteLayout.getHeight() > 0) {
            filterFragment.setFilterAnimation(filteLayout, false);
        }
        if (isRightShow) {
            sortFragment.setFilterAnimation(sortLayout, isRightShow);
        } else if (sortLayout.getHeight() > 0) {
            sortFragment.setFilterAnimation(sortLayout, false);
        }
    }

    //天数筛选
    @OnClick(R.id.ll_filter_attendance_account) public void onConditionFilter() {
        isShow = !isShow;
        isRightShow = false;
        toggleViewState();
    }

    //排序菜单
    @OnClick(R.id.ll_filter_attendance_sort) public void onConditionSort() {
        isRightShow = !isRightShow;
        isShow = false;
        toggleViewState();
    }

    @OnClick(R.id.attendance_list_shadow) public void onDismiss() {
        isShow = isRightShow = false;
        toggleViewState();
    }

    @Override public String getFragmentName() {
        return AbsenceStuentListFragment.class.getName();
    }

    @Override public void onAttendances(List<Attendance> attendances, int curPage, int maxPages, int pages) {
        hideLoading();
        if (curPage == 1) itemList.clear();
        account = pages;
        if (!isCustom) {
            textTipsAccount.setText(titleText + "出勤会员共" + account + "人");
        } else {
            textTipsAccount.setText(start + "至" + end + "出勤会员共" + account + "人");
        }
        for (Attendance attendance : attendances) {
            itemList.add(new AttendanceRankItem(attendance, getContext()));
        }
        commonFlexAdapter.setTag("revert", isRevert);
        commonFlexAdapter.notifyDataSetChanged();
    }

    @Override public void onNoMore() {

    }

    @Override public void clearDatas() {

    }

    @Override public void onShowError(String e) {

    }

    @Override public void onShowError(@StringRes int e) {

    }

    @Override public void onSelectItem(int position) {
        isShow = false;
        switch (position) {
            case 0:
                showLoading();
                titleText = "最近7天";
                start = DateUtils.minusDay(new Date(), 6);
                end = DateUtils.getStringToday();
                attendanceRankPresenter.setParams(start, end, rankType, false);
                isCustom = false;
                toggleViewState();
                break;
            case 1:
                showLoading();
                titleText = "最近30天";
                start = DateUtils.minusDay(new Date(), 29);
                end = DateUtils.getStringToday();
                attendanceRankPresenter.setParams(start, end, rankType, false);
                isCustom = false;
                toggleViewState();
                break;
            case 2:
                getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
                    .replace(R.id.frag_attendance_rank, filterCustomFragment)
                    .addToBackStack(null)
                    .commit();
                break;
        }
    }

    @Override public void onSettingData(String start, String end) {
        getChildFragmentManager().popBackStackImmediate();
        showLoading();
        attendanceRankPresenter.setParams(start, end, rankType, false);
        isShow = false;
        isCustom = true;
        this.start = start;
        this.end = end;
        toggleViewState();
    }

    @Override public void onBack() {
        getChildFragmentManager().popBackStackImmediate();
    }

    @Override public void onHighToLow(int position) {
        showLoading();
        rankType = position;
        resetSortData();
        dataList.get(position).isHighToLow = true;
        dataList.get(position).isLowToHigh = false;
        sortFragment.refresh();

        isRevert = false;
        commonFlexAdapter.setStatus(position);
        attendanceRankPresenter.setParams(start, end, rankType, false);

        isRightShow = false;
        toggleViewState();
    }

    @Override public void onLowToHigh(int position) {
        showLoading();
        rankType = position;
        resetSortData();
        dataList.get(position).isLowToHigh = true;
        dataList.get(position).isHighToLow = false;
        sortFragment.refresh();

        isRevert = true;
        commonFlexAdapter.setStatus(position);
        attendanceRankPresenter.setParams(start, end, rankType, true);

        isRightShow = false;
        toggleViewState();
    }

    @Override public boolean onItemClick(int position) {
        Intent it = new Intent(getActivity(), StudentsDetailActivity.class);
        if (attendanceRankPresenter.handleStudentBean(position) != null) {
            it.putExtra("student", attendanceRankPresenter.handleStudentBean(position));
        } else {
            it.putExtra("student", new StudentBean());
        }
        startActivity(it);
        return false;
    }

    @Override public void noMoreLoad(int i) {
        ToastUtils.show("没有更多数据");
    }

    /**
     * 先屏蔽掉加载
     */
    @Override public void onLoadMore(int i, int i1) {
        //attendanceRankPresenter.getRank();
    }
}
