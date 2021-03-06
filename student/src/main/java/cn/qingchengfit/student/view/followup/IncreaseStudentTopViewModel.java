package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.graphics.Color;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.listener.DateGroupDimension;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.utils.DateUtils;
import com.github.mikephil.charting.data.LineData;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

public class IncreaseStudentTopViewModel extends BaseViewModel {
  private String type = "";

  public void setType(@IncreaseType String type) {
    this.type = type;
  }

  public void setDateDimension(@DateGroupDimension String dateDimension) {
    if (!dateDimension.equals(this.dateDimension.getValue())) {
      this.dateDimension.setValue(dateDimension);
    }
  }

  public final MutableLiveData<CharSequence> tvContent = new MutableLiveData<>();

  private final MutableLiveData<String> dateDimension = new MutableLiveData<>();

  private final MediatorLiveData<HashMap<String, String>> dates = new MediatorLiveData<>();
  public final MediatorLiveData<LineData> lineData = new MediatorLiveData<>();
  public final MutableLiveData<Boolean> showLoading = new MutableLiveData<>();
  public final MutableLiveData<HashMap<String, Object>> curSelectPositionDate =
      new MutableLiveData<>();

  private final LiveData<List<StatDate>> remoteDatas;
  private List<StatDate> preStatDates = new ArrayList<>();

  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject StudentRepository studentRepository;

  @Inject IncreaseStudentTopViewModel() {
    dates.addSource(dateDimension, dimension -> {
      preStatDates.clear();
      String stringToday = cn.qingchengfit.utils.DateUtils.getStringToday();
      HashMap<String, String> dates = new HashMap<>();
      dates.put("end", stringToday);
      this.dates.setValue(dates);
      HashMap<String, Object> curDates = new HashMap<>();
      curDates.put("end", stringToday);
      Calendar calendar = Calendar.getInstance();
      switch (dimension) {
        case DateGroupDimension.DAY:
          curDates.put("start", DateUtils.Date2YYYYMMDD(calendar.getTime()));

          break;
        case DateGroupDimension.WEEK:
          calendar.add(Calendar.WEEK_OF_YEAR, -1);
          curDates.put("start", DateUtils.Date2YYYYMMDD(calendar.getTime()));

          break;
        case DateGroupDimension.MONTH:
          calendar.add(Calendar.MONTH, -1);
          curDates.put("start", DateUtils.getStartDayOfMonth(new Date()));

          break;
      }
      curSelectPositionDate.setValue(curDates);
    });
    remoteDatas = Transformations.switchMap(dates,
        params -> Transformations.map(loadSource(params), input -> {
          List<StatDate> statDates = dealResource(input);
          if (statDates == null) {
            return preStatDates;
          } else {
            if (preStatDates.isEmpty()) {
              statDates.addAll(preStatDates);
              return preStatDates = statDates;
            } else {
              if (!statDates.get(0).getStart().equals(preStatDates.get(0).getStart())) {
                statDates.addAll(preStatDates);
                return preStatDates = statDates;
              } else {
                return preStatDates;
              }
            }
          }
        }));
    lineData.addSource(remoteDatas, statDates -> {
      if (statDates == null) return;
      Single.just(statDates)
          .filter(statDates1 -> !statDates1.isEmpty())
          .flatMap(statDates12 -> {
            List<Integer> counts = new ArrayList<>();
            for (StatDate statDate : statDates12) {
              counts.add(statDate.getCount());
            }
            return Maybe.just(counts);
          })
          .map(counts -> StudentBusinessUtils.transformBean2DataByType(counts, counts.size(), type,
              !preStatDates.isEmpty()))
          .doAfterTerminate(() -> showLoading.setValue(false))
          .subscribe(lineData::setValue);
    });
  }

  private LiveData<Resource<List<StatDate>>> loadSource(HashMap<String, String> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    if (!params.isEmpty()) {
      params1.putAll(params);
    }
    params1.put("date_group_dimension", dateDimension.getValue());
    params1.put("status", type.equals(IncreaseType.INCREASE_MEMBER) ? 0 : 2);
    return studentRepository.qcGetIncreaseStat(loginStatus.staff_id(), params1);
  }

  public void upDateTvContent(int curpos, int count) {
    if (dateDimension.getValue() == null) return;
    StatDate statDate = preStatDates.get(curpos);

    String start = "";
    String mid = "";
    switch (dateDimension.getValue()) {
      case DateGroupDimension.DAY:
        start = statDate.getStart();
        break;
      case DateGroupDimension.WEEK:
        start = statDate.getStart() + "至" + statDate.getEnd();
        break;
      case DateGroupDimension.MONTH:
        start = DateUtils.getYYMMfromServer(statDate.getStart());
        break;
    }

    int color = Color.parseColor("#4987FF");
    switch (type) {
      case IncreaseType.INCREASE_MEMBER:
        mid = "新增注册";
        color = Color.parseColor("#4987FF");
        break;
      case IncreaseType.INCREASE_STUDENT:
        mid = "新增会员";
        color = Color.parseColor("#0DB14B");
        break;
    }
    tvContent.setValue(new SpanUtils().append(start)
        .append(mid)
        .append(String.valueOf(count))
        .setForegroundColor(color)
        .append("人")
        .create());
  }


  public final MutableLiveData<Integer> curPostion = new MutableLiveData<>();

  public void updateSelectPos(int curPos, int count) {
    if (dateDimension.getValue() == null) return;
    StatDate statDate = preStatDates.get(curPos);
    HashMap<String, Object> params = new HashMap<>();
    params.put("start", statDate.getStart());
    params.put("end", statDate.getEnd());
    curSelectPositionDate.setValue(params);
  }


  public void loadMore() {
    showLoading.setValue(true);
    HashMap<String, String> params = new HashMap<>();
    params.put("end", remoteDatas.getValue().get(0).getStart());
    dates.setValue(params);
  }
}
