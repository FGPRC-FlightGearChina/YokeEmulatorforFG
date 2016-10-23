package org.FGPRC.yokeEmu;

import android.app.*;
import android.view.*;
import android.hardware.*;
import android.view.InputQueue.*;
import android.graphics.*;
import android.util.*;
import android.widget.*;
import android.os.*;

public class YokeView extends SurfaceView implements SensorEventListener,Runnable,SurfaceHolder.Callback
	{
		private static final int MSG_TYPE_RUNNABLR=1;
		private float[] tmp;
		private float[] values=new float[2];
		private Handler mUIhandler;
		private Movement movement;
		private Sensor msensor;
		private SensorManager msensormanager;
		private Canvas mcanvas;
		private Activity mactivity;
		private boolean isRunning=false,update=true;
		private SurfaceHolder msurfaceholder;
		private Paint paint;
		private Thread mMainThread;
		private float textDrift;
		private float screenHeight,screenWidth,screenCtrX,screenCtxy;
		private float currentx,currenty,currentz,refy,refz;
		private Bitmap yoke,yokeBackground,background;

		public void calibrate(){
			update=true;
		}
		@Override
		public void surfaceCreated ( SurfaceHolder p1 )
			{
				screenWidth = getWidth ( );
				screenHeight = getHeight ( );
				textDrift = paint.getTextSize ( );
				background = Bitmap.createScaledBitmap ( background, (int)screenWidth, (int)screenHeight, true );

				init ( );
				if ( !isRunning )
					{
						mMainThread = new Thread ( this );

						isRunning = true;
						mMainThread.start ( );
					}
				// TODO: Implement this method
			}

		@Override
		public void surfaceChanged ( SurfaceHolder p1, int p2, int p3, int p4 )
			{
				msurfaceholder = p1;
				// TODO: Implement this method
			}

		@Override
		public void surfaceDestroyed ( SurfaceHolder p1 )
			{msensormanager.unregisterListener ( this );
				try
					{
						isRunning = false;
						if ( mMainThread != null )
							{
								mMainThread.interrupt ( );}
					}
				catch (Exception e)
					{

					}
				// TODO: Implement this method
			}



		@Override
		public void onSensorChanged ( SensorEvent p1 )
			{
				if ( p1.sensor.equals ( msensor ) )
					{
						tmp = p1.values;
						currentx = ( currentx + tmp [ 0 ] ) / 2;
						currenty = ( currenty + tmp [ 1 ] ) / 2;
						currentz = ( currentz + tmp [ 2 ] ) / 2;

					}
				// TODO: Implement this method
			}

		@Override
		public void onAccuracyChanged ( Sensor p1, int p2 )
			{
				// TODO: Implement this method
			}

		@Override
		public void run ( )
			{
				try
					{
						Thread.sleep ( 100 );
					}
				catch (InterruptedException e)
					{e.printStackTrace ( );}
				while ( true )
					{
						draw ( );
					}
				// TODO: Implement this method
			}

		public YokeView ( Activity activity )
			{
				super ( activity );
				mactivity = activity;
				msurfaceholder = this.getHolder ( );
				msurfaceholder.addCallback ( this );
				mMainThread = new Thread ( this );
				paint = new Paint ( );
				paint.setAntiAlias ( true );
				paint.setColor ( Color.WHITE );
				this.setKeepScreenOn ( true );
				yoke = BitmapFactory.decodeResource ( mactivity.getResources ( ), R.drawable.yoke );
				yokeBackground = BitmapFactory.decodeResource ( mactivity.getResources ( ), R.drawable.yokebg );
				background = BitmapFactory.decodeResource ( mactivity.getResources ( ), R.drawable.bg );
				mUIhandler=new Handler(){
					public void handleMessage(Message msg)
					{
						switch(msg.what){
							case 0:
								Toast.makeText(getContext(),"Calibrated",Toast.LENGTH_LONG).show();
								break;
							case MSG_TYPE_RUNNABLR:
								Runnable r=(Runnable)msg.obj;
								r.run();
								break;
						}
					}
				};

			}
		private void init ( )
			{
				msensormanager = (SensorManager)mactivity.getSystemService ( Service.SENSOR_SERVICE );
				msensor = msensormanager.getDefaultSensor ( Sensor.TYPE_ORIENTATION );
				msensormanager.registerListener ( this, msensor, msensormanager.SENSOR_DELAY_GAME );


			}
		private synchronized void draw ( )
			{
				if ( update )
					{
						update = false;
						refz = currentz;
						refy = currenty;
						mUIhandler.sendEmptyMessage(0);
						if ( movement == null )
							{
								movement = new Movement ( refz, refy );}
						else
							{movement.init ( refz, refy );}
						}

				try
					{
						mcanvas = msurfaceholder.lockCanvas ( );
						if ( mcanvas != null )
							{
								mcanvas.drawBitmap ( background, 0, 0, paint );
								mcanvas.drawText ( "X: " + String.valueOf ( currentx ), textDrift, textDrift, paint );
								mcanvas.drawText ( "Y: " + String.valueOf ( currenty ), textDrift, textDrift * 2, paint );
								mcanvas.drawText ( "Z: " + String.valueOf ( currentz ), textDrift, textDrift * 3, paint );

								values = movement.calculate ( currentz, currenty );
								mcanvas.drawText ( "Scaled Y: " + String.valueOf ( values [ 1 ] ), textDrift, textDrift * 4, paint );
								mcanvas.drawText ( "Scaled Z: " + String.valueOf ( values [ 0 ] ), textDrift, textDrift * 5, paint );
								mcanvas.drawText ( "Ref Y: " + String.valueOf ( refy ), textDrift, textDrift * 6, paint );
								mcanvas.drawText ( "Ref Z: " + String.valueOf ( refz ), textDrift, textDrift * 7, paint );


							}
					}
				catch (Exception e)
					{Log.e ( "Failed to draw canvas", "Detail: " + e.getMessage ( ) );

					}
				finally
					{
						msurfaceholder.unlockCanvasAndPost ( mcanvas );
					}
			}
	}
