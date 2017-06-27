package cn.qingchengfit.staffkit.views.allotsales.choose;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MutiChooseSalersPresenterPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private RestRepository restRepository;

    @Inject public MutiChooseSalersPresenterPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    void querySalers(String staffid) {
        RxRegiste(restRepository.getGet_api()
            .qcGetSalers(staffid, null, null, gymWrapper.id(), gymWrapper.model())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<Sellers>>() {
                @Override public void call(QcResponseData<Sellers> qcResponseSalers) {
                    if (ResponseConstant.checkSuccess(qcResponseSalers)) {
                        view.onSalers(qcResponseSalers.data.users);
                    } else {
                        view.onShowError(qcResponseSalers.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    void allotSalers(String staffid, List<String> students, List<String> salers, String curentid) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_ids", StringUtils.List2Str(students));
        body.put("seller_ids", StringUtils.List2Str(salers));
        body.put("seller_id", curentid);
        RxRegiste(restRepository.getPost_api()
            .qcModifySellers(staffid, gymWrapper.getParams(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onAllotSuccess();
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onSalers(List<Staff> salers);

        void onAllotSuccess();
    }
}
