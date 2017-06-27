package cn.qingchengfit.staffkit.views.card;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.responese.CardBindStudents;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/2/27.
 */

public class FixRealCardBindPresenter extends BasePresenter {

    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RealcardWrapper realCard;
    private OnBindStudentListener onBindStudentListener;

    @Inject public FixRealCardBindPresenter() {
    }

    public void setOnBindStudentListener(OnBindStudentListener onBindStudentListener) {
        this.onBindStudentListener = onBindStudentListener;
    }

    public void queryGetCardDetail() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("card_id", realCard.id());
        RxRegiste(restRepository.getGet_api()
            .qcGetBindStudent(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<CardBindStudents>>() {
                @Override public void call(QcResponseData<CardBindStudents> cardBindStudentsQcResponseData) {
                    if (ResponseConstant.checkSuccess(cardBindStudentsQcResponseData)) {
                        handleBindData(cardBindStudentsQcResponseData.data.users);
                    } else {
                        onBindStudentListener.onGetFailed(cardBindStudentsQcResponseData.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    private void handleBindData(List<QcStudentBean> data) {
        List<StudentBean> beanList = new ArrayList<>();
        for (QcStudentBean studentBean : data) {
            beanList.add(studentBean.toStudentBean(gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model()));
        }
        if (onBindStudentListener != null) {
            onBindStudentListener.onGetDataSuccess(beanList);
        }
    }

    public interface OnBindStudentListener {
        void onGetDataSuccess(List<StudentBean> studentList);

        void onGetFailed(String msg);
    }
}
