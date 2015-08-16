package nodomain.myfirstapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.text.Format;
import java.util.Set;
import java.util.UUID;
import java.util.Formatter;

public class MyActivity extends AppCompatActivity implements View.OnTouchListener{
    public final static String EXTRA_MESSAGE = "nodomain.myfirstapp.MESSAGE";
    final String DEBUG_TAG = "onTouchEventDebug";
    final String DEBUG_TAG_MOVE = "onTouchEventDebugMove";

    /*
    View myView = findViewById(R.id.imageViewRobotTouch);
    myView.OnTouchListener(new View.OnTouchListener()) {
        public boolean onTouch(View v, MotionEvent event) {

            return true;
        }
    });
    */

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void startBluetooth(View view) {

        final String TAG = "startBluetooth";
        final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        Log.d(TAG, "Trying to start bluetooth");

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("00:06:66:6A:5C:41");

        BluetoothSocket bluetoothSocket = null;

        OutputStream outputStream = null;

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            //bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException ex) {
            Log.e(TAG, "Failed to create socket");
            ex.printStackTrace();
        }

        try {
            //ParcelUuid list[] = device.getUuids();
            if (!bluetoothSocket.isConnected()) {
                bluetoothSocket.connect();
            }
            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException ex) {
            Log.e(TAG, "Failed to create connection or output stream", ex);
        }

        try {
            outputStream.write("Hello from Android".getBytes());
        } catch (IOException ex) {
            Log.e(TAG, "Failed to write to output stream", ex);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "Action was MOVE");
                //{0,0} is in top left corner of view. Positive x to the right and positive y is downwards.
                Log.d(DEBUG_TAG_MOVE, "X = " + Float.toString(event.getX()) + "Y = " + Float.toString(event.getY()));
                //TextView xPosTextView = (TextView) findViewById(R.id.textViewXPosition);
                ((TextView) findViewById(R.id.textViewXPosition)).setText("X = " + Float.toString(event.getX()));
                ((TextView) findViewById(R.id.textViewYPosition)).setText("Y = " + Float.toString(event.getY()));
                printSamples(event);
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "Movement occurred outside bounds of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

//http://developer.android.com/reference/java/util/Formatter.html
    void printSamples(MotionEvent ev) {
        final int historySize = ev.getHistorySize();
        final int pointerCount = ev.getPointerCount();

        //For each pointerCounter (i.e. finger or touch point) loop through and print out the positions
        for (int p = 0; p < pointerCount; p++) {
            //Log.d(DEBUG_TAG_MOVE, String.format("Pointer number: %d", p));

            //Loop through and print out all positions that were batched into this event
            for (int h = 0; h < historySize; h++) {
                Log.d(DEBUG_TAG_MOVE, String.format("Pointer %d: Time: %d X: %f Y: %f )",
                        ev.getPointerId(p), ev.getHistoricalEventTime(h), ev.getHistoricalX(p, h), ev.getHistoricalY(p, h)));
            }

            //Print current position
            Log.d(DEBUG_TAG_MOVE, String.format("Pointer %d: Time: %d X: %f Y: %f )",
                    ev.getPointerId(p), ev.getEventTime(), ev.getX(p), ev.getY(p)));
        }
/*
            Log.d(DEBUG_TAG_MOVE, String.format("At time %d:", (ev.getHistoricalEventTime(h))));
            for (int p = 0; p < pointerCount; p++) {

            }
        }

        Log.d(DEBUG_TAG, String.format("At time %d:", (ev.getEventTime())));
        for (int p = 0; p < pointerCount; p++) {
            Log.d(DEBUG_TAG, String.format("  pointer %d: (%f,%f )",
                    ev.getPointerId(p), ev.getX(p), ev.getY(p)));
        }
  */
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        View myView = findViewById(R.id.imageViewRobotTouch);
        myView.setOnTouchListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                //openSearch();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*
    //code taken from
    //http://developer.android.com/training/gestures/detector.html
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final String DEBUG_TAG = "onTouchEventDebug";
        final String DEBUG_TAG_MOVE = "onTouchEventDebugMove";

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "Action was MOVE");
                //screen size is ~460 x 800. {0,0} is in top left corner and positive x to the right and positive y is downwards
                Log.d(DEBUG_TAG_MOVE, "X = " + Float.toString(event.getX()) + "Y = " + Float.toString(event.getY()));
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
    */
}