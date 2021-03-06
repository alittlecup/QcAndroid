package cn.qingchengfit.staffkit.views.allotsales;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.AllotSalePreView;
import cn.qingchengfit.model.responese.AllotSalePreViews;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yangming on 16/10/11.
 */
public class SalesListPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StaffRespository restRepository;

    @Inject public SalesListPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getSalesPreviewList() {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetAllotSalesPreView(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<AllotSalePreViews>>() {
                @Override public void call(QcDataResponse<AllotSalePreViews> allotSalePreViewsQcResponseData) {
                    if (allotSalePreViewsQcResponseData.status == 200) {
                        view.onSalesPreview(allotSalePreViewsQcResponseData.data.sellers);
                    } else {
                        view.onShowError(allotSalePreViewsQcResponseData.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public interface PresenterView extends PView {
        void onSalesPreview(List<AllotSalePreView> list);

        void onShowError(String e);
    }
}