package org.FGPRC.yokeEmu;
import android.app.*;
import android.graphics.*;
import android.hardware.*;
import android.os.*;
import android.view.*;
import android.widget.Toast;
import java.util.*;
import android.util.*;
import android.graphics.drawable.*;
import android.opengl.*;
public class YokeFragment extends Fragment
	{
		private View v;
		@Override
		public void onCreate ( Bundle savedInstanceState )
			{
				// TODO: Implement this method
				super.onCreate ( savedInstanceState );
				v=Temp.getYokeView(getActivity());
				
				
			}
		public YokeFragment(){
			super();
			Log.d("Fragment","Builded");
			}



		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
			{
				// TODO: Implement this method

				Log.d(getActivity().getApplicationInfo().packageName+" "+"YokeFragment's view created","Created");
				return v;}

	
	}
