package com.qingchengfit.fitcoach.fragment;

import com.qingchengfit.fitcoach.http.bean.GetCodeBean;

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
 * Created by Paper on 15/6/28 2015.
 */
public abstract class LoginPresenter {

    public abstract void onPhoneDone();

    /**
     * 登录
     */
    public abstract void doLogin(String arCode, String account, String code);

    public abstract void getCode(GetCodeBean getCodeBean);

    public abstract void goRegister();
}
