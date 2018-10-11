package cn.qingchengfit.student.view.followrecord;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.FollowRecord;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.item.FollowRecordItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class FollowRecordViewModel extends FlexibleViewModel<List<FollowRecord>,FollowRecordItem,Map<String,Object>> {
  MutableLiveData<List<FollowRecord>> datas = new MutableLiveData<>();
  @Inject FollowRecordViewModel(){}
  @Inject StudentRepository repository;
  @Inject StudentWrap studentWrap;

  @NonNull @Override
  protected LiveData<List<FollowRecord>> getSource(@NonNull Map<String, Object> stringObjectMap) {
    repository.qcGetTrackRecords(datas,defaultResult,studentWrap.id());
    return datas;
  }

  @Override protected boolean isSourceValid(@Nullable List<FollowRecord> followRecords) {
    return followRecords != null;
  }

  @Override protected List<FollowRecordItem> map(@NonNull List<FollowRecord> followRecords) {
    List<FollowRecordItem> items = new ArrayList<>();
    for (FollowRecord followRecord : followRecords) {
      items.add(new FollowRecordItem(followRecord));
    }
    return items;
  }
}
