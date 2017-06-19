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
 * Created by Paper on 2017/6/13.
 *
 * "resume": {
 * "status": 1,
 * "weight": 0,
 * "exp_jobs": [],
 * "height": 0,
 * "exp_cities": [],
 * "gd_district": {},
 * "id": "XQZDaw4k",
 * "min_salary": -1,
 * "completion": 45.5,
 * "user_id": 7060,
 * "certificates": [],
 * "username": "",
 * "max_salary": -1,
 * "max_education": -1,
 * "educations": [],
 * "work_year": 0,
 * "photos": [],
 * "birthday": "",
 * "experiences": [],
 * "is_share": false,
 * "self_description": "",
 * "gender": 0,
 * "created_at": "2017-06-09T11:07:33",
 * "brief_description": "",
 * "avatar": "http://zoneke-img.b0.upaiyun.com/header/43a167df-ee5b-4641-84bb-a36fa018e0aa.png"
 * }
 */

public class ResumeHome {

    public String id;
    public String user_id;
    public String username;
    public String birthday;
    public String avatar;
    public String self_description;  //富文本
    public String created_at;
    public String brief_description;  //一句话描述
    public boolean is_share;
    public int status;
    public int gender;
    public float weight;
    public float height;
    public List<String> exp_jobs;
    public List<CityBean> exp_cities;
    public List<Certificate> certificates;
    public List<WorkExp> experiences;
    public List<Education> educations;
    public List<String> photos;
    public float max_salary; //薪水
    public float min_salary;
    public float completion;
    public int max_education;
    public int work_year;
}
