package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import dagger.Module;
import dagger.Provides;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/1/28 2016.
 */

@Module public class GymMoudle {
    CoachService coachService;
    Brand brand;
    GymStatus status;

    public GymMoudle(CoachService coachService, Brand brand, GymStatus gymStatus) {
        this.coachService = coachService;
        this.brand = brand;
        this.status = gymStatus;
    }

    @Provides CoachService provideGymBase() {
        return coachService;
    }

    @Provides Brand provideBrand() {
        return brand;
    }

    @Provides GymStatus provideGymStatus() {
        return this.status;
    }
}
