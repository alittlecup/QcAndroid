package cn.qingchengfit.staffkit.views.cardtype.detail;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.CardtplBody;
import cn.qingchengfit.model.body.ShopsBody;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/16 2016.
 */
public class EditCardTypePresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    private EditCardTypeView view;

    @Inject public EditCardTypePresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (EditCardTypeView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
    }

    public void editCardInfo(String staffid, CardtplBody body) {
        CardtplBody body1 = body.clone();
        body1.id = null;
        body1.shops = null;
        RxRegiste(restRepository.getPost_api()
            .qcUpdateCardtpl(staffid, body.id, body1, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onEditSucceess();
                    } else {
                        view.onEditFailed(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onEditFailed("error!");
                }
            }));
    }

    public void FixGyms(String staffid, String cardtplid, String shops) {
        RxRegiste(restRepository.getPost_api()
            .qcFixGyms(staffid, cardtplid, new ShopsBody.Builder().shops(shops).build(), gymWrapper.getParams())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSuccessShops();
                    } else {
                        view.onEditFailed(qcResponse.getMsg());
                    }
                }
            }));
    }

    public void addCardInfo(String staffid, CardtplBody body) {

        RxRegiste(restRepository.getPost_api()
            .qcCreateCardtpl(staffid, body, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onEditSucceess();
                    } else {
                        view.onEditFailed(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }
}
