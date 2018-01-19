package cn.qingchengfit.saasbase.db;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.utils.LogUtil;
import com.qingchengfit.fitcoach.App;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 2017/5/15.
 */

public class QcDbManager {
    private static final String TAG = "QCDbManager";
    // rx响应式数据库,
    private BriteDatabase briteDatabase;

    @Inject public QcDbManager(App application, LoginStatus loginStatus) {
        QcDbHelper dbOpenHelper;

        // sqlbrite 初始化,构造出响应式数据库,添加log
        SqlBrite sqlBrite;
        sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override public void log(String message) {
                LogUtil.d("db" + message);
            }
        });
        // 原生的sqllitehelper 用来建立数据库和数据表,以及构造,rx响应式数据库
        dbOpenHelper = new QcDbHelper(application, loginStatus);
        // 执行slqbirte 构造数据库的语句
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
        briteDatabase.setLoggingEnabled(false);
    }

    public static String getLikeString(String keyword) {
        return "%" + keyword + "%";
    }

    public BriteDatabase getDataBase() {
        return briteDatabase;
    }
}
