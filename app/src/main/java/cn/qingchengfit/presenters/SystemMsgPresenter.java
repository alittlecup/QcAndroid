package cn.qingchengfit.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ClearNotiBody;
import cn.qingchengfit.model.responese.NotificationGlance;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import com.anbillon.qcmvplib.CView;
import com.anbillon.qcmvplib.PView;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SystemMsgPresenter extends BasePresenter {
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    private MVPView view;

    @Inject public SystemMsgPresenter() {
    }

    public void querySimpleList(String json) {
        RxRegiste(
            restRepository.getGet_api().qcGetNotificationIndex(json).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<List<NotificationGlance>>>() {
                @Override public void call(QcResponseData<List<NotificationGlance>> notificationGlanceQcResponseData) {
                    if (ResponseConstant.checkSuccess(notificationGlanceQcResponseData)) {
                        view.onNotificationList(notificationGlanceQcResponseData.getData());
                    } else {
                        view.onShowError(notificationGlanceQcResponseData.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    public void clearNoti(String type) {
        RxRegiste(restRepository.getPost_api()
            .qcClearTypeNoti(new ClearNotiBody(type))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onClearNotiOk();
                    } else {
                        view.onShowError(qcResponse.getMsg());
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
        void onNotificationList(List<NotificationGlance> list);

        void onClearNotiOk();
    }
}
