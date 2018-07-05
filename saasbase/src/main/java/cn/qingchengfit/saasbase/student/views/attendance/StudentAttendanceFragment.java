package cn.qingchengfit.saasbase.student.views.attendance;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentSaasAttendanceHomeBinding;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.presenters.attendance.StudentAttendancePresenter;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.QcFilterToggle;
import com.anbillon.flabellum.annotations.Leaf;
import javax.inject.Inject;
import rx.functions.Action0;

/**
 * Created by huangbaole on 2017/11/13.
 */
@Leaf(module = "student", path = "/student/attendance")
public class StudentAttendanceFragment extends BaseFragment implements StudentAttendancePresenter.MVPView {
    FragmentSaasAttendanceHomeBinding binding;
    AttendanceFilterView filterFragment;
    @Inject
    StudentAttendancePresenter presenter;
    @Inject
    LoginStatus loginStatus;

    public ObservableField<String> latestDay = new ObservableField<>("最近7天");

    private Action0 dismissAction = () -> {
        binding.fragFilter.setVisibility(View.GONE);
        binding.textRecentCondition.setChecked(false);
    };
    private int offSetDay = 6;

    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saas_attendance_home, container, false);
        binding.setFragment(this);
        initToolbar(binding.includeToolbar.toolbar);
        delegatePresenter(presenter, this);
        binding.setToolbarModel(new ToolbarModel("出勤统计"));
        initFragment();
        loadData("", "");
        return binding.getRoot();
    }

    private void loadData(String start, String end) {
        presenter.loadAttendanceData(loginStatus.staff_id(), start, end);
    }

    private void initFragment() {
        filterFragment = new AttendanceFilterView();
        stuff(R.id.frag_filter, filterFragment);
    }

    public void onLatestDayClick(View view) {
        if (view instanceof QcFilterToggle) {
            if (((QcFilterToggle) view).isChecked()) {
                binding.fragFilter.setVisibility(View.VISIBLE);
                filterFragment.showPage(0);
            } else {
                binding.fragFilter.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAbsence(AttendanceCharDataBean statistic) {
        //binding.lineChartDate.setData(StudentBusinessUtils.transformBean2Data(statistic.datas, offSetDay
        //        , Color.parseColor("#FF8CB4B9"), Color.parseColor("#648CB4B9")));
    }

    public void toStudentAbsentce(View v) {
        Uri uri = Uri.parse("qcstaff://student/student/absentce");
        routeTo(uri, null);
    }

    public void toStudentRank() {
        Uri uri = Uri.parse("qcstaff://student/attendance/rank");
        routeTo(uri, null);
    }

    public void toStudentNosign() {
        Uri uri = Uri.parse("qcstaff://student/attendance/nosign");
        routeTo(uri, null);
    }
}
