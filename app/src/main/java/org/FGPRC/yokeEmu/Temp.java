package org.FGPRC.yokeEmu;
import android.app.*;

public class Temp
	{private static YokeView myv;
	public static YokeView getYokeView(Activity activity){
		if(myv==null){
			myv=new YokeView(activity);
		}
		return myv;
	}



		}
