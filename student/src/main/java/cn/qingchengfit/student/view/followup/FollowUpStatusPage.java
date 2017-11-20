package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.databinding.FragmentFollowUpStatusBinding;
import cn.qingchengfit.saasbase.student.bean.FollowUpFilterModel;
import cn.qingchengfit.saasbase.student.items.FollowUpItem;
import cn.qingchengfit.saasbase.student.items.TotalCountFooterItem;
import cn.qingchengfit.saasbase.student.network.body.QcStudentBeanWithFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.presenters.followup.FollowUpStatusPresenter;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpFilterFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpHomeFragment;
import cn.qingchengfit.saasbase.student.views.followup.FollowUpStatusTopFragment;
import cn.qingchengfit.student.base.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageFollowupStatusBinding;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStatusViewModel;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;


/**
 * Created by huangbaole on 2017/11/6.
 */
@Leaf(module = "student", path = "/followup/status")
public class FollowUpStatusPage extends StudentBaseFragment<PageFollowupStatusBinding,FollowUpStatusViewModel>  {

    @Need
    String title="新增注册";
    @Need
    Integer type=0;

    FollowUpFilterView followUpFilterView;
    FollowUpStatusTopView topView;


    @Override
    protected void initViewModel() {
        mViewModel= ViewModelProviders.of(this,factory).get(FollowUpStatusViewModel.class);
        mViewModel.dataType=type;
        mViewModel.getLiveItems().observe(this,items->{
            mViewModel.items.set(items);
        });
        mViewModel.getFilterIndex().observe(this,integer -> {
            followUpFilterView.showPage(integer);
        });
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding=PageFollowupStatusBinding.inflate(inflater,container,false);
        mBinding.setToolbarModel(new ToolbarModel(title));
        initToolbar(mBinding.includeToolbar.toolbar);
        mBinding.setViewModel(mViewModel);
        mBinding.recyclerViewToday.setLayoutManager(new LinearLayoutManager(getContext()));
        initView();
        initFragment();
        return mBinding.getRoot();
    }

    private void initFragment() {
        followUpFilterView=new FollowUpFilterView();
        topView=new FollowUpStatusTopView();
        stuff(R.id.frag_chart, topView);
        stuff(R.id.frag_filter, followUpFilterView);

    }

    private void initView() {
        switch (type) {
            case FollowUpStudentPage.FollowUpStatus.NEW_CREATE_USERS:
                mBinding.qftGender.setVisibility(View.GONE);
                break;
            case FollowUpStudentPage.FollowUpStatus.NEW_FOLLOWING_USERS:
                mBinding.qtbFilter.setVisibility(View.GONE);
                break;
            case FollowUpStudentPage.FollowUpStatus.NEW_MEMBER_USERS:
                mBinding.qftStatus.setVisibility(View.GONE);
                mBinding.qtbFilter.setVisibility(View.GONE);
                break;
        }

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mBinding.fragFilter.getLayoutParams();
        lp.topMargin = (int) (MeasureUtils.getActionbarBarHeight(getContext()) + getResources().getDimension(R.dimen.qc_item_height));
        mBinding.fragFilter.setLayoutParams(lp);
    }
}
