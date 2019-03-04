package cn.qingcheng.gym.responsitory.local;

import android.arch.lifecycle.LiveData;
import cn.qingcheng.gym.bean.GymType;
import java.util.List;

public interface GymDBManager {
  LiveData<List<GymType>> loadGymTypes();

  void insertGymTypes(List<GymType> gymTypes);
}
