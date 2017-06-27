package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponsePermission;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
 * Created by Paper on 16/2/1 2016.
 */
public class GymDetailPresenter extends BasePresenter {
    GymDetailView gymDetailView;
    GymUseCase gymUseCase;
    @Inject GymWrapper gymWrapper;
    @Inject LoginStatus loginStatus;
    private Subscription sp;
    private Subscription spWelcome;
    private RestRepository restRepository;
    private Subscription spUnreadCount;

    @Inject public GymDetailPresenter(GymUseCase gymUseCase, RestRepository restRepository) {
        this.gymUseCase = gymUseCase;
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        gymDetailView = (GymDetailView) v;
        RxRegiste(QCDbManager.queryAllFunctions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<String>>() {
                @Override public void call(List<String> strings) {
                    gymDetailView.onModule(strings);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.d(throwable.getMessage());
                }
            }));
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        gymDetailView = null;
        if (sp != null) sp.unsubscribe();
        if (spWelcome != null) spWelcome.unsubscribe();
        if (spUnreadCount != null && spUnreadCount.isUnsubscribed()) {
            spUnreadCount.unsubscribe();
        }
    }

    void manageBrand() {
        RxRegiste(restRepository.getGet_api()
            .qcGetBrands(loginStatus.staff_id())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<BrandsResponse>>() {
                @Override public void call(QcResponseData<BrandsResponse> brandsResponse) {
                    if (brandsResponse.getData().brands != null) {
                        for (Brand brand : brandsResponse.getData().brands) {
                            if (brand.id.equals(gymWrapper.brand_id())) {
                                gymWrapper.setBrand(brand);
                                gymDetailView.onManageBrand();
                            }
                        }
                    }
                }
            }, new NetWorkThrowable()));
    }

    void updatePermission() {

        RxRegiste(restRepository.getGet_api()
            .qcPermission(loginStatus.staff_id(), gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponsePermission>() {
                @Override public void call(QcResponsePermission qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse) && qcResponse.data.permissions != null) {
                        SerPermisAction.writePermiss(qcResponse.data.permissions);
                        getGymWelcome();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public void getGymWelcome() {
        spWelcome = gymUseCase.getGymWelcom(gymWrapper.id(), gymWrapper.model(), new Action1<QcResponseData<GymDetail>>() {
            @Override public void call(QcResponseData<GymDetail> qcResponseGymDetail) {
                if (ResponseConstant.checkSuccess(qcResponseGymDetail)) {
                    gymDetailView.onGymInfo(qcResponseGymDetail.data.gym);
                    gymDetailView.onSuperUser(qcResponseGymDetail.data.superuser);
                    if (qcResponseGymDetail.data.banners != null) gymDetailView.setBanner(qcResponseGymDetail.data.banners);
                    gymDetailView.setInfo(qcResponseGymDetail.data.stat);

                    gymDetailView.studentPreview(qcResponseGymDetail.data.welcome_url, qcResponseGymDetail.data.hint_url);
                    gymDetailView.setRecharge(qcResponseGymDetail.data.recharge);
                    gymDetailView.onSpecialPoint(qcResponseGymDetail.data.qingcheng_activity_count);
                    if (qcResponseGymDetail.data.gym.module_custom != null) {
                        try {
                            QCDbManager.insertFunction((List<String>) qcResponseGymDetail.data.gym.module_custom);
                        } catch (Exception e) {
                            QCDbManager.insertFunction(null);
                            Timber.d(e.getMessage());
                            CrashUtils.sendCrash(e);
                        }
                    }
                    App.gCanReload = true;
                } else {
                    gymDetailView.onFailed();
                }
            }
        });
    }

    public void quitGym() {
        RxRegiste(restRepository.getPost_api()
            .qcQuitGym(loginStatus.staff_id(), gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        gymDetailView.onQuitGym();
                    } else {
                        ToastUtils.show(qcResponse.getMsg());
                        gymDetailView.onFailed();
                    }
                    ;
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    gymDetailView.onFailed();
                }
            })

        );
    }
}
