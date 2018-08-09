package cn.qingchengfit.saascommon.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentModuleManager {

  private static final ConcurrentHashMap<String,Object> COMPONENTS_MODULE =
      new ConcurrentHashMap<>();

  public static <T> void register(@NonNull Class<T> IModuleClass, @NonNull T moduleImpl) {
    COMPONENTS_MODULE.put(IModuleClass.getName(), moduleImpl);
  }

  public static <T> void unresister(@NonNull Class<T> IModuleClass) {
    COMPONENTS_MODULE.remove(IModuleClass.getName());
  }

  public static <T> T get(Class<T> IModuleClass) {
    Object object = COMPONENTS_MODULE.get(IModuleClass.getName());
    if(IModuleClass.isAssignableFrom(object.getClass())){
      return (T) object;
    }
    try {
     return IModuleClass.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
