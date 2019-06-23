package cn.qingchengfit.saasbase.course.detail;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.ToastUtils;
import javax.inject.Inject;

public class ScheduleDetailVM extends BaseViewModel {
  @Inject ICourseModel courseModel;
  public MutableLiveData<ScheduleDetail> detail = new MutableLiveData<>();
  public MutableLiveData<ScheduleOrders> detailOrders = new MutableLiveData<>();
  public MutableLiveData<SchedulePhotos> detailPhotos = new MutableLiveData<>();
  public MutableLiveData<Boolean> cancelResult=new MutableLiveData<>();

  @Inject public ScheduleDetailVM() {

  }

  public void loadCourseDetail(String scheduleId) {
    courseModel.qcGetScheduleDetail(scheduleId)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            detail.setValue(response.data.schedule);
          } else {
            ToastUtils.show(response.msg);
          }
        }, throwable -> {
        });
  }

  public void loadCouseOrders(String scheduleId) {
    courseModel.qcGetScheduleDetailOrder(scheduleId)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            detailOrders.setValue(response.data);
          } else {
            ToastUtils.show(response.msg);
          }
        }, throwable -> {
        });
  }

  public void loadCoursePhotos(String scheduleId) {
    courseModel.qcGetScheduleDetailPhotos(scheduleId)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            detailPhotos.setValue(response.data);
          } else {
            ToastUtils.show(response.msg);
          }
        }, throwable -> {
        });
  }

  public void postCancelOrder(String order_id) {
    courseModel.qcPostScheduleOrderCancel(order_id)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            ToastUtils.show("取消预约成功");
            cancelResult.setValue(true);
          } else {
            cancelResult.setValue(false);

            ToastUtils.show(response.msg);
          }
        }, throwable -> {
        });
  }
}
