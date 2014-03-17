package com.example.resisterreader;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.example.resisterreader.util.SystemUiHider;


public class FullscreenActivity extends Activity {
    /**
     * Assorted internal values for the application. These can be modified to
     * control behavior cleanly.
     */
    public final static String   EXTRA_MESSAGE          = "com.example.myfirstapp.MESSAGE";
    private static final boolean AUTO_HIDE              = true;
    private static final int     AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final boolean TOGGLE_ON_CLICK        = true;
    private static final int     HIDER_FLAGS            = SystemUiHider.FLAG_HIDE_NAVIGATION;
    private SystemUiHider mSystemUiHider;

    /**
     * Create a new delayed touch listener.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        public boolean onTouch1(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            return false;
        }
    };
    Handler mHideHandler   = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Run on the creation of the activity.
     * @param savedInstanceState -> Saved state of the activity for reloading purposes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call onCreate for the parent class.
        super.onCreate(savedInstanceState);
        // Make the app fullscreen.
        setContentView(R.layout.activity_fullscreen);
        // Load the appropriate screen contents from R (the resources singleton)
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView  = findViewById(R.id.fullscreen_content);
        final TextView textGenerateNumber = (TextView) findViewById(R.id.text4);

        /*
        NOTE: I commented out this button for user input use in next iteration.

            buttonGenerate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                }
            });
        */

        // Load the UI hider, initialize it, and set the change listener.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
            int mControlsHeight;
            int mShortAnimTime;

            /**
             * Listener for visibility change.
             *
             * This code targets Android Honeycomb specifically. It will be
             * ignored for older versions of Android.
             *
             * @param visible -> Boolean indicating whether it is visible
             */
            @Override
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
            public void onVisibilityChange(boolean visible) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    if (mControlsHeight == 0) {
                        mControlsHeight = controlsView.getHeight();
                    }
                    if (mShortAnimTime == 0) {
                        mShortAnimTime = getResources().getInteger(
                        android.R.integer.config_shortAnimTime);
                    }
                    controlsView
                        .animate()
                        .translationY(visible ? 0 : mControlsHeight)
                        .setDuration(mShortAnimTime);
                } else {
                    controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
                if (visible && AUTO_HIDE) {
                    delayedHide(AUTO_HIDE_DELAY_MILLIS);
                }
            }
        });
        // Set the onClick listener for hiding the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });
        // The resistor-reading magic.
        Button button1 = (Button) findViewById(R.id.dummy_button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create Intent for image capture.
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a temp file for the captured image.
                String filePath = Environment.getExternalStorageDirectory() + "";
                filePath = filePath + "/Pictures" + File.separator + "temp_img.jpg";
                File f = new File(filePath);
                try {
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.fromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 0);
                private ArrayList result;
                // The actual resistance calculation
                public void scan(Mat resistor_img) {
                    Mat img = f;
                    Mat edges;
                    Imgproc.Canny(img, edges, 50.0, 150.0);
                    Imgproc.cvtColor(img, img, cv2.COLOR_BGR2HSV);
                    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                    Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
                    for (MatOfPoint contour : contours) {
                        double area = Imgproc.contourArea(contour);
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
                    result = sorted(result, key = lambda result : result[0]);
                }
                textGenerateNumber.setText(String.valueOf(result) + "\u2126" + " \u00B1 5%");
            }
        });
        findViewById(R.id.dummy_button1).setOnTouchListener(mDelayHideTouchListener);
    }

    /**
     * Reloads state appropriately.
     * @param savedInstanceState -> Saved state to be reloaded.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    /**
     * Callback for hiding interface elements with a delay.
     * @param delayMillis -> The delay wait time
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
