package android.cousera.modernartui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by Kadete on 02/11/2014.
 */
public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    SeekBar bar;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // load the layout

        bar = (SeekBar)findViewById(R.id.seekBar); // make seekbar object
        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
        // since we are using this class as the listener the class is "this"


        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        //imageView4 = (ImageView) findViewById(R.id.imageView4);//White logo
        imageView5 = (ImageView) findViewById(R.id.imageView5);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.more_information) {

            AlertDialogFragment dFragment = new AlertDialogFragment();
            // Show DialogFragment
            dFragment.show(getFragmentManager(), "Dialog Fragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        imageView1.setBackgroundColor(Color.argb(0xFF, i, 0xAA, i*2));
        imageView2.setBackgroundColor(Color.argb(0xFF, 0xAA-i, i, i*2));
        imageView3.setBackgroundColor(Color.argb(0xFF, 0xAA, i*2, i));
        imageView5.setBackgroundColor(Color.argb(0xFF, i*2, 0, 0XAA-i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
