package cn.qingchengfit.staffkit.views.allotsales.choose;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.constant.StaffRespository;
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
    private StaffRespository restRepository;

    @Inject public MutiChooseSalersPresenterPresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    void querySalers(String staffid) {
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetSalers(staffid, null, null, gymWrapper.id(), gymWrapper.model())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<Sellers>>() {
                @Override public void call(QcDataResponse<Sellers> qcResponseSalers) {
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
        RxRegiste(restRepository.getStaffAllApi()
            .qcModifySellers(staffid, gymWrapper.getParams(), body)
            .onBackpressureBuffer()
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
