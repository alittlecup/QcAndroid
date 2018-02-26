package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.PageFollowupStatusBinding;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.viewmodel.followup.FollowUpFilterViewModel;
import cn.qingchengfit.student.viewmodel.followup.FollowUpStatusViewModel;
import cn.qingchengfit.student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.utils.MeasureUtils;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/11/6.
 */
@Leaf(module = "student", path = "/followup/status")
public class FollowUpStatusPage extends StudentBaseFragment<PageFollowupStatusBinding,FollowUpStatusViewModel>  {

    @Need
    String title;
    @Need
    Integer type;

    FollowUpFilterView followUpFilterView;
    FollowUpStatusTopView topView;
    private FollowUpFilterEndView filterView;
    private FollowUpFilterViewModel filterViewModel;


    @Override
    protected void subscribeUI() {
        mViewModel.dataType=type;
        mViewModel.getLiveItems().observe(this,items->{
            mViewModel.items.set(items);
        });
        mViewModel.getFilterIndex().observe(this,integer -> {
            followUpFilterView.showPage(integer);
        });
        mViewModel.getFilterClick().observe(this, aVoid -> {
            openDrawer();
            filterViewModel = ViewModelProviders.of(filterView, factory).get(FollowUpFilterViewModel.class);
            filterViewModel.getmFilterMap().observe(this, map -> {
                if (map != null) {
                    closeDrawer();
                    // REFACTOR: 2017/12/11如何处理数据
                    mViewModel.setFilterMap(map);

                    filterViewModel.getmFilterMap().setValue(null);
                }
            });
        });
    }

    @Override
    public PageFollowupStatusBinding initDataBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding=PageFollowupStatusBinding.inflate(inflater,container,false);
        mBinding.setToolbarModel(new ToolbarModel(title));
        initToolbar(mBinding.includeToolbar.toolbar);
        mBinding.setViewModel(mViewModel);
        mBinding.recyclerViewToday.setAdapter(new CommonFlexAdapter(new ArrayList()));
        mBinding.recyclerViewToday.setLayoutManager(new LinearLayoutManager(getContext()));
        initView();
        initFragment();
        return mBinding;
    }


    private void openDrawer() {
        mBinding.drawer.openDrawer(GravityCompat.END);
    }

    private void closeDrawer() {
        mBinding.drawer.closeDrawer(GravityCompat.END);
    }

    private void initFragment() {
        followUpFilterView=new FollowUpFilterView();
        topView=new FollowUpStatusTopView();
        stuff(R.id.frag_chart, topView);
        stuff(R.id.frag_filter, followUpFilterView);

        filterView = new FollowUpFilterEndView();
        stuff(R.id.frame_student_filter, filterView);

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
