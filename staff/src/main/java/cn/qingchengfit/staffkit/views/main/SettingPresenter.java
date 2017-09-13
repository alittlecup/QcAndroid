package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import cn.qingchengfit.model.responese.SettingUsecase;
import cn.qingchengfit.model.responese.StaffResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import javax.inject.Inject;
import rx.functions.Action1;

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
 * Created by Paper on 16/4/22 2016.
 */
public class SettingPresenter extends BasePresenter {

    private SettingUsecase usecase;
    private SettingView view;

    @Inject public SettingPresenter(SettingUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (SettingView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getSelfInfo() {
        usecase.getSelfInfo(new Action1<QcDataResponse<StaffResponse>>() {
            @Override public void call(QcDataResponse<StaffResponse> qcResponseSelfInfo) {
                if (qcResponseSelfInfo.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSelfInfo(qcResponseSelfInfo.data.staff);
                } else {

                }
            }
        });
    }
}
