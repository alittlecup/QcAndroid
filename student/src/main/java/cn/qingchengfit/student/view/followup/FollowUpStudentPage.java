package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageFollowupStudentBinding;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStudentViewModel;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/11/20.
 */
@Leaf(module = "student", path = "/followup/student")
public class FollowUpStudentPage extends StudentBaseFragment<PageFollowupStudentBinding, FollowUpStudentViewModel>
        implements FlexibleAdapter.OnItemClickListener {
    @Override
    protected void subscribeUI() {
        mViewModel.getLiveItems().observe(this, items -> {
            mViewModel.items.set(items);
        });
    }

    @Override
    public PageFollowupStudentBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = PageFollowupStudentBinding.inflate(inflater, container, false);
        mBinding.setToolbarModel(new ToolbarModel("会员跟进"));
        mBinding.setViewModel(mViewModel);
        initToolbar(mBinding.includeToolbar.toolbar);
        mBinding.recyclerview.setAdapter(new CommonFlexAdapter(new ArrayList()));
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.loadSource(mViewModel.getFilter());
        mBinding.setItemClickListener(this);
        return mBinding;
    }


    @Override
    public boolean onItemClick(int position) {
        Uri uri = Uri.parse("student://student/followup/status");
        routeTo(uri, new cn.qingchengfit.student.view.followup.FollowUpStatusPageParams().title(mViewModel.getLiveItems().getValue().get(position).getTitle()).type(position).build());
        return false;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FollowUpStatus {
        int NEW_CREATE_USERS = 0;
        int NEW_FOLLOWING_USERS = 1;
        int NEW_MEMBER_USERS = 2;
    }
}

