package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.base.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageFollowupStudentBinding;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStudentViewModel;
import eu.davidea.flexibleadapter.utils.Log;

/**
 * Created by huangbaole on 2017/11/20.
 */
@Leaf(module = "student", path = "/followup/student")
public class FollowUpStudentPage extends StudentBaseFragment<PageFollowupStudentBinding, FollowUpStudentViewModel> {
    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, factory).get(FollowUpStudentViewModel.class);
        mViewModel.getLiveItems().observe(this, items -> {
            mViewModel.items.set(items);
        });
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageFollowupStudentBinding.inflate(inflater, container, false);
        mBinding.setToolbarModel(new ToolbarModel("会员跟进"));
        mBinding.setViewModel(mViewModel);
        initToolbar(mBinding.includeToolbar.toolbar);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.loadSource(mViewModel.getFilter());
        return mBinding.getRoot();
    }
}

