package android.cousera.dailyselfie.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class SelfieContract implements BaseColumns{

    public static final String AUTHORITY = "android.cousera.dailyselfie.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY + "/");

    public static final String SELFIE_TABLE_NAME = "selfie";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, SELFIE_TABLE_NAME);

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_THUMB = "thumb";

}
