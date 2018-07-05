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
    FilterListStringFragment genderFragment;
    FollowUpTopSalerView salersView;
    IncreaseStudentSortViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getParentFragment()).get(IncreaseStudentSortViewModel.class);
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
            mViewModel.studentStatus.setValue(position == 0 ? "会员状态" : status[position]);
            // TODO: 2018/6/22 数据回传
            //mViewModel.getStatus().setValue(position == 0 ? null : String.valueOf(position - 1));
            dismiss();
        });

        genderFragment = new FilterListStringFragment();
        String[] genders = new String[]{"全部", "男", "女"};
        genderFragment.setStrings(genders);
        genderFragment.setOnSelectListener(position -> {
            mViewModel.gender.setValue(position == 0 ? "性别" : genders[position]);
            //mViewModel.getGenderM().setValue(position == 0 ? null : String.valueOf(position - 1));
            dismiss();
        });

        salersView = new FollowUpTopSalerView();
        salersView.setOnItemClick(staff -> {
            mViewModel.salerName.setValue(staff == null ? "销售" : staff.username);
            //mViewModel.getStaff().setValue(staff);
            dismiss();
        });
    }

    @Override
    protected String[] getTags() {
        return new String[]{"salerList","studentStatus","gender"};
    }

    @Override
    protected Fragment getFragmentByTag(String tag) {
        if (tag.equalsIgnoreCase(getTags()[0])) {
            return salersView;
        } else if (tag.equalsIgnoreCase(getTags()[1])) {
            return studentStatusFragment;
        } else if (tag.equalsIgnoreCase(getTags()[4])) {
            return genderFragment;
        }
        return new EmptyFragment();
    }
}
