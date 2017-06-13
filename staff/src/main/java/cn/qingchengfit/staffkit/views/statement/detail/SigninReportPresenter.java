package cn.qingchengfit.staffkit.views.statement.detail;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.SigninReportDetail;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yangming on 16/9/22.
 */
public class SigninReportPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepository restRepository;

    @Inject public SigninReportPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    public void queryReportDetail(String shop_id, final String start, final String end, String card_id, String cards_extra) {
        HashMap<String, Object> params = new HashMap<>();
        if (gymWrapper.inBrand()) {
            params.put("brand_id", gymWrapper.brand_id());
            if (!TextUtils.isEmpty(shop_id)) {
                params.put("shop_id", shop_id);
            }
        } else {
            params = gymWrapper.getParams();
        }
        params.put("start", start);
        params.put("end", end);

        RxRegiste(restRepository.getGet_api()
            .qcGetSigninReportDetail(App.staffId, params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<SigninReportDetail>>() {
                @Override public void call(QcResponseData<SigninReportDetail> responseData) {
                    if (responseData.status == ResponseConstant.SUCCESS) {
                        view.onSuccess(responseData.data);
                    } else {
                        view.onFailed(responseData.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onFailed(throwable.getMessage());
                }
            }));
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface PresenterView extends PView {
        void onSuccess(SigninReportDetail signinDetail);

        void onFailed(String e);
    }
}