package cn.qingchengfit.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Permission;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.db.GymFunctionDao;

@Database(entities = {CoachService.class, Permission.class, QcStudentBean.class, GymFunctionDao.FuctionModule.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
  public abstract CoachServiceDao serviceDao();
  public abstract PermissionDao permissonDao();
  public abstract StudentDao studentDao();
  public abstract GymFunctionDao functionDao();
}