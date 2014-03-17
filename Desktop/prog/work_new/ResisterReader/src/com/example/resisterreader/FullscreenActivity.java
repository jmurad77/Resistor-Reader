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

import com.example.resisterreader.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {//implements View.OnClickListener {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		
		//Button buttonGenerate = (Button)findViewById(R.id.dummy_button2);
	    final TextView textGenerateNumber = (TextView)findViewById(R.id.text4);
	    /*****I commented out this button for user input use in next itteration.
	    buttonGenerate.setOnClickListener(new OnClickListener()
	    {

	           @Override
	           public void onClick(View v) 
	           {
	        	    
	            // TODO Auto-generated method stub
	            textGenerateNumber.setText(String.valueOf(random_number) + "\u2126" + " \u00B1 5%");
	           }});*****/

	    

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
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
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
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
		Button button1 = (Button) findViewById(R.id.dummy_button1);
		button1.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
             //Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
             //startActivityForResult(intent,0);
         	Intent intent = new Intent();
             intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
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
             //start of random number generator
     		 Random generator = new Random();
      		 int min = 100;
      		 int max = 500;
      		 int randomIndex = generator.nextInt(max + 1);
      		 randomIndex = ((randomIndex % 10) * 50);
      		 final int random_number = min + randomIndex;
      		 //has the program wait 1sec to generate number
      		 try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             textGenerateNumber.setText(String.valueOf(random_number) + 
            		 "\u2126" + " \u00B1 5%"); //after camera
         } });
		

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button1).setOnTouchListener(
				mDelayHideTouchListener);
	}//end of onCreate
	
	/** Called when the user clicks the Send button */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
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
			// TODO Auto-generated method stub
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	 
	  
	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	
	
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
