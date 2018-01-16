package cn.qingchengfit.staffkit.views.signin.zq.presenter;

import android.content.Context;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.views.signin.zq.model.AccessBody;
import cn.qingchengfit.staffkit.views.signin.zq.model.BottomModel;
import cn.qingchengfit.staffkit.views.signin.zq.model.Guard;
import cn.qingchengfit.staffkit.views.signin.zq.model.GuardWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/9/26.
 */

public class ZqAccessPresenter extends BasePresenter {

  @Inject QcRestRepository restRepository;
  @Inject GymWrapper gymWrapper;

  MVPView view;

  @Inject
  public ZqAccessPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  public void getAccess() {

    RxRegiste(restRepository.createGetApi(Get_Api.class)
        .qcGetAccess(App.staffId, gymWrapper.getParams())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GuardWrapper>>() {
          @Override public void call(QcDataResponse<GuardWrapper> guardWrapperQcDataResponse) {
            if (ResponseConstant.checkSuccess(guardWrapperQcDataResponse)){
              if (view != null){
                view.onGetAccess(guardWrapperQcDataResponse.data.guards);
              }
            }else{
              view.onShowError(guardWrapperQcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void changeZqStatus(String guardId, int status){
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("status", status);
    RxRegiste(restRepository.createPostApi(Post_Api.class)
        .qcChangeAccessStatus(App.staffId, guardId, gymWrapper.getParams(), params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if(ResponseConstant.checkSuccess(qcResponse)){
              if (view != null){
                view.changeStatusOk();
              }
            }else{
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));

  }

  public void deleteZqAccess(String guardId) {
    RxRegiste(restRepository.createPostApi(Post_Api.class)
        .qcDeleteAccess(App.staffId, guardId, gymWrapper.getParams())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if(ResponseConstant.checkSuccess(qcResponse)){
              if (view != null){
                view.onDeleteOk();
              }
            }else{
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void addZqAccess(AccessBody body){
    RxRegiste(restRepository.createPostApi(Post_Api.class)
        .qcAddAccess(App.staffId, body)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if(ResponseConstant.checkSuccess(qcResponse)){
              if (view != null){
                view.onAddOk();
              }
            }else{
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void editZqAccess(String guardId, AccessBody body){
    RxRegiste(restRepository.createPostApi(Post_Api.class)
        .qcEditAccess(App.staffId, guardId, gymWrapper.getParams(), body)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if(ResponseConstant.checkSuccess(qcResponse)){
              if (view != null){
                view.onEditOk();
              }
            }else{
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public List<BottomModel> getBottomList(Context context, int status){
    List<BottomModel> bottomlist = new ArrayList<>();
    switch (status){
      case 1:
        bottomlist.add(new BottomModel(context.getResources().getString(R.string.setting_long_open),3));
        bottomlist.add(new BottomModel(context.getResources().getString(R.string.setting_long_close),2));
        break;
      case 2:
        bottomlist.add(new BottomModel(context.getResources().getString(R.string.cancel_long_close),1));
        bottomlist.add(new BottomModel(context.getResources().getString(R.string.setting_long_open),3));
        break;
      case 3:
        bottomlist.add(new BottomModel(context.getResources().getString(R.string.cancel_long_open),1));
        bottomlist.add(new BottomModel(context.getResources().getString(R.string.setting_long_close),2));
        break;
    }
    bottomlist.add(new BottomModel(context.getResources().getString(R.string.edit_zq_access),0));
    bottomlist.add(new BottomModel(context.getResources().getString(R.string.delete_zq_access),4));
    bottomlist.add(new BottomModel(context.getResources().getString(R.string.cancel),5));
    return bottomlist;
  }

  @Override public void unattachView() {
    super.unattachView();
    if (view != null){
      view = null;
    }
  }

  public interface MVPView extends CView {
    void onGetAccess(List<Guard> guardList);

    void changeStatusOk();

    void onDeleteOk();

    void onAddOk();

    void onEditOk();
  }

}
