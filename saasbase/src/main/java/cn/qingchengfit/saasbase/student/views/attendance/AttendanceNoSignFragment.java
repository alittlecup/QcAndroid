package cn.qingchengfit.saasbase.student.views.attendance;

import android.Manifest;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentAttendanceStudentNosignBinding;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.saasbase.student.items.NotSignClassItem;
import cn.qingchengfit.saasbase.student.presenters.attendance.AttendanceNosignPresenter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcFilterToggle;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by huangbaole on 2017/11/14.
 */

@Leaf(module = "student", path = "/attendance/nosign")
public class AttendanceNoSignFragment extends BaseFragment implements AttendanceNosignPresenter.MVPView {
    private FragmentAttendanceStudentNosignBinding binding;
    AttendanceNosignFilterView filterView;
    public ObservableInt topCount = new ObservableInt(5);
    public ObservableField<String> topDay = new ObservableField<>("未签课7天内");
    @Inject
    LoginStatus loginStatus;
    @Inject
    AttendanceNosignPresenter presenter;
    private String start, end;
    private CommonFlexAdapter commonFlexAdapter;
    private List<AbstractFlexibleItem> items = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentAttendanceStudentNosignBinding.inflate(inflater, container, false);
        binding.setToolbarModel(new ToolbarModel("会员出勤"));
        delegatePresenter(presenter, this);
        initToolbar(binding.includeToolbar.toolbar);
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
        binding.recyclerview.setAdapter(commonFlexAdapter);
    }

    private void initData() {
        start = DateUtils.minusDay(new Date(), 6);
        end = DateUtils.getStringToday();
    }


    private void initFragment() {
        filterView = new AttendanceNosignFilterView();
        filterView.setDismissAction(dismissAction);
        filterView.setCountSelectAction((title, count) -> {
            topCount.set(count);
            dismissAction.call();
            loadData();
        });
        filterView.setDaySelectAction((start, end, title) -> {
            topDay.set(title);
            this.start = start;
            this.end = end;
            loadData();
            dismissAction.call();
        });
        stuff(R.id.frag_filter, filterView);
    }

    private void loadData() {
        showLoadingTrans();
        presenter.getNotSignStudent(loginStatus.staff_id(), start, end, topCount.get());
    }

    private Action0 dismissAction = () -> {
        binding.fragFilter.setVisibility(View.GONE);
        binding.qcRadioGroup.clearCheck();
    };

    public void onTopDayClick(View v, int index) {
        if (v instanceof QcFilterToggle) {
            if (((QcFilterToggle) v).isChecked()) {
                binding.fragFilter.setVisibility(View.VISIBLE);
                filterView.showPage(index);

            } else {
                binding.fragFilter.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onGetNotSign(List<StudentWIthCount> studentList) {
        hideLoadingTrans();
        if (studentList.size() >= 0) {
            items.clear();
        }
        if (studentList.size() == 0) {
            items.add(new CommonNoDataItem(R.drawable.vd_img_empty_universe,
                    getResources().getString(R.string.text_empty_title_not_sign),
                    getResources().getString(R.string.text_empty_content_not_sign)));
        }
        for (StudentWIthCount student : studentList) {
            items.add(new NotSignClassItem(student));
        }
        commonFlexAdapter.updateDataSet(items);
        binding.tipsAbsenceAccount.setText(
                getResources().getString(R.string.text_not_sign_tip, DateUtils.interval(start, end) + 1,
                        topCount.get(), studentList.size()));
    }
}
