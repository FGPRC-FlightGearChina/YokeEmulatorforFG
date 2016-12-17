package org.FGPRC.yokeEmu;
import android.app.*;
import java.util.regex.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;

public class Wizard extends Fragment
	{
		private View v;
		private OnClickListener monclicklistener;
		private EditText edip,edport;
		private Button go,setting;
		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
			{
				v = inflater.inflate ( R.layout.wizard, null );
				edip = (EditText)v.findViewById ( R.id.wizardIP );
				edport = (EditText)v.findViewById ( R.id.wizardPort );
				go = (Button)v.findViewById ( R.id.wizardButton );
				setting = (Button)v.findViewById ( R.id.wizardsetting );
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

										Temp.setTelnetConnector ( new TelnetConnector ( edip.getText ( ).toString ( ), Integer.parseInt ( edport.getText ( ).toString ( ) ) ) );

										getFragmentManager ( ).beginTransaction ( ).hide(Wizard.this).add(MainActivity.MainViewId(),new YokeFragment()).commit();
									}
								else
									{Toast.makeText ( getActivity ( ), R.string.invalid_ip_address, Toast.LENGTH_LONG ).show ( );
										// TODO: Implement this method
									}}
					};
				go.setOnClickListener ( monclicklistener );
				setting.setOnClickListener ( new OnClickListener ( ){

							@Override
							public void onClick ( View p1 )
								{
								/**
								This fragment wasn't completed
								
									MainActivity.setIsMainUI(false);
									getFragmentManager().beginTransaction().addToBackStack(MainActivity.FRAGMENT_WIZARD).hide(Wizard.this).add(MainActivity.MainViewId(),new Setting()).commit();*/
									Toast.makeText(getActivity(),"This function isn't avail now",Toast.LENGTH_LONG).show();
									// TODO: Implement this method
								}
						} );
				// TODO: Implement this method
				return v;
			}

		@Override
		public void onHiddenChanged ( boolean hidden )
			{
				// TODO: Implement this method
				super.onHiddenChanged ( hidden );
				if(hidden==false){MainActivity.setIsMainUI(true);}
			}

			
		
		public static boolean isIP ( String addr )
			{
				if ( addr.length ( ) < 7 || addr.length ( ) > 15 || "".equals ( addr ) )
					{
						return false;
					}

				String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

				Pattern pat = Pattern.compile ( rexp );  

				Matcher mat = pat.matcher ( addr );  

				boolean ipAddress = mat.find ( );

				return ipAddress;
			}

	}
