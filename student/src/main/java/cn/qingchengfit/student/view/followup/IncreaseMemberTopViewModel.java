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
import javax.inject.Inject;

public class IncreaseMemberTopViewModel extends BaseViewModel {

  @Inject StudentRepository repository;

  public LiveData<HashMap<String, Object>> getDates() {
    return dates;
  }

  private final MutableLiveData<HashMap<String, Object>> dates = new MutableLiveData<>();
  public final LiveData<String> count;
  public final MutableLiveData<String> curContent = new MutableLiveData<>();
  public int status = 0;

  @Inject IncreaseMemberTopViewModel() {
    count = Transformations.switchMap(dates,
        params -> Transformations.map(loadFollowCount(params), input -> {
          StatDate statDate = dealResource(input);
          if (statDate != null) {
            return String.valueOf(statDate.getCount());
          } else {
            return "";
          }
        }));
  }

  private LiveData<Resource<StatDate>> loadFollowCount(HashMap<String, Object> params) {
    params.put("status", status);
    return repository.qcGetFollowStat(params);
  }

  public void loadSource(HashMap<String, Object> params) {
    String start = (String) params.get("start");
    String end = (String) params.get("end");

    curContent.setValue(end + "至" + start);

    dates.setValue(params);
  }
}
