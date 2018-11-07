package cn.qingchengfit.saas.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.events.EventAddress;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saas.network.GetApi;
import cn.qingchengfit.saas.network.PostApi;
import cn.qingchengfit.saas.response.GymWrap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BaseGymInfoPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  private MVPView view;
  private String gymid;
  private Gym editBody = new Gym();

  @Inject public BaseGymInfoPresenter() {
  }

  public String getGymid() {
    return gymid;
  }

  public void setGymid(String gymid) {
    this.gymid = gymid;
  }

  public void setGymPhoto(String s) {
    this.editBody.setPhoto(s);
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
    RxBusAdd(EventAddress.class).subscribe(new Action1<EventAddress>() {
      @Override public void call(EventAddress eventAddress) {
        editBody.setAddress(eventAddress.address);
        editBody.setGd_lat(eventAddress.lat);
        editBody.setGd_lng(eventAddress.log);
        editBody.gd_district_id = eventAddress.city_code;
        view.onAddress(eventAddress.address);
      }
    });
  }
  public void setContact(String contact){this.editBody.setContact(contact);}
  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void queryGymInfo() {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .queryGymInfo(gymid).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GymWrap>>() {
          @Override public void call(QcDataResponse<GymWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onGym(qcResponse.data.gym);
              editBody.description = qcResponse.data.gym.description;
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void editGymInfo() {
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
        .editGymIntro(gymid, editBody).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onEditOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void setGymName(String name) {
    editBody.setName(name);
  }

  public void setGymPhone(String p) {
    editBody.phone = p;
  }

  public String getGymDesc() {
    return editBody.description;
  }

  public void setGymDesc(String s) {
    editBody.description = s;
  }

  public interface MVPView extends CView {
    void onGym(Gym gym);

    void onAddress(String s);

    void onEditOk();
  }
}
