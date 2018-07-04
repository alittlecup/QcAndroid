package cn.qingchengfit.router.qc;

import cn.qingchengfit.router.QCResult;

public interface QcRouter {

  QCResult call();

  void callAsync();

  void callAsync(IQcRouteCallback callback);
}
