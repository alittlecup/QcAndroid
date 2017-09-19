package cn.qingchengfit.notisetting.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.NotiSmsCountListWrap;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.SmsListWrap;
import cn.qingchengfit.notisetting.bean.NotiSettingMsgDetail;
import cn.qingchengfit.notisetting.bean.NotiSmsCount;
import cn.qingchengfit.staffkit.constant.Get_Api;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NotiSettingSendListDetailPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  @Inject LoginStatus loginStatus;
  int curPage = 1, totalPage = 1;
  private MVPView view;
  private HashMap<String, Object> params = new HashMap<>();

  @Inject public NotiSettingSendListDetailPresenter() {

  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  public void setParams(HashMap<String, Object> p) {
    curPage = totalPage = 1;
    params.clear();
    params.putAll(gymWrapper.getParams());
    if (p != null) params.putAll(p);
  }


  public void getList() {
    if (curPage <= totalPage) {
      params.put("page", curPage);
      RxRegiste(qcRestRepository.createGetApi(Get_Api.class)
          .qcGetNotiSMSs(loginStatus.staff_id(), params)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<SmsListWrap>>() {
            @Override public void call(QcDataResponse<SmsListWrap> qcResponse) {
              if (ResponseConstant.checkSuccess(qcResponse)) {
                view.onMsgs(qcResponse.data.messages, qcResponse.data.total_count, curPage);
                totalPage = qcResponse.data.pages;
                curPage++;
              } else {
                view.onShowError(qcResponse.getMsg());
              }
            }
          }, new NetWorkThrowable()));

      if (curPage == 1) {
        RxRegiste(qcRestRepository.createGetApi(Get_Api.class)
            .qcGetNotiSmsCount(loginStatus.staff_id(), params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<NotiSmsCountListWrap>>() {
              @Override public void call(QcDataResponse<NotiSmsCountListWrap> qcResponse) {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                  String a = "", b = "", c = "";
                  int appCount = 0, appTotal = 0;
                  for (NotiSmsCount message : qcResponse.data.messages) {
                    switch (message.type) {
                      case 1:
                        a = message.success_count + "/" + (message.fail_count
                            + message.success_count);
                        break;
                      case 2:
                        b = message.success_count + "/" + (message.fail_count
                            + message.success_count);
                        break;
                      case 3:
                      case 4:
                        appCount += message.success_count;
                        appTotal += (message.fail_count + message.success_count);
                        break;
                      default:
                        break;
                    }
                    c = appCount + "/" + appTotal;
                    view.onSendInfo(a, b, c);
                  }
                } else {
                  view.onShowError(qcResponse.getMsg());
                }
              }
            }, new NetWorkThrowable()));
      }
    } else {
      view.onMsgs(null, 0, 0);
    }
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onMsgs(List<NotiSettingMsgDetail> list, int totalcount, int curPage);
    void onSendInfo(String a, String b, String c);
  }
}
