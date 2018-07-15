package cn.qingchengfit.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import cn.qingchengfit.model.base.QcStudentBean;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import java.util.List;

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
 * Created by Paper on 2018/1/23.
 */
@Dao public interface StudentDao {
  @Query("SELECT * FROM menbers") public Flowable<List<QcStudentBean>> getAllStudent();

  @Query("SELECT * FROM menbers where id = :arg0")
  public Flowable<QcStudentBean> getStudentById(String arg0);

  @Query("SELECT * FROM menbers where phone = :arg0")
  public Flowable<QcStudentBean> getStudentByPhone(String arg0);

  @Query("SELECT * FROM menbers where brand_id = :arg0")
  public Flowable<List<QcStudentBean>> getStudentByBrand(String arg0);

  @Query("SELECT * FROM menbers where brand_id = :arg0 and (username like :arg1 or phone like :arg1 )")
  public Maybe<List<QcStudentBean>> getStudentByBrandAndKeyWord(String arg0, String arg1);

  @Query("SELECT * FROM menbers where (username like :arg0 or phone like :arg0 )")
  public Maybe<List<QcStudentBean>> getStudentByKeyWord(String arg0);

  @Query("SELECT * FROM menbers where supoort_gym_ids like :arg0")
  public Flowable<List<QcStudentBean>> getStudentByGym(String arg0);

  @Delete void delStudent(QcStudentBean... qcStudentBeans);

  @Query("Delete FROM menbers" ) void delStudentAll();

  @Update void updataStudent(QcStudentBean... qcStudentBeans);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertStudent(QcStudentBean... qcStudentBeans);
}
