package de.mlauinger.studienarbeit.javadrone.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;

import com.codeminders.ardrone.DroneVideoListener;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import de.mlauinger.studienarbeit.javadrone.R;
import de.mlauinger.studienarbeit.javadrone.controller.DroneController;
import de.mlauinger.studienarbeit.javadrone.imageRecognition.Runner;

public class ControlSettings extends AppCompatActivity implements DroneVideoListener {

    private FrameLayout settingsContainer;
    private ImageView droneVideo;
    private Button takeOffAndLand;
    private Runner runner;
    private Frame frame;
    private AndroidFrameConverter converterToBitmap;
    private OpenCVFrameConverter.ToIplImage converterToIplImage;
    private DroneController droneController;
    public boolean automaticState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_control_settings);
        getFragmentManager().beginTransaction()
                .add(R.id.settings_fragment, new SettingsImageFragment())
                .commit();
        initializeViewElements();
        runner = new Runner(this);
        converterToBitmap = new AndroidFrameConverter();
        converterToIplImage = new OpenCVFrameConverter.ToIplImage();
        droneController = new DroneController();
        DroneController.addImageListender(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        droneController.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        droneController.resume();
    }

    private void initializeViewElements() {
        settingsContainer = (FrameLayout) findViewById(R.id.settings_fragment);
        Switch toggleAutomatic = (Switch) findViewById(R.id.toggle_automatic);
        droneVideo = (ImageView) findViewById(R.id.droneVideoStream);
        toggleAutomatic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                automaticState = isChecked;
            }
        });
        takeOffAndLand = (Button) findViewById(R.id.landing);
    }

    public void switchScreen(View view) {
        Intent manualControl = new Intent(this, ControlScreen.class);
        startActivity(manualControl);
    }

    public void showSettings(View view) {
        if (settingsContainer.isShown()) {
            settingsContainer.setVisibility(View.INVISIBLE);
            return;
        }
        settingsContainer.setVisibility(View.VISIBLE);

    }

    public void doEmergency(View view) {
        droneController.emergencyShutdown();
    }

    public void doTakeOff(View view) {
        if (getString(R.string.takeoff) == takeOffAndLand.getText()) {
            droneController.performTakeOff();
            takeOffAndLand.setText(getString(R.string.landing));
            return;
        }
        droneController.performLanding();

    }

    public void showDroneConfiguration(View view) {
    }

    @Override
    public void frameReceived(int startX, int startY, int w, int h,
                              int[] rgbArray, int offset, int scansize) {
        (new VideoDisplayer(startX, startY, w, h, rgbArray, offset, scansize)).execute();
    }

    private class VideoDisplayer extends AsyncTask<Void, Integer, Void> {

        public Bitmap b;
        public int[] rgbArray;
        public int offset;
        public int scansize;
        public int w;
        public int h;

        public VideoDisplayer(int x, int y, int width, int height, int[] arr, int off, int scan) {
            super();
            rgbArray = arr;
            offset = off;
            scansize = scan;
            w = width;
            h = height;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap bitmap = Bitmap.createBitmap(rgbArray, offset, scansize, w, h, Bitmap.Config.RGB_565);
            bitmap.setDensity(100);
            frame = converterToBitmap.convert(bitmap);
            opencv_core.IplImage image = runner.findCircle(converterToIplImage.convert(frame), droneController, automaticState);
            frame = converterToIplImage.convert(image);
            bitmap = converterToBitmap.convert(frame);
            b = bitmap;
            b.setDensity(100);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            //((BitmapDrawable) droneStream.getDrawable()).getBitmap().recycle();
            droneVideo.setImageBitmap(b);
        }
    }
}
