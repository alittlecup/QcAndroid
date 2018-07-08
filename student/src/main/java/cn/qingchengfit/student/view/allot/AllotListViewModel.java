package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.flexble.FlexibleFactory;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.network.Status;
import cn.qingchengfit.student.bean.AllotDataResponse;
import cn.qingchengfit.student.item.AllotStaffItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/11/21.
 */

public class AllotListViewModel
    extends FlexibleViewModel<List<AllotDataResponse>, AllotStaffItem, Integer> {

  public final ObservableField<List<AllotStaffItem>> items = new ObservableField<>();
  public final ObservableBoolean isLoading = new ObservableBoolean(false);

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StudentRepository repository;
  public Integer type = 0;

  @Inject public AllotListViewModel() {
  }

  public void refresh() {
  }

  @NonNull @Override
  protected LiveData<List<AllotDataResponse>> getSource(@NonNull Integer integer) {
    isLoading.set(true);
    String path = "";
    switch (integer) {
      case 0:
        path = "sellers";
        break;
      case 1:
        path = "coaches";
        break;
    }

    return Transformations.map(
        repository.qcGetStaffList(loginStatus.staff_id(), path, gymWrapper.getParams()), input ->dealResource(input)!=null?dealResource(input).sellers:null);
  }

  @Override protected boolean isSourceValid(@Nullable List<AllotDataResponse> response) {
    return response != null;
  }

  @Override protected List<AllotStaffItem> map(@NonNull List<AllotDataResponse> response) {
    return FlexibleItemProvider.with(new AllotStaffItemFactory()).from(response);
  }

  static class AllotStaffItemFactory
      implements FlexibleItemProvider.Factory<AllotDataResponse, AllotStaffItem> {

    @NonNull @Override public AllotStaffItem create(AllotDataResponse response) {
      return FlexibleFactory.create(AllotStaffItem.class, response);
    }
  }
}
