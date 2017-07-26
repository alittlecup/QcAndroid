package cn.qingchengfit.staffkit.presenters;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.ResponseSu;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.GymUtils;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QuerySuPresenter extends BasePresenter {
    @Inject RestRepository restRepository;
    private MVPView view;

    @Inject public QuerySuPresenter() {
    }

    public void querySu(final CoachService coachService) {
        RxRegiste(restRepository.getGet_api()
            .qcCheckSu(App.staffId, GymUtils.getParams(coachService, null))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<ResponseSu>>() {
                @Override public void call(QcResponseData<ResponseSu> jsonObjectQcResponseData) {
                    if (ResponseConstant.checkSuccess(jsonObjectQcResponseData)) {
                        try {
                            view.onGetSu(jsonObjectQcResponseData.data.is_superuser(), coachService);
                        } catch (Exception e) {

                        }
                    } else {
                        view.onShowError(jsonObjectQcResponseData.getMsg());
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
        void onGetSu(boolean isSu, CoachService coachService);
    }
}
