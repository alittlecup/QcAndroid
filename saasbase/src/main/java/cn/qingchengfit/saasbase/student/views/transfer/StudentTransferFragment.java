package cn.qingchengfit.saasbase.student.views.transfer;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentStudentTransferBinding;
import cn.qingchengfit.saasbase.student.items.FollowUpItem;
import cn.qingchengfit.saasbase.student.network.body.QcStudentBeanWithFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.saasbase.student.presenters.transfer.StudentTransferPresenter;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by huangbaole on 2017/11/13.
 */
@Leaf(module = "student", path = "/student/transfer")
public class StudentTransferFragment extends SaasBaseFragment implements StudentTransferPresenter.MVPView {
    FragmentStudentTransferBinding binding;
    TransferFilterFragment filterFragment;
    StudentTransferTopView topView;
    @Inject
    StudentTransferPresenter presenter;
    @Inject
    LoginStatus loginStatus;
    StudentFilter filter = new StudentFilter();
    private List<AbstractFlexibleItem> datas = new ArrayList<>();
    CommonFlexAdapter commonFlexAdapter;

    public ObservableField<String> topSalerName = new ObservableField<>("销售");
    public ObservableField<String> topDay = new ObservableField<>("最近7天");

    @Override
    protected boolean isfitSystemPadding() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_transfer, container, false);
        delegatePresenter(presenter, this);
        initToolbar(binding.includeToolbar.toolbar);
        binding.setToolbarModel(new ToolbarModel("会员转化"));
        binding.setFragment(this);
        initFragment();
        initView();
        showLoadingTrans();
        loadData();
        return binding.getRoot();
    }

    private void initView() {

        commonFlexAdapter = new CommonFlexAdapter(datas);
        binding.recyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        binding.recyclerView.setNestedScrollingEnabled(true);
        binding.recyclerView.setAdapter(commonFlexAdapter);
    }

    private void initFragment() {
        filterFragment = new TransferFilterFragment();
        filterFragment.setDismissAction(() -> {
            binding.fragFilter.setVisibility(View.GONE);
            binding.qcRadioGroup.clearCheck();
        });
        filterFragment.setSelectDayAction((start, end, title) -> {
            topDay.set(title);
            filter.registerTimeStart = start;
            filter.registerTimeEnd = end;
            loadData();
            binding.fragFilter.setVisibility(View.GONE);
            binding.qcRadioGroup.clearCheck();

        });
        filterFragment.setStaffCallback(staff -> {
            topSalerName.set(staff == null ? "销售" : staff.username);
            filter.sale = staff;
            loadData();
            binding.fragFilter.setVisibility(View.GONE);
            binding.qcRadioGroup.clearCheck();

        });
        stuff(R.id.frag_filter, filterFragment);
        topView = new StudentTransferTopView();
        stuff(R.id.frag_transfer_graph, topView);

    }

    private void loadData() {
        presenter.loadData(loginStatus.staff_id(), filter);

    }

    public void onSalerClick(View view) {
        filterFragment.showPage(0);
        binding.fragFilter.setVisibility(View.VISIBLE);
    }

    public void onDayClick(View view) {
        filterFragment.showPage(1);
        binding.fragFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStudentTransferBeanLoaded(StudentTransferBean bean) {
        hideLoadingTrans();
        changeTopView(bean);
        if (bean != null && bean.users != null) {
            commonFlexAdapter.clear();

            for (QcStudentBeanWithFollow s : bean.users) {
                datas.add(new FollowUpItem(s, 4));
            }
            commonFlexAdapter.updateDataSet(datas);
        }
        binding.tvStudentListHint.setText(topDay.get() + "注册的会员");


    }

    private void changeTopView(StudentTransferBean bean) {
        String title = "注册日期为" + topDay.get() + "的会员转化率";
        topView.onFollowUpConver(bean, title);

    }
}
