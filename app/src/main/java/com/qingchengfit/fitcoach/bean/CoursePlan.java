package com.qingchengfit.fitcoach.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
 * Created by Paper on 16/8/3.
 */
public class CoursePlan implements Parcelable {
    private String name;
    private List<String> tags;
    private Long id;
    public String url;
    public int type;//1.个人 2.所属 3.会议

    private CoursePlan(Builder builder) {
        setName(builder.name);
        setTags(builder.tags);
        setId(builder.id);
        url = builder.url;
        type = builder.type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public static final class Builder {
        private String name;
        private List<String> tags;
        private Long id;
        private String url;
        private int type;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder tags(List<String> val) {
            tags = val;
            return this;
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public CoursePlan build() {
            return new CoursePlan(this);
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeStringList(this.tags);
        dest.writeValue(this.id);
        dest.writeString(this.url);
        dest.writeInt(this.type);
    }

    protected CoursePlan(Parcel in) {
        this.name = in.readString();
        this.tags = in.createStringArrayList();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.url = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<CoursePlan> CREATOR = new Creator<CoursePlan>() {
        @Override public CoursePlan createFromParcel(Parcel source) {
            return new CoursePlan(source);
        }

        @Override public CoursePlan[] newArray(int size) {
            return new CoursePlan[size];
        }
    };
}
