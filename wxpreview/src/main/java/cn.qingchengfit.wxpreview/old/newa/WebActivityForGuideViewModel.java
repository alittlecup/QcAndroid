package cn.qingchengfit.wxpreview.old.newa;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.MiniProgram;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.calendar.BaseView;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.wxpreview.old.newa.bean.Shop;
import cn.qingchengfit.wxpreview.old.newa.bean.ShopWrapper;
import cn.qingchengfit.wxpreview.old.newa.network.WxPreviewApi;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class WebActivityForGuideViewModel extends BaseViewModel {
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  public MutableLiveData<Shop> shopMutableLiveData = new MutableLiveData<>();
  public MutableLiveData<MiniProgram> miniProgramMutableLiveData = new MutableLiveData<>();

  @Inject public WebActivityForGuideViewModel() {

  }

  public void loadShopDetail() {

    qcRestRepository.createGetApi(WxPreviewApi.class)
        .qcGetShopDetail(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            shopMutableLiveData.setValue(response.data.shop);
            miniProgramMutableLiveData.setValue(response.data.mini_program);
          } else {
            defaultResult.setValue(Resource.error(response.getMsg(), null));
          }
        }, throwable -> {
          ToastUtils.show("网络异常");
        });
  }

  public void loadTrainerShopDetail() {

    qcRestRepository.createGetApi(WxPreviewApi.class)
        .qcGetTrainerShopDetail(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            shopMutableLiveData.setValue(response.data.shop);
            miniProgramMutableLiveData.setValue(response.data.mini_program);
          } else {
            defaultResult.setValue(Resource.error(response.getMsg(), null));
          }
        }, throwable -> {
          ToastUtils.show("网络异常");
        });
  }
}
