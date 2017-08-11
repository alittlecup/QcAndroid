package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.model.EndFairTips;
import cn.qingchengfit.recruit.model.EndFairTopsWrap;
import cn.qingchengfit.recruit.network.GetApi;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/7/17.
 */

public class EndFairPresenter extends BasePresenter {

  @Inject QcRestRepository qcRestRepository;
  private MVPView view;

  @Inject public EndFairPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void queryEndFairList() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .qcGetEndFair().onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<EndFairTopsWrap>>() {
          @Override public void call(QcDataResponse<EndFairTopsWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onEndFairList(qcResponse.data.gyms);
            } else {
              //view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

public interface MVPView extends CView {
  void onEndFairList(List<EndFairTips> endFairList);
}

}