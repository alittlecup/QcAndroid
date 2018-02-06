package cn.qingchengfit.saasbase.login.views;

import android.content.Context;
import cn.qingchengfit.di.CView;

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
 * Created by Paper on 15/11/19 2015.
 */
public interface LoginView extends CView {
    //正在登录
    void onShowLogining();

    //错误展示
    void onError(String msg);

    //取消登录
    void cancelLogin();
    Context getContext();

    /**
     * 用户状态
     *
     * @param status 0，全新用户 1已有场馆用户
     */
    void onSuccess(int status);
}
