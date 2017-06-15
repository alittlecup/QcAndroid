package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.body.AddDayOffBody;
import cn.qingchengfit.model.body.FixCard;
import cn.qingchengfit.model.responese.Cards;
import cn.qingchengfit.model.responese.DayOffs;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponseRealcardHistory;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.HashMap;
import javax.inject.Inject;
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
 * Created by Paper on 16/3/17 2016.
 */
public class RealCardUsecase {
    @Inject RestRepository restRepository;

    @Inject public RealCardUsecase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public Subscription getAllCards(String brand_id, String gymid, String gymmoel, int page, String keyword, HashMap<String, Object> filter,
        Action1<QcResponseData<Cards>> action1) {
        return restRepository.getGet_api()
            .qcGetBrandCard(App.staffId, brand_id, gymid, gymmoel, page, keyword, filter)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription getBalanceCard(String brand_id, String gymid, String gymmoel, int page, String keyword,
        HashMap<String, Object> filter, Action1<QcResponseData<Cards>> action1) {
        return restRepository.getGet_api()
            .qcGetBalanceCard(App.staffId, brand_id, gymid, gymmoel, page, keyword, filter)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription getDayOffList(String card_id, String brandid, String gymid, String gymModel,
        Action1<QcResponseData<DayOffs>> action1) {
        return restRepository.getGet_api()
            .qcGetDayOff(App.staffId, brandid, card_id, gymid, gymModel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription unRegisteRealCard(String cardid, String brandid, String gymid, String gymmodel, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcUnregisteCard(App.staffId, cardid, brandid, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription createDayOff(String brandid, String gymid, String gymmodel, AddDayOffBody body, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcAddDayOff(App.staffId, brandid, gymid, gymmodel, body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription cancelDayOff(String leave_id, String brandid, String gymid, String gymmodel, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcDelDayOff(App.staffId, leave_id, brandid, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription getRealCardHistory(String cardid, String brand_id, String gymid, String gymmodel, String start, String end,
        int page, Action1<QcResponseRealcardHistory> action1) {
        return restRepository.getGet_api()
            .qcGetCardhistory(App.staffId, cardid, brand_id, gymid, gymmodel, start, end, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            });
    }

    public Subscription resumeCard(String cardid, String brandid, String gymid, String gymmodel, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcResumeCard(App.staffId, cardid, brandid, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            });
    }

    public Subscription updateCard(String cardid, String brandid, String gymid, String gymmodel, FixCard card,
        Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcUndateCard(App.staffId, cardid, brandid, gymid, gymmodel, card)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            });
    }
}
