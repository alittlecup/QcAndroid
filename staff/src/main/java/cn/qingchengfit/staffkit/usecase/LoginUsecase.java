package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.responese.Login;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
import cn.qingchengfit.staffkit.usecase.bean.LoginBody;
import cn.qingchengfit.staffkit.usecase.bean.RegisteBody;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Paper on 16/1/18 2016.
 */
public class LoginUsecase {

    RestRepository restRepository;

    @Inject public LoginUsecase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public Observable<QcResponseData<Login>> login(LoginBody loginBody) {
        return restRepository.qcLogin(loginBody).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    public Observable<QcResponse> queryCode(GetCodeBody body) {
        return restRepository.qcQueryCode(body).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    public Observable<QcResponseData<Login>> registe(RegisteBody body) {
        return restRepository.qcRegiste(body).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }
}
