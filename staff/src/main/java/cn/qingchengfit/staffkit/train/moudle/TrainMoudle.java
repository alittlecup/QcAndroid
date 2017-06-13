package cn.qingchengfit.staffkit.train.moudle;

import dagger.Module;
import dagger.Provides;

/**
 * Created by fb on 2017/4/7.
 */

@Module public class TrainMoudle {
    TrainIds trainIds;

    public TrainMoudle(TrainIds trainIds) {
        this.trainIds = trainIds;
    }

    @Provides TrainIds provideTrain() {
        return trainIds;
    }
}
