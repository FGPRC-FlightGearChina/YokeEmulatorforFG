package org.FGPRC.yokeEmu;
import android.app.*;
import android.content.*;

public class Temp
	{private static YokeView myv;
	private static PreferenceManager mpm;
	private static TelnetConnector mtn;

	public static void setTelnetConnector ( TelnetConnector connector )
			{
				Temp.mtn = connector;
			}

		public static TelnetConnector getTelnetConnector ( )
			{
				return mtn;
			}
	public static synchronized BoeingStyleYokeView getYokeView(Activity activity){
		if(myv==null||!(myv instanceof BoeingStyleYokeView)){
			myv=new BoeingStyleYokeView(activity);
		}
		return (BoeingStyleYokeView)myv;
	}
	public static void resetYokeView(Activity activity){
		myv=new BoeingStyleYokeView(activity);
	}
	public static void createPrefManager(Context ctx,String name){
		mpm=new PreferenceManager(ctx,name);
	}
	public static PreferenceManager getPrefernenceManager(){
		return mpm;
	}
	public static String getYokeViewType(){
		if(myv instanceof  BoeingStyleYokeView){return "Boeing style";}
		else{return "unknown";}
	}
	
	



		}
