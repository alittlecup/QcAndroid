package cn.qingchengfit.network;

import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import io.reactivex.functions.Consumer;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import retrofit2.HttpException;
import rx.functions.Action1;

public class HttpThrowable implements Action1<Throwable>, Consumer<Throwable> {

  @Override public void call(Throwable throwable) {
    process(throwable);
  }

  @Override public void accept(Throwable throwable) throws Exception {
    process(throwable);
  }

  private void process(Throwable throwable) {
    if (throwable instanceof SocketException
        || throwable instanceof HttpException
        || throwable instanceof SocketTimeoutException
        || throwable instanceof UnknownHostException) {
      ToastUtils.show("网络连接异常");
    } else {
      LogUtil.d(throwable.getMessage());
    }
  }
}
