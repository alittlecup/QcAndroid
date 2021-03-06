package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.body.OptionBody;
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
 * Created by Paper on 16/4/25 2016.
 */
public class CardStandardUsecase {
    StaffRespository restRepository;

    @Inject public CardStandardUsecase(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    public Subscription createCardstandard(String cardtplid, String gymid, String gymmodel, String brand_id, OptionBody body,
        Action1<QcResponse> action1) {
        return restRepository.getStaffAllApi()
            .qcCreateStandard(App.staffId, cardtplid, gymid, gymmodel, brand_id, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1);
    }

    public Subscription updateCardstandard(String gymid, String gymmodel, String brand_id, String cardtpl_id, OptionBody body,
        Action1<QcResponse> action1) {
        return restRepository.getStaffAllApi()
            .qcUpdateCardStandard(App.staffId, cardtpl_id, gymid, gymmodel, brand_id, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1);
    }

    public Subscription DelCardstandard(String gymid, String gymmodel, String brand_id, String option_id, Action1<QcResponse> action1) {
        return restRepository.getStaffAllApi()
            .qcDelCardStandard(App.staffId, option_id, gymid, gymmodel, brand_id)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1);
    }
}
