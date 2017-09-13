package cn.qingchengfit.staffkit.views.student.edit;

import android.content.Intent;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.mvpbase.Presenter;
import cn.qingchengfit.staffkit.rxbus.event.EventFreshStudent;
import cn.qingchengfit.staffkit.usecase.StudentUsecase;
import cn.qingchengfit.staffkit.usecase.bean.User_Student;
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
 * Created by Paper on 16/4/20 2016.
 */
public class EditStudentInfoPresenter implements Presenter {
    @Inject StudentUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    private EditStudentInfoView view;
    private Subscription edSp;
    private Subscription addSp;

    @Inject public EditStudentInfoPresenter() {

    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (EditStudentInfoView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (edSp != null) edSp.unsubscribe();
        if (addSp != null) addSp.unsubscribe();
    }

    public void editStudent(User_Student user_student) {

        final User_Student body = new User_Student();
        body.setPhone(user_student.getPhone());
        body.setArea_code(user_student.getArea_code());
        body.setUsername(user_student.getUsername());
        body.setAddress(user_student.getAddress());
        body.setAvatar(user_student.getAvatar());
        body.setDate_of_birth(user_student.getDate_of_birth());
        body.setGender(user_student.getGender());
        body.setJoined_at(user_student.getJoined_at());
        body.setRecommend_by_id(user_student.getRecommend_by_id());
        body.setOrigin(user_student.getOrigin());
        body.setRemarks(user_student.getRemarks());

        edSp = usecase.updateStudent(user_student.getId(), gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(), body,
            new Action1<QcDataResponse>() {
                @Override public void call(QcDataResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        RxBus.getBus().post(new EventFreshStudent());
                        view.onSuccess();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            });
    }

    public void saveStudent(final User_Student user_student) {
      addSp =
          usecase.addStudent(user_student, gymWrapper.getParams(), new Action1<QcDataResponse>() {
            @Override public void call(QcDataResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    RxBus.getBus().post(new EventFreshStudent());
                    view.onSuccess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        });
    }
}
