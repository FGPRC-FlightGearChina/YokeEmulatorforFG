package org.FGPRC.yokeEmu;
import java.io.*;
import java.net.*;
import org.apache.commons.net.telnet.*;
import org.apache.commons.net.util.*;

public class TelnetConnector implements Closeable
	{

		@Override
		public void close ( ) throws IOException
			{
				moutputStream.close ( );
				mtelnetclient.disconnect ( );
				// TODO: Implement this method
			}

		private TelnetClient mtelnetclient;
		private OutputStream moutputStream;

		public TelnetConnector ( String IP, int port ) throws IOException
			{
				mtelnetclient = new TelnetClient ( );
				mtelnetclient.connect ( IP, port );
				mtelnetclient.setKeepAlive ( true );
				moutputStream = mtelnetclient.getOutputStream ( );

			}
		public void sendMessage ( TelnetData td) throws IOException
			{
				moutputStream.write ( new StringBuilder().append ( td.toString()).append ( "\r\n" ).toString ( ).getBytes ( ) );
				moutputStream.flush ( );
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
						return new StringBuilder().append(elevatoer).append(",").append(aliron).append(",").append(rudder).toString();
					}
				
			}
	}
