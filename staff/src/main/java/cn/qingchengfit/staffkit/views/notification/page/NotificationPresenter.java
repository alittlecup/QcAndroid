package cn.qingchengfit.staffkit.views.notification.page;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.body.ClearNotiBody;
import cn.qingchengfit.model.responese.Notification;
import cn.qingchengfit.model.responese.NotificationMsg;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NotificationPresenter extends BasePresenter {
    private MVPView view;

    private RestRepository restRepository;
    private int page = 1;
    private int totalpage = 1;

    @Inject public NotificationPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public void refresh(String type) {
        page = 1;
        HashMap<String, Object> params = new HashMap<>();
        params.put("page", page + "");
        params.put("type__in", type);
        RxRegiste(restRepository.getGet_api()
            .qcGetNotification(params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<Notification>>() {
                @Override public void call(QcDataResponse<Notification> qcResponseNotification) {
                    if (ResponseConstant.checkSuccess(qcResponseNotification)) {
                        totalpage = qcResponseNotification.data.pages;
                        view.onRefresh(qcResponseNotification.data.notifications, qcResponseNotification.data.unread_count);
                    } else {
                        view.onShowError(qcResponseNotification.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    void loadMore(String staffid, String type) {
        page++;
        if (page > totalpage) {
            view.onLoadmore(null, 0);
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        //        if (GymUtils.isInBrand(coachService)){
        //            params.put("brand_id",brand.getId());
        //        }else {
        //            params.put("brand_id",coachService.getBrand_id());
        //            params.put("shop_id",coachService.getShop_id());
        //        }
        //        params.put("staff_id",staffid);
        params.put("page", page + "");
        params.put("type__in", type);
        RxRegiste(restRepository.getGet_api()
            .qcGetNotification(params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<Notification>>() {
                @Override public void call(QcDataResponse<Notification> qcResponseNotification) {
                    if (ResponseConstant.checkSuccess(qcResponseNotification)) {
                        view.onLoadmore(qcResponseNotification.data.notifications, qcResponseNotification.data.unread_count);
                    }
                }
            }));
    }

    void clearOneNoti(String staffid, String notiId, final int pos) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", notiId);
        //        params.put("type",notiType);
        RxRegiste(restRepository.getPost_api()
            .qcClearAllNoti(staffid, params).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onClearPos(pos);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    if (view != null) view.onShowError(throwable.getMessage());
                }
            }));
        view.onClearPos(pos);
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
                        view.onClearOk();
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onRefresh(List<NotificationMsg> data, int unreadcount);

        void onLoadmore(List<NotificationMsg> data, int unreadcount);

        void onClearPos(int pos);

        void onClearOk();
    }
}
