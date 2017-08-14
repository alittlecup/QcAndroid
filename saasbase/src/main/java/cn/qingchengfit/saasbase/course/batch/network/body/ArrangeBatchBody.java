package cn.qingchengfit.saasbase.course.batch.network.body;

import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.course.batch.bean.BatchOpenRule;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.course.batch.bean.Time_repeat;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;

public class ArrangeBatchBody {



    public String teacher_id;
    public String course_id;
    public String shop_id;
    public String from_date;
    public String to_date;
    public int max_users;
    public List<String> spaces;
    public ArrayList<Rule> rules;
    public ArrayList<Time_repeat> time_repeats;
    public String batch_id;
    public boolean is_free = false;
    public BatchOpenRule open_rule = new BatchOpenRule();


    public int checkAddBatch(){
        if (!CmStringUtils.isEmpty(teacher_id))
            return R.string.err_no_trainer;
        if (!CmStringUtils.isID(course_id))
            return R.string.err_no_course;
        if (!CmStringUtils.isEmpty(from_date))
            return R.string.err_no_start_date;
        if (!CmStringUtils.isEmpty(to_date))
            return R.string.err_no_end_date;
        if (DateUtils.formatDateFromYYYYMMDD(from_date).getTime() >
            DateUtils.formatDateFromYYYYMMDD(to_date).getTime())
            return R.string.err_start_great_end;
        if (max_users <= 0)
            return R.string.err_max_user;
        if (spaces == null || spaces.size() == 0)
            return R.string.err_no_space;
        if (!is_free &&(rules == null || rules.size() == 0))
            return R.string.err_no_rule;
        if (time_repeats == null || time_repeats.size() == 0)
            return R.string.err_no_time_repeat;
        if (open_rule == null)
            return R.string.err_no_openrule;
        return 0;
    }



}