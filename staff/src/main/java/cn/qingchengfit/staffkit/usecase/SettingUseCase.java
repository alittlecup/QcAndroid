package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.StaffResponse;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.FeedBackBody;
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
 * Created by Paper on 16/2/22 2016.
 */
public class SettingUseCase {

    private RestRepository restRepository;

    @Inject public SettingUseCase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }





    public Subscription report(FeedBackBody body, Action1<QcResponse> action1) {
      return restRepository.qcReport(body)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(action1);
    }



    public Subscription getSelfInfo(Action1<QcDataResponse<StaffResponse>> action1) {
        return restRepository.getGet_api()
            .qcGetSelfInfo(App.staffId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription fixSelfInfo(Staff staffbean, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcModifyStaffs(App.staffId, staffbean)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }
}
