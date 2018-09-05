package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saascommon.filter.FilterListStringFragment;
import cn.qingchengfit.views.fragments.BaseFilterFragment;
import cn.qingchengfit.views.fragments.EmptyFragment;

/**
 * Created by huangbaole on 2017/11/7.
 */

public class MemberIncreaseFilterView extends BaseFilterFragment {
    FilterListStringFragment studentStatusFragment;
    FollowUpTopSalerView salersView;
    IncreaseMemberSortViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getParentFragment()).get(IncreaseMemberSortViewModel.class);
        initFragment();
        mViewModel.loadFollorStatus();
        mViewModel.recordStatus.observe(this,items->{
            if(items==null)return;
            String[] status=new String[items.size()+1];
            status[0]="全部";
            for(int i=0;i<items.size();i++){
                status[i+1]=items.get(i).getTrack_status();
            }
            studentStatusFragment.setStrings(status);
        });
    }

    @Override
    public void dismiss() {
        mViewModel.filterVisible.setValue(false);
    }

    private void initFragment() {
        studentStatusFragment = new FilterListStringFragment();
        studentStatusFragment.setOnSelectListener(position -> {
            mViewModel.followUpStatus.setValue(position == 0 ? "跟进状态" : mViewModel.recordStatus.getValue().get(position).getTrack_status());
            mViewModel.setStudentStatus(position == 0 ? null : mViewModel.recordStatus.getValue().get(position).getId());
            dismiss();
        });

        salersView = new FollowUpTopSalerView();
        salersView.setOnItemClick(staff -> {
            mViewModel.salerName.setValue(staff == null ? "全部销售" : staff.username);
            mViewModel.setSaller(staff);
            dismiss();
        });
    }

    @Override
    protected String[] getTags() {
        return new String[]{"salerList","followStatus"};
    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if (tag.equalsIgnoreCase(getTags()[0])) {
            return salersView;
        } else if (tag.equalsIgnoreCase(getTags()[1])) {
            return studentStatusFragment;
        }
        return new EmptyFragment();
    }
}
