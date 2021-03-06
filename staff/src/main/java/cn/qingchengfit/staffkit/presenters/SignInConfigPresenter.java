package cn.qingchengfit.staffkit.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SignInConfigPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StaffRespository restRepository;
    private MVPView view;

    @Inject public SignInConfigPresenter() {
    }

    public void getSignInConfigs() {
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetSignInCostConfig(App.staffId, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<SignInCardCostBean.Data>>() {
                @Override public void call(QcDataResponse<SignInCardCostBean.Data> dataQcResponseData) {
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
