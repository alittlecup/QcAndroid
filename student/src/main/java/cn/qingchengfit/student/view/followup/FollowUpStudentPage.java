package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anbillon.flabellum.annotations.Leaf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpStatusParams;
import cn.qingchengfit.student.base.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageFollowupStudentBinding;
import cn.qingchengfit.student.view.followup.FollowUpStatusPageParmas;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStudentViewModel;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.utils.Log;

/**
 * Created by huangbaole on 2017/11/20.
 */
@Leaf(module = "student", path = "/followup/student")
public class FollowUpStudentPage extends StudentBaseFragment<PageFollowupStudentBinding, FollowUpStudentViewModel>
        implements FlexibleAdapter.OnItemClickListener {
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
        mBinding.setItemClickListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onItemClick(int position) {
        Uri uri = Uri.parse("student://student/followup/status");
        routeTo(uri, new FollowUpStatusPageParmas().title(mViewModel.getLiveItems().getValue().get(position).getTitle()).type(position).build());
        return false;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FollowUpStatus {
        int NEW_CREATE_USERS = 0;
        int NEW_FOLLOWING_USERS = 1;
        int NEW_MEMBER_USERS = 2;
    }
}

