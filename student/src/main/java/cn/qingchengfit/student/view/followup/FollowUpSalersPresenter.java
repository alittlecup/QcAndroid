package cn.qingchengfit.student.view.followup;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.student.bean.SalerListWrap;
import cn.qingchengfit.student.respository.IStudentModel;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by huangbaole on 2017/11/8.
 */

public class FollowUpSalersPresenter extends BasePresenter<FollowUpSalersPresenter.MVPView> {
    @Inject IStudentModel studentModel;
    @Inject
    GymWrapper gymWrapper;

    @Inject
    public FollowUpSalersPresenter() {

    }

    public void getFilterSelers(String staff_id) {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(studentModel
                .qcGetTrackStudentsFilterSalers(staff_id, params)
               .compose(RxHelper.schedulersTransformerFlow())
                .subscribe(new Consumer<QcDataResponse<SalerListWrap>>() {
                    @Override
                    public void accept(QcDataResponse<SalerListWrap> salerListWrapQcDataResponse)
                        throws Exception {
                        mvpView.onSalers(salerListWrapQcDataResponse.data.sellers);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) {
                        mvpView.onShowError(throwable.getMessage());
                    }
                }));
    }


    public interface MVPView extends CView {

        void onSalers(List<Staff> sellers);
    }
}
