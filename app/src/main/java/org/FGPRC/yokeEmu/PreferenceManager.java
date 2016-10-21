package org.FGPRC.yokeEmu;
import android.content.*;

public class PreferenceManager
{
	private SharedPreferences msp;
	public PreferenceManager(Context ctx,String name){
		msp=ctx.getSharedPreferences(name,0);
	}
	public SharedPreferences getSharedPreference(){
		return msp;
	}
	
}
