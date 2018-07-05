package cn.qingchengfit.saasbase.student.other;

import cn.qingchengfit.saascommon.network.RxHelper;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saascommon.utils.StringUtils;

/**
 * Created by huangbaole on 2017/11/2.
 */

public class ChooseStaffPresenter extends BasePresenter<ChooseStaffPresenter.MVPView> {

    @Inject
    IStudentModel studentModel;
    @Inject
    GymWrapper gymWrapper;
    @Inject
    public ChooseStaffPresenter() {

    }
    public void loadData(String staffid){
        RxRegiste(studentModel
                .qcGetSalers(staffid, null, null, gymWrapper.id(), gymWrapper.model())
                .compose(RxHelper.schedulersTransformer())
                .subscribe(qcResponseSalers -> {
                    if (ResponseConstant.checkSuccess(qcResponseSalers)) {
                        mvpView.onStaffList(qcResponseSalers.data.users);
                    } else {
                        mvpView.onShowError(qcResponseSalers.getMsg());
                    }
                }, throwable -> mvpView.onShowError(throwable.getMessage())));
    }
   public void allotSalers(String staffid, List<String> students, List<String> salers, String curentid) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_ids", StringUtils.List2Str(students));
        body.put("seller_ids", StringUtils.List2Str(salers));
        body.put("seller_id", curentid);
        RxRegiste(studentModel
                .qcModifySellers(staffid, gymWrapper.getParams(), body)
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
        void onStaffList(List<Staff> staffs);

        void onShowError(String msg);
        void onAllotSuccess();
    }
}
