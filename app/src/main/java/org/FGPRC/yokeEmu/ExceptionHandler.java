package org.FGPRC.yokeEmu;

import android.content.*;
import android.os.*;
import android.app.*;
import android.view.View.*;
import java.io.*;
import android.view.*;
import android.content.pm.PackageManager.*;
import android.content.pm.*;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler
	{private static ExceptionHandler exceptionHandler;
		private ExceptionHandler ( )
			{}
		public static ExceptionHandler getInstance ( )
			{
				if ( exceptionHandler == null )
					exceptionHandler = new ExceptionHandler ( );
				return exceptionHandler;
			}
		private boolean inited=false;
		private Context ctx,ce;
		private Thread.UncaughtExceptionHandler defaultHandler;
		public void init ( Context ctx )
			{   ce = ctx;
				if ( inited )return;
				this.ctx = ctx;
				defaultHandler = Thread.getDefaultUncaughtExceptionHandler ( );
				Thread.setDefaultUncaughtExceptionHandler ( this );
			}

		@Override
		public void uncaughtException ( Thread p1, Throwable p2 )
			{

				StringWriter stringWriter=new StringWriter ( );
				PrintWriter printWriter=new PrintWriter ( stringWriter );
				p2.printStackTrace ( printWriter );
				String filename=Long.toHexString ( System.currentTimeMillis ( ) ) + ".log";
				File parent=ctx.getExternalFilesDir ( "err-log" );
				final File file=new File ( parent, filename );
				try
					{
						FileOutputStream fos=new FileOutputStream ( file );
						fos.write("Device message:\n".getBytes());
						try
							{
								fos.write ( ( "Current application version:" + ctx.getPackageManager ( ).getPackageInfo ( ctx.getPackageName ( ), 0 ).versionName + "\n" ).getBytes ( ) );
							}
						catch (PackageManager.NameNotFoundException e)
							{}
						catch (IOException e)
							{}
						fos.write ( ( "Model:" + Build.MODEL + " " + Build.DEVICE + "\n" ).getBytes ( ) );
						fos.write(("System version:"+Build.VERSION.RELEASE+"/"+Build.VERSION.SDK+"\n").getBytes());
						fos.write(("CPU:"+Build.CPU_ABI+"/"+Build.CPU_ABI2+"\n\n").getBytes());
						fos.write("Error message:\n\n\n".getBytes());
						
						
						fos.write ( stringWriter.toString ( ).getBytes ( ) );
						fos.close ( );
					}
				catch (IOException e)
					{
						e.printStackTrace ( );

					}
				new Thread ( ) {
						@Override
						public void run ( )
							{
								Looper.prepare ( );
								String s ="A fetal error occured,the err-log was saved to the following path:\n";
								String e="\nPlease send it to the developer.";
								new AlertDialog.Builder ( ctx ).setTitle ( "Oops" )
									.setCancelable ( false )
									.setMessage ( s + file.getAbsolutePath ( ) + ". " + e ).setNeutralButton ( "OK", new DialogInterface.OnClickListener ( ) {


											@Override
											public void onClick ( DialogInterface dialog, int which )
												{
													android.os.Process.killProcess ( android.os.Process.myPid ( ) );
												}
										} )
									.create ( ).show ( );
								Looper.loop ( );
							}
					}.start ( );

			}
	}


