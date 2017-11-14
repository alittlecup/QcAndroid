package cn.qingchengfit.saasbase.student.views.attendance;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentAttendanceStudentRankBinding;
import cn.qingchengfit.saasbase.student.bean.Attendance;
import cn.qingchengfit.saasbase.student.items.AttendanceRankItem;
import cn.qingchengfit.saasbase.student.presenters.attendance.AttendanceRankPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcFilterToggle;
import rx.functions.Action0;

/**
 * Created by huangbaole on 2017/11/14.
 */
@Leaf(module = "student",path = "/attendance/rank")
public class AttendanceRankFragment extends BaseFragment implements AttendanceRankPresenter.MVPView {
    FragmentAttendanceStudentRankBinding binding;
    AttendanceRankFilterView rankFilterView;
    public ObservableField<String> topDay = new ObservableField<>("最近30天");
    public ObservableBoolean revert = new ObservableBoolean(false);
    public ObservableField<String> sortype = new ObservableField<>(SORTTYPE.DAYS);

    @Inject
    LoginStatus loginStatus;
    @Inject
    AttendanceRankPresenter presenter;
    private String start, end;

    List<AttendanceRankItem> items = new ArrayList();
    CommonFlexAdapter commonFlexAdapter;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SORTTYPE {
        String CHECKIN = "checkin";
        String GROUP = "group";
        String PRIVATE = "private";
        String DAYS = "days";

    }

    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    public String typeToString(@SORTTYPE String curType) {
        switch (curType) {
            case SORTTYPE.CHECKIN:
                return "签到次数";
            case SORTTYPE.GROUP:
                return "团课节数";
            case SORTTYPE.PRIVATE:
                return "私教节数";
            case SORTTYPE.DAYS:
                return "出勤天数";
        }
        return "";
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attendance_student_rank, container, false);
        delegatePresenter(presenter, this);
        initToolbar(binding.includeToolbar.toolbar);
        binding.setToolbarModel(new ToolbarModel("出勤统计"));
        binding.setFragment(this);
        initFragment();
        initView();
        initData();
        loadData();
        return binding.getRoot();
    }

    private void initView() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        commonFlexAdapter = new CommonFlexAdapter(items);
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.HORIZONTAL, 1));
        binding.recyclerview.setAdapter(commonFlexAdapter);


    }

    private void initData() {
        start = DateUtils.minusDay(new Date(), 29);
        end = DateUtils.getStringToday();
    }

    private Action0 dimissAction = () -> {
        binding.topTimeToggle.setChecked(false);
        binding.fragFilter.setVisibility(View.GONE);
    };

    private void initFragment() {
        rankFilterView = new AttendanceRankFilterView();
        rankFilterView.setDismissAction(dimissAction);
        rankFilterView.setSelectDayAction((start, end, title) -> {
            topDay.set(title);
            this.start = start;
            this.end = end;
            loadData();
            dimissAction.call();

        });
        rankFilterView.setSortAction((pos, revert) -> {
            switch (pos) {
                case 0:
                    sortype.set(SORTTYPE.DAYS);
                    break;
                case 1:
                    sortype.set(SORTTYPE.CHECKIN);
                    break;
                case 2:
                    sortype.set(SORTTYPE.GROUP);
                    break;
                case 3:
                    sortype.set(SORTTYPE.PRIVATE);
                    break;
            }
            this.revert.set(revert);
            commonFlexAdapter.setStatus(pos);
            loadData();
            dimissAction.call();
        });
        stuff(R.id.frag_filter, rankFilterView);
    }

    private void loadData() {
        presenter.getRank(loginStatus.staff_id(), start, end, sortype.get(), revert.get());
    }

    public void onTopDayFilterClick(View view) {
        if (view instanceof QcFilterToggle) {
            if (!((QcFilterToggle) view).isChecked()) {
                binding.fragFilter.setVisibility(View.GONE);
            } else {
                binding.fragFilter.setVisibility(View.VISIBLE);
                rankFilterView.showPage(0);
            }
        }
    }

    private boolean sortFlag;

    public void onSortFilterClick() {
        if (!sortFlag) {
            binding.fragFilter.setVisibility(View.VISIBLE);
            rankFilterView.showPage(1);
        } else {
            binding.fragFilter.setVisibility(View.GONE);
        }
        sortFlag = !sortFlag;
    }


    @Override
    public void onAttendances(List<Attendance> attendances, int pages) {
        hideLoading();
        items.clear();
        binding.tipsAbsenceAccount.setText(topDay.get()+ "出勤会员共" + pages + "人");
        for (Attendance attendance : attendances) {
            items.add(new AttendanceRankItem(attendance, getContext()));
        }
        commonFlexAdapter.setTag("revert",revert.get());
        commonFlexAdapter.updateDataSet(items);

    }
}
