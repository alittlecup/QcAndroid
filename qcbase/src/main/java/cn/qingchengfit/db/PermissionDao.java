package cn.qingchengfit.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import cn.qingchengfit.model.base.Permission;
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
@Dao
public interface PermissionDao {
  @Query("SELECT * FROM permissions") public List<Permission> getAllPermission();
  @Query("SELECT * FROM permissions where shop_id = :arg0 and `key`= :arg1 ") public List<Permission> getByShopIdAndKey(String arg0,String arg1);
  @Query("SELECT * FROM permissions where `key`= (:arg0) ") public List<Permission> getByKey(String arg0);
  @Query("SELECT * FROM permissions where shop_id = (:arg0) and `key`= (:arg1) ") public List<Permission> getByShopIdAndKeyValue(String arg0,String arg1);
  @Query("SELECT * FROM permissions where value = (:arg0) and `key`= (:arg1) ") public List<Permission> getByKeyValue(boolean arg0,String arg1);
  //@Query("SELECT * FROM permissions where shop_id = :shopid and `key`= :key ") public List<Permission> getByShopIdAndKeyValue(String shopid,String key);
  @Delete public void deletePermisson(Permission... permissions);
  @Query("Delete FROM permissions") public void deletePermissonAll();
  @Insert(onConflict = OnConflictStrategy.REPLACE) public void writePermmission(Permission... permissions);


  //getByShopIdAndKeyValue:
  //SELECT * FROM Permission WHERE shop_id = ? and key = ? and value = ?;

  //getByKeyValue:
  //SELECT * FROM Permission WHERE key = ? and value = ?;
  //
  //getByKey:
  //SELECT * FROM Permission WHERE key = ?;
}
