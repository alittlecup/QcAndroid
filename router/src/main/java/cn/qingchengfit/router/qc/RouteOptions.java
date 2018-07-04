package cn.qingchengfit.router.qc;

import android.content.Context;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class RouteOptions {
  private String moduleName;
  private String actionName;
  private Map<String, Object> params = new HashMap<>();
  private WeakReference<Context> context;

  public RouteOptions(String moduleName) {
    this.moduleName = moduleName;
  }

  public RouteOptions setActionName(String actionName) {
    this.actionName = actionName;
    return this;
  }

  public RouteOptions addParam(String key, Object value) {
    params.put(key, value);
    return this;
  }

  public RouteOptions addParams(Map<String, Object> params) {
    this.params.putAll(params);
    return this;
  }

  public RouteOptions setContext(Context context) {
    if (context != null) {
      this.context = new WeakReference<>(context);
    }
    return this;
  }

  public String getModuleName() {
    return moduleName;
  }

  public String getActionName() {
    return actionName;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public Context getContext() {
    if (context != null) {
      Context context = this.context.get();
      if (context != null) {
        return context;
      }
    }
    return null;
  }
}
