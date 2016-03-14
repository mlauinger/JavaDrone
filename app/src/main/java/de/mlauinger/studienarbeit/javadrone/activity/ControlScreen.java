package de.mlauinger.studienarbeit.javadrone.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.codeminders.ardrone.DroneVideoListener;

import de.mlauinger.studienarbeit.javadrone.R;
import de.mlauinger.studienarbeit.javadrone.controller.DroneConfigurationController;
import de.mlauinger.studienarbeit.javadrone.controller.DroneController;
import de.mlauinger.studienarbeit.javadrone.dialogs.CustomNotification;
import de.mlauinger.studienarbeit.javadrone.imageRecognition.Runner;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_core.*;

public class ControlScreen extends AppCompatActivity implements DroneVideoListener {

    DroneController droneController;
    Button landing;
    Button takeoff;
    Button emergency;
    Button flyLeft;
    Button flyRight;
    Button flyForward;
    Button flyBackward;
    Button turnLeft;
    Button turnRight;
    ImageView droneStream;
    DroneConfigurationController configController;
    Runner runner;
    AndroidFrameConverter converterToBitmap;
    OpenCVFrameConverter.ToIplImage converterToIplImage;
    Frame frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen);
        droneController = new DroneController();
        configController = new DroneConfigurationController(this);
        droneController.sendConfigurations(configController);
        initializeViewElements();
        runner = new Runner(this);
        converterToBitmap = new AndroidFrameConverter();
        converterToIplImage = new OpenCVFrameConverter.ToIplImage();
        DroneController.addImageListender(this);
    }

    private void initializeViewElements() {
        landing = (Button) findViewById(R.id.landing);
        takeoff = (Button) findViewById(R.id.takeoff);
        emergency = (Button) findViewById(R.id.emergency);
        flyLeft = (Button) findViewById(R.id.flyleft);
        flyRight = (Button) findViewById(R.id.flyright);
        flyForward = (Button) findViewById(R.id.flyforward);
        flyBackward = (Button) findViewById(R.id.flybackward);
        turnLeft = (Button) findViewById(R.id.turnleft);
        turnRight = (Button) findViewById(R.id.turnright);
        droneStream = (ImageView) findViewById(R.id.droneVideoStream);
    }

    public void doTakeOff(View view) {
        droneController.performTakeOff();
        takeoff.setEnabled(false);
        landing.setEnabled(true);
        flyLeft.setEnabled(true);
        flyRight.setEnabled(true);
        flyForward.setEnabled(true);
        flyBackward.setEnabled(true);
        turnLeft.setEnabled(true);
        turnRight.setEnabled(true);
    }

    public void doEmergency(View view) {
        droneController.emergencyShutdown();
    }

    public void flyForward(View view) {
        droneController.flyDrone(-1f, 0);
    }

    public void flyBackward(View view) {
        droneController.flyDrone(1f, 0);
    }

    public void flyLeft(View view) {
        droneController.flyDrone(0, -1f);
    }

    public void flyRight(View view) {
        droneController.flyDrone(0, 1f);
    }


    public void turnLeft(View view) {
        droneController.turnDrone(-1f);
    }

    public void turnRight(View view) {
        droneController.turnDrone(1f);
    }

    public void doLanding(View view) {
        droneController.performLanding();
        takeoff.setEnabled(true);
        landing.setEnabled(false);
        flyLeft.setEnabled(false);
        flyRight.setEnabled(false);
        flyForward.setEnabled(false);
        flyBackward.setEnabled(false);
        turnLeft.setEnabled(false);
        turnRight.setEnabled(false);
    }

    public void showDroneConfiguration(View view) {
        System.out.println(droneController.getConfiguration());
        Dialog droneConfigurations = new CustomNotification().showNotification(this, "Drone Configuration", droneController.getConfiguration());
        droneConfigurations.show();
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

            ((BitmapDrawable) droneStream.getDrawable()).getBitmap().recycle();
            frame = converterToBitmap.convert(b);
            opencv_core.IplImage image = runner.findCircle(converterToIplImage.convert(frame),droneController);

            frame = converterToIplImage.convert(image);
            Bitmap bitmap = converterToBitmap.convert(frame);
            droneStream.setImageBitmap(bitmap);
        }
    }
}
