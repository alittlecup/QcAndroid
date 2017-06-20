package cn.qingchengfit.recruit.model;

import cn.qingchengfit.model.base.CityBean;
import java.util.List;

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
 * Created by Paper on 2017/6/17.
 */

public class ResumeIntents {
  public Integer status;
  public List<String> exp_jobs;
  public List<CityBean> exp_cities;
  public Integer min_salary;
  public Integer max_salary;

  private ResumeIntents(Builder builder) {
    status = builder.status;
    exp_jobs = builder.exp_jobs;
    exp_cities = builder.exp_cities;
    min_salary = builder.min_salary;
    max_salary = builder.max_salary;
  }

  public static final class Builder {
    private Integer status;
    private List<String> exp_jobs;
    private List<CityBean> exp_cities;
    private Integer min_salary;
    private Integer max_salary;

    public Builder() {
    }

    public Builder status(Integer val) {
      status = val;
      return this;
    }

    public Builder exp_jobs(List<String> val) {
      exp_jobs = val;
      return this;
    }

    public Builder exp_cities(List<CityBean> val) {
      exp_cities = val;
      return this;
    }

    public Builder min_salary(Integer val) {
      min_salary = val;
      return this;
    }

    public Builder max_salary(Integer val) {
      max_salary = val;
      return this;
    }

    public ResumeIntents build() {
      return new ResumeIntents(this);
    }
  }
}
