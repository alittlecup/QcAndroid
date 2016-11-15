package com.qingchengfit.fitcoach.http.bean;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/2/23 2016.
 */
public class CreatBrandBody {
    public String name;
    public String photo;

    private CreatBrandBody(Builder builder) {
        name = builder.name;
        photo = builder.photo;
    }


    public static final class Builder {
        private String name;
        private String photo;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder photo(String val) {
            photo = val;
            return this;
        }

        public CreatBrandBody build() {
            return new CreatBrandBody(this);
        }
    }
}
