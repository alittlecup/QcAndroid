package cn.qingchengfit.model.responese;

/**
 * Created by yangming on 16/9/18.
 */
public class SignInUrl {

    public Data data;

    public class Data {
        private String checkin_url;
        private String checkout_url;

        public String getCheckin_url() {
            return checkin_url;
        }

        public void setCheckin_url(String checkin_url) {
            this.checkin_url = checkin_url;
        }

        public String getCheckout_url() {
            return checkout_url;
        }

        public void setCheckout_url(String checkout_url) {
            this.checkout_url = checkout_url;
        }
    }
}
