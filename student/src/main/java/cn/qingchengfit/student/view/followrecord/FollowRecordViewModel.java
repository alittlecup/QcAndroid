package cn.qingchengfit.student.view.followrecord;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.student.bean.FollowRecord;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.item.FollowRecordItem;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class FollowRecordViewModel
    extends FlexibleViewModel<List<FollowRecord>, FollowRecordItem, Map<String, Object>> {
  MutableLiveData<List<FollowRecord>> datas = new MutableLiveData<>();

  @Inject public FollowRecordViewModel() {

  }

  @Inject StudentRepository repository;
  @Inject StudentWrap studentWrap;

  public MutableLiveData<List<Integer>> types = new MutableLiveData<>();

  @NonNull @Override
  protected LiveData<List<FollowRecord>> getSource(@NonNull Map<String, Object> stringObjectMap) {
    if (studentWrap != null && studentWrap.id() != null) {
      repository.qcGetTrackRecords(datas, defaultResult, studentWrap.id(),
          (HashMap<String, Object>) stringObjectMap);
    }
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

  //教练端会员详情跟进记录筛选
  public void onClickCategory(View v, int i) {
    ViewGroup parent = ((ViewGroup)v.getParent());
    for (int j = 0 ; j < parent.getChildCount(); j++){
      View child = parent.getChildAt(j);
        child.setSelected(false);
    }
    v.setSelected(true);
    List<Integer> typeList = new ArrayList<>();
    switch (i) {
      case 0:
        for (int j = 1 ; j < 5; j++){
          typeList.add(j);
        }
        break;
      case 5:
        typeList.add(5);
        break;
    }
    types.setValue(typeList);
  }
}
