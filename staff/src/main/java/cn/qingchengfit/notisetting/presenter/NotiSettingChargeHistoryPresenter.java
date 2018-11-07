package cn.qingchengfit.notisetting.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.ShopOrdersWrap;
import cn.qingchengfit.notisetting.bean.ShopOrder;
import cn.qingchengfit.staffkit.constant.Get_Api;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NotiSettingChargeHistoryPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  @Inject LoginStatus loginStatus;

  int curPage = 1, totalPage = 1;
  private MVPView view;
  private HashMap<String, Object> params = new HashMap<>();

  @Inject public NotiSettingChargeHistoryPresenter() {

  }

  public void queryList() {
    if (curPage <= totalPage) {
      params.put("page", curPage);
      params.putAll(gymWrapper.getParams());
      params.put("type", "gym_sms");
      RxRegiste(qcRestRepository.createRxJava1Api(Get_Api.class)
          .qcGetGymOrders(params)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<ShopOrdersWrap>>() {
            @Override public void call(QcDataResponse<ShopOrdersWrap> qcResponse) {
              if (ResponseConstant.checkSuccess(qcResponse)) {
                view.onList(qcResponse.data.orders, qcResponse.data.total_count, curPage);
                curPage++;
                totalPage = qcResponse.data.pages;
              } else {
                view.onShowError(qcResponse.getMsg());
              }
            }
          }, new NetWorkThrowable()));
    } else {
      view.onList(null, 1, 1);
    }
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onList(List<ShopOrder> list, int total, int curPage);
  }
}
