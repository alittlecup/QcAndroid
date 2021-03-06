package cn.qingchengfit.staffkit.allocate.coach.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.Staffs;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by fb on 2017/5/4.
 */

public class MutiChooseCoachPresenter extends BasePresenter {

    @Inject StaffRespository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private CoachAllView view;

    @Inject public MutiChooseCoachPresenter() {
    }

    public void getCoachPreviewList() {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetAllAllocateCoaches(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<Staffs>>() {
                @Override public void call(QcDataResponse<Staffs> allotSalePreViewsQcResponseData) {
                    if (allotSalePreViewsQcResponseData.status == 200) {
                        view.onCoaches(allotSalePreViewsQcResponseData.data.teachers);
                    } else {
                        view.onShowError(allotSalePreViewsQcResponseData.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void allotCoaches(String staffid, List<String> students, List<String> coaches) {
        HashMap<String, Object> body = gymWrapper.getParams();
        body.put("user_ids", StringUtils.List2Str(students));
        body.put("coach_ids", StringUtils.List2Str(coaches));
        RxRegiste(restRepository.getStaffAllApi()
            .qcAllocateCoach(staffid, body).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onAllotSuccess();
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

    @Override public void attachView(PView v) {
        view = (CoachAllView) v;
    }

    public interface CoachAllView extends CView {
        void onCoaches(List<Staff> salers);

        void onAllotSuccess();
    }
}
