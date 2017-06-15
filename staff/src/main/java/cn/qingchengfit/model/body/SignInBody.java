package cn.qingchengfit.model.body;

/**
 * Created by yangming on 16/9/6.
 */
public class SignInBody {

    private int checkin_id;
    private String locker_id;

    public int getCheckin_id() {
        return checkin_id;
    }

    public void setCheckin_id(int checkin_id) {
        this.checkin_id = checkin_id;
    }

    public String getLocker_id() {
        return locker_id;
    }

    public void setLocker_id(String locker_id) {
        this.locker_id = locker_id;
    }
}
