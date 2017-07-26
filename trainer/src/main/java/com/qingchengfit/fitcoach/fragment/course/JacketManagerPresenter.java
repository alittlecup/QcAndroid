package com.qingchengfit.fitcoach.fragment.course;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.GymUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.EditJacketBody;
import com.qingchengfit.fitcoach.http.bean.QcResponseJacket;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/8/1.
 */
public class JacketManagerPresenter extends BasePresenter {

    private Brand brand;
    private CoachService coachService;
    private RestRepository restRepository;
    private JacketManagerView view;

    @Inject public JacketManagerPresenter(Brand brand, CoachService coachService, RestRepository restRepository) {
        this.brand = brand;
        this.coachService = coachService;
        this.restRepository = restRepository;
    }

    @Override public void attachView(PView v) {
        view = (JacketManagerView) v;
    }

    public void completeJacket(String courseid, List<String> jackets, boolean openCustom) {
        RxRegiste(restRepository.getPost_api()
            .qcEditJacket(App.coachid + "", courseid, GymUtils.getParams(coachService, brand),
                new EditJacketBody.Builder().photos(jackets).random_show_photos(!openCustom).build())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSuccess();
                    } else {
                        view.onFaied(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public void queryJacket(String staffid, String courseid) {
        RxRegiste(restRepository.getGet_api()
            .qcGetJacket(staffid, courseid, GymUtils.getParams(coachService, brand))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseJacket>() {
                @Override public void call(QcResponseJacket qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        ArrayList<String> r = new ArrayList<String>();
                        for (int i = 0; i < qcResponse.data.photos.size(); i++) {
                            r.add(qcResponse.data.photos.get(i).photo);
                        }
                        view.setJacket(r);
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

    public interface JacketManagerView extends CView {
        void setJacket(List<String> jackets);

        void onSuccess();

        void onFaied(String s);

        void onSwitch(boolean s);
    }
}
