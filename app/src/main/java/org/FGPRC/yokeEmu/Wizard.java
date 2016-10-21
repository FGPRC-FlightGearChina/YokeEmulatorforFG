package org.FGPRC.yokeEmu;
import android.app.*;
import java.util.regex.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class Wizard extends Fragment
	{
		private View v;
		private OnClickListener monclicklistener;
		private EditText edip,edport;

		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
			{
				v = inflater.inflate ( R.layout.wizard, null );
				edip = (EditText)v.findViewById ( R.id.wizardIP );
				edport = (EditText)v.findViewById ( R.id.wizardPort );
				Button go=(Button)v.findViewById ( R.id.wizardButton );
				final PreferenceManager pm=Temp.getPrefernenceManager ( );
				if ( pm != null )
					{
						edip.setText ( pm.getSharedPreference ( ).getString ( "IP", "" ) );
						edport.setText ( pm.getSharedPreference ( ).getString ( "port", "0" ) );
					}
				monclicklistener = new OnClickListener ( ){

						@Override
						public void onClick ( View p1 )
							{if ( isIP ( edip.getText ( ).toString ( ) ) )
									{pm.getSharedPreference ( ).edit ( ).putString ( "IP", edip.getText ( ).toString ( ) ).putString ( "port", edport.getText ( ).toString ( ) ).apply ( );
									getFragmentManager().beginTransaction().replace(MainActivity.MainViewId(),new YokeFragment(),"YOKE").commit();
									}
								else
									{Toast.makeText ( getActivity ( ), "Invalid IP address!", Toast.LENGTH_LONG ).show ( );
										// TODO: Implement this method
									}}
					};
				go.setOnClickListener ( monclicklistener );
				// TODO: Implement this method
				return v;
			}

		public static boolean isIP ( String addr )
			{
				if ( addr.length ( ) < 7 || addr.length ( ) > 15 || "".equals ( addr ) )
					{
						return false;
					}
				/**
				 * 判断IP格式和范围
				 */
				String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

				Pattern pat = Pattern.compile ( rexp );  

				Matcher mat = pat.matcher ( addr );  

				boolean ipAddress = mat.find ( );

				return ipAddress;
			}

	}
