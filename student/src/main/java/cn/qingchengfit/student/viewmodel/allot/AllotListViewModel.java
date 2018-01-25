package cn.qingchengfit.student.viewmodel.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.student.items.AllotStaffItem;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponse;
import cn.qingchengfit.saasbase.common.flexble.FlexibleFactory;
import cn.qingchengfit.saasbase.common.flexble.FlexibleItemProvider;
import cn.qingchengfit.saasbase.common.flexble.FlexibleViewModel;
import cn.qingchengfit.student.respository.StudentRespository;

/**
 * Created by huangbaole on 2017/11/21.
 */

public class AllotListViewModel extends FlexibleViewModel<List<AllotDataResponse>, AllotStaffItem, Integer> {

    public final ObservableField<List<AllotStaffItem>> items = new ObservableField<>();
    public final ObservableBoolean isLoading = new ObservableBoolean(false);

    @Inject
    LoginStatus loginStatus;
    @Inject
    GymWrapper gymWrapper;
    @Inject
    StudentRespository respository;
    public Integer type ;

    @Inject
    public AllotListViewModel() {
    }

    public void refresh() {
        identifier.setValue(1);
    }

    @NonNull
    @Override
    protected LiveData<List<AllotDataResponse>> getSource(@NonNull Integer integer) {
        isLoading.set(true);
        String path = "";
        switch (type) {
            case 0:
                path = "sellers";
                break;
            case 1:
                path = "coaches";
                break;
        }
        return Transformations.map(respository.qcGetStaffList(loginStatus.staff_id(), path
                , gymWrapper.getParams()), input -> input.sellers);
    }


    @Override
    protected boolean isSourceValid(@Nullable List<AllotDataResponse> response) {
        return response != null;
    }

    @Override
    protected List<AllotStaffItem> map(@NonNull List<AllotDataResponse> response) {
        return FlexibleItemProvider.with(new AllotStaffItemFactory()).from(response);
    }

    static class AllotStaffItemFactory implements FlexibleItemProvider.Factory<AllotDataResponse, AllotStaffItem> {

        @NonNull
        @Override
        public AllotStaffItem create(AllotDataResponse response) {
            return FlexibleFactory.create(AllotStaffItem.class, response);
        }
    }
}