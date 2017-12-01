package cn.qingchengfit.saasbase.course.course.network.body;

import java.util.List;

public class EditJacketBody {

    List<String> photos;
    boolean random_show_photos;

    private EditJacketBody(Builder builder) {
        photos = builder.photos;
        random_show_photos = builder.random_show_photos;
    }

    public static final class Builder {
        private List<String> photos;
        private boolean random_show_photos;

        public Builder() {
        }

        public Builder photos(List<String> val) {
            photos = val;
            return this;
        }

        public Builder random_show_photos(boolean val) {
            random_show_photos = val;
            return this;
        }

        public EditJacketBody build() {
            return new EditJacketBody(this);
        }
    }
}