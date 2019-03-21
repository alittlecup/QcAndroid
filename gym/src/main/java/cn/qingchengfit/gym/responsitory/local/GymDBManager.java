package cn.qingchengfit.gym.responsitory.local;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.gym.bean.GymType;
import java.util.List;

public interface GymDBManager {
  LiveData<List<GymType>> loadGymTypes();

  void insertGymTypes(List<GymType> gymTypes);
}
