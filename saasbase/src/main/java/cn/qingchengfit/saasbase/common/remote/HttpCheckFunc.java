package cn.qingchengfit.saasbase.common.remote;

import cn.qingchengfit.network.response.QcDataResponse;
import rx.functions.Func1;

/**
 * Created by huangbaole on 2017/11/15.
 */

public class HttpCheckFunc<T> implements Func1<QcDataResponse<T>, T> {

    @Override
    public T call(QcDataResponse<T> tQcDataResponse) {
        if(tQcDataResponse.status!=200){
            throw new HttpException(tQcDataResponse.getMsg(),0);
        }
        return tQcDataResponse.getData();
    }
}
