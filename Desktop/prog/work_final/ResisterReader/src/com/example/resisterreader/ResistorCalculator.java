/*********************package com.example.resisterreader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

//import OpenCV-2.4.8-android-sdk.sdk.java.bin.classes.org.opencv.core.MatOfPoint;
//import OpenCV-2.4.8-android-sdk.sdk.java.bin.classes.org.opencv.core.Scalar; find out how or if i should add these

class ResistorCalculator {

    private ArrayList result;

    public void scan(Mat resistor_img) { //change type void once return type if figured out
        Mat img = resistor_img;
        Mat edges;
        Imgproc.Canny(img, edges, 50.0, 150.0);
        Imgproc.cvtColor(img, img, cv2.COLOR_BGR2HSV);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour); //changed this from int to double
            if (area > 10 && area < 30) {
                Rect bounding_box = Imgproc.boundingRect(contour);
                int centroid_x = bounding_box.x + bounding_box.width / 2;
                int centroid_y = bounding_box.y + bounding_box.height / 2;
                Mat centroid_color = img[centroid_y, centroid_x, 0];
                if (centroid_color => 0 && centroid_color < 22) {
                    result.add([centroid_x, centroid_y, 'O']);
                } else if (centroid_color >= 22 && centroid_color < 38) {
                    result.add([centroid_x, centroid_y, 'Y']);
                } else if (centroid_color >= 38 && centroid_color < 75) {
                    result.add([centroid_x, centroid_y, 'G']);
                } else if (centroid_color >= 75 && centroid_color < 130) {
                    result.add([centroid_x, centroid_y, 'B']);
                } else if (centroid_color >= 130 && centroid_color < 160) {
                    result.add([centroid_x, centroid_y, 'V']);
                } else if (centroid_color >= 160 && centroid_color < 179) {
                    result.add([centroid_x, centroid_y, 'R']);
                }
            }
        }

        // result = sorted(result, key = lambda result : result[0]);
        // return [x[2] for x in result]
    }
}****************/