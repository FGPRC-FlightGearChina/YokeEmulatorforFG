package org.FGPRC.yokeEmu;
import android.app.*;
import android.content.*;

public class Temp
	{private static YokeView myv;
	private static PreferenceManager mpm;
	private static TelnetConnector mtn;

	public static void setMtn ( TelnetConnector mtn )
			{
				Temp.mtn = mtn;
			}

		public static TelnetConnector getMtn ( )
			{
				return mtn;
			}
	public static YokeView getYokeView(Activity activity){
		if(myv==null){
			myv=new YokeView(activity);
		}
		return myv;
	}
	public static void createPrefManager(Context ctx,String name){
		mpm=new PreferenceManager(ctx,name);
	}
	public static PreferenceManager getPrefernenceManager(){
		return mpm;
	}
	



		}
