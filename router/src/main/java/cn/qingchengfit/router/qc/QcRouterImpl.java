package cn.qingchengfit.router.qc;

import cn.qingchengfit.router.IComponentCallback;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;

public class QcRouterImpl implements QcRouter {
  private RouteOptions routeOptions;

  public QcRouterImpl(RouteOptions routeOptions) {
    this.routeOptions = routeOptions;
  }

  @Override public QCResult call() {
    return QC.obtainBuilder(routeOptions.getModuleName())
        .setActionName(routeOptions.getActionName())
        .setContext(routeOptions.getContext())
        .addParams(routeOptions.getParams())
        .build()
        .call();
  }

  @Override public void callAsync() {
    QC.obtainBuilder(routeOptions.getModuleName())
        .setActionName(routeOptions.getActionName())
        .setContext(routeOptions.getContext())
        .addParams(routeOptions.getParams())
        .build()
        .callAsync();
  }

  @Override public void callAsync(final IQcRouteCallback callback) {
    QC.obtainBuilder(routeOptions.getModuleName())
        .setActionName(routeOptions.getActionName())
        .setContext(routeOptions.getContext())
        .addParams(routeOptions.getParams())
        .build()
        .callAsync(new IComponentCallback() {
          @Override public void onResult(QC qc, QCResult result) {
            callback.onResult(result);
          }
        });
  }
}
