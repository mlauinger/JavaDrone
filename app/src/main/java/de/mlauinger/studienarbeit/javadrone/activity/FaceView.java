package de.mlauinger.studienarbeit.javadrone.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.view.View;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

class FaceView extends View implements Camera.PreviewCallback {
    public static final int SUBSAMPLING_FACTOR = 4;

    private opencv_core.IplImage grayImage;
    private opencv_objdetect.CvHaarClassifierCascade classifier;
    private opencv_core.CvMemStorage storage;
    private opencv_core.CvSeq faces;

    public FaceView(CameraScreen context) throws IOException {
        super(context);
    }

    public void onPreviewFrame(final byte[] data, final Camera camera) {
        try {
            Camera.Size size = camera.getParameters().getPreviewSize();
            processImage(data, size.width, size.height);
            camera.addCallbackBuffer(data);
        } catch (RuntimeException e) {
            // The camera has probably just been released, ignore.
        }
    }

    protected void processImage(byte[] data, int width, int height) {
        // First, downsample our image and convert it into a grayscale IplImage
        int f = SUBSAMPLING_FACTOR;
        if (grayImage == null || grayImage.width() != width/f || grayImage.height() != height/f) {
            grayImage = opencv_core.IplImage.create(width/f, height/f, IPL_DEPTH_8U, 1);
        }
        int imageWidth  = grayImage.width();
        int imageHeight = grayImage.height();
        int dataStride = f*width;
        int imageStride = grayImage.widthStep();
        ByteBuffer imageBuffer = grayImage.getByteBuffer();
        for (int y = 0; y < imageHeight; y++) {
            int dataLine = y*dataStride;
            int imageLine = y*imageStride;
            for (int x = 0; x < imageWidth; x++) {
                imageBuffer.put(imageLine + x, data[dataLine + f*x]);
            }
        }

        cvClearMemStorage(storage);
        faces = cvHaarDetectObjects(grayImage, classifier, storage, 1.1, 3,
                CV_HAAR_FIND_BIGGEST_OBJECT | CV_HAAR_DO_ROUGH_SEARCH);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(20);

        String s = "FacePreview - This side up.";
        float textWidth = paint.measureText(s);
        canvas.drawText(s, (getWidth()-textWidth)/2, 20, paint);

        if (faces != null) {
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.STROKE);
            float scaleX = (float)getWidth()/grayImage.width();
            float scaleY = (float)getHeight()/grayImage.height();
            int total = faces.total();
            for (int i = 0; i < total; i++) {
                CvRect r = new CvRect(cvGetSeqElem(faces, i));
                int x = r.x(), y = r.y(), w = r.width(), h = r.height();
                canvas.drawRect(x*scaleX, y*scaleY, (x+w)*scaleX, (y+h)*scaleY, paint);
            }
        }
    }
}
