package cn.qingchengfit.staffkit.presenters;

import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SignInConfigPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    private MVPView view;

    @Inject public SignInConfigPresenter() {
    }

    public void getSignInConfigs() {
        RxRegiste(restRepository.getGet_api()
            .qcGetSignInCostConfig(App.staffId, gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<SignInCardCostBean.Data>>() {
                @Override public void call(QcResponseData<SignInCardCostBean.Data> dataQcResponseData) {
                    view.onCardCost(dataQcResponseData.getData().card_costs);
                }
            }));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onCardCost(List<SignInCardCostBean.CardCost> cardCosts);
    }
}
