package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.utils.StringUtils;
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
 * Created by Paper on 2017/3/15.
 */

public class ShortMsg implements Parcelable {
    public static final Parcelable.Creator<ShortMsg> CREATOR = new Parcelable.Creator<ShortMsg>() {
        @Override public ShortMsg createFromParcel(Parcel source) {
            return new ShortMsg(source);
        }

        @Override public ShortMsg[] newArray(int size) {
            return new ShortMsg[size];
        }
    };
    public String id;
    public String title;
    public int status; //status=1 已发送 2 草稿
    public String created_at;
    public String content;
    public List<QcStudentBean> users;
    public Staff created_by;

    public ShortMsg() {
    }

    protected ShortMsg(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.content = in.readString();
        this.users = in.createTypedArrayList(QcStudentBean.CREATOR);
        this.created_by = in.readParcelable(Staff.class.getClassLoader());
    }

    public String getShortTitle() {
        if (StringUtils.isEmpty(title)) {
            if (users == null || users.size() == 0) {
                return "(未填写收件人)";
            } else {
                int maxSize = 3;
                if (users.size() < 3) {
                    maxSize = users.size();
                }
                String ret = "";
                for (int i = 0; i < maxSize; i++) {
                    if (i < maxSize - 1) {
                        ret = TextUtils.concat(ret, users.get(i).getUsername(), Configs.SEPARATOR).toString();
                    } else {
                        ret = TextUtils.concat(ret, users.get(i).getUsername(), "等" + users.size() + "人").toString();
                    }
                }
                return ret;
            }
        } else {
            return title;
        }
    }

    public String getMultiStudentsHtml() {
        if (users != null) {
            String ret = "<html><p style=\"line-height:0.8;\">";
            for (int i = 0; i < users.size(); i++) {
                ret = TextUtils.concat(ret, StringUtils.getStringHtml(users.get(i).getUsername(), "#333333"),
                    StringUtils.getStringHtml(" (" + users.get(i).getPhone() + ")", "#999999"), "<br></br>").toString();
            }
            return ret + "</p></html>";
        } else {
            return "";
        }
    }

    public String getContent() {
        if (StringUtils.isEmpty(content)) {
            return "(未填写短信内容)";
        } else {
            return content;
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.content);
        dest.writeTypedList(this.users);
        dest.writeParcelable(this.created_by, flags);
    }
}
