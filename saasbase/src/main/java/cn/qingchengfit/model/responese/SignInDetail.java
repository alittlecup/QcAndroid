package cn.qingchengfit.model.responese;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangming on 16/9/5.
 */
public class SignInDetail {

    @SerializedName("data") public Data data;

    public class Data {
        @SerializedName("check_in") public SignInTasks.SignInTask check_in;

        public SignInTasks.SignInTask getCheck_in() {
            return check_in;
        }

        public void setCheck_in(SignInTasks.SignInTask check_in) {
            this.check_in = check_in;
        }
    }
}
