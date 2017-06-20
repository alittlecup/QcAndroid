package cn.qingchengfit.recruit.utils;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.Constants;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.model.TeacherImpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
 * Created by Paper on 2017/5/26.
 */

public class RecruitBusinessUtils {

  public static String getSalary(float min, float max) {
    if (min == -1 && max == -1) {
      return "面谈";
    } else if (min == 0 && max == 0) {
      return "不限";
    } else if (min == 0) {
      return "<" + getMoney(max);
    } else if (max == 0) {
      return "<" + getMoney(min);
    } else {
      return getMoney(min).replace("K", "") + "-" + getMoney(max);
    }
  }

  public static String getDegree(Context context, int x) {
    if (x < 0) return "不限";
    String[] degrees = context.getResources().getStringArray(R.array.education_degree);
    if (x > 0 && x < degrees.length) {
      return degrees[x];
    } else {
      return "";
    }
  }

  public static String getMoney(float m) {
    if (m > 1000) {
      if (m % 1000 != 0) {
        return String.format(Locale.CHINA, "%.1fK", m / 1000);
      } else {
        return String.format(Locale.CHINA, "%.0fK", m / 1000);
      }
    } else {
      return String.format(Locale.CHINA, "%.1f", m);
    }
  }

  /**
   * 获取年龄限制
   */
  public static String getGender(int gender) {
    switch (gender) {
      case 0:
        return "男性";
      case 1:
        return "女性";
      default:
        return "不限";
    }
  }

  /**
   * 获取工作年龄限制
   */
  public static String getWorkYear(int min, int max) {
    if (min == -1 && max == -1) {
      return "不限";
    } else if (min == -1) {
      return "<" + max + "年";
    } else if (max == -1) {
      return ">" + min + "年";
    } else {
      return min + "-" + max + "年";
    }
  }

  /**
   * 获取年龄
   */
  public static String getAge(int min, int max) {
    if (min == -1 && max == -1) {
      return "不限";
    } else if (min == -1) {
      return "<" + max + "岁";
    } else if (max == -1) {
      return ">" + min + "岁";
    } else {
      return min + "-" + max + "岁";
    }
  }

  /**
   * 获取身高限制
   */
  public static String getHeight(float min, float max) {
    if (min == -1 && max == -1) {
      return "不限";
    } else if (min == -1) {
      return "<" + max + "cm";
    } else if (max == -1) {
      return ">" + min + "cm";
    } else {
      return min + "-" + max + "cm";
    }
  }

  public static String getWeight(float min, float max) {
    if (min == -1 && max == -1) {
      return "不限";
    } else if (min == -1) {
      return "<" + max + "kg";
    } else if (max == -1) {
      return ">" + min + "kg";
    } else {
      return min + "-" + max + "kg";
    }
  }

  public static List<String> impress2Str(List<TeacherImpression> impressions) {

    List<String> ret = new ArrayList<>();
    if (impressions == null) return ret;
    for (int i = 0; i < impressions.size(); i++) {
      ret.add(impressions.get(i).comment + "(" + impressions.get(i).count + ")");
    }
    return ret;
  }

  public static String getStrFromCities(List<CityBean> ct) {
    String ret = "";
    for (CityBean cityBean : ct) {
      ret = TextUtils.concat(ret, cityBean.name, Constants.SEPARATE).toString();
    }
    if (ret.endsWith(Constants.SEPARATE)) ret = ret.substring(0, ret.length() - 1);
    return ret;
  }

  public static List<String> getListStrFromCities(List<CityBean> ct) {
    List<String> ret = new ArrayList<>();
    for (CityBean cityBean : ct) {
      ret.add(cityBean.getName());
    }
    return ret;
  }

  public static List<String> getIdsFromCities(List<CityBean> ct) {
    List<String> ret = new ArrayList<>();
    for (CityBean cityBean : ct) {
      ret.add(cityBean.id + "");
    }
    return ret;
  }
}
