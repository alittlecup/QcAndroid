package cn.qingchengfit.saasbase.student.presenters.transfer;

import android.text.TextUtils;

import java.util.HashMap;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by huangbaole on 2017/11/13.
 */

public class StudentTransferPresenter extends BasePresenter<StudentTransferPresenter.MVPView> {

    @Inject
    GymWrapper gymWrapper;
    @Inject
    IStudentModel studentModel;

    @Inject
    public StudentTransferPresenter(){}

    public void loadData(String staffID, StudentFilter filter) {
        HashMap<String, Object> params = gymWrapper.getParams();

        if (filter.sale != null && !"-1".equals(filter.sale.id)) {// -1是全部
            params.put("seller_id", filter.sale.id);//无销售seller_id=0
        }

        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        if (filter.sale != null && !"-1".equals(filter.sale.id)) {// -1是全部
            params.put("seller_id", filter.sale.id);//无销售seller_id=0
        }
        RxRegiste(studentModel
                .qcGetTrackStudentsConver(staffID, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trackStudentsQcResponseData -> mvpView.onStudentTransferBeanLoaded(trackStudentsQcResponseData.data)
                        , throwable -> mvpView.onShowError(throwable.getMessage())));
    }

    public interface MVPView extends CView {
        void onStudentTransferBeanLoaded(StudentTransferBean bean);
    }
}
