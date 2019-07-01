package cn.qingchengfit.saasbase.course.detail;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhoto;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class ScheduleDetailVM extends BaseViewModel {
  @Inject ICourseModel courseModel;
  public MutableLiveData<ScheduleDetail> detail = new MutableLiveData<>();
  public MutableLiveData<ScheduleOrders> detailOrders = new MutableLiveData<>();
  public MutableLiveData<SchedulePhotos> detailPhotos = new MutableLiveData<>();
  public MutableLiveData<Boolean> cancelResult = new MutableLiveData<>();
  public MutableLiveData<Boolean> delPhotoResult = new MutableLiveData<>();
  public MutableLiveData<ScheduleShareDetail> scheduleShareDetail = new MutableLiveData<>();
  public String shopID;//TODO 添加获取 shop ID 接口
  public MutableLiveData<Boolean> signOpen = new MutableLiveData<>();
  public double signType;
  @Inject GymWrapper gymWrapper;

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

  public void loadShopInfo() {
    courseModel.qcGetGymExtra().compose(RxHelper.schedulersTransformer()).subscribe(response -> {
      if (ResponseConstant.checkSuccess(response)) {
        shopID = response.data.shopID;
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

  public void delPhotos(String scheduleId, String photos) {
    courseModel.qcDeleteSchedulePhotos(scheduleId, photos)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            delPhotoResult.setValue(true);
          } else {
            delPhotoResult.setValue(false);

            ToastUtils.show(response.msg);
          }
        }, throwable -> {
        });
  }

  /**
   * 这里是判断是否显示签课和点击签课按钮跳转不同的配置信息
   * 应该是按照 key 去匹配的，由于是两个 APP 共享，应该是写一个 UseCase 去分别实现
   * 不过赶工期就按照默认的 key 顺序去处理了
   */
  public void loadCourseConfig() {
    courseModel.qcGetShopConfig().compose(RxHelper.schedulersTransformer()).subscribe(response -> {
      if (ResponseConstant.checkSuccess(response)) {
        List<SignInConfig.Config> configs = response.data.configs;
        signOpen.setValue((boolean) configs.get(1).getValue());
        signType = (double) configs.get(0).getValue();
      } else {
        ToastUtils.show(response.msg);

      }
    }, throwable -> {
    });
  }

  public List<String> getPhotoUrls() {
    SchedulePhotos value = detailPhotos.getValue();
    List<String> urls = new ArrayList<>();
    if (value == null || value.photos == null || value.photos.isEmpty()) return urls;
    for (SchedulePhoto photo : value.photos) {
      urls.add(photo.getPhoto());
    }
    return urls;
  }

  public void setService(CoachService service) {
    courseModel.setService(service);
  }

  public void loadScheduleShare(String scheduleId) {
    courseModel.qcGetScheduleShareInfo(scheduleId, null)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            ScheduleShareDetail data = response.data;
            scheduleShareDetail.setValue(data);
          } else {
            ToastUtils.show(response.msg);

          }
        }, throwable -> {
        });
  }
  public void putScheduleShare(String scheduleId, Map<String,Object> body) {
    courseModel.qcPutScheduleShareInfo(scheduleId, body)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            ScheduleShareDetail data = response.data;
            scheduleShareDetail.setValue(data);
          } else {
            ToastUtils.show(response.msg);

          }
        }, throwable -> {
        });
  }
}
