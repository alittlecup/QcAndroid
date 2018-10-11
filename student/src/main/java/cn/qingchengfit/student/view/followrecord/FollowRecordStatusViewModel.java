package cn.qingchengfit.student.view.followrecord;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.items.SimpleTextItemItem;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.FollowRecord;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import cn.qingchengfit.student.item.FollowRecordStatusItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class FollowRecordStatusViewModel extends FlexibleViewModel<List<FollowRecordStatus>,FollowRecordStatusItem,HashMap<String,Object>> {
  //MutableLiveData<>
  MutableLiveData<List<FollowRecordStatus>> datas = new MutableLiveData<>();
  @Inject StudentRepository studentRepository;

  @Inject FollowRecordStatusViewModel() {
    //studentRepository
  }

  @NonNull @Override
  protected LiveData<List<FollowRecordStatus>> getSource(@NonNull HashMap<String, Object> stringObjectMap) {
    studentRepository.qcGetTrackStatus(datas,defaultResult);
    return datas;
  }

  @Override protected boolean isSourceValid(@Nullable List<FollowRecordStatus> followRecords) {
    return followRecords != null;
  }

  @Override protected List<FollowRecordStatusItem> map(@NonNull List<FollowRecordStatus> followRecords) {
    List<FollowRecordStatusItem> list = new ArrayList<>();
    for (FollowRecordStatus followRecord : followRecords) {
      list.add(new FollowRecordStatusItem(followRecord));
    }
    return list;
  }

  public void addFollowStatus(String status){
    HashMap<String,Object> param = new HashMap<>();
    param.put("track_status",status);
    studentRepository.qcAddTrackStatus(param,defaultResult);
  }
  public void editFollowStatus(String id,String status){
    HashMap<String,Object> param = new HashMap<>();
    param.put("track_status",status);
    studentRepository.qcEditTrackStatus(id,param,defaultResult);
  }

  public void deleteFollowStatus(String id){
    studentRepository.qcDelTrackStatus(id,defaultResult);
  }

}
