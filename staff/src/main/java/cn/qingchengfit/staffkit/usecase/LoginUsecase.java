package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.responese.Login;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.constant.Post_Api;
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

    @Inject QcRestRepository restRepository;

    @Inject public LoginUsecase() {
    }

    public Observable<QcDataResponse<Login>> login(LoginBody loginBody) {
      return restRepository.createPostApi(Post_Api.class).qcLogin(loginBody)
          .observeOn(AndroidSchedulers.mainThread())
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io());
    }

    public Observable<QcResponse> queryCode(GetCodeBody body) {
      return restRepository.createPostApi(Post_Api.class).qcGetCode(body)
          .observeOn(AndroidSchedulers.mainThread())
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io());
    }

    public Observable<QcDataResponse<Login>> registe(RegisteBody body) {
      return restRepository.createPostApi(Post_Api.class).qcRegister(body)
          .observeOn(AndroidSchedulers.mainThread())
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io());
    }
}
