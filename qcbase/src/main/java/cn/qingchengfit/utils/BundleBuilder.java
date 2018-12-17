package cn.qingchengfit.utils;

import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/10/4.
 */

public class BundleBuilder {
  Bundle bundle;

  public BundleBuilder() {
    bundle = new Bundle();
  }

  public static BundleBuilder fromMap(Map<String, ? extends Object> params) {
    BundleBuilder bundleBuilder = new BundleBuilder();
    bundleBuilder.withMap(params);
    return bundleBuilder;
  }

  public BundleBuilder withByte(String key, byte value) {
    bundle.putByte(key, value);
    return this;
  }

  public BundleBuilder putChar(String key, char value) {
    bundle.putChar(key, value);
    return this;
  }

  public BundleBuilder withShort(String key, short value) {
    bundle.putShort(key, value);
    return this;
  }

  public BundleBuilder withFloat(String key, float value) {
    bundle.putFloat(key, value);
    return this;
  }

  public BundleBuilder putCharSequence(String key, CharSequence value) {
    bundle.putCharSequence(key, value);
    return this;
  }

  public BundleBuilder withParcelable(String key, Parcelable value) {
    bundle.putParcelable(key, value);
    return this;
  }

  public BundleBuilder withParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
    bundle.putParcelableArrayList(key, value);
    return this;
  }

  public BundleBuilder withParcelableArray(String key, Parcelable[] value) {
    bundle.putParcelableArray(key, value);
    return this;
  }

  public BundleBuilder withIntegerArrayList(String key, ArrayList<Integer> value) {
    bundle.putIntegerArrayList(key, value);
    return this;
  }

  public BundleBuilder withStringArrayList(String key, ArrayList<String> value) {
    bundle.putStringArrayList(key, value);
    return this;
  }

  public BundleBuilder withCharSequenceArrayList(String key, ArrayList<CharSequence> value) {
    bundle.putCharSequenceArrayList(key, value);
    return this;
  }

  public BundleBuilder withSerializable(String key, Serializable value) {
    bundle.putSerializable(key, value);
    return this;
  }

  public BundleBuilder withByteArray(String key, byte[] value) {
    bundle.putByteArray(key, value);
    return this;
  }

  public BundleBuilder withShortArray(String key, short[] value) {
    bundle.putShortArray(key, value);
    return this;
  }

  public BundleBuilder withCharArray(String key, char[] value) {
    bundle.putCharArray(key, value);
    return this;
  }

  public BundleBuilder withFloatArray(String key, float[] value) {
    bundle.putFloatArray(key, value);
    return this;
  }

  public BundleBuilder withCharSequenceArray(String key, CharSequence[] value) {
    bundle.putCharSequenceArray(key, value);
    return this;
  }

  public BundleBuilder withBundle(String key, Bundle value) {
    bundle.putBundle(key, value);
    return this;
  }

  public BundleBuilder withBundle(Bundle value) {
    bundle.putAll(value);
    return this;
  }

  public BundleBuilder withInt(String key, int value) {
    bundle.putInt(key, value);
    return this;
  }

  public BundleBuilder withIntArray(String key, int[] value) {
    bundle.putIntArray(key, value);
    return this;
  }

  public BundleBuilder withLong(String key, long value) {
    bundle.putLong(key, value);
    return this;
  }

  public BundleBuilder withBoolean(String key, boolean value) {
    bundle.putBoolean(key, value);
    return this;
  }

  public BundleBuilder withLongArray(String key, long[] value) {
    bundle.putLongArray(key, value);
    return this;
  }

  public BundleBuilder withString(String key, String value) {
    bundle.putString(key, value);
    return this;
  }

  public BundleBuilder withMap(Map<String, ? extends Object> value) {
    if (value != null && !value.isEmpty()) {
      for (String key : value.keySet()) {
        withObject(key, value.get(key));
      }
    }
    return this;
  }

  public BundleBuilder withObject(String key, Object value) {
    if (value instanceof Byte) {
      withByte(key, (Byte) value);
    } else if (value instanceof Short) {
      withShort(key, (Short) value);
    } else if (value instanceof Float) {
      withFloat(key, (Float) value);
    } else if (value instanceof Integer) {
      withInt(key, (Integer) value);
    } else if (value instanceof Character) {
      putChar(key, (Character) value);
    } else if (value instanceof String) {
      withString(key, (String) value);
    } else if (value instanceof Boolean) {
      withBoolean(key, (Boolean) value);
    } else if (value instanceof Long) {
      withLong(key, (Long) value);
    } else if (value instanceof Bundle) {
      withBundle(key, (Bundle) value);
    } else if (value instanceof CharSequence) {
      putCharSequence(key, (CharSequence) value);
    } else if (value instanceof Serializable) {
      withSerializable(key, (Serializable) value);
    } else if (value instanceof Parcelable) {
      withParcelable(key, (Parcelable) value);
    } else {
      throw new RuntimeException("please check " + value + "is right type  or use other function");
    }
    return this;
  }

  public Bundle build() {
    return bundle;
  }
}
