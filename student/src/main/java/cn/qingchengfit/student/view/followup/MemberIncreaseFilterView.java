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
    }

    @Override
    public void dismiss() {
        mViewModel.filterVisible.setValue(false);
    }

    private void initFragment() {
        studentStatusFragment = new FilterListStringFragment();
        String[] status = new String[]{"全部", "新注册", "已接洽", "会员"};
        studentStatusFragment.setStrings(status);
        studentStatusFragment.setOnSelectListener(position -> {
            mViewModel.followUpStatus.setValue(position == 0 ? "跟进状态" : status[position]);
            // TODO: 2018/6/22 数据回传
            mViewModel.setStudentStatus(position == 0 ? null : String.valueOf(position - 1));
            dismiss();
        });

        salersView = new FollowUpTopSalerView();
        salersView.setOnItemClick(staff -> {
            mViewModel.salerName.setValue(staff == null ? "销售" : staff.username);
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
