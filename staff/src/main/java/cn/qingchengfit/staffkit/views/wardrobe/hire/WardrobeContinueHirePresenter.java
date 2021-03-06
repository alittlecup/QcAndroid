package cn.qingchengfit.staffkit.views.wardrobe.hire;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.utils.DateUtils;
import java.util.Date;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WardrobeContinueHirePresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private StaffRespository restRepository;

    @Inject public WardrobeContinueHirePresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    public void changeDay(String lockerend, Date date) {
        //        int daycount = DateUtils.dayNumFromToday(DateUtils.formatDateFromServer(lockerend));
        //        if (daycount <= 0) {
        //            view.onContinueDay(DateUtils.interval(new Date(new Date().getTime() + DateUtils.DAY_TIME), date));
        //        } else {
        view.onContinueDay(DateUtils.interval(new Date(DateUtils.formatDateFromServer(lockerend).getTime() + DateUtils.DAY_TIME), date));
        //        }
    }

    public void comfirContinue(String staffid, HashMap<String, Object> body) {
        RxRegiste(restRepository.getStaffAllApi()
            .qcContinueLocker(staffid, gymWrapper.getParams(), body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onHireOk();
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    if (view != null) view.onShowError(throwable.getMessage());
                }
            }));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onContinueDay(int d);

        void onHireOk();
    }
}
