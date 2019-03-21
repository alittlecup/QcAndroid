package cn.qingchengfit.gym.responsitory.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import cn.qingchengfit.gym.bean.GymType;
import java.util.List;

@Dao public interface GymTypeDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertGymType(List<GymType> gymTypes);
  @Query("SELECT * FROM gymtype") LiveData<List<GymType>> qcGetAllGymType();

}
