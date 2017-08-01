package cn.qingchengfit.staffkit.views.gym.choose;

import android.content.Intent;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.StaffShip;
import cn.qingchengfit.model.responese.StaffShipResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.usecase.CoachUseCase;
import cn.qingchengfit.staffkit.views.adapter.ImageTwoTextBean;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/1/29 2016.
 */
public class ChooseStaffPresenter implements Presenter {

    private CoachUseCase coachUseCase;
    private CoachService coachService;
    private ChooseStaffView view;

    @Inject public ChooseStaffPresenter(CoachUseCase coachUseCase, CoachService studentBase) {
        this.coachUseCase = coachUseCase;
        this.coachService = studentBase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (ChooseStaffView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {

    }

    @SuppressWarnings("unused") public void getStaffs() {
        coachUseCase.getAllStaffs(coachService.getId(), coachService.getModel(), null, new Action1<QcDataResponse<StaffShipResponse>>() {
            @Override public void call(QcDataResponse<StaffShipResponse> qcResponseStaffs) {
                if (qcResponseStaffs.getStatus() == ResponseConstant.SUCCESS) {
                    List<ImageTwoTextBean> datas = new ArrayList<ImageTwoTextBean>();
                    for (StaffShip bean : qcResponseStaffs.data.ships) {
                        datas.add(new ImageTwoTextBean(bean.user.getAvatar(),
                            bean.user.getUsername() + " | " + (bean.position.name != null ? bean.position.name : "职位无"),
                            bean.user.getPhone()));
                    }
                    view.onGetStaffs(datas);
                } else {
                    // ToastUtils.logHttp(qcResponseStaffs);
                }
            }
        });
    }
}
