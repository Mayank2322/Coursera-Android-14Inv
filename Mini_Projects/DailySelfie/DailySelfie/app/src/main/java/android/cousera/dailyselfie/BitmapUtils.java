package android.cousera.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.cousera.dailyselfie.Utils.SAVE_DATE_FORMATTER;

public class BitmapUtils {
    public static final String APP_DIR = "DailySelfie/Selfies";
    public static String mBitmapStoragePath;
    public static final String IMAGE_EXTENSION = ".jpg";

    public static void initStoragePath(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                String root = context.getExternalFilesDir(null).getCanonicalPath();
                File bitmapStorageDir = new File(root, BitmapUtils.APP_DIR);
                bitmapStorageDir.mkdirs();
                mBitmapStoragePath = bitmapStorageDir.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getBitmapFromFile(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    public static boolean storeBitmapToFile(Bitmap bitmap, String filePath) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(filePath));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static String getThumbPath(String imagePath) {
        File image = new File(imagePath);

        String path = image.getAbsolutePath();
        String fileWithoutExt = image.getName().split("\\.")[0];
        path = path.replace(IMAGE_EXTENSION,"").replace(fileWithoutExt,"");
        return path + fileWithoutExt + "_thumb"+ IMAGE_EXTENSION;
    }

    public static File createImageFile() throws IOException {
        String timeStamp = SAVE_DATE_FORMATTER.format(Calendar.getInstance().getTime());
        File image = new File(mBitmapStoragePath+"/"+timeStamp + IMAGE_EXTENSION);
        image.createNewFile();

        return image;
    }
}
