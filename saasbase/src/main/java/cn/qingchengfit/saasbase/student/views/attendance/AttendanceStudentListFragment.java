package cn.qingchengfit.saasbase.student.views.attendance;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentAttendanceStudentlistBinding;
import cn.qingchengfit.saasbase.student.bean.Absentce;
import cn.qingchengfit.saasbase.student.items.AttendanceStudentItem;
import cn.qingchengfit.saasbase.student.presenters.attendance.AttendanceStudentListPresenter;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcFilterToggle;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import rx.functions.Action0;

/**
 * Created by huangbaole on 2017/11/13.
 */
@Leaf(module = "student",path = "/student/absentce")
public class AttendanceStudentListFragment extends SaasBaseFragment implements AttendanceStudentListPresenter.MVPView{
    FragmentAttendanceStudentlistBinding binding;
    public ObservableField<String> topFilterText = new ObservableField<>("缺勤7-30天");

    AttendanceStudentListFilterFragment filterFragment;

    @Inject
    LoginStatus loginStatus;

    @Inject
    AttendanceStudentListPresenter presenter;

    CommonFlexAdapter commonFlexAdapter;

    List<AbstractFlexibleItem> items=new ArrayList<>();

    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attendance_studentlist, container, false);
        binding.setToolbarModel(new ToolbarModel("缺勤统计"));
        binding.setFragment(this);
        delegatePresenter(presenter,this);
        initToolbar(binding.includeToolbar.toolbar);
        initView();
        initFragment();
        loadData("7","30");
        return binding.getRoot();
    }

    private Action0 dismissAction = () -> {
        binding.fragFilter.setVisibility(View.GONE);
        binding.topDayToggle.setChecked(false);
    };

    private void initFragment() {
        filterFragment = new AttendanceStudentListFilterFragment();
        filterFragment.setDismissAction(dismissAction);
        filterFragment.setDaySelectAction((start, end, title) -> {
            topFilterText.set(title);
            loadData(start,end);
            dismissAction.call();
        });
        stuff(R.id.frag_filter, filterFragment);
    }

    private void initView() {

        commonFlexAdapter=new CommonFlexAdapter(items);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerview.setAdapter(commonFlexAdapter);
    }

    private void loadData(String start, String end) {
        presenter.queryAbsenceList(loginStatus.staff_id(),start,end);
    }

    public void onTopDayFilterClick(View v) {
        if (v instanceof QcFilterToggle) {
            if (((QcFilterToggle) v).isChecked()) {
                binding.fragFilter.setVisibility(View.VISIBLE);
                filterFragment.showPage(0);
            } else {
                binding.fragFilter.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAbsence(List<Absentce> attendances, int total_count) {
        hideLoading();
        binding.tipsAbsenceAccount.setText(String.format(Locale.CHINA, getString(R.string.absence_account), topFilterText.get(), total_count));
        items.clear();
        for (Absentce absentce : attendances) {
            items.add(new AttendanceStudentItem(absentce));
        }
        commonFlexAdapter.notifyDataSetChanged();
    }
}
