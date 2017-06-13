package cn.qingchengfit.staffkit.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.staffkit.usecase.bean.Permission;

public class QCDbOpenHelper extends SQLiteOpenHelper {

    public QCDbOpenHelper(Context context) {
        super(context, QCDbConstant.AT_DB_NAME, null, QCDbConstant.AT_DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(QCDb.CardTable.CREATE);
        db.execSQL(QcStudentBean.CREATE_TABLE);
        db.execSQL(CoachService.CREATE_TABLE);
        db.execSQL(Permission.CREATE_TABLE);
        db.execSQL(MyFunctionModel.CREATE_TABLE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 12) {
            db.execSQL("DROP TABLE " + QCDb.CardTable.TABLE_NAME);
            db.execSQL("DROP TABLE " + QcStudentBean.TABLE_NAME);
            db.execSQL("DROP TABLE " + CoachService.TABLE_NAME);
            db.execSQL("DROP TABLE " + Permission.TABLE_NAME);
            db.execSQL("DROP TABLE " + MyFunctionModel.TABLE_NAME);
            onCreate(db);
        }
    }
}
