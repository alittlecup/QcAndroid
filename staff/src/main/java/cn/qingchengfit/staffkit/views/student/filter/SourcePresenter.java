package cn.qingchengfit.staffkit.views.student.filter;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.StudentSourceBean;
import cn.qingchengfit.model.responese.TrackFilterOrigin;
import cn.qingchengfit.model.responese.TrackFilterOrigins;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
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
public class SourcePresenter extends BasePresenter {

    public PresenterView view;
    public RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private int type;

    @Inject public SourcePresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public void addOrigin(String name) {
        HashMap<String, Object> params = gymWrapper.getParams();
        HashMap<String, Object> body = new HashMap<>();
        body.put("name", name);
        RxRegiste(restRepository.getPost_api()
            .qcAddOrigin(loginStatus.staff_id(), params, body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    view.onSuccess();
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void getOrigins(String sellerId, boolean inSales) {
        HashMap<String, Object> params = gymWrapper.getParams();

        if (type == 3) {
            RxRegiste(restRepository.getGet_api()
                .qcGetUsersStudentsOrigins(App.staffId, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<QcDataResponse<TrackFilterOrigins>, List<StudentSourceBean>>() {
                    @Override public List<StudentSourceBean> call(QcDataResponse<TrackFilterOrigins> responseData) {
                        List<StudentSourceBean> sourceList = new ArrayList<StudentSourceBean>();
                        if (responseData.data.origins != null) {
                            for (TrackFilterOrigin origin : responseData.data.origins) {
                                sourceList.add(origin.toStudentSource());
                            }
                        }
                        return sourceList;
                    }
                })
                .subscribe(new Action1<List<StudentSourceBean>>() {
                    @Override public void call(List<StudentSourceBean> responseData) {
                        view.onSources(responseData);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        view.onShowError(throwable.getMessage());
                    }
                }));
        } else {
            if (inSales) {
                if (!TextUtils.isEmpty(sellerId)) {
                    params.put("seller_id", sellerId);
                } else {
                    params.put("seller_id", 0);
                }
            }
            RxRegiste(restRepository.getGet_api()
                .qcGetTrackStudentsOrigins(App.staffId, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<QcDataResponse<TrackFilterOrigins>, List<StudentSourceBean>>() {
                    @Override public List<StudentSourceBean> call(QcDataResponse<TrackFilterOrigins> responseData) {
                        List<StudentSourceBean> sourceList = new ArrayList<StudentSourceBean>();
                        if (responseData.data.origins != null) {
                            for (TrackFilterOrigin origin : responseData.data.origins) {
                                sourceList.add(origin.toStudentSource());
                            }
                        }
                        return sourceList;
                    }
                })
                .subscribe(new Action1<List<StudentSourceBean>>() {
                    @Override public void call(List<StudentSourceBean> responseData) {
                        view.onSources(responseData);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        view.onShowError(throwable.getMessage());
                    }
                }));
        }
    }

    public interface PresenterView extends PView {
        void onSources(List<StudentSourceBean> salers);

        void onShowError(String e);

        void onSuccess();
    }
}