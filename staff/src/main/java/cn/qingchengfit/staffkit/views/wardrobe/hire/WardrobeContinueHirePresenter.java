package cn.qingchengfit.staffkit.views.wardrobe.hire;

import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
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
    private RestRepository restRepository;

    @Inject public WardrobeContinueHirePresenter(RestRepository restRepository) {
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
        RxRegiste(restRepository.getPost_api()
            .qcContinueLocker(staffid, gymWrapper.getParams(), body)
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
