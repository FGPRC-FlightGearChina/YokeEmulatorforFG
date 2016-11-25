package org.FGPRC.yokeEmu;
import android.app.*;
import android.os.*;
import android.view.*;
import android.util.*;
import android.content.*;
import java.io.*;
import android.widget.*;
public class YokeFragment extends Fragment
	{
		private View v;
		@Override
		public void onCreate ( Bundle savedInstanceState )
			{
				// TODO: Implement this method
				super.onCreate ( savedInstanceState );
				v = Temp.getYokeView ( getActivity ( ) );


			}
		public YokeFragment ( )
			{
				super ( );
				setHasOptionsMenu ( true );
				Log.d ( "Fragment", "Builded" );
			}



		@Override
		public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
			{
				// TODO: Implement this method

				Log.d ( getActivity ( ).getApplicationInfo ( ).packageName + " " + "YokeFragment's view created", "Created" );
				return v;}

		@Override
		public void onCreateOptionsMenu ( Menu menu, MenuInflater inflater )
			{
				inflater.inflate ( R.menu.yokemenu, menu );
				// TODO: Implement this method
			}

		@Override
		public boolean onOptionsItemSelected ( MenuItem item )
			{
				switch ( item.getItemId ( ) )
					{
						case R.id.calibrate:
							Temp.getYokeView(getActivity()).calibrate();
							break;
						case R.id.exit:
							AlertDialog.Builder adb0=new AlertDialog.Builder ( getActivity ( ) );
							adb0.setTitle ( R.string.confirm )
							    .setMessage ( "Confirm to quit?" )
							    .setNegativeButton ( R.string.cancel, null )
								.setPositiveButton ( R.string.exit, new DialogInterface.OnClickListener ( ){

										@Override
										public void onClick ( DialogInterface p1, int p2 )
											{
												if(Temp.getTelnetConnector()!=null){
													Temp.getTelnetConnector().close();
												}
												android.os.Process.killProcess ( android.os.Process.myPid ( ) );
												// TODO: Implement this method
											}
									} )
								.create ( ).show ( );
							break;
						case R.id.setting:
							Toast.makeText(getContext(),"0v0",Toast.LENGTH_LONG).show();
							break;
						case R.id.reconnect:
							if(Temp.getTelnetConnector()!=null){
								Temp.getTelnetConnector().reconnect();
							}
							break;
						case R.id.about:
							try
								{
									InputStream ins=getActivity ( ).getAssets ( ).open ( "LICENSE" );
									byte[] cache= new byte[ins.available ( )];
									ins.read ( cache );
									ins.close ( );
									AlertDialog.Builder adb=new AlertDialog.Builder ( getActivity ( ) );
									adb.setTitle ( "ABOUT OPENSOURCE LICENSE" )
										.setMessage ( new String ( cache ) )
										.setPositiveButton ( R.string.ok, null )
										.create ( ).show ( );
								}
							catch (IOException e)
								{}
							break;

					}
				// TODO: Implement this method
				return true;
			}




	}
