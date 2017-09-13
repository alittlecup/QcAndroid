package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.model.responese.QcResponseOption;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
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
 * Created by Paper on 16/3/16 2016.
 */
public class CardTypeUsecase {
    @Inject RestRepository restRepository;

    @Inject public CardTypeUsecase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    //    public Subscription queryCardTypeList(String brand_id, int type, Action1<QcResponseData<CardTpls>> action1) {
    //        String t = type == 0 ? null : Integer.toString(type);
    //        return restRepository.getGet_api().qcGetCardTpls(App.staffId, brand_id, t).observeOn(AndroidSchedulers.mainThread())
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .subscribe(action1, new Action1<Throwable>() {
    //                    @Override
    //                    public void call(Throwable throwable) {
    //
    //                    }
    //                });
    //    }

    public Subscription queryCardTpl(String cardtpl_id, String gymid, String model, String brand_id, Action1<QcResponseOption> action1) {
        return restRepository.getGet_api()
            .qcGetOptions(App.staffId, cardtpl_id, gymid, model, brand_id)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryGymCardTpl(String gymid, String model, int type, Action1<QcDataResponse<GymCardtpl>> action1) {
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

    //    public Subscription createCardTpl(CardtplBody body, String brandid, String gymid, String gymmodel, Action1<QcResponse> action1) {
    //        return restRepository.getPost_api().qcCreateCardtpl(App.staffId, body, brandid, gymid, gymmodel)
    //                .observeOn(AndroidSchedulers.mainThread())
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .subscribe(action1, new Action1<Throwable>() {
    //                    @Override
    //                    public void call(Throwable throwable) {
    //                    }
    //                });
    //    }

    /**
     * 更新卡模板
     *
     * @param body
     * @param action1
     * @return
     */
    //    public Subscription updateCardTpl(String gymid,String gymmodel,String brandid,CardtplBody body, Action1<QcResponse> action1) {
    //        CardtplBody b = body.clone();
    //        b.id = null;
    //        return restRepository.getPost_api().qcUpdateCardtpl(App.staffId, body.id, b,gymid,gymmodel,brandid)
    //                .observeOn(AndroidSchedulers.mainThread())
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .subscribe(action1, new Action1<Throwable>() {
    //                    @Override
    //                    public void call(Throwable throwable) {
    //                    }
    //                });
    //    }

    /**
     * 删除卡模板
     */
    public Subscription delCardTpl(String id, String gymid, String gymmodel, String brand_id, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcDelCardtpl(App.staffId, id, gymid, gymmodel, brand_id)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            });
    }

    //public Subscription addCardTplStandard(String gymid,String model,Action1<QcResponse> action1){
    //        return  restRepository.getGet_api().qc(App.staffId, gymid, model)
    //                .observeOn(AndroidSchedulers.mainThread())
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .subscribe(action1, new Action1<Throwable>() {
    //                    @Override
    //                    public void call(Throwable throwable) {
    //                    }
    //                });
    //    }
}
