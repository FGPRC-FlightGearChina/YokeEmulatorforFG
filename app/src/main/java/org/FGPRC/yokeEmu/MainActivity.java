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
	YokeView my;
	private boolean isMainUI=true;
    private static YokeFragment yf;
	private static Throwable cacheThrowable;
	private static Handler mErrHdl;
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
        ExceptionHandler.getInstance ().init (this);
		mErrHdl=new Handler(){
			public void handleMessage(Message msg){
				Log.d("Error","Error fetched");
				Throwable t=(Throwable)msg.obj;
				AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
				adb.setTitle("ERROR");
				adb.setMessage(t.getMessage());
				adb.setPositiveButton("OK",null);
				adb.create().show();
			}
		};
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setFullScreen ();
		Temp.createPrefManager(this,"Config");
        setContentView(R.layout.main);
        yf = new YokeFragment ();
		getFragmentManager().beginTransaction().replace(MainViewId(),new Wizard()).commit();
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
                adb.setTitle ("Confirm")
                    .setMessage ("Confirm to exit?")
                    .setNegativeButton ("Cancel", null)
                    .setPositiveButton ("Exit", new DialogInterface.OnClickListener (){

                        @Override
                        public void onClick ( DialogInterface p1, int p2 )
                        {
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

		
		
	
}
