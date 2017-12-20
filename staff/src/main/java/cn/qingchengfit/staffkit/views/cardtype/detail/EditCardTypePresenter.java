package cn.qingchengfit.staffkit.views.cardtype.detail;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.CardtplBody;
import cn.qingchengfit.model.body.ShopsBody;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.views.cardtype.UUIDModel;
import java.util.HashMap;
import java.util.Iterator;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
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
    @Inject QcRestRepository restRepository;
    private EditCardTypeView view;

    @Inject public EditCardTypePresenter() {
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
        super.unattachView();
        view = null;
    }

    public void editCardInfo(String staffid, CardtplBody body) {
        CardtplBody body1 = body.clone();
        body1.id = null;
        body1.shops = null;
        RxRegiste(restRepository.createPostApi(Post_Api.class)
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
        RxRegiste(restRepository.createPostApi(Post_Api.class)
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

    public String dealParamsToWeb(String json){
        String params = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String key = it.next();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public void stashCardTplInfo(CardtplBody body){
        HashMap<String, Object> map = new HashMap<>();
        if (gymWrapper.inBrand()) {
            map.put("brand_id", gymWrapper.brand_id());
        }else{
            map.putAll(gymWrapper.getParams());
        }
        RxRegiste(restRepository.createPostApi(Post_Api.class)
            .qcStashNewCardTpl(loginStatus.staff_id(), body, map)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<UUIDModel>>() {
                @Override public void call(QcDataResponse<UUIDModel> uuidModelQcDataResponse) {
                    if (ResponseConstant.checkSuccess(uuidModelQcDataResponse)){
                        view.onStashSuccessed(uuidModelQcDataResponse.data.uuid);
                    }else {
                        view.onEditFailed(uuidModelQcDataResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    public void addCardInfo(String staffid, CardtplBody body) {

        RxRegiste(restRepository.createPostApi(Post_Api.class)
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
