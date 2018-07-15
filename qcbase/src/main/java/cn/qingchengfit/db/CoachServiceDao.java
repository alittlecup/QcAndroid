package cn.qingchengfit.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import cn.qingchengfit.model.base.CoachService;
import io.reactivex.Flowable;
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
 * Created by Paper on 2018/1/21.
 */
@Dao public interface CoachServiceDao {
  @Query("SELECT * FROM coachservice") public Flowable<List<CoachService>> getAllCoachService();
  @Query("SELECT * FROM coachservice") public List<CoachService> getAllCoachServiceNow();

  @Query("SELECT * FROM coachservice WHERE brand_id = (:arg0)")
  Flowable<List<CoachService>> getAllCoachServiceByBrand(String arg0);

  @Query("SELECT * FROM coachservice WHERE id = (:arg0) AND model = (:arg1) ")
  Flowable<CoachService> getByIdModel(String arg0, String arg1);

  @Query("SELECT * FROM coachservice WHERE id = (:arg0) AND model = (:arg1) ")
  CoachService getByIdModelNow(String arg0, String arg1);

  @Query("SELECT * FROM coachservice WHERE gym_id = (:arg0) ") Flowable<CoachService> getByGymId(
    String arg0);
 @Query("SELECT * FROM coachservice WHERE gym_id = (:arg0) ") CoachService getByGymIdNow(
    String arg0);

  @Query("SELECT * FROM coachservice WHERE brand_id = (:arg0) and shop_id = (:arg1)")
  Flowable<CoachService> getByBrandIdAndShopId(String arg0, String arg1);

  @Query("SELECT * FROM coachservice WHERE brand_id = (:arg0) and shop_id = (:arg1)")
  CoachService getByBrandIdAndShopIdNow(String arg0, String arg1);

  @Query("SELECT * FROM coachservice WHERE brand_id = (:arg0) and shop_id IN (:arg1)")
  Flowable<List<CoachService>> getByBrandIdAndShops(String arg0, List<String> arg1);

  @Delete public void delete(CoachService... services);
  @Query("DELETE FROM coachservice") public void deleteAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertService(
    CoachService... coachServices);

  @Update public void upDateServiceById(CoachService coachService);
}
