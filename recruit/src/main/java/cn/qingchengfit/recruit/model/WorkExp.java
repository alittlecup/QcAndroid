package cn.qingchengfit.recruit.model;

import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.recruit.utils.RecruitBusinessUtils;
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
 * Created by Paper on 2017/6/12.
 */

public class WorkExp implements Cloneable {
    /**
     * coach : {"id":6}
     * description : 无
     * is_authenticated : false
     * position : 教练
     * private_course : 421
     * city : 北京
     * end : 2015-11-16T15:29:00
     * username : 中美引力工作室
     * group_user : 123
     * sale : 10000000
     * start : 2015-09-16T15:29:00
     * group_course : 100
     * private_user : 568
     */

    public Staff coach;
    public String description;
    public boolean is_authenticated;
    public String position;
    public Integer private_course;
    public String end;
    public String name;
    public String id;
    public Integer group_user;
    public Integer sale;
    public String start;
    public float coach_score;
    public float course_score;
    public Integer group_course;
    public Integer private_user;
    public Gym gym;
    public boolean is_hidden;
    public boolean group_is_hidden;
    public boolean private_is_hidden;
    public boolean sale_is_hidden;
    public List<TeacherImpression> impression;

    public List<String> getImpressList() {
        return RecruitBusinessUtils.impress2Str(impression);
    }

    public WorkExp getPostBody() throws CloneNotSupportedException {
        WorkExp exp = (WorkExp) this.clone();
        exp.id = null;
        return exp;
    }
}
