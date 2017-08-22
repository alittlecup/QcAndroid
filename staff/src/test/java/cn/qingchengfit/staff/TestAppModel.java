package cn.qingchengfit.staff;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.rest.RestRepository;
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
 * Created by Paper on 16/8/19.
 */
@Module public class TestAppModel {
    App testApp;
    RestRepository restRepository;
    Brand brand;
    CoachService coachService;

    private TestAppModel(Builder builder) {
        testApp = builder.testApp;
        restRepository = builder.restRepository;
        brand = builder.brand;
        coachService = builder.coachService;
    }

    @Provides App provideApp() {
        return this.testApp;
    }

    @Provides Brand provideBrand() {
        return this.brand;
    }

    @Provides CoachService provideGym() {
        return this.coachService;
    }

    @Provides RestRepository provideRest() {
        return this.restRepository;
    }

    public static final class Builder {
        private App testApp;
        private RestRepository restRepository;
        private Brand brand;
        private CoachService coachService;

        public Builder() {
        }

        public Builder testApp(App val) {
            testApp = val;
            return this;
        }

        public Builder restRepository(RestRepository val) {
            restRepository = val;
            return this;
        }

        public Builder brand(Brand val) {
            brand = val;
            return this;
        }

        public Builder gymBase(CoachService val) {
            coachService = val;
            return this;
        }

        public TestAppModel build() {
            return new TestAppModel(this);
        }
    }
}
