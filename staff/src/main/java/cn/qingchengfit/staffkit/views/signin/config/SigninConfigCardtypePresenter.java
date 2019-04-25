package cn.qingchengfit.staffkit.views.signin.config;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SignInCostBody;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.network.ActivityLifeCycleEvent;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public class SigninConfigCardtypePresenter extends BasePresenter {
    PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private StaffRespository restRepository;

    @Inject public SigninConfigCardtypePresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    public void getCardCostList() {

        HashMap<String, Object> params = gymWrapper.getParams();
        Observable observable =  restRepository.getStaffAllApi().qcGetSignInCostConfig(loginStatus.staff_id(), params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<SignInCardCostBean.Data>() {
            @Override protected void _onNext(SignInCardCostBean.Data signInCardCostBean) {
                view.onGetCostList(signInCardCostBean.card_costs);
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                view.onShowError(message);
            }
        }));

    }

    public void confirm(List<SignInCostBody.CardCost> card_costs) {
        HashMap<String, Object> params = gymWrapper.getParams();
        SignInCostBody body = new SignInCostBody();
        body.setShop_id(gymWrapper.shop_id());
        body.setCard_costs(card_costs);
        Observable observable = restRepository.getStaffAllApi().qcPutSignInCostConfig(App.staffId, params, body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onCostConfigSuccess();
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                view.onShowError(message);
            }
        }, ActivityLifeCycleEvent.PAUSE, lifecycleSubject));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onGetCostList(List<SignInCardCostBean.CardCost> signInConfigs);

        void onCostConfigSuccess();
    }
}
