package org.FGPRC.yokeEmu;
import java.io.*;
import java.net.*;
import org.apache.commons.net.telnet.*;
import org.apache.commons.net.util.*;
import android.os.*;
import android.util.*;

public class TelnetConnector implements Closeable
	{

		public static final String STATUS_NOT_AVAIL="NOT AVAIL";
		public static final String STATUS_DISCONNECTED="DISCONNECTED";
		public static final String STATUS_CONNECTING="CONNECTING";
		public static final String STATUS_CONNECTED="CONNECTED";
		private NetworkThread mNetworkThread;

		@Override
		public void close ( )
			{
				if ( mNetworkThread.getHandler ( ) != null )
					{
						mNetworkThread.getHandler ( ).sendEmptyMessage ( mNetworkThread.TYPE_CLOSE );}
				// TODO: Implement this method
			}


		private String mIP;
		private int mport;
		protected static String currentstatus=STATUS_NOT_AVAIL;
		public void reconnect(){
			if(mNetworkThread.getHandler()!=null){
				mNetworkThread.getHandler().sendEmptyMessage(mNetworkThread.TYPE_RECONNECT);
			}
		}
		public String getStatus ( )
			{
				return currentstatus;
			}
		public TelnetConnector ( String IP, int port ) 
			{mIP = IP;
				mport = port;
				mNetworkThread = new NetworkThread ( );
				mNetworkThread.start ( );
				



			}
		public void sendMessage ( TelnetData td )
			{if ( mNetworkThread.getHandler ( ) != null )
					{
						mNetworkThread.getHandler ( ).sendMessage ( mNetworkThread.getHandler ( ).obtainMessage ( mNetworkThread.TYPE_SEND, td ) );}
				Log.d ( "Failed to get Handler", "Handler is null!" );

			}
		private class NetworkThread extends Thread
			{
				private Handler h;
				private OutputStream moutputStream;
				private TelnetClient mtelnetclient;
				@Override
				protected static final int TYPE_SEND=11;
				protected static final int TYPE_CLOSE=12;
				protected static final int TYPE_RECONNECT=13;
				public Handler getHandler ( )
					{
						return h;
					}
				public void connect ( ) throws IOException
					{
						currentstatus = STATUS_CONNECTING;
						Log.d("org.FGPRC.yokeEmu","Connecting to"+mIP+":"+String.valueOf(mport));
						mtelnetclient = new TelnetClient ( );
						mtelnetclient.setCharset ( Charsets.toCharset ( "UTF8" ) );
						mtelnetclient.setConnectTimeout ( 5000 );
						mtelnetclient.connect ( mIP, mport );
						mtelnetclient.setKeepAlive ( true );
						moutputStream = mtelnetclient.getOutputStream ( );
						currentstatus = STATUS_CONNECTED;
					}
				public void run ( )
					{try
							{connect ( );
							}
						catch (Exception e)
							{   currentstatus = STATUS_DISCONNECTED;
								e.printStackTrace ( );
								MainActivity.showErrMsg ( e );}
						Looper.prepare ( );
						h = new Handler ( ){
								public void handleMessage ( Message msg )
									{
										switch ( msg.what )
											{

												case TYPE_SEND:
													try
														{
															TelnetData td=(TelnetData)msg.obj;
															if ( moutputStream == null )
																{Log.d ( "Failed to send data", "Outputstream is null" );return;}
															moutputStream.write ( ( td.toString ( ) + "/r/n" ).getBytes ( ) );
															moutputStream.flush ( );
														}
													catch (Exception e)
														{e.printStackTrace ( );
															MainActivity.showErrMsg ( e );}
													break;
												case TYPE_CLOSE:
													try
														{
															moutputStream.close ( );
															mtelnetclient.disconnect ( );
														}
													catch (IOException e)
														{e.printStackTrace ( );
															MainActivity.showErrMsg ( e );}
													break;
												case TYPE_RECONNECT:
													try
														{connect ( );
														}
													catch (Exception e)
														{   currentstatus = STATUS_DISCONNECTED;
															e.printStackTrace ( );
															MainActivity.showErrMsg ( e );}
													break;
											}
									}

							};
						Log.d ( "Handler", "Created" );
						Looper.loop ( );

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
						/**takes no effect */
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
