package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.saasbase.login.bean.Login;
import cn.qingchengfit.saasbase.login.bean.LoginBody;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import rx.Observable;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/12/2 2015.
 */
public interface Repository {

    Observable<QcResponseData<Login>> qcLogin(LoginBody loginBody);
}
