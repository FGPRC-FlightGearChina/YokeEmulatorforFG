package org.FGPRC.yokeEmu;

import android.app.*;
import android.view.*;
import android.hardware.*;
import android.view.InputQueue.*;
import android.graphics.*;
import android.util.*;
import android.os.*;
import android.widget.*;

public class BoeingStyleYokeView extends YokeView
	{
		private float backgroundHeight;
		private float thr_column_width,thr_column_height,thr_panel_height,thr_panel_width;
		private boolean isPressed=false;
		private boolean isSmoothControlActive;
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
		private Bitmap yoke,yokeBackground,background,thr_bg,thr_culumn;

		private float picDriftWidth,picDriftHeight;
		public void calibrate ( )
			{
				update = true;
			}
		@Override
		public void surfaceCreated ( SurfaceHolder p1 )
			{
				msurfaceholder.addCallback(this);
				screenWidth = getWidth ( );
				screenHeight = getHeight ( );
				textDrift = paint.getTextSize ( );
				screenCtrX = screenWidth / 2;
				screenCtrY = screenHeight / 2;
				lineSpace = screenCtrY / 4;
				backgroundHeight=4*lineSpace*13/20;
				background = Bitmap.createScaledBitmap ( background, (int)screenWidth, (int)screenHeight, true );
				thr_panel_height=screenHeight;
				thr_panel_width=thr_panel_height*186/800;
				thr_column_height=thr_panel_height*50/800;
				thr_column_width=thr_column_height*113/50;
				yoke = Bitmap.createScaledBitmap ( yoke, (int)( 4 * lineSpace ),(int)backgroundHeight, true );
			    yokeBackground = Bitmap.createScaledBitmap ( yokeBackground, (int)( 4 * lineSpace ), (int)backgroundHeight, true );
				thr_bg=Bitmap.createScaledBitmap(thr_bg,(int)thr_panel_width,(int)thr_panel_height,true);
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
			{Log.e("ERROR","DESTROYED");
			isRunning=false;
				try
					{
						if ( mMainThread != null )
							{
								mMainThread.interrupt ( );
								msensormanager.unregisterListener ( this );
								msurfaceholder.removeCallback(this);
								
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
						if(isSmoothControlActive){
						currentx = ( currentx + tmp [ 0 ] ) / 2;
						currenty = ( currenty + tmp [ 1 ] ) / 2;
						currentz = ( currentz + tmp [ 2 ] ) / 2;
						}
						else{
							currentx=tmp[0];
							currenty=tmp[1];
							currentz=tmp[2];
						}

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
				Log.d("SmoothControl",Boolean.toString(isSmoothControlActive));
				try
					{
						Thread.sleep ( 100 );
					}
				catch (InterruptedException e)
					{e.printStackTrace ( );}
				while ( isRunning&&!mMainThread.isInterrupted() )
					{
						draw ( );
					}
				// TODO: Implement this method
			}

		public BoeingStyleYokeView ( Activity activity )
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
				thr_culumn=BitmapFactory.decodeResource(mactivity.getResources(),R.drawable.theottle_column);
				thr_bg=BitmapFactory.decodeResource(mactivity.getResources(),R.drawable.throttle_panel);
				mUIhandler = new Handler ( ){
						public void handleMessage ( Message msg )
							{
								switch ( msg.what )
									{
										case 0:
											Toast.makeText ( getContext ( ), R.string.calibrated, Toast.LENGTH_LONG ).show ( );
											break;
									}
							}
					};
				isSmoothControlActive=Temp.getPrefernenceManager().getSharedPreference().getBoolean("isSmoothControl",true);

			}
		private void init ( )
			{
				msensormanager = (SensorManager)mactivity.getSystemService ( Service.SENSOR_SERVICE );
				msensor = msensormanager.getDefaultSensor ( Sensor.TYPE_ORIENTATION );
				checksensor(msensor);
				msensormanager.registerListener ( this, msensor, msensormanager.SENSOR_DELAY_GAME );


			}
		
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
					{
						mcanvas = msurfaceholder.lockCanvas ( );
						if ( mcanvas != null )
							{
								mcanvas.drawBitmap ( background, 0, 0, paint );

								mcanvas.drawBitmap ( yokeBackground, screenCtrX - picDriftWidth, screenCtrY - picDriftHeight, paint );

								mcanvas.drawPoint ( screenCtrX, screenCtrY, paint );

								mcanvas.drawText ( "X: " + String.valueOf ( currentx ), textDrift, textDrift, paint );
								mcanvas.drawText ( "Y: " + String.valueOf ( currenty ), textDrift, textDrift * 2, paint );
								mcanvas.drawText ( "Z: " + String.valueOf ( currentz ), textDrift, textDrift * 3, paint );

								values = movement.calculate ( currentz, currenty );
								mcanvas.save ( );
								mcanvas.rotate ( values [ 1 ] * 70, screenCtrX , screenCtrY - screenCtrY * values [ 0 ] );

								mcanvas.drawBitmap ( yoke, screenCtrX - picDriftWidth, screenCtrY - screenCtrY * values [ 0 ] - picDriftHeight, paint );
								mcanvas.restore ( );
								mcanvas.drawText ( "Scaled Y: " + String.valueOf ( values [ 1 ] ), textDrift, textDrift * 4, paint );
								mcanvas.drawText ( "Scaled Z: " + String.valueOf ( values [ 0 ] ), textDrift, textDrift * 5, paint );
								mcanvas.drawText ( "Ref Y: " + String.valueOf ( refy ), textDrift, textDrift * 6, paint );
								mcanvas.drawText ( "Ref Z: " + String.valueOf ( refz ), textDrift, textDrift * 7, paint );
								mcanvas.drawText ( "Current pitch: " + String.valueOf ( (int)( values [ 0 ] * 100 ) ) + "%", textDrift, textDrift * 8, paint );
								mcanvas.drawText ( "Current bank: " + String.valueOf ( (int)( values [ 1 ] * 100 ) ) + "%", textDrift, textDrift * 9, paint );
								mcanvas.drawText ( "Network status:" + mtel.getStatus ( ), textDrift, textDrift * 10, paint );
								mtd.setAliron ( values [ 1 ] );
								mtd.setElevatoer ( values [ 0 ] );
								mtel.sendMessage ( mtd );

							}

						
					}
				catch (Exception e)
					{Log.e ( "Failed to draw canvas", "Detail: " + e.getMessage ( ) );
						e.printStackTrace ( );
						MainActivity.showErrMsg ( e );

					}
				finally
					{
						if(mcanvas!=null){
						msurfaceholder.unlockCanvasAndPost ( mcanvas );
						isRealeased = true;}
					}
			}

		@Override
		public boolean onTouchEvent ( MotionEvent event )
			{
				switch(event.getAction()){
					case event.ACTION_DOWN:
						break;
					case event.ACTION_MOVE:
						break;
					case event.ACTION_UP:
						break;
				}

				// TODO: Implement this method
				return super.onTouchEvent ( event );
			}

}
