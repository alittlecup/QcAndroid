package cn.qingchengfit.staffkit.views.setting;

import android.content.Intent;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.StaffResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.mvpbase.CommonPView;
import cn.qingchengfit.staffkit.usecase.SettingUseCase;
import javax.inject.Inject;
import rx.Subscription;
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
 * Created by Paper on 16/3/4 2016.
 */
public class FixSelfInofPresneter implements Presenter {

    @Inject SettingUseCase useCase;
    private Subscription sp;
    private CommonPView commonPView;
    private Subscription spUpdate;

    @Inject public FixSelfInofPresneter(SettingUseCase useCase) {
        this.useCase = useCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        commonPView = (CommonPView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        commonPView = null;
        if (sp != null) sp.unsubscribe();
        if (spUpdate != null) spUpdate.unsubscribe();
    }

    public void querySelfInfo() {
        sp = useCase.getSelfInfo(new Action1<QcDataResponse<StaffResponse>>() {
            @Override public void call(QcDataResponse<StaffResponse> qcResponse) {

            }
        });
    }

    public void commitSelfInfo(Staff staff) {
        spUpdate = useCase.fixSelfInfo(staff, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    commonPView.onSuccess();
                } else {
                    // ToastUtils.logHttp(qcResponse);
                }
            }
        });
    }
}
