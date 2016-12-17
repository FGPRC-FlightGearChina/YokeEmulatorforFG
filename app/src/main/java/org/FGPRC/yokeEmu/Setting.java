package org.FGPRC.yokeEmu;
import android.preference.*;
import android.util.*;
import android.content.*;
import android.view.*;
import android.os.*;

public class Setting extends PreferenceFragment
	{

		@Override
		public void onCreate ( Bundle savedInstanceState )
			{
				// TODO: Implement this method
				super.onCreate ( savedInstanceState );
				addPreferencesFromResource(R.xml.settingpage);
				getPreferenceManager().setSharedPreferencesName("Config");
			}

		
		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
			{
				// TODO: Implement this method
				return super.onCreateView ( inflater, container, savedInstanceState );
			}
	
    
	
}
