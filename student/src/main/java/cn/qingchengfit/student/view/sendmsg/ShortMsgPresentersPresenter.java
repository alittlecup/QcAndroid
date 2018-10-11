package cn.qingchengfit.student.view.sendmsg;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.bean.ShortMsg;
import cn.qingchengfit.student.bean.ShortMsgBody;
import cn.qingchengfit.student.bean.ShortMsgDetail;
import cn.qingchengfit.student.bean.ShortMsgList;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentModel;
import cn.qingchengfit.student.respository.StudentRepository;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ShortMsgPresentersPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject IStudentModel studentModel;
    private MVPView view;

    @Inject public ShortMsgPresentersPresenter() {
    }

    public void queryShortMsgList(Integer status, String key) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (!StringUtils.isEmpty(key)) params.put("q", key);
        RxRegiste(studentModel.qcQueryShortMsgList(loginStatus.staff_id(), status, params)
            .compose(RxHelper.schedulersTransformerFlow())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onShortMsgList(qcResponse.getData().group_messages);
                } else {
                    view.onShowError(qcResponse.getMsg());
                }
            }, throwable -> view.onShowError(throwable.getMessage())));
    }

    public void queryShortMsgDetail(String id) {
        RxRegiste(studentModel.qcQueryShortMsgDetail(loginStatus.staff_id(), id, gymWrapper.getParams())
            .compose(RxHelper.schedulersTransformerFlow())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onShortMsgDetail(qcResponse.getData().group_message);
                } else {
                    view.onShowError(qcResponse.getMsg());
                }
            }, throwable -> view.onShowError(throwable.getMessage())));
    }

    public void sendMsg(final ShortMsgBody body) {
        RxRegiste(studentModel
            .qcPostShortMsg(loginStatus.staff_id(), body, gymWrapper.getParams())
            .compose(RxHelper.schedulersTransformerFlow())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    if (body.send == 1) {
                        view.onPostSuccess();
                    } else {
                        view.onPutSuccess();
                    }
                } else {
                    view.onShowError(qcResponse.getMsg());
                }
            }, throwable -> view.onShowError(throwable.getMessage())));
    }

    public void updateShortMsg(ShortMsgBody body) {
        RxRegiste(studentModel
            .qcPutShortMsg(loginStatus.staff_id(), body, gymWrapper.getParams())
           .compose(RxHelper.schedulersTransformerFlow())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onPutSuccess();
                } else {
                    view.onShowError(qcResponse.getMsg());
                }
            }, throwable -> view.onShowError(throwable.getMessage())));
    }

    public void delShortMsg(String messageid) {
        RxRegiste(studentModel
            .qcDelShortMsg(loginStatus.staff_id(), messageid, gymWrapper.getParams())
           .compose(RxHelper.schedulersTransformerFlow())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onDelSuccess();
                } else {
                    view.onShowError(qcResponse.getMsg());
                }
            }, throwable -> view.onShowError(throwable.getMessage())));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onShortMsgList(List<ShortMsg> list);

        void onShortMsgDetail(ShortMsg detail);

        void onPostSuccess();

        void onPutSuccess();

        void onDelSuccess();
    }
}
