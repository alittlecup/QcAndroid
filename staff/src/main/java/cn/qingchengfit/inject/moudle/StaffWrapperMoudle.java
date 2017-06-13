package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.inject.model.StaffWrapper;
import dagger.Module;
import dagger.Provides;

/**
 * Created by fb on 2017/2/17.
 */

@Module public class StaffWrapperMoudle {

    private StaffWrapper staffWrapper;

    public StaffWrapperMoudle(StaffWrapper staffWrapper) {
        this.staffWrapper = staffWrapper;
    }

    @Provides StaffWrapper provideStaff() {
        return staffWrapper;
    }
}
