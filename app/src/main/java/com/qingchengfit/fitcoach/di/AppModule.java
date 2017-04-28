package com.qingchengfit.fitcoach.di;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import com.qingchengfit.fitcoach.App;
import dagger.Module;
import dagger.Provides;

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
 * Created by Paper on 2017/4/17.
 */
@Module
public class AppModule {
    private LoginStatus loginStatus;
    private GymWrapper gymWrapper;
    private App app;

    private AppModule(Builder builder) {
        loginStatus = builder.loginStatus;
        gymWrapper = builder.gymWrapper;
        app = builder.app;
    }

    @Provides
    LoginStatus providerLoginStatus(){
        return loginStatus;
    }

    @Provides
    GymWrapper provideGym(){
        return gymWrapper;
    }
    @Provides
    App provideApplicationContext() {
        return app;
    }

    public static final class Builder {
        private LoginStatus loginStatus;
        private GymWrapper gymWrapper;
        private App app;

        public Builder() {
        }

        public Builder loginStatus(LoginStatus val) {
            loginStatus = val;
            return this;
        }

        public Builder gymWrapper(GymWrapper val) {
            gymWrapper = val;
            return this;
        }

        public Builder app(App val) {
            app = val;
            return this;
        }

        public AppModule build() {
            return new AppModule(this);
        }
    }
}
