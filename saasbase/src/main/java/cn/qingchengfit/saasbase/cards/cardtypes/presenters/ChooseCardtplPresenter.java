package cn.qingchengfit.saasbase.cards.cardtypes.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.repository.CardApi;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ChooseCardtplPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  @Inject LoginStatus loginStatus;
  private MVPView view;

  @Inject public ChooseCardtplPresenter() {
  }

  public void queryCardtplsNoPermission() {
    HashMap<String, Object> p = gymWrapper.getParams();
    p.put("key", PermissionServerUtils.CARDSETTING);
    p.put("method", "get");
    RxRegiste(qcRestRepository.createGetApi(CardApi.class)
        .qcGetCardTplsPermission(loginStatus.staff_id(), p)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CardTplListWrap>>() {
          @Override public void call(QcDataResponse<CardTplListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onCardtplListDone(qcResponse.data.card_tpls);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onCardtplListDone(List<CardTpl> cardtpls);
  }
}
