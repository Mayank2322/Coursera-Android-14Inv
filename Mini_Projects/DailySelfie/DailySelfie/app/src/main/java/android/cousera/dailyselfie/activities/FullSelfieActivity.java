package android.cousera.dailyselfie.activities;

import android.app.Activity;
import android.cousera.dailyselfie.BitmapUtils;
import android.cousera.dailyselfie.R;
import android.cousera.dailyselfie.Utils;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class FullSelfieActivity extends Activity {

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PATH = "urlPath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.full_selfie);


        ImageView mBitmap = (ImageView) findViewById(R.id.full_image_view);
        String selfieName = getIntent().getStringExtra(EXTRA_NAME);
        String filePath = getIntent().getStringExtra(EXTRA_PATH);
        setTitle(selfieName);
        new LoadBitmapTask(this, mBitmap).execute(filePath);
        setProgressBarIndeterminateVisibility(true);
    }

    static class LoadBitmapTask extends AsyncTask<String, String, Bitmap> {

        private ImageView mImageView;
        private Activity mActivity;

        public LoadBitmapTask(Activity activity, ImageView imageView) {
            mImageView = imageView;
            mActivity = activity;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String selfiePath = params[0];

            return BitmapUtils.getBitmapFromFile(selfiePath);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mImageView.setImageBitmap(result);
            mActivity.setProgressBarIndeterminateVisibility(false);
            super.onPostExecute(result);
        }
    }
}