package cn.qingchengfit.di.model;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.event.EventLoginChange;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.utils.PreferenceUtils;
import com.baidu.android.pushservice.PushManager;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.RxBus;
import com.tencent.qcloud.timchat.common.AppData;
import com.xiaomi.mipush.sdk.MiPushClient;

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
 * Created by Paper on 2017/3/1.
 */

public class LoginStatus {
    private Staff loginUser;
    private String session;
    private String userId;

    private LoginStatus(Builder builder) {
        setLoginUser(builder.loginUser);
        setSession(builder.session);
        setUserId(builder.userId);
    }

    public boolean isLogined(){
        if (TextUtils.isEmpty(session) || loginUser == null){
            return false;
        }else return true;
    }
    public Staff getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(Staff loginUser) {
        this.loginUser = loginUser;
    }

    public String getSession() {
        return session;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void logout(Context context){
        App.coachid = 0;
        session = "";
        loginUser = null;
        PushManager.stopWork(context.getApplicationContext());
        MiPushClient.unregisterPush(context.getApplicationContext());
        AppData.clear(context);
        PreferenceUtils.setPrefString(context, Configs.PREFER_SESSION, "");
        PreferenceUtils.setPrefString(context, "user_info", "");
        PreferenceUtils.setPrefString(context, "coach", "");
        RxBus.getBus().post(new EventLoginChange());
    }

    public String staff_id(){
        if (loginUser!= null)
            return loginUser.getId();
        else return "";
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String staff_name() {
        if (loginUser!= null)
            return loginUser.getUsername();
        else return "";
    }

    public static final class Builder {
        private Staff loginUser;
        private String session;
        private String userId;

        public Builder() {
        }

        public Builder loginUser(Staff val) {
            loginUser = val;
            return this;
        }

        public Builder session(String val) {
            session = val;
            return this;
        }

        public Builder userId(String val) {
            userId = val;
            return this;
        }

        public LoginStatus build() {
            return new LoginStatus(this);
        }
    }
}
