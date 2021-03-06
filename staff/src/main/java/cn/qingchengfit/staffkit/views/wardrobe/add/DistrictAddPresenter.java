package cn.qingchengfit.staffkit.views.wardrobe.add;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.utils.StringUtils;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DistrictAddPresenter extends BasePresenter {
    @Inject StaffRespository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;

    @Inject public DistrictAddPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void editRegion(String staffid, String name, String region_id) {
        if (StringUtils.isEmpty(name)) {
            view.onShowError("请填写区域名");
            return;
        }
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("region_id", region_id);
        RxRegiste(restRepository.getStaffAllApi()
            .qcEditLockerRegion(staffid, gymWrapper.getParams(), body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onEditSuccess();
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

    public void delRegion(String staffid, String id) {

        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("region_id", id);
        RxRegiste(restRepository.getStaffAllApi()
            .qcDelLockerRegion(staffid, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onDelSuccess();
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

    public void addRegion(String staffid, String name) {
        if (StringUtils.isEmpty(name)) {
            view.onShowError("请填写更衣柜名");
            return;
        }
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);
        RxRegiste(restRepository.getStaffAllApi()
            .qcAddLockerRegion(staffid, gymWrapper.getParams(), body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onAddSuccess();
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

    public interface MVPView extends CView {
        void onAddSuccess();

        void onEditSuccess();

        void onDelSuccess();
    }
}
