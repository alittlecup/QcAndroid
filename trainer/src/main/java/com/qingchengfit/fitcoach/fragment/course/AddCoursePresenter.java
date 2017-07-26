package com.qingchengfit.fitcoach.fragment.course;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.GymUtils;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.CourseBody;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/8/2.
 */
public class AddCoursePresenter extends BasePresenter {

    RestRepository restRepository;
    Brand brand;
    CoachService coachService;
    private AddCourseView view;

    @Inject public AddCoursePresenter(RestRepository restRepository, Brand brand, CoachService coachService) {
        this.restRepository = restRepository;
        this.brand = brand;
        this.coachService = coachService;
    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (AddCourseView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void addCourse(String staffid, CourseBody body) {
        RxRegiste(restRepository.getPost_api()
            .qcCreateCourse(staffid, body, GymUtils.getParams(coachService, brand))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSuccess();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public void setSupportGyms() {

    }

    public interface AddCourseView extends PView {
        void showSuitGym();

        void onSuccess();

        void onFailed(String s);
    }
}
