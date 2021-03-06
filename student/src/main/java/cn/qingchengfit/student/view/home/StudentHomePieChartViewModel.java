package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.student.bean.StatData;
import com.github.mikephil.charting.data.PieData;
import javax.inject.Inject;

public class StudentHomePieChartViewModel extends BaseViewModel {
  public final MutableLiveData<Boolean> showDivider = new MutableLiveData<>();
  public final MutableLiveData<Integer> backgroundColor = new MutableLiveData<>();
  public final MutableLiveData<StatData> pieData = new MutableLiveData<>();

  @Inject StudentHomePieChartViewModel() {
    showDivider.setValue(false);
  }
}
