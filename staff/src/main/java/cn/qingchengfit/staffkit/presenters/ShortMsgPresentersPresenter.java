package cn.qingchengfit.staffkit.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ShortMsgBody;
import cn.qingchengfit.model.responese.ShortMsg;
import cn.qingchengfit.model.responese.ShortMsgDetail;
import cn.qingchengfit.model.responese.ShortMsgList;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ShortMsgPresentersPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    private MVPView view;

    @Inject public ShortMsgPresentersPresenter() {
    }

    public void queryShortMsgList(Integer status, String key) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (!StringUtils.isEmpty(key)) params.put("q", key);
        RxRegiste(restRepository.getGet_api()
            .qcQueryShortMsgList(App.staffId, status, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<ShortMsgList>>() {
                @Override public void call(QcDataResponse<ShortMsgList> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onShortMsgList(qcResponse.getData().group_messages);
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

    public void queryShortMsgDetail(String id) {
        RxRegiste(restRepository.getGet_api()
            .qcQueryShortMsgDetail(App.staffId, id, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<ShortMsgDetail>>() {
                @Override public void call(QcDataResponse<ShortMsgDetail> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onShortMsgDetail(qcResponse.getData().group_message);
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

    public void sendMsg(final ShortMsgBody body) {
        RxRegiste(restRepository.getPost_api()
            .qcPostShortMsg(App.staffId, body, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (body.send == 1) {
                            view.onPostSuccess();
                        } else {
                            view.onPutSuccess();
                        }
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

    public void updateShortMsg(ShortMsgBody body) {
        RxRegiste(restRepository.getPost_api()
            .qcPutShortMsg(App.staffId, body, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onPutSuccess();
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

    public void delShortMsg(String messageid) {
        RxRegiste(restRepository.getPost_api()
            .qcDelShortMsg(App.staffId, messageid, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onShortMsgList(List<ShortMsg> list);

        void onShortMsgDetail(ShortMsg detail);

        void onPostSuccess();

        void onPutSuccess();

        void onDelSuccess();
    }
}
