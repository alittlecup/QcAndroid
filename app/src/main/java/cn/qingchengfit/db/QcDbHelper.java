package cn.qingchengfit.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.utils.LogUtil;
import com.qingcheng.model.base.CoachServiceModel;
import com.qingcheng.model.base.userModel;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
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

public class QcDbHelper extends SQLiteOpenHelper {
    private BriteDatabase briteDatabase;

    @Inject public QcDbHelper(App context, LoginStatus loginStatus) {
        super(context, loginStatus.getUserId() + "trainer.db", null, 1);

        SqlBrite sqlBrite;
        sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override public void log(String message) {
                LogUtil.d("db" + message);
            }
        });
        briteDatabase = sqlBrite.wrapDatabaseHelper(this, Schedulers.io());
        briteDatabase.setLoggingEnabled(BuildConfig.DEBUG);
    }

    public BriteDatabase getBriteDatabase() {
        return briteDatabase;
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(CoachServiceModel.CREATE_TABLE);
        db.execSQL(userModel.CREATE_TABLE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
