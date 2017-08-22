package cn.qingchengfit.model.body;

/**
 * Created by yangming on 16/9/18.
 */
public class SignInIgnorBody {

    private Integer checkin_id;
    private boolean is_ignore;

    public SignInIgnorBody(Integer checkin_id, boolean is_ignore) {
        this.checkin_id = checkin_id;
        this.is_ignore = is_ignore;
    }

    public Integer getCheckin_id() {
        return checkin_id;
    }

    public void setCheckin_id(Integer checkin_id) {
        this.checkin_id = checkin_id;
    }

    public boolean is_ignore() {
        return is_ignore;
    }

    public void setIs_ignore(boolean is_ignore) {
        this.is_ignore = is_ignore;
    }
}
