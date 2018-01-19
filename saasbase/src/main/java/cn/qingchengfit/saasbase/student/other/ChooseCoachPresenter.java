package cn.qingchengfit.saasbase.student.other;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.utils.StringUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/2.
 */

public class ChooseCoachPresenter extends BasePresenter<ChooseCoachPresenter.MVPView> {

    @Inject
    IStudentModel studentModel;
    @Inject
    GymWrapper gymWrapper;
    @Inject
    public ChooseCoachPresenter() {

    }
    public void getCoachPreviewList(String staffId) {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(studentModel
                .qcGetAllAllocateCoaches(staffId, params)
                .compose(RxHelper.schedulersTransformer())
                .subscribe(allotSalePreViewsQcResponseData -> {
                    if (allotSalePreViewsQcResponseData.status == 200) {
                        mvpView.onCoaches(allotSalePreViewsQcResponseData.data.teachers);
                    } else {
                        mvpView.onShowError(allotSalePreViewsQcResponseData.getMsg());
                    }
                }, throwable -> mvpView.onShowError(throwable.getMessage())));
    }

    public void allotCoaches(String staffid, List<String> students, List<String> coaches) {
        HashMap<String, Object> body = gymWrapper.getParams();
        body.put("user_ids", StringUtils.List2Str(students));
        body.put("coach_ids", StringUtils.List2Str(coaches));
        RxRegiste(studentModel
                .qcAllocateCoach(staffid, body)
                .compose(RxHelper.schedulersTransformer())
                .subscribe(qcResponse -> {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        mvpView.onAllotSuccess();
                    } else {
                        mvpView.onShowError(qcResponse.getMsg());
                    }
                }, throwable -> mvpView.onShowError(throwable.getMessage())));
    }

    public interface MVPView extends CView {
        void onCoaches(List<Staff> salers);

        void onAllotSuccess();
    }
}
