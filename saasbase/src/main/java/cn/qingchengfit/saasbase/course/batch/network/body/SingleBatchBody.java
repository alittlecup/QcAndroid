package cn.qingchengfit.saasbase.course.batch.network.body;

import cn.qingchengfit.saasbase.course.batch.bean.BatchOpenRule;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import java.util.ArrayList;
import java.util.List;

public class SingleBatchBody {
    public String id;
    public String model;
    public String start;
    public String end;

    public String teacher_id;
    public String course_id;
    public String shop_id;
    public String from_date;
    public String to_date;
    public List<String> spaces;
    public ArrayList<Rule> rule;
    public boolean is_free;
    public int max_users;
    public BatchOpenRule open_rule;
}