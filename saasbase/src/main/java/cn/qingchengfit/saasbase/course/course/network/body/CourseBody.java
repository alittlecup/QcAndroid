package cn.qingchengfit.saasbase.course.course.network.body;

import cn.qingchengfit.model.base.IBody;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.CmStringUtils;

public class CourseBody implements IBody{

    public String name;
    public int length;
    public int is_private;
    public String model;
    public String id;
    public String photo;
    /**
     * 新增
     */

    public String descrition;
    public String plan_id;
    public Integer capacity;
    public Integer min_users;
    public String shop_ids;

    public CourseBody() {
    }

    public CourseBody(String name, int length, int is_private, String model, String model_id) {
        this.name = name;
        this.length = length;
        this.is_private = is_private;
        this.model = model;
        this.id = model_id;
    }

    private CourseBody(Builder builder) {
        name = builder.name;
        length = builder.length;
        is_private = builder.is_private;
        model = builder.model;
        id = builder.id;
        photo = builder.photo;
        descrition = builder.descrition;
        plan_id = builder.plan_id;
        capacity = builder.capacity;
        min_users = builder.min_users;
        shop_ids = builder.shop_ids;
    }

    @Override public int check(int type) {
        if (CmStringUtils.isEmpty(name))
            return R.string.err_no_course_name;
        if (length <= 0)
            return R.string.err_no_course_time;
        if (capacity <= 0)
            return R.string.err_no_course_max_count;
        if (is_private == 0 && min_users <= 0){
            return R.string.err_no_course_min_count;
        }
        return 0;
    }

    @Override public int check() {
        return check(0);
    }

    public static final class Builder {
        private String name;
        private int length;
        private int is_private;
        private String model;
        private String id;
        private String photo;
        private String descrition;
        private String plan_id;
        private Integer capacity;
        private Integer min_users;
        private String shop_ids;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder length(int val) {
            length = val;
            return this;
        }

        public Builder is_private(int val) {
            is_private = val;
            return this;
        }

        public Builder model(String val) {
            model = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder photo(String val) {
            photo = val;
            return this;
        }

        public Builder descrition(String val) {
            descrition = val;
            return this;
        }

        public Builder plan_id(String val) {
            plan_id = val;
            return this;
        }

        public Builder capacity(Integer val) {
            capacity = val;
            return this;
        }

        public Builder min_users(Integer val) {
            min_users = val;
            return this;
        }

        public Builder shop_ids(String val) {
            shop_ids = val;
            return this;
        }

        public CourseBody build() {
            return new CourseBody(this);
        }
    }
}