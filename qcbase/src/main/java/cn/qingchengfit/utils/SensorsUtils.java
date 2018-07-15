package cn.qingchengfit.utils;

import android.content.Context;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by Paper on 2016/12/13.
 */

public class SensorsUtils {

  public static class TrackBuilder{
    String key;
    private JSONObject jsonObject = new JSONObject();

    TrackBuilder(String key) {
      this.key = key;
    }
    public TrackBuilder addProperty(String key,String value){
      try {
        jsonObject.put(key,value);
      } catch (JSONException e) {

      }
      return this;
    }

    public TrackBuilder addProperty(String key, Number value) {
      try {
        jsonObject.put(key, value);
      } catch (JSONException e) {

      }
      return this;
    }

    public TrackBuilder addProperty(String key, boolean value) {
      try {
        jsonObject.put(key, value);
      } catch (JSONException e) {

      }
      return this;
    }


    public void commit(Context context){
      try {
        if (jsonObject.length() > 0){
          SensorsDataAPI.sharedInstance(context).track(key,jsonObject);
        }else {
          SensorsDataAPI.sharedInstance(context).track(key);
        }
      } catch (Exception e) {

      }

    }


  }

  public static TrackBuilder track(String key){
    return new TrackBuilder(key);
  }



  public static void track(String key, String json, Context context) {
    try {
      if (json == null) {
        SensorsDataAPI.sharedInstance(context).track(key);
      } else {
        JSONObject properties = new JSONObject(json);
        SensorsDataAPI.sharedInstance(context).track(key, properties);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (Exception e) {

    }
  }
}
