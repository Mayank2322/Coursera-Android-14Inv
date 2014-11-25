package android.cousera.dailyselfie.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SelfieDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "selfie_db";
    private static final String COMMA_SEP = ", ";
	
	private static final String CREATE_CMD =
			"CREATE TABLE " + SelfieContract.SELFIE_TABLE_NAME+ " (" +
			SelfieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
			SelfieContract.COLUMN_NAME + " TEXT NOT NULL" + COMMA_SEP +
			SelfieContract.COLUMN_PATH + " TEXT NOT NULL" + COMMA_SEP +
			SelfieContract.COLUMN_THUMB + " TEXT NOT NULL)";
	
	public SelfieDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+SelfieContract.SELFIE_TABLE_NAME);
		onCreate(db);
	}

}
