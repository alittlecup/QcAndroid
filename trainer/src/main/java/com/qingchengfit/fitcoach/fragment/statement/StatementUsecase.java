package com.qingchengfit.fitcoach.fragment.statement;

import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.report.bean.GymCardtpl;
import cn.qingchengfit.saasbase.report.bean.QcResponseStatementDetail;
import cn.qingchengfit.saasbase.report.bean.StatementGlanceResp;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.fragment.statement.model.Sellers;
import com.qingchengfit.fitcoach.http.RestRepository;
import java.util.HashMap;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
public class StatementUsecase {

    @Inject RestRepository restRepository;

    @Inject public StatementUsecase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public Subscription queryClassGlance(String brand_id, String id, String model, Action1<QcDataResponse<StatementGlanceResp>> action1) {
        return restRepository.getGet_api()
            .qcGetReportGlance(App.coachid, brand_id, null, id, model)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryClassGlance(String brand_id, String shopid, Action1<QcDataResponse<StatementGlanceResp>> action1) {
        return restRepository.getGet_api()
            .qcGetReportGlance(App.coachid, brand_id, shopid, null, null)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    //public Subscription querySaleGlance(String brand_id, String id, String model, Action1<QcResponseData<StatementGlanceResp>> action1) {
    //    return restRepository.getGet_api()
    //        .qcGetSaleGlance(App.coachid, brand_id, null, id, model)
    //        .observeOn(AndroidSchedulers.mainThread())
  //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //        .subscribe(action1, new Action1<Throwable>() {
    //            @Override public void call(Throwable throwable) {
    //
    //            }
    //        });
    //}

    public Observable<QcResponseStatementDetail> queryStatementDetail(String start, String end, HashMap<String, Object> params) {
        return restRepository.getGet_api()
            .qcGetStatementDatail(App.coachid, start, end, params)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io());
    }

    //public Subscription queryResponseDetail(String id, String model, Action1<QcResponseServiceDetial> action1) {
    //    return restRepository.getGet_api()
    //        .qcGetServiceDetail(model, id)
    //        .observeOn(AndroidSchedulers.mainThread())
  //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //
    //        .subscribe(action1, new Action1<Throwable>() {
    //            @Override public void call(Throwable throwable) {
    //
    //            }
    //        });
    //}
    //
    //public Subscription queryCards(Action1<QcResponseCards> action1) {
    //    return restRepository.getGet_api()
    //        .qcGetSaleCard(App.staffId)
    //        .observeOn(AndroidSchedulers.mainThread())
  //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //        .subscribe(action1, new Action1<Throwable>() {
    //            @Override public void call(Throwable throwable) {
    //
    //            }
    //        });

    public Subscription queryCardTypeList(String brand_id, int type, Action1<QcDataResponse<CardTplListWrap>> action1) {
        String t = type == 0 ? null : Integer.toString(type);
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", "10212");
        return restRepository.getGet_api()
            .qcGetCardTpls("7409", params, "staff_model", "true")
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryGymCardTpl(int type, Action1<QcDataResponse<GymCardtpl>> action1, HashMap<String, Object> params) {
        String t = type == 0 ? null : Integer.toString(type);
        return restRepository.getGet_api()
            .qcGetGymCardtpl(String.valueOf(App.coachid), params, t)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            });
    }

    //public Subscription queryGymCardTpl(String gymid, String model, int type, Action1<QcResponseData<GymCardtpl>> action1) {
    //    String t = type == 0 ? null : Integer.toString(type);
    //    return restRepository.getGet_api()
    //        .qcGetGymGymDetailCardtpl(App.coachid, gymid, model, t)
    //        .observeOn(AndroidSchedulers.mainThread())
  //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //        .subscribe(action1, new Action1<Throwable>() {
    //            @Override public void call(Throwable throwable) {
    //            }
    //        });
    //}

    //public Subscription queryCourse(String brand_id, String gymid, String gymmodel, Action1<QcResponseData<CourseTypeSamples>> action1) {
    //    return restRepository.getGet_api()
    //        .qcGetAllCourses(App.coachid, brand_id, gymid, gymmodel)
    //        .observeOn(AndroidSchedulers.mainThread())
  //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //        .subscribe(action1, new Action1<Throwable>() {
    //            @Override public void call(Throwable throwable) {
    //
    //            }
    //        });
    //}

    //public Subscription queryCoach(String brand_id, String gymid, String gymmodel, Action1<QcResponseData<Staffs>> action1) {
    //    return restRepository.getGet_api()
    //        .qcGetAllCoaches(App.staffId, brand_id, gymid, gymmodel)
    //        .observeOn(AndroidSchedulers.mainThread())
  //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //        .subscribe(action1, new NetError());
    //}

    public Subscription querySalers(String brandid, String gymid, String gymmodel, Action1<QcDataResponse<Sellers>> action1) {
        return restRepository.getGet_api()
            .qcGetSalersAndCoach(App.coachid, brandid, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }
}
