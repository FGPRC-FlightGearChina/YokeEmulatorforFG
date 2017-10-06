package org.FGPRC.yokeEmu;

import android.os.*;
import android.view.*;
import android.content.pm.*;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.util.*;

public class MainActivity extends Activity 
{
	BoeingStyleYokeView my;
	public static final String FRAGMENT_WIZARD="WIZAED";
	private static boolean isMainUI=true;
    private static YokeFragment yf;
	private static Throwable cacheThrowable;
		private static Handler mErrHdl;

		public static void setIsMainUI ( boolean isMainUI )
			{
				MainActivity.isMainUI = isMainUI;
			}


	public static void showErrMsg(Throwable t){
		if(cacheThrowable!=null&&t.getCause().equals(cacheThrowable.getCause())){return;}
		mErrHdl.sendMessage(mErrHdl.obtainMessage(0,t));
		cacheThrowable=t;
	}
    public final static int MainViewId ( )
    {
        return R.id.mainRelativeLayout1;
    };
    @Override
    public void onCreate ( Bundle savedInstanceState )
    {

        super.onCreate (savedInstanceState);
        UncaughtExceptionHandler.getInstance ().init (this);
		mErrHdl=new Handler(){
			public void handleMessage(Message msg){
				Log.d("Error","Error fetched");
				Throwable t=(Throwable)msg.obj;
				AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
				adb.setTitle(R.string.error);
				adb.setMessage(t.getMessage());
				adb.setPositiveButton(R.string.ok,null);
				adb.create().show();
			}
		};
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setFullScreen ();
		Temp.createPrefManager(this,"Config");
        setContentView(R.layout.main);
        yf = new YokeFragment ();
		getFragmentManager().beginTransaction().replace(MainViewId(),new Wizard(),FRAGMENT_WIZARD).commit();
		
		}
    public void setFullScreen ( )
    {
        getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
	
    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event )
    {if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (isMainUI)
            {
                AlertDialog.Builder adb=new AlertDialog.Builder (this);
                adb.setTitle (R.string.confirm)
                    .setMessage (R.string.confirm_to_exit)
                    .setNegativeButton (R.string.cancel, null)
                    .setPositiveButton (R.string.exit, new DialogInterface.OnClickListener (){

                        @Override
                        public void onClick ( DialogInterface p1, int p2 )
							{
								if(Temp.getTelnetConnector()!=null){
										Temp.getTelnetConnector().close();
									}
                            android.os.Process.killProcess (android.os.Process.myPid ());
                            // TODO: Implement this method
                        }
                    })
                    .create ()
                    .show ();



                return true;
            }
            else
            {return super.onKeyDown (keyCode, event);}

        }
        // TODO: Implement this method
        return super.onKeyDown (keyCode, event);
    }

	@Override
	protected void onPause ()
	{
		// TODO: Implement this method
		getFragmentManager().beginTransaction().replace(MainViewId(),new Wizard(),FRAGMENT_WIZARD).commit();
		super.onPause ( );
	}

	

		
		
	
}
