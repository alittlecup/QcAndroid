/*
 * Copyright 2017 Davide Steduto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.qingchengfit.saascommon.flexble;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.reflect.Constructor;

public class FlexibleFactory {

  private FlexibleFactory() {
  }

  /**
   * Creates a new instance of the given {@code Class} with optional constructor parameters.
   *
   * @param itemClass a {@code Class} whose instance is requested
   * @param constructorParams the constructor parameters of the class to create
   * @return a newly created instance of type {@code T}.
   */
  @SuppressWarnings("ConstantConditions") public static <T> T create(@NonNull Class<T> itemClass,
      @Nullable Object... constructorParams) {
    if (itemClass == null) {
      throw new IllegalArgumentException("Unknown model class " + itemClass);
    }
    try {
      if (constructorParams == null || constructorParams.length == 0) {
        return itemClass.getConstructor().newInstance();
      } else {
        Class[] classes = new Class[constructorParams.length];
        for (int i = 0; i < constructorParams.length; i++) {
          classes[i] = constructorParams[i].getClass();
        }
        return itemClass.getConstructor(classes).newInstance(constructorParams);
      }
    } catch (NoSuchMethodException e) {
      Class[] classes = new Class[constructorParams.length];
      Constructor<?>[] constructors = itemClass.getConstructors();
      for (Constructor constructor : constructors) {
        if (constructor.getParameterTypes().length == constructorParams.length) {
          Class[] parameterTypes = constructor.getParameterTypes();
          for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].isAssignableFrom(constructorParams[i].getClass())) {
              classes[i] = parameterTypes[i];
            } else {
              throw new RuntimeException(e.getMessage()
                  + " "
                  + itemClass.getCanonicalName()
                  + ".<init>() has mismatching Constructor param");
            }
          }
        } else {
          throw new RuntimeException(e.getMessage()
              + " "
              + itemClass.getCanonicalName()
              + ".<init>() has mismatching Constructor param");
        }
      }
      try {
        return itemClass.getConstructor(classes).newInstance(constructorParams);
      } catch (NoSuchMethodException e1) {
        throw new RuntimeException(e.getMessage()
            + " "
            + itemClass.getCanonicalName()
            + ".<init>() has mismatching Constructor param");
      } catch (Exception e1) {
        throw new RuntimeException(e);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}