package cn.qingchengfit.staffkit.train.presenter;

import cn.qingchengfit.staffkit.mvpbase.PView;

/**
 * Created by fb on 2017/3/22.
 */

public interface SignUpView<T> extends PView {

    void onGetSignUpDataSuccess(T data);

    void onFailed(String msg);

    void onSuccess();

    void onDelSuccess();
}
