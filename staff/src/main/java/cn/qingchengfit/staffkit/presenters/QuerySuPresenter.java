package cn.qingchengfit.staffkit.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Permission;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.GymUtils;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QuerySuPresenter extends BasePresenter {
    @Inject RestRepository restRepository;
    private MVPView view;

    @Inject public QuerySuPresenter() {
    }

    public void getPermission(final CoachService coachService) {
        RxRegiste(restRepository.getGet_api()
            .qcPermission(App.staffId, GymUtils.getParams(coachService, null))
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponsePermission>() {
                @Override public void call(QcResponsePermission qcResponsePermission) {
                    if (ResponseConstant.checkSuccess(qcResponsePermission)){
                        boolean hasPermission = false;
                        if (qcResponsePermission.data.permissions != null){
                            for (Permission permission : qcResponsePermission.data.permissions) {
                                if (permission.getKey().equalsIgnoreCase(PermissionServerUtils.STUDIO_LIST_CAN_CHANGE)){
                                    hasPermission = permission.value();
                                    break;
                                }
                            }
                        }
                        view.onGetSu(hasPermission,coachService);
                    }else view.onShowError(qcResponsePermission.getMsg());
                }
            },new NetWorkThrowable()));
            //.subscribe(new Action1<QcResponseData<ResponseSu>>() {
            //    @Override public void call(QcResponseData<ResponseSu> jsonObjectQcResponseData) {
            //        if (ResponseConstant.checkSuccess(jsonObjectQcResponseData)) {
            //            try {
            //                view.onGetSu(jsonObjectQcResponseData.data.is_superuser(), coachService);
            //            } catch (Exception e) {
            //
            //            }
            //        } else {
            //            view.onShowError(jsonObjectQcResponseData.getMsg());
            //        }
            //    }
            //}, new NetWorkThrowable()));
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
