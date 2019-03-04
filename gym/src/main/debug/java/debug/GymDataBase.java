package debug;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import cn.qingcheng.gym.bean.GymType;
import cn.qingcheng.gym.responsitory.local.IGymDao;

@Database(entities = { GymType.class }, version = 1, exportSchema = false)

public abstract class GymDataBase extends RoomDatabase implements IGymDao {
}
