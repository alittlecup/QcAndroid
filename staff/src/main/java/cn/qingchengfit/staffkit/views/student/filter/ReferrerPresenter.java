package cn.qingchengfit.staffkit.views.student.filter;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentReferrerBean;
import cn.qingchengfit.model.responese.Referrers;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/11.
 */
public class ReferrerPresenter extends BasePresenter {

    public PresenterView view;
    public RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    @Inject public ReferrerPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getFilterSelers() {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(restRepository.getGet_api()
            .qcGetTrackStudentsRecommends(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(new Func1<QcDataResponse<Referrers>, List<StudentReferrerBean>>() {
                @Override public List<StudentReferrerBean> call(QcDataResponse<Referrers> referrersQcResponseData) {
                    List<StudentReferrerBean> referrers = new ArrayList<StudentReferrerBean>();
                    if (referrersQcResponseData.data.users != null) {
                        for (Staff staff : referrersQcResponseData.data.users) {
                            referrers.add(staff.toReferrerBean());
                        }
                    }
                    return referrers;
                }
            })
            .subscribe(new Action1<List<StudentReferrerBean>>() {
                @Override public void call(List<StudentReferrerBean> studentReferrerBeen) {
                    view.onReferrers(studentReferrerBeen);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void getReferrer(String query) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("q", query);
        params.put("show_all", "1");
        RxRegiste(restRepository.getGet_api()
            .qcGetTrackStudentsRecommendsSelect(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(new Func1<QcDataResponse<Referrers>, List<StudentReferrerBean>>() {
                @Override public List<StudentReferrerBean> call(QcDataResponse<Referrers> referrersQcResponseData) {
                    List<StudentReferrerBean> referrers = new ArrayList<StudentReferrerBean>();
                    if (referrersQcResponseData.data.users != null) {
                        for (Staff staff : referrersQcResponseData.data.users) {
                            referrers.add(staff.toReferrerBean());
                        }
                    }
                    return referrers;
                }
            })
            .subscribe(new Action1<List<StudentReferrerBean>>() {
                @Override public void call(List<StudentReferrerBean> studentReferrerBeen) {
                    view.onReferrers(studentReferrerBeen);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public interface PresenterView extends PView {
        void onReferrers(List<StudentReferrerBean> salers);

        void onShowError(String e);
    }
}