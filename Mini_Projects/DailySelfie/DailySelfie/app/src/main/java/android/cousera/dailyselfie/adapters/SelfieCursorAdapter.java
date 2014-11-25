package android.cousera.dailyselfie.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.cousera.dailyselfie.R;
import android.cousera.dailyselfie.models.Selfie;
import android.cousera.dailyselfie.provider.SelfieContract;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.cousera.dailyselfie.BitmapUtils.IMAGE_EXTENSION;
import static android.cousera.dailyselfie.BitmapUtils.getBitmapFromFile;
import static android.cousera.dailyselfie.BitmapUtils.initStoragePath;
import static android.cousera.dailyselfie.Utils.SAVE_DATE_FORMATTER;
import static android.cousera.dailyselfie.Utils.SHOW_DATE_FORMATTER;

public class SelfieCursorAdapter extends CursorAdapter {

    private static LayoutInflater sLayoutInflater = null;
    private List<Selfie> mSelfies = new ArrayList<Selfie>();
    private Context mContext;
    ContentValues values = new ContentValues();

    private Map<String,Bitmap> mBitmaps = new HashMap<String,Bitmap>();

    public void clearList() {
        mBitmaps.clear();
        mContext.getContentResolver().delete(SelfieContract.CONTENT_URI, null, null);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
    }

    public SelfieCursorAdapter(Context context) {
        super(context,null,0);
        mContext = context;
        sLayoutInflater = LayoutInflater.from(mContext);

        initStoragePath(mContext);
    }

    @Override
    public Object getItem(int position) {
        return mSelfies.get(position);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        Cursor oldCursor = super.swapCursor(newCursor);
        mSelfies.clear();
        if (newCursor !=null) {
            newCursor.moveToFirst();
            while(!newCursor.isAfterLast()) {
                Selfie selfie = Selfie.fromCursor(newCursor);
                mSelfies.add(selfie);
                newCursor.moveToNext();
            }
        }

        return oldCursor;
    }

    public void addSelfie(Selfie selfie) {

        mSelfies.add(selfie);

        values.clear();

        values.put(SelfieContract.COLUMN_NAME, selfie.getName());
        values.put(SelfieContract.COLUMN_PATH, selfie.getPath());
        values.put(SelfieContract.COLUMN_THUMB, selfie.getThumbPath());

        mContext.getContentResolver().insert(SelfieContract.CONTENT_URI,values);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        Bitmap bitmap;
        String path = cursor.getString(cursor.getColumnIndex(SelfieContract.COLUMN_THUMB));
        if (mBitmaps.containsKey(path)) {
            bitmap = mBitmaps.get(path);
        } else {
            bitmap = getBitmapFromFile(path);
            mBitmaps.put(path, bitmap);
        }
        holder.image.setImageBitmap(bitmap);

        String dateStr = cursor.getString(cursor.getColumnIndex(SelfieContract.COLUMN_NAME)).replace(IMAGE_EXTENSION, "");
        Date date;
        try {
            date = SAVE_DATE_FORMATTER.parse(dateStr);
        } catch (ParseException e) {
            Toast.makeText(context,"COULDN'T PARSE DATE",Toast.LENGTH_LONG).show();
            return;
        }
        holder.name.setText(SHOW_DATE_FORMATTER.format(date));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View newView;
        ViewHolder holder = new ViewHolder();

        newView = sLayoutInflater.inflate(R.layout.list_selfie_item, parent,
                false);
        holder.image = (ImageView) newView.findViewById(R.id.imagePhoto);
        holder.name = (TextView) newView.findViewById(R.id.namePhoto);

        newView.setTag(holder);

        return newView;
    }




}
