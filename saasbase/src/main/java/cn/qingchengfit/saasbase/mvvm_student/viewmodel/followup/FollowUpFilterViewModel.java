package cn.qingchengfit.saasbase.mvvm_student.viewmodel.followup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import cn.qingchengfit.saasbase.mvvm_student.usercase.FilterUserCase;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.saasbase.mvvm_student.usercase.FollowUpFilterUserCase;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentFilterViewModel;

/**
 * Created by huangbaole on 2017/12/11.
 */

public class FollowUpFilterViewModel extends StudentFilterViewModel {

    @Inject
    FollowUpFilterUserCase userCase;

    @Override
    public LiveData<List<FilterModel>> getRemoteFilters() {
        return followUpRemoteFilter;
    }

    protected final LiveData<List<FilterModel>> followUpRemoteFilter;

    @Inject
    public FollowUpFilterViewModel(FilterUserCase filterUserCasem,FollowUpFilterUserCase userCase) {
        super(filterUserCasem);
        followUpRemoteFilter = Transformations.map(userCase.getFilterModel(), input -> input);
    }

    @Override
    public void loadfilterModel() {
        userCase.excute(loginStatus.staff_id(), gymWrapper.getParams());
    }
}
