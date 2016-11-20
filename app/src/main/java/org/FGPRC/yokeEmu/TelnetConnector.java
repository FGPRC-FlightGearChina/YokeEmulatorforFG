package org.FGPRC.yokeEmu;
import java.io.*;
import java.net.*;
import org.apache.commons.net.telnet.*;
import org.apache.commons.net.util.*;
import android.os.*;
import android.util.*;

public class TelnetConnector implements Closeable
	{
		private NetworkThread mNetworkThread;

		@Override
		public void close ( )
			{
				if(mNetworkThread.getHandler()!=null){
				mNetworkThread.getHandler ( ).sendEmptyMessage ( mNetworkThread.TYPE_CLOSE );}
				// TODO: Implement this method
			}


		private String mIP;
		private int mport;
		public TelnetConnector ( String IP, int port ) 
			{mIP = IP;
				mport = port;
				mNetworkThread = new NetworkThread ( );
				mNetworkThread.start ( );
				



			}
		public void sendMessage ( TelnetData td )
			{if(mNetworkThread.getHandler()!=null){
				mNetworkThread.getHandler ( ).sendMessage ( mNetworkThread.getHandler ( ).obtainMessage ( mNetworkThread.TYPE_SEND, td ) );}
			 Log.d("Failed to get Handler","Handler is null!");
			 
				}
		private class NetworkThread extends Thread
			{
				private Handler h;
				private OutputStream moutputStream;
				private TelnetClient mtelnetclient;
				@Override
				protected static final int TYPE_SEND=11;
				protected static final int TYPE_CLOSE=12;
				public Handler getHandler ( )
					{
						return h;
					}
				public void run ( )
					{try
							{
								mtelnetclient = new TelnetClient ( );
								mtelnetclient.connect ( mIP, mport );
								mtelnetclient.setKeepAlive ( true );
								moutputStream = mtelnetclient.getOutputStream ( );
							}
						catch (Exception e)
							{e.printStackTrace ( );
							MainActivity.showErrMsg(e);}
						Looper.prepare();
						h = new Handler ( ){
								public void handleMessage ( Message msg )
									{
										switch ( msg.what )
											{
						
												case TYPE_SEND:
													try
														{
															TelnetData td=(TelnetData)msg.obj;
															moutputStream.write ( ( td.toString ( ) + "/r/n" ).getBytes ( ) );
															moutputStream.flush ( );
														}
													catch (Exception e)
														{e.printStackTrace ( );
														MainActivity.showErrMsg(e);}
													break;
												case TYPE_CLOSE:
													try
														{
															moutputStream.close ( );
															mtelnetclient.disconnect ( );
														}
													catch (IOException e)
														{e.printStackTrace ( );
														MainActivity.showErrMsg(e);}
													break;
											}
								Looper.loop();
									}
							};
						// TODO: Implement this method

					}

			}
		public static class TelnetData
			{
				private float aliron,elevatoer,rudder;

				public void setAliron ( float aliron )
					{
						this.aliron = aliron;
					}

				public void setElevatoer ( float elevatoer )
					{
						this.elevatoer = elevatoer;
					}
				public void setRudder ( float rudder )
					{
						/*takes no effect*/
						this.rudder = rudder;
					}
				public static TelnetData getInstance ( )
					{
						return new TelnetData ( );

					}
				public TelnetData ( )
					{}

				@Override
				public String toString ( )
					{
						// TODO: Implement this method
						return new StringBuilder ( ).append ( aliron ).append ( "," ).append ( elevatoer )./*append(",").append(rudder).*/toString ( );
					}

			}
	}
