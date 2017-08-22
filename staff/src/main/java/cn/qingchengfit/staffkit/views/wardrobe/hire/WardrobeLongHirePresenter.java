package cn.qingchengfit.staffkit.views.wardrobe.hire;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.HireWardrobeBody;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WardrobeLongHirePresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private RestRepository restRepository;

    @Inject public WardrobeLongHirePresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void hireLong(String staffid, HireWardrobeBody body) {
        if (body.is_long_term_borrow) {
            if (body.deal_mode == 1 && body.card_id == null) {
                view.onShowError("请选择会员卡");
                return;
            }

            RxRegiste(restRepository.getPost_api()
                .qcHireLocker(staffid, gymWrapper.getParams(), body)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponse>() {
                    @Override public void call(QcResponse qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)) {
                            view.onHireOk();
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
    }

    public void queryStuCard(String stuId) {
        RxRegiste(restRepository.getGet_api()
            .qcGetStudentCards(loginStatus.staff_id(), stuId, gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseStudentCards>() {
                @Override public void call(QcResponseStudentCards qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onStuCards(qcResponse.data.cards);
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

    public interface MVPView extends CView {
        void onHireOk();

        void onStuCards(List<Card> cards);
    }
}
