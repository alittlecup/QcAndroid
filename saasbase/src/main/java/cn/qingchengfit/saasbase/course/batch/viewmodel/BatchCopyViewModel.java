package cn.qingchengfit.saasbase.course.batch.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.view.Gravity;
import android.view.View;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saasbase.common.mvvm.BaseViewModel;
import cn.qingchengfit.saasbase.db.utils.CommonInputViewAdapter;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import java.util.Date;
import javax.inject.Inject;

import static cn.qingchengfit.saasbase.utils.RouterUtils.routeTo;

/**
 * Created by fb on 2018/4/8.
 */

public class BatchCopyViewModel extends BaseViewModel {

  @Inject
  public BatchCopyViewModel(){
    startTime.set(DateUtils.Date2YYYYMMDD(new Date()));
    endTime.set(DateUtils.Date2YYYYMMDD(new Date()));
    startCopyTime.set(DateUtils.Date2YYYYMMDD(new Date()));
    endCopyTime.set(DateUtils.Date2YYYYMMDD(new Date()));
    description.set("将 - -期间全部教练的全部课程，复制到- -至- -");
  }

  public ObservableField<String> startTime = new ObservableField<>();
  public ObservableField<String> endTime = new ObservableField<>();
  public ObservableField<Staff> coach = new ObservableField<>();
  public ObservableField<Course> course = new ObservableField<>();
  public ObservableField<String> startCopyTime = new ObservableField<>();
  public ObservableField<String> endCopyTime = new ObservableField<>();
  private TimeDialogWindow pwTime;
  public ObservableField<String> description = new ObservableField<>();
  @Inject QcRestRepository restRepository;

  public void onSureClick(){
    if (DateUtils.interval(startTime.get(), endTime.get()) > 31){
      ToastUtils.show("所选日期不能超过31天");
      return;
    }
    if (DateUtils.interval(startTime.get(), endTime.get()) < 1){
      ToastUtils.show("结束日期不能早于开始日期");
      return;
    }
    startCopyTime.set(DateUtils.Date2YYYYMMDD(new Date()));
  }

  public void onCoach(View view){
    Intent intent = new Intent();
    routeTo(view.getContext(),"course", "/batch/choose/trainer",null, intent);
  }

  public void onTimeStart(View view, String date){
    if (pwTime == null) {
      pwTime = new TimeDialogWindow(view.getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    }
    pwTime.setOnTimeSelectListener(date1 -> {
      if (view instanceof CommonInputView){
        CommonInputViewAdapter.setContent(((CommonInputView)view), DateUtils.Date2YYYYMMDD(date1));
      }
    });
    pwTime.showAtLocation(view, Gravity.BOTTOM, 0, 0, DateUtils.formatDateFromYYYYMMDD(date));
  }
}
