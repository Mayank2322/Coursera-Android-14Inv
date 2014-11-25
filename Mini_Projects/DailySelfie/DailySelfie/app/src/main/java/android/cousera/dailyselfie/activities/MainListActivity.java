package android.cousera.dailyselfie.activities;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.cousera.dailyselfie.BitmapUtils;
import android.cousera.dailyselfie.R;
import android.cousera.dailyselfie.adapters.SelfieCursorAdapter;
import android.cousera.dailyselfie.models.Selfie;
import android.cousera.dailyselfie.provider.SelfieContract;
import android.cousera.dailyselfie.receiver.AlarmReceiver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_TAKE_PHOTO = 0;

    private String mCurrentPhotoPath;
    private SelfieCursorAdapter mAdapter;

    private static final String ALARM_KEY = "alarmKey";
    private static final String SELFIE_KEY = "selfieKey";

    private static final long REPEAT_DELAY = 2*60*1000L; // 2 minutes

    private PendingIntent mAlarmOperation;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString(SELFIE_KEY);
        }

        getListView().setBackground(getResources().getDrawable(R.drawable.background_grad2));
        getListView().setDivider(null);
        getListView().setDividerHeight(1);

        mAdapter = new SelfieCursorAdapter(getApplicationContext());
        getListView().setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);

        mSharedPreferences = getSharedPreferences("selfie", Context.MODE_PRIVATE);

        setAlarm(null,false);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Selfie selfie = (Selfie)mAdapter.getItem(position);

        Intent intent = new Intent(this,FullSelfieActivity.class);
        intent.putExtra(FullSelfieActivity.EXTRA_NAME,selfie.getName());
        intent.putExtra(FullSelfieActivity.EXTRA_PATH,selfie.getPath());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_alarm);
        setAlarm(item,false);
        return true;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = BitmapUtils.createImageFile();
                mCurrentPhotoPath = photoFile.getAbsolutePath();

            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(),"could not create image file",Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }
        else if (id == R.id.action_clear_list) {
            mAdapter.clearList();
            return true;
        }
        else if (id == R.id.action_alarm) {
            setAlarm(item,true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_TAKE_PHOTO == requestCode) {
            if (resultCode == RESULT_CANCELED){

                new File(mCurrentPhotoPath).delete();
            }
            if (resultCode == RESULT_OK) {

                Selfie selfie = new Selfie();
                selfie.setName(new File(mCurrentPhotoPath).getName());
                selfie.setPath(mCurrentPhotoPath);

                Bitmap fullSized = BitmapUtils.getBitmapFromFile(mCurrentPhotoPath);
                Float aspectRatio = ((float)fullSized.getHeight())/(float)fullSized.getWidth();
                Bitmap thumb = Bitmap.createScaledBitmap(fullSized, 70, (int)(70*aspectRatio), false);
                String thumbPath = BitmapUtils.getThumbPath(mCurrentPhotoPath);
                selfie.setThumbPath(thumbPath);
                BitmapUtils.storeBitmapToFile(thumb, thumbPath);

                fullSized.recycle();
                thumb.recycle();

                mCurrentPhotoPath = null;

                mAdapter.addSelfie(selfie);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String select = "((" + SelfieContract._ID + " NOTNULL))";

        String[] columns_to_return = { SelfieContract._ID,
                SelfieContract.COLUMN_NAME,
                SelfieContract.COLUMN_PATH,
                SelfieContract.COLUMN_THUMB };

        return new CursorLoader(this, SelfieContract.CONTENT_URI, columns_to_return, select,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
        mAdapter.swapCursor(newCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    protected void setAlarm(MenuItem item, boolean toggle) {

        if (mAlarmOperation == null) {
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            mAlarmOperation = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        }

        boolean alarmToggle = mSharedPreferences.getBoolean(ALARM_KEY, true);
        if (toggle) {
            alarmToggle = !alarmToggle;
            mSharedPreferences.edit().putBoolean(ALARM_KEY, alarmToggle).apply();
        }

        AlarmManager alarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        if (alarmToggle) {
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime()+ REPEAT_DELAY,
                    REPEAT_DELAY, mAlarmOperation);
        }
        else
            alarm.cancel(mAlarmOperation);

        if (item != null) {
            Toast.makeText(getApplicationContext(),"Alarm " + ((alarmToggle) ? "Enabled!": "Disabled") , Toast.LENGTH_LONG).show();
        }
    }

}
