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
    public ArrayList<Rule> rules = new ArrayList<>();
    public ArrayList<Time_repeat> time_repeats;
    public String batch_id;
    public boolean is_free = false;
    public BatchOpenRule open_rule = new BatchOpenRule();

    public ArrangeBatchBody() {
    }

    private ArrangeBatchBody(Builder builder) {
        teacher_id = builder.teacher_id;
        course_id = builder.course_id;
        shop_id = builder.shop_id;
        from_date = builder.from_date;
        to_date = builder.to_date;
        max_users = builder.max_users;
        spaces = builder.spaces;
        rules = builder.rules;
        time_repeats = builder.time_repeats;
        batch_id = builder.batch_id;
        is_free = builder.is_free;
        open_rule = builder.open_rule;
    }

    public int checkAddBatch(){
        if (CmStringUtils.isEmpty(teacher_id))
            return R.string.err_no_trainer;
        if (!CmStringUtils.isID(course_id))
            return R.string.err_no_course;
        if (CmStringUtils.isEmpty(from_date))
            return R.string.err_no_start_date;
        if (CmStringUtils.isEmpty(to_date))
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

    public static final class Builder {
        private String teacher_id;
        private String course_id;
        private String shop_id;
        private String from_date;
        private String to_date;
        private int max_users;
        private List<String> spaces;
        private ArrayList<Rule> rules;
        private ArrayList<Time_repeat> time_repeats;
        private String batch_id;
        private boolean is_free;
        private BatchOpenRule open_rule;

        public Builder() {
        }

        public Builder teacher_id(String val) {
            teacher_id = val;
            return this;
        }

        public Builder course_id(String val) {
            course_id = val;
            return this;
        }

        public Builder shop_id(String val) {
            shop_id = val;
            return this;
        }

        public Builder from_date(String val) {
            from_date = val;
            return this;
        }

        public Builder to_date(String val) {
            to_date = val;
            return this;
        }

        public Builder max_users(int val) {
            max_users = val;
            return this;
        }

        public Builder spaces(List<String> val) {
            spaces = val;
            return this;
        }

        public Builder rules(ArrayList<Rule> val) {
            rules = val;
            return this;
        }

        public Builder time_repeats(ArrayList<Time_repeat> val) {
            time_repeats = val;
            return this;
        }

        public Builder batch_id(String val) {
            batch_id = val;
            return this;
        }

        public Builder is_free(boolean val) {
            is_free = val;
            return this;
        }

        public Builder open_rule(BatchOpenRule val) {
            open_rule = val;
            return this;
        }

        public ArrangeBatchBody build() {
            return new ArrangeBatchBody(this);
        }
    }
}