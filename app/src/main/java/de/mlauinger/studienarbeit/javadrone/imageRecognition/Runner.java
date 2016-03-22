package de.mlauinger.studienarbeit.javadrone.imageRecognition;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.mlauinger.studienarbeit.javadrone.controller.DroneController;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class Runner {


    final int INTERVAL = 100;// 1sec
    final int CAMERA_NUM = 0; // Default camera for this time
    private OpenCVFrameConverter.ToIplImage converter;
    private static final float SMALLEST_AREA = 600.0f;
    private CvMemStorage contourStorage;
    int x, y;
    float w, h;
    Movement movement;
    SharedPreferences storedPrefs;

    public Runner(Context context){
            movement = new Movement();
            storedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public IplImage findCircle(IplImage img, DroneController controller,boolean autofly) {
        IplImage returnImage = img;
        if (img != null) {
            cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
            IplImage rgbImg = cvCloneImage(img);
            //cvCvtColor(img, img, CV_BGR2HSV);
            IplImage detectThrs = getThresholdImage(img);
            if(storedPrefs.getBoolean("alt_image",false)){
                returnImage = detectThrs;
            }
            IplImage contur = findBiggestContour(detectThrs);
            findCenter(contur);
            IplImage circle = drawCircle(rgbImg);
            if(!storedPrefs.getBoolean("alt_image",false)){
                returnImage = circle;
            }
            CirclePos circlePos = new CirclePos();
            circlePos.setPosx(x);
            circlePos.setPosy(y);
            circlePos.setRadius((int) (h + w) / 2);
            circlePos.setHeight(img.height());
            circlePos.setWidth(img.width());
            if(autofly) {
                movement.followCircle(circlePos, controller);
            }
        }
        try {
            Thread.sleep(INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnImage;
    }

    private IplImage drawCircle(IplImage src) {
        CvPoint center = cvPointFrom32f(new CvPoint2D32f(x, y));
        int radius = Math.round((w + h) / 2);
        cvCircle(src, center, radius, CvScalar.BLUE, 6, CV_AA, 0);
        return src;
    }

    private IplImage getThresholdImage(IplImage orgImg) {
        IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
        double sMin = storedPrefs.getInt("blue_value",0);
        double vMin = storedPrefs.getInt("green_value",0);
        double hMin = storedPrefs.getInt("red_value",0);
        double sMax = storedPrefs.getInt("blueM_value",0);
        double vMax = storedPrefs.getInt("greenM_value",0);
        double hMax = storedPrefs.getInt("redM_value",0);
        cvInRangeS(orgImg, new CvScalar(sMax, vMax, hMax, 0), cvScalar(sMin, hMin, vMin, 0), imgThreshold);
        cvErode(imgThreshold, imgThreshold, null, 5);
        cvDilate(imgThreshold, imgThreshold, null, 5);
        //IplImage gray = cvCreateImage(cvGetSize(orgImg), 8, 1);
        //cvCanny(imgThreshold,gray,50,150,3);
        return imgThreshold;
    }


    private IplImage findBiggestContour(IplImage imgThreshed) {
        CvSeq bigContour = null;
        CvSeq contour2 = null;
        // generate all the contours in the threshold image as a list
        CvSeq contours = new CvSeq();
        contourStorage = CvMemStorage.create();
        cvFindContours(imgThreshed, contourStorage, contours,
                Loader.sizeof(CvContour.class),
                CV_RETR_LIST,CV_LINK_RUNS,cvPoint(0,0));

        // find the largest contour in the list based on bounded box size
        float maxArea = SMALLEST_AREA;
        double areaMax = 1000, areaC=0;
        CvBox2D maxBox = null;
        contour2= contours;
        while (contours != null && !contours.isNull()) {
            if (contours.elem_size() > 0) {
                CvBox2D box = cvMinAreaRect2(contours, contourStorage);
                if (box != null) {
                    CvSize2D32f size = box.size();
                    float area = size.width() * size.height();
                    if (area > maxArea) {
                        maxArea = area;
                        bigContour = contours;
                        w = size.width();
                        h = size.height();
                    }
                }
            }
            areaC = cvContourArea(contours,CV_WHOLE_SEQ,1);

            if(areaC>areaMax) {
                areaMax = areaC;
            }

            contours = contours.h_next();
        }
        while(contour2 !=null && !contour2.isNull())
        {
            areaC= cvContourArea(contour2,CV_WHOLE_SEQ,1);

            if(areaC<areaMax)
            {
                cvDrawContours(imgThreshed,contour2,CV_RGB(0,0,0),CV_RGB(0,0,0),
                        0,CV_FILLED,8,cvPoint(0,0));
            }

            contour2=contour2.h_next();
        }
        return imgThreshed;
    }

    private void findCenter (IplImage imgThreshed) {
        CvMoments moments = new CvMoments(Loader.sizeof(Moments.class));
        cvMoments(imgThreshed, moments, 1);
        double m10, m01, area;
        m10 = cvGetSpatialMoment(moments, 1, 0);
        m01 = cvGetSpatialMoment(moments, 0, 1);
        area = cvGetCentralMoment(moments, 0, 0);
        x = (int) (m10 / area);
        y = (int) (m01 / area);
    }
}