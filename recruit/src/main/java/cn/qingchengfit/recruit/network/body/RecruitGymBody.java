package cn.qingchengfit.recruit.network.body;

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
 * Created by Paper on 2017/6/28.
 */

public class RecruitGymBody {
  public Integer staff_count;
  public Integer member_count;
  public Integer coach_count;
  public String area;
  public String detail_description;
  public List<String> facilities;

  private RecruitGymBody(Builder builder) {
    staff_count = builder.staff_count;
    member_count = builder.member_count;
    coach_count = builder.coach_count;
    area = builder.area;
    detail_description = builder.detail_description;
    facilities = builder.facilities;
  }

  public static final class Builder {
    private Integer staff_count;
    private Integer member_count;
    private Integer coach_count;
    private String area;
    private String detail_description;
    private List<String> facilities;

    public Builder() {
    }

    public Builder staff_count(Integer val) {
      staff_count = val;
      return this;
    }

    public Builder member_count(Integer val) {
      member_count = val;
      return this;
    }

    public Builder coach_count(Integer val) {
      coach_count = val;
      return this;
    }

    public Builder area(String val) {
      area = val;
      return this;
    }

    public Builder detail_description(String val) {
      detail_description = val;
      return this;
    }

    public Builder facilities(List<String> val) {
      facilities = val;
      return this;
    }

    public RecruitGymBody build() {
      return new RecruitGymBody(this);
    }
  }
}
