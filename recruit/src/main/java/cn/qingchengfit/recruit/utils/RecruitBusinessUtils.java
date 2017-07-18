package cn.qingchengfit.recruit.utils;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.Constants;
import cn.qingchengfit.model.base.CityBean;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.model.TeacherImpression;
import java.util.ArrayList;
import java.util.HashMap;
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

  public static HashMap<String, Object> getWrokExpParams(int pos, HashMap<String, Object> params) {
    switch (pos) {
      case 0:
        params.put("min_work_year", 0);
        params.put("max_work_year", 0);
        break;
      case 1:
        params.put("min_work_year", 1);
        params.put("max_work_year", 3);
        break;
      case 2:
        params.put("min_work_year", 3);
        params.put("max_work_year", 5);
        break;
      case 3:
        params.put("min_work_year", 5);
        params.put("max_work_year", 8);
        break;
      case 4:
        params.put("min_work_year", 8);
        params.put("max_work_year", 10);
        break;
      case 5:
        params.put("min_work_year", 10);
        params.put("max_work_year", -1);
        break;
      case 6:
        params.put("min_work_year", -1);
        params.put("max_work_year", -1);
        break;
      default:
        params.put("min_work_year", null);
        params.put("max_work_year", null);
        break;
    }
    return params;
  }

  public static HashMap<String, Object> getSalaryFilter(int position,
      HashMap<String, Object> params) {
    switch (position) {
      case 0:
        params.put("min_salary", null);
        params.put("max_salary", null);
        break;
      case 1:
        params.put("min_salary", -1);
        params.put("max_salary", -1);
        break;
      case 2:
        params.put("min_salary", -1);
        params.put("max_salary", 5000);
        break;
      case 3:
        params.put("min_salary", 5000);
        params.put("max_salary", 10000);
        break;
      case 4:
        params.put("min_salary", 10000);
        params.put("max_salary", 15000);
        break;
      case 5:
        params.put("min_salary", 15000);
        params.put("max_salary", 20000);
        break;
      case 6:
        params.put("min_salary", 20000);
        params.put("max_salary", 50000);
        break;
      case 7:
        params.put("min_salary", 50000);
        params.put("max_salary", -1);
        break;
      default:
        params.put("min_salary", null);
        params.put("max_salary", null);
        break;
    }
    return params;
  }

  public static HashMap<String, Object> getGenderParams(int pos, HashMap<String, Object> params) {
    switch (pos) {
      case 0:
        params.put("gender", -1);
        break;
      case 1:
        params.put("gender", 0);
        break;
      case 2:
        params.put("gender", 1);
        break;
      default:
        params.put("gender", null);
        break;
    }
    return params;
  }

  public static HashMap<String, Object> getDegreeParams(int pos, HashMap<String, Object> params) {
    if (pos == -1) {
      params.put("education", null);
    } else if (pos < 7) {
      params.put("education", pos + 1);
    } else {
      params.put("education", -1);
    }
    return params;
  }

  public static HashMap<String, Object> getAgeParams(int pos, HashMap<String, Object> params) {
    switch (pos) {
      case 0:
        params.put("min_age", -1);
        params.put("max_age", 20);
        break;
      case 1:
        params.put("min_age", 20);
        params.put("max_age", 25);
        break;
      case 2:
        params.put("min_age", 25);
        params.put("max_age", 30);
        break;
      case 3:
        params.put("min_age", 30);
        params.put("max_age", 40);
        break;
      case 4:
        params.put("min_age", 40);
        params.put("max_age", -1);
        break;
      case 5:
        params.put("min_age", -1);
        params.put("max_age", -1);
        break;
      default:
        params.put("min_age", null);
        params.put("max_age", null);
        break;
    }
    return params;
  }

  public static HashMap<String, Object> getHeightParams(int pos, HashMap<String, Object> params) {
    switch (pos) {
      case 0:
        params.put("min_height", -1);
        params.put("max_height", 160);
        break;
      case 1:
        params.put("min_height", 160);
        params.put("max_height", 170);
        break;
      case 2:
        params.put("min_height", 170);
        params.put("max_height", 180);
        break;
      case 3:
        params.put("min_height", 180);
        params.put("max_height", 190);
        break;
      case 4:
        params.put("min_height", 190);
        params.put("max_height", -1);
        break;
      case 5:
        params.put("min_height", -1);
        params.put("max_height", -1);
        break;
      default:
        params.put("min_height", null);
        params.put("max_height", null);
        break;
    }
    return params;
  }

  public static HashMap<String, Object> getWorkYearParams(int i, HashMap<String, Object> params) {
    switch (i) {
      case 0:
        params.put("min_work_year", 0);
        params.put("max_work_year", 0);
        break;
      case 1:
        params.put("min_work_year", 1);
        params.put("max_work_year", 3);
        break;
      case 2:
        params.put("min_work_year", 3);
        params.put("max_work_year", 5);
        break;
      case 3:
        params.put("min_work_year", 5);
        params.put("max_work_year", 8);
        break;
      case 4:
        params.put("min_work_year", 8);
        params.put("max_work_year", 10);
        break;
      case 5:
        params.put("min_work_year", 10);
        params.put("max_work_year", -1);
        break;
      default:
        params.put("min_work_year", null);
        params.put("max_work_year", null);
        break;
    }
    return params;
  }

  public static HashMap<String, Object> getWeightParams(int pos, HashMap<String, Object> params) {
    switch (pos) {
      case 0:
        params.put("min_weight", -1);
        params.put("max_weight", 50);
        break;
      case 1:
        params.put("min_weight", 50);
        params.put("max_weight", 60);
        break;
      case 2:
        params.put("min_weight", 60);
        params.put("max_weight", 70);
        break;
      case 3:
        params.put("min_weight", 70);
        params.put("max_weight", 80);
        break;
      case 4:
        params.put("min_weight", 80);
        params.put("max_weight", -1);
        break;
      case 5:
        params.put("min_weight", -1);
        params.put("max_weight", -1);
        break;
      default:
        params.put("min_weight", null);
        params.put("max_weight", null);
        break;
    }
    return params;
  }

  public static String getSalary(Integer min, Integer max) {
    return getSalary(min, max, "不限");
  }

  public static String getSalary(Integer min, Integer max, String replace) {
    if (min == null || max == null) {
      return "";
    }
    if (min < 0 && max <= 0) {
      return replace;
    } else if (min == 0 && max == 0) {
      return replace;
    } else if (min < 0) {
      return "<" + getMoney(max);
    } else if (max < 0) {
      return ">" + getMoney(min);
    } else {
      return getMoney(min).replace("K", "") + "-" + getMoney(max);
    }
  }

  public static String getDegree(Context context, int x) {
    if (x < 0) return "不限";
    String[] degrees = context.getResources().getStringArray(R.array.add_resume_education_degree);
    if (x == 0) return "";
    x--;
    if (x >= 0 && x < degrees.length) {
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
    } else if (m == 1000) {
      return "1K";
    } else {
      return String.format(Locale.CHINA, "%.1f", m);
    }
  }

  /**
   * 获取年龄限制
   */
  public static String getGender(int gender) {
    return getGender(gender, "");
  }

  public static String getGender(int gender, String plus) {
    switch (gender) {
      case 0:
        return "男性";
      case 1:
        return "女性";
      default:
        return plus + "不限";
    }
  }

  /**
   * 获取工作年龄限制
   */
  public static String getWorkYear(int min, int max) {
    return getWorkYear(min, max, "");
  }

  public static String getWorkYear(int min, int max, String plus) {
    if (min == -1 && max == -1) {
      return plus + "不限";
    } else if (min == 0 && max == 0) {
      return "应届生";
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
    return getAge(min, max, "");
  }

  public static String getAge(int min, int max, String plus) {
    if (min == -1 && max == -1) {
      return plus + "不限";
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
  public static String getHeight(Float min, Float max) {
    return getHeight(min, max, "");
  }

  public static String getHeight(Float min, Float max, String plus) {
    if (min == -1 && max == -1) {
      return plus + "不限";
    } else if (min == -1) {
      return "<" + max.intValue() + "cm";
    } else if (max == -1) {
      return ">" + min.intValue() + "cm";
    } else {
      return min.intValue() + "-" + max.intValue() + "cm";
    }
  }

  public static String getWeight(float min, float max) {
    return getWeight(min, max, "");
  }

  public static String getWeight(float min, float max, String plus) {
    if (min == -1 && max == -1) {
      return plus + "不限";
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

  public static String getHotJob(List<Job> jobs) {
    String ret = "";
    for (Job cityBean : jobs) {
      ret = TextUtils.concat(ret, cityBean.name, Constants.SEPARATE).toString();
    }
    if (ret.endsWith(Constants.SEPARATE)) ret = ret.substring(0, ret.length() - 1);
    if (ret.length() > 50) ret = ret.substring(0, 50) + "...";
    return ret;
  }
  //
  //public static String getResumeInfo(Resume resume,Context context){
  //  String workYear = resume.work_year == 0 ?"应届生":resume.work_year+"年经验";
  //  String birthDay = DateUtils.getAge(DateUtils.formatDateFromServer(resume.birthday))+"岁";
  //  String height = CmStringUtils.getMaybeInt(resume.height)+"cm";
  //  String weight = CmStringUtils.getMaybeInt(resume.weight)+"kg";
  //  String degree = getDegree(context,resume.max_education);
  //  String seperate = " / ";
  //  return TextUtils.concat(workYear,seperate,birthDay,seperate,height,","+weight,seperate,degree).toString();
  //}
  //

  public static String getPositionDamen(List<String> damens) {
    StringBuilder sb = new StringBuilder();

    if (damens.size() == 1) {
      sb.append(damens.get(0));
    } else {
      int index = 0;
      for (String str : damens) {
        if (index == 0) {
          sb.append(str);
        } else {
          sb.append("/" + str);
        }
        index++;
      }
    }
    return sb.toString();
  }
}
