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
		private boolean isRealeased=true;
		private TelnetConnector mtel;
		private TelnetConnector.TelnetData mtd;
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
		private float screenHeight,screenWidth,screenCtrX,screenCtrY,lineSpace;
		private float currentx,currenty,currentz,refy,refz;
		private Bitmap yoke,yokeBackground,background;
		private float[] line100,line75,line50,line25;

		private float picDriftWidth,picDriftHeight;
		public void calibrate ( )
			{
				update = true;
			}
		@Override
		public void surfaceCreated ( SurfaceHolder p1 )
			{
				screenWidth = getWidth ( );
				screenHeight = getHeight ( );
				textDrift = paint.getTextSize ( );
				screenCtrX = screenWidth / 2;
				screenCtrY = screenHeight / 2;
				lineSpace = screenCtrY / 4;
				line100 = new float[]{
						screenCtrX - 4 * lineSpace,0,screenCtrX + 4 * lineSpace,0,
						screenCtrX - 4 * lineSpace,0,screenCtrX - 4 * lineSpace,screenHeight,
						screenCtrX - 4 * lineSpace,screenHeight,screenCtrX + 4 * lineSpace,screenHeight,
						screenCtrX + 4 * lineSpace,0,screenCtrX + 4 * lineSpace,screenHeight
					};
				line75 = new float[]{
						screenCtrX - 3 * lineSpace,lineSpace,screenCtrX + 3 * lineSpace,lineSpace,
						screenCtrX - 3 * lineSpace,lineSpace,screenCtrX - 3 * lineSpace,screenHeight - lineSpace,
						screenCtrX - 3 * lineSpace,screenHeight - lineSpace,screenCtrX + 3 * lineSpace,screenHeight - lineSpace,
						screenCtrX + 3 * lineSpace,lineSpace,screenCtrX + 3 * lineSpace,screenHeight - lineSpace
					};
				line50 = new float[]{
						screenCtrX - 2 * lineSpace,2 * lineSpace,screenCtrX + 2 * lineSpace,2 * lineSpace,
						screenCtrX - 2 * lineSpace,2 * lineSpace,screenCtrX - 2 * lineSpace,screenHeight - 2 * lineSpace,
						screenCtrX - 2 * lineSpace,screenHeight - 2 * lineSpace,screenCtrX + 2 * lineSpace,screenHeight - 2 * lineSpace,
						screenCtrX + 2 * lineSpace,2 * lineSpace,screenCtrX + 2 * lineSpace,screenHeight - 2 * lineSpace
					};
				line25 = new float[]{
						screenCtrX - 1 * lineSpace,3 * lineSpace,screenCtrX + 1 * lineSpace,3 * lineSpace,
						screenCtrX - 1 * lineSpace,3 * lineSpace,screenCtrX - 1 * lineSpace,screenHeight - 3 * lineSpace,
						screenCtrX - 1 * lineSpace,screenHeight - 3 * lineSpace,screenCtrX + 1 * lineSpace,screenHeight - 3 * lineSpace,
						screenCtrX + 1 * lineSpace,3 * lineSpace,screenCtrX + 1 * lineSpace,screenHeight - 3 * lineSpace
					};


				background = Bitmap.createScaledBitmap ( background, (int)screenWidth, (int)screenHeight, true );

				yoke = Bitmap.createScaledBitmap ( yoke, (int)( 4 * lineSpace ), (int)scaletogetHeight ( 4 * lineSpace ), true );
			    yokeBackground = Bitmap.createScaledBitmap ( yokeBackground, (int)( 4 * lineSpace ), (int)scaletogetHeight ( 4 * lineSpace ), true );
				picDriftWidth = yoke.getWidth ( ) / 2;
				picDriftHeight = yoke.getHeight ( ) / 2;
				mtel = Temp.getTelnetConnector ( );
				mtd = new TelnetConnector.TelnetData ( );
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
								mMainThread.interrupt ( );
								mMainThread.destroy ( );
								mMainThread = null;
								mtel.close ( );}
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
				mUIhandler = new Handler ( ){
						public void handleMessage ( Message msg )
							{
								switch ( msg.what )
									{
										case 0:
											Toast.makeText ( getContext ( ), "Calibrated", Toast.LENGTH_LONG ).show ( );
											break;
										case MSG_TYPE_RUNNABLR:
											Runnable r=(Runnable)msg.obj;
											r.run ( );
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
		private static final float scaletogetHeight ( float in )
			{
				return in * 13 / 20;
			};
		private synchronized void draw ( )
			{
				if ( update )
					{
						update = false;
						refz = currentz;
						refy = currenty;
						mUIhandler.sendEmptyMessage ( 0 );
						if ( movement == null )
							{
								movement = new Movement ( refz, refy );}
						else
							{movement.init ( refz, refy );}
					}

				try
					{if ( isRealeased )
							{
								mcanvas = msurfaceholder.lockCanvas ( );
								isRealeased = false;
								if ( mcanvas != null )
									{
										mcanvas.drawBitmap ( background, 0, 0, paint );

										mcanvas.drawBitmap ( yokeBackground, screenCtrX - picDriftWidth, screenCtrY - picDriftHeight, paint );

										mcanvas.drawPoint ( screenCtrX, screenCtrY, paint );

										mcanvas.drawLines ( line100, paint );
										mcanvas.drawLines ( line75, paint );
										mcanvas.drawLines ( line50, paint );
										mcanvas.drawLines ( line25, paint );

										mcanvas.drawText ( "X: " + String.valueOf ( currentx ), textDrift, textDrift, paint );
										mcanvas.drawText ( "Y: " + String.valueOf ( currenty ), textDrift, textDrift * 2, paint );
										mcanvas.drawText ( "Z: " + String.valueOf ( currentz ), textDrift, textDrift * 3, paint );

										values = movement.calculate ( currentz, currenty );
										mcanvas.save ( );
										mcanvas.rotate ( values [ 1 ] * 70, screenCtrX + screenCtrY * values [ 1 ], screenCtrY - screenCtrY * values [ 0 ] );

										mcanvas.drawBitmap ( yoke, screenCtrX + screenCtrY * values [ 1 ] - picDriftWidth, screenCtrY - screenCtrY * values [ 0 ] - picDriftHeight, paint );
										mcanvas.restore ( );
										mcanvas.drawText ( "Scaled Y: " + String.valueOf ( values [ 1 ] ), textDrift, textDrift * 4, paint );
										mcanvas.drawText ( "Scaled Z: " + String.valueOf ( values [ 0 ] ), textDrift, textDrift * 5, paint );
										mcanvas.drawText ( "Ref Y: " + String.valueOf ( refy ), textDrift, textDrift * 6, paint );
										mcanvas.drawText ( "Ref Z: " + String.valueOf ( refz ), textDrift, textDrift * 7, paint );
										mcanvas.drawText ( "Current pitch: " + String.valueOf ( (int)( values [ 0 ] * 100 ) ) + "%", textDrift, textDrift * 8, paint );
										mcanvas.drawText ( "Current bank: " + String.valueOf ( (int)( values [ 1 ] * 100 ) ) + "%", textDrift, textDrift * 9, paint );
										mcanvas.drawText ( "Network status:" + mtel.getStatus ( ), textDrift, textDrift * 10, paint );
										mtd.setAliron ( values [ 0 ] );
										mtd.setElevatoer ( values [ 1 ] );
										mtel.sendMessage ( mtd );

									}
								else
									{msurfaceholder.unlockCanvasAndPost ( mcanvas );
										isRealeased = true;}

							}
					}
				catch (Exception e)
					{Log.e ( "Failed to draw canvas", "Detail: " + e.getMessage ( ) );
						e.printStackTrace ( );
						MainActivity.showErrMsg ( e );

					}
				finally
					{if ( !isRealeased )
							{
								msurfaceholder.unlockCanvasAndPost ( mcanvas );
								isRealeased = true;}

					}
			}

		@Override
		public boolean onTouchEvent ( MotionEvent event )
			{

				// TODO: Implement this method
				return super.onTouchEvent ( event );
			}

	}
