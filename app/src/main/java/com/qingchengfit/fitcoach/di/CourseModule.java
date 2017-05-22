package com.qingchengfit.fitcoach.di;

import cn.qingchengfit.model.base.CoachService;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.http.RestRepository;
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
 * Created by Paper on 16/11/17.
 */
@Module public class CourseModule {

    CoachService coachService;
    Brand brand;
    RestRepository restRepository;

    private CourseModule(Builder builder) {
        coachService = builder.coachService;
        brand = builder.brand;
        restRepository = builder.restRepository;
    }

    @Provides CoachService provideService() {
        return this.coachService;
    }

    @Provides public Brand getBrand() {
        return brand;
    }

    @Provides public RestRepository getRestRepository() {
        return restRepository;
    }

    public static final class Builder {
        private CoachService coachService;
        private Brand brand;
        private RestRepository restRepository;

        public Builder() {
        }

        public Builder coachService(CoachService val) {
            coachService = val;
            return this;
        }

        public Builder brand(Brand val) {
            brand = val;
            return this;
        }

        public Builder restRepository(RestRepository val) {
            restRepository = val;
            return this;
        }

        public CourseModule build() {
            return new CourseModule(this);
        }
    }
}
