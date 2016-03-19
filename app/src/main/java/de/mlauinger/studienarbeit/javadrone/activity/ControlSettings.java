package de.mlauinger.studienarbeit.javadrone.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    private Switch toggleAutomatic;
    private ImageView droneVideo;
    private Runner runner;
    private AndroidFrameConverter converterToBitmap;
    private OpenCVFrameConverter.ToIplImage converterToIplImage;
    private DroneController droneController;

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
    }

    private void initializeViewElements() {
        settingsContainer = (FrameLayout) findViewById(R.id.settings_fragment);
        toggleAutomatic = (Switch) findViewById(R.id.toggle_automatic);
        droneVideo = (ImageView) findViewById(R.id.droneVideoStream);
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
            b = Bitmap.createBitmap(rgbArray, offset, scansize, w, h, Bitmap.Config.RGB_565);
            b.setDensity(100);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            ((BitmapDrawable) droneVideo.getDrawable()).getBitmap().recycle();
            if (toggleAutomatic.isSelected()) {

                Frame frame = converterToBitmap.convert(b);
                opencv_core.IplImage image = runner.findCircle(converterToIplImage.convert(frame), droneController);

                frame = converterToIplImage.convert(image);
                b = converterToBitmap.convert(frame);
            }
            droneVideo.setImageBitmap(b);
        }
    }
}
