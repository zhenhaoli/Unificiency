package lmu.de.unificiencyandroid.components.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by robertMueller on 02.01.17.
 */

public class UserDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "USER_DB";
    public static final String TABLE_NAME = "USER";
    public static final String COL_ID = "ID";
    public static final String COL_NICKNAME = "NCIKNAME";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_MAJOR = "MAJOR";



    public UserDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table" + TABLE_NAME + "("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NICKNAME+" TEXT, " + COL_EMAIL + " TEXT, " + COL_MAJOR + " Text"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
