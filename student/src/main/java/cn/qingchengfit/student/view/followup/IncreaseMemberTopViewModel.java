package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.respository.StudentRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class IncreaseMemberTopViewModel extends BaseViewModel {

  @Inject StudentRepository repository;

  public LiveData<HashMap<String, Object>> getDates() {
    return dates;
  }

  private final MutableLiveData<HashMap<String, Object>> dates = new MutableLiveData<>();
  public final LiveData<String> count;
  public final MutableLiveData<String> curContent = new MutableLiveData<>();
  public final MutableLiveData<Integer> selectPos = new MutableLiveData<>();
  public int status = 0;

  @Inject IncreaseMemberTopViewModel() {
    count = Transformations.switchMap(dates,
        params -> Transformations.map(loadFollowCount(params), input -> {
          StatDate statDate = dealResource(input);
          if (statDate != null) {
            return String.valueOf(statDate.getCount());
          } else {
            return "- -";
          }
        }));
  }

  private LiveData<Resource<StatDate>> loadFollowCount(HashMap<String, Object> params) {
    Map<String, Object> datas = new HashMap<>();
    datas.put("status", status);
    datas.putAll(params);
    return repository.qcGetFollowStat(params);
  }

  public void loadSource(HashMap<String, Object> params) {
    String start = (String) params.get("start");
    String end = (String) params.get("end");
    if (start.equals(end)) {
      curContent.setValue(start);
    } else {
      curContent.setValue(start + "至" + end);
    }

    dates.setValue(params);
  }
}
