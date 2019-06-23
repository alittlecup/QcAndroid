package cn.qingchengfit.staffkit.views.gym.site;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.utils.ToastUtils;
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
 * Created by Paper on 16/5/10 2016.
 */
public class SiteDetailPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private GymUseCase useCase;
    private SiteDetailView view;
    private Subscription spFix, spDel;

    @Inject public SiteDetailPresenter(GymUseCase useCase) {
        this.useCase = useCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (SiteDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spDel != null) spDel.unsubscribe();
        if (spFix != null) spFix.unsubscribe();
    }

    public void delSite(String siteid) {
        spDel = useCase.delSite(siteid, gymWrapper.id(), gymWrapper.model(), new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onDelSucceed();
                } else {
                     ToastUtils.show(qcResponse.getMsg());
                    view.onFailed();
                }
            }
        });
    }

    public void fixSite(String id, Space space) {
        Space fix = new Space(space.getName(), space.getCapacity(), space.is_support_private(), space.is_support_team());
        fix.setShop_id(space.getShop_id());
        fix.setId(null);
        spFix = useCase.updateSite(id, gymWrapper.id(), gymWrapper.model(), null, fix, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onFixSucceed();
                } else {
                    ToastUtils.show(qcResponse.getMsg());
                    view.onFailed();
                }
            }
        });
    }
}
