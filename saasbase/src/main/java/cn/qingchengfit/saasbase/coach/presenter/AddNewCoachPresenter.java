package cn.qingchengfit.saasbase.coach.presenter;

import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saas.response.GymWrap;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.coach.event.LoadingEvent;
import cn.qingchengfit.saasbase.coach.model.CoachResponse;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import cn.qingchengfit.saasbase.repository.PostApi;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.utils.UpYunClient;
import com.bumptech.glide.Glide;
import java.io.File;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/8/24.
 */

public class AddNewCoachPresenter extends BasePresenter {

  private MVPView view;
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository restRepository;

  @Inject
  public AddNewCoachPresenter() {
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView) v;
  }

  @Override public void unattachView() {
    if (view != null){
      view = null;
    }
    super.unattachView();
  }

  public void upLoadFile(final String filePath){

    Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(Subscriber<? super String> subscriber) {
        String upImg = UpYunClient.upLoadImg("header/", new File(filePath));
        subscriber.onNext(upImg);
      }
    })
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Subscriber<String>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            RxBus.getBus().post(new LoadingEvent(false));
          }

          @Override public void onNext(String upImg) {
            if (view != null){
              view.onUpLoadSucc(upImg);
            }
          }
        });
  }

  public void confirmAdd(String staffId, String name, String phone, String imgUrl, int resourceId, String area){

    Staff coach = new Staff(name, phone, imgUrl, resourceId == R.id.gender_male ? 0 : 1);
    coach.area_code = area;
    restRepository.createPostApi(PostApi.class)
        .qcAddCoach(staffId, gymWrapper.id(), gymWrapper.model(), coach)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponseData<CoachResponse>>() {
          @Override public void call(QcResponseData<CoachResponse> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (view != null){
                view.onConfirm(qcResponse.data.teacher);
              }
            } else {
              if (view != null){
                view.onFailed();
              }
            }
          }
        });
  }

  public interface MVPView extends CView{
    void onUpLoadSucc(String upImg);

    void onConfirm(Staff teacher);

    void onFailed();
  }

}
