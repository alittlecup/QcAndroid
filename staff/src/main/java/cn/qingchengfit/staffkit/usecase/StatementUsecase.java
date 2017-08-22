package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.responese.CardTpls;
import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.model.responese.QcResponseCards;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponseServiceDetial;
import cn.qingchengfit.model.responese.QcResponseStatementDetail;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.model.responese.Staffs;
import cn.qingchengfit.model.responese.StatementGlanceResp;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
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

    public Subscription queryClassGlance(String brand_id, String id, String model, Action1<QcResponseData<StatementGlanceResp>> action1) {
        return restRepository.getGet_api()
            .qcGetReportGlance(App.staffId, brand_id, null, id, model)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryClassGlance(String brand_id, String shopid, Action1<QcResponseData<StatementGlanceResp>> action1) {
        return restRepository.getGet_api()
            .qcGetReportGlance(App.staffId, brand_id, shopid, null, null)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription querySaleGlance(String brand_id, String id, String model, Action1<QcResponseData<StatementGlanceResp>> action1) {
        return restRepository.getGet_api()
            .qcGetSaleGlance(App.staffId, brand_id, null, id, model)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription querySaleGlance(String brand_id, String shopid, Action1<QcResponseData<StatementGlanceResp>> action1) {
        return restRepository.getGet_api()
            .qcGetSaleGlance(App.staffId, brand_id, shopid, null, null)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    //    public Observable<QcResponseSaleDetail> querySaleDetail(String start, String end, String shop_id, String card_tpl_id, String cards_extra, String seller_id, String type, String chargetype, String brand_id, String gymid, String gymmodel) {
    //        return restRepository.getGet_api().qcGetSaleDatail(App.staffId, start, end, shop_id, card_tpl_id, cards_extra, seller_id, type, chargetype, brand_id, gymid, gymmodel)
    //                .observeOn(AndroidSchedulers.mainThread())
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io());
    //    }

    public Observable<QcResponseStatementDetail> queryStatementDetail(String start, String end, String teacherid, String course_id,
        String course_extra, String shop_id, String brand_id, String gymid, String gymmod) {
        return restRepository.getGet_api()
            .qcGetStatementDatail(App.staffId, start, end, teacherid, course_id, course_extra, shop_id, brand_id, gymid, gymmod)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io());
    }

    public Subscription queryResponseDetail(String id, String model, Action1<QcResponseServiceDetial> action1) {
        return restRepository.getGet_api()
            .qcGetServiceDetail(model, id)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())

            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryCards(Action1<QcResponseCards> action1) {
        return restRepository.getGet_api()
            .qcGetSaleCard(App.staffId)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryCardTypeList(String brand_id, int type, Action1<QcResponseData<CardTpls>> action1) {
        String t = type == 0 ? null : Integer.toString(type);
        HashMap<String, Object> params = new HashMap<>();
        params.put("brand_id", brand_id);
        return restRepository.getGet_api()
            .qcGetCardTpls(App.staffId, params, t, "1")
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryGymCardTpl(String gymid, String model, int type, Action1<QcResponseData<GymCardtpl>> action1) {
        String t = type == 0 ? null : Integer.toString(type);
        return restRepository.getGet_api()
            .qcGetGymCardtpl(App.staffId, gymid, model, t)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            });
    }

    public Subscription queryCourse(String brand_id, String gymid, String gymmodel, Action1<QcResponseData<CourseTypeSamples>> action1) {
        return restRepository.getGet_api()
            .qcGetAllCourses(App.staffId, brand_id, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryCoach(String brand_id, String gymid, String gymmodel, Action1<QcResponseData<Staffs>> action1) {
        return restRepository.getGet_api()
            .qcGetAllCoaches(App.staffId, brand_id, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetError());
    }

    public Subscription querySalers(String brandid, String shopid, String gymid, String gymmodel,
        Action1<QcResponseData<Sellers>> action1) {
        return restRepository.getGet_api()
            .qcGetSalersAndCoach(App.staffId, brandid, shopid, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetError());
    }
}
