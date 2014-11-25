package android.cousera.dailyselfie.models;

import android.cousera.dailyselfie.provider.SelfieContract;
import android.database.Cursor;

public class Selfie {

    private String name;
    private String path;
    private String thumbPath;

    public static Selfie fromCursor(Cursor cursor) {
        Selfie selfie = new Selfie();

        selfie.setPath(cursor.getString(cursor.getColumnIndex(SelfieContract.COLUMN_PATH)));
        selfie.setName(cursor.getString(cursor.getColumnIndex(SelfieContract.COLUMN_NAME)));

        return selfie;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }


}

