package cn.qingchengfit.network;

import cn.qingchengfit.network.response.QcDataResponse;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

/**
 * Created by yangming on 16/11/16.
 */

public class HttpUtil {

    /**
     * 构造方法私有
     */
    private HttpUtil() {
    }

    /**
     * 获取单例
     */
    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //添加线程管理并订阅
    public Subscription toSubscribe(Observable ob, final ResultSubscribe subscriber) {
        //数据预处理
        Observable.Transformer<QcDataResponse<Object>, Object> result = RxHelper.handleResult();
        //重用操作符
        Observable observable = ob.compose(result).doOnSubscribe(new Action0() {
            @Override public void call() {
                //显示Dialog和一些其他操作
            }
        });
        // 订阅
        return observable.subscribe(subscriber);
    }

    //添加线程管理并订阅--带生命周期管理
    public Subscription toSubscribe(Observable ob, final ResultSubscribe subscriber, final ActivityLifeCycleEvent event,
        final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        //数据预处理
        Observable.Transformer<QcDataResponse<Object>, Object> result = RxHelper.handleResult(event, lifecycleSubject);
        //重用操作符
        Observable observable = ob.compose(result).doOnSubscribe(new Action0() {
            @Override public void call() {
                //显示Dialog和一些其他操作
            }
        });
        // 订阅
        return observable.subscribe(subscriber);
    }

    /**
     * 在访问HttpUtil时创建单例
     */
    private static class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }
}
