package cn.qingchengfit.staffkit.views.statement.glance;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.StatementGlance;
import cn.qingchengfit.model.responese.StatementGlanceResp;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import java.util.HashMap;
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
 * Created by Paper on 16/3/8 2016.
 */
public class GlancePresenter extends BasePresenter {

    @Inject StatementUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    private StatementGlanceView view;
    private Subscription saleSp;
    private Subscription classSp;
    private RestRepository restRepository;
    private Action1<QcResponseData<StatementGlanceResp>> actionSaleGlance = new Action1<QcResponseData<StatementGlanceResp>>() {
        @Override public void call(QcResponseData<StatementGlanceResp> qcResponseSaleGlance) {
            if (qcResponseSaleGlance.getStatus() == ResponseConstant.SUCCESS) {
                StatementGlance month = qcResponseSaleGlance.data.month;
                StatementGlance week = qcResponseSaleGlance.data.week;
                StatementGlance today = qcResponseSaleGlance.data.today;

                view.setData(TextUtils.concat("实收金额" + month.total_cost + "元").toString(),
                    TextUtils.concat("实收金额" + week.total_cost + "元").toString(),
                    TextUtils.concat("实收金额" + today.total_cost + "元").toString());
            } else {
                view.onFailed(qcResponseSaleGlance.getMsg());
            }
        }
    };
    private Action1<QcResponseData<StatementGlanceResp>> actionGlassGlance = new Action1<QcResponseData<StatementGlanceResp>>() {
        @Override public void call(QcResponseData<StatementGlanceResp> qcResponseReportGlance) {
            if (qcResponseReportGlance.getStatus() == ResponseConstant.SUCCESS) {
                StatementGlance month = qcResponseReportGlance.data.month;
                StatementGlance week = qcResponseReportGlance.data.week;
                StatementGlance today = qcResponseReportGlance.data.today;

                view.setData(TextUtils.concat(month.course_count + "", "节课程,服务", month.user_count + "", "人次").toString(),
                    TextUtils.concat(week.course_count + "", "节课程,服务", week.user_count + "", "人次").toString(),
                    TextUtils.concat(today.course_count + "", "节课程,服务", today.user_count + "", "人次").toString());
            } else {
                view.onFailed(qcResponseReportGlance.getMsg());
            }
        }
    };

    @Inject public GlancePresenter(StatementUsecase usecase, RestRepository restRepository) {
        this.restRepository = restRepository;
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (StatementGlanceView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        if (saleSp != null) saleSp.unsubscribe();
        if (classSp != null) classSp.unsubscribe();
        view = null;
    }

    public void querySaleGlance(String shopid) {
        if (TextUtils.isEmpty(shopid)) {
            if (gymWrapper.inBrand()) {
                saleSp = usecase.querySaleGlance(gymWrapper.brand_id(), null, null, actionSaleGlance);
            } else {
                saleSp = usecase.querySaleGlance(null, gymWrapper.id(), gymWrapper.model(), actionSaleGlance);
            }
        } else {
            saleSp = usecase.querySaleGlance(gymWrapper.brand_id(), shopid, new Action1<QcResponseData<StatementGlanceResp>>() {
                @Override public void call(QcResponseData<StatementGlanceResp> qcResponseSaleGlance) {
                    if (qcResponseSaleGlance.getStatus() == ResponseConstant.SUCCESS) {
                        StatementGlance month = qcResponseSaleGlance.data.month;
                        StatementGlance week = qcResponseSaleGlance.data.week;
                        StatementGlance today = qcResponseSaleGlance.data.today;

                        view.setData(TextUtils.concat("实收金额" + month.total_cost + "元").toString(),
                            TextUtils.concat("实收金额" + week.total_cost + "元").toString(),
                            TextUtils.concat("实收金额" + today.total_cost + "元").toString());
                    } else {
                        view.onFailed(qcResponseSaleGlance.getMsg());
                    }
                }
            });
        }
    }

    public void queryClassGlance(String shop_id) {
        if (TextUtils.isEmpty(shop_id)) {
            if (gymWrapper.inBrand()) {
                classSp = usecase.queryClassGlance(gymWrapper.brand_id(), null, null, actionGlassGlance);
            } else {
                classSp = usecase.queryClassGlance(null, gymWrapper.id(), gymWrapper.model(), actionGlassGlance);
            }
        } else {
            classSp = usecase.queryClassGlance(gymWrapper.brand_id(), shop_id, actionGlassGlance);
        }
    }

    public void querySignInGlance(String shop_id) {
        HashMap<String, Object> params = new HashMap<>();
        if (gymWrapper.inBrand()) {
            params.put("brand_id", gymWrapper.brand_id());
            if (!TextUtils.isEmpty(shop_id)) {
                params.put("shop_id", shop_id);
            }
        } else {
            params = gymWrapper.getParams();
        }

        RxRegiste(restRepository.getGet_api()
            .qcGetSigninGlance(loginStatus.staff_id(), params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<StatementGlanceResp>>() {
                @Override public void call(QcResponseData<StatementGlanceResp> qcResponseReportGlance) {
                    if (qcResponseReportGlance.getStatus() == ResponseConstant.SUCCESS) {
                        StatementGlance month = qcResponseReportGlance.data.month;
                        StatementGlance week = qcResponseReportGlance.data.week;
                        StatementGlance today = qcResponseReportGlance.data.today;

                        view.setData(TextUtils.concat("签到", month.total_count + "", "人次").toString(),
                            TextUtils.concat("签到", week.total_count + "", "人次").toString(),
                            TextUtils.concat("签到", today.total_count + "", "人次").toString());
                    } else {
                        view.onFailed(qcResponseReportGlance.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }
}
