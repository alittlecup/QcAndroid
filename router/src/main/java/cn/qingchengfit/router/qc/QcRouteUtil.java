package cn.qingchengfit.router.qc;

import android.app.Application;
import cn.qingchengfit.router.QC;

public final class QcRouteUtil {

  public static void init(Application application) {
    QC.init(application);
  }

  public static QcRouter setRouteOptions(RouteOptions routeOptions) {
    return new QcRouterImpl(routeOptions);
  }
}
