package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.model.responese.QcResponseOption;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
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
    @Inject StaffRespository restRepository;

    @Inject public CardTypeUsecase(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    //    public Subscription queryCardTypeList(String brand_id, int type, Action1<QcResponseData<CardTpls>> action1) {
    //        String t = type == 0 ? null : Integer.toString(type);
    //        return restRepository.getStaffAllApi().qcGetCardTpls(App.staffId, brand_id, t).observeOn(AndroidSchedulers.mainThread())
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .subscribe(action1, new Action1<Throwable>() {
    //                    @Override
    //                    public void call(Throwable throwable) {
    //
    //                    }
    //                });
    //    }

    public Subscription queryCardTpl(String cardtpl_id, String gymid, String model, String brand_id, Action1<QcResponseOption> action1) {
        return restRepository.getStaffAllApi()
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
        return restRepository.getStaffAllApi()
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
    //        return restRepository.getStaffAllApi().qcCreateCardtpl(App.staffId, body, brandid, gymid, gymmodel)
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
    //        return restRepository.getStaffAllApi().qcUpdateCardtpl(App.staffId, body.id, b,gymid,gymmodel,brandid)
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
        return restRepository.getStaffAllApi()
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
    //        return  restRepository.getStaffAllApi().qc(App.staffId, gymid, model)
    //                .observeOn(AndroidSchedulers.mainThread())
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .subscribe(action1, new Action1<Throwable>() {
    //                    @Override
    //                    public void call(Throwable throwable) {
    //                    }
    //                });
    //    }
}
