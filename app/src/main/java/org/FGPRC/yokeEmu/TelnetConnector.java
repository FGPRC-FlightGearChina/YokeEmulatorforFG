package org.FGPRC.yokeEmu;
import java.io.*;
import java.net.*;
import org.apache.commons.net.telnet.*;
import org.apache.commons.net.util.*;

public class TelnetConnector implements Closeable
	{
		public static  String CONTROL_ELEV="";
		public static final String CONTROL_ALIR="";

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
		public void sendMessage ( String key, float data ) throws IOException
			{
				StringBuilder str=new StringBuilder();
				moutputStream.write (str.append("set ").append (key).append(" ").append(data).append("\r\n").toString().getBytes());
				moutputStream.flush ( );
			}
		public void sendMessage (String[] key,float[] data) throws IOException{
			if(key.length!=data.length){return;}
		for(int i=0;i<key.length;i++){
			sendMessage(key[i],data[i]);
		}
				
			}
		
	}
