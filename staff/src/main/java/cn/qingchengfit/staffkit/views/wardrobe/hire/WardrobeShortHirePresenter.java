package cn.qingchengfit.staffkit.views.wardrobe.hire;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.HireWardrobeBody;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.StringUtils;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WardrobeShortHirePresenter extends BasePresenter {
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;

    @Inject public WardrobeShortHirePresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void hireWardrobe(String staffid, HireWardrobeBody body) {
        if (body.is_long_term_borrow || StringUtils.isEmpty(body.user_id)) {
            view.onShowError("请选择学员");
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
                        view.onHireOK();
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
        void onHireOK();
    }
}
