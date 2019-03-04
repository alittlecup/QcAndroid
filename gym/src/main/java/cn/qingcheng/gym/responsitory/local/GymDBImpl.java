package cn.qingcheng.gym.responsitory.local;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import cn.qingcheng.gym.bean.GymType;
import debug.GymDataBase;
import java.util.List;

public class GymDBImpl implements GymDBManager {
  IGymDao iGymDao;

  public GymDBImpl(Application application) {
    iGymDao = Room.databaseBuilder(application, GymDataBase.class, "gym_database")
        .allowMainThreadQueries()
        .build();
  }

  @Override public LiveData<List<GymType>> loadGymTypes() {
    return null;
  }

  @Override public void insertGymTypes(List<GymType> gymTypes) {

  }
}
