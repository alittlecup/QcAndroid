package cn.qingchengfit.presenters;

import cn.qingchengfit.chat.model.Record;
import cn.qingchengfit.chat.model.RecordWrap;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ClearNotiBody;
import cn.qingchengfit.model.responese.NotificationGlance;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import com.qingchengfit.fitcoach.http.RestRepository;
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
        RxRegiste(restRepository.getGet_api()
            .qcGetNotificationIndex(json)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<List<NotificationGlance>>>() {
                @Override public void call(QcDataResponse<List<NotificationGlance>> notificationGlanceQcResponseData) {
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
            .onBackpressureBuffer()
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

    public void queryRecruitMsgList(){
        RxRegiste(restRepository.getGet_api()
            .qcGetRecruitMessageList()
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<RecordWrap>>() {
                @Override public void call(
                    QcDataResponse<RecordWrap> recordWrapQcResponseData) {
                    if (ResponseConstant.checkSuccess(recordWrapQcResponseData)){
                        view.onMessageList(recordWrapQcResponseData.data.records);
                    }else{
                        view.onShowError(recordWrapQcResponseData.getMsg());
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

        void onMessageList(List<Record> recordList);
    }
}
