package org.FGPRC.yokeEmu;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import java.io.*;

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
				final String filename=Long.toHexString ( System.currentTimeMillis ( ) ) + ".log";
				File parent=ctx.getExternalFilesDir ( "err-log" );
				final File file=new File ( parent, filename );
				try
					{
						FileOutputStream fos=new FileOutputStream ( file );
						fos.write("Device message:\n".getBytes());
						try
							{
								fos.write ( ( "Current application version:" + ctx.getPackageManager ( ).getPackageInfo ( ctx.getPackageName ( ), 0 ).versionName + "\n" ).getBytes ( ) );
								fos.write(("Yoke style:"+Temp.getYokeViewType()+"\n").getBytes());
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
									.setMessage ( s + file.getAbsolutePath ( ) + ". " + e )
									.setNeutralButton ( "OK", new DialogInterface.OnClickListener ( ) {


											@Override
											public void onClick ( DialogInterface dialog, int which )
												{
													android.os.Process.killProcess ( android.os.Process.myPid ( ) );
												}
										} )
									.setNegativeButton ( "Send to the developer", new DialogInterface.OnClickListener ( ){

											@Override
											public void onClick ( DialogInterface p1, int p2 )
												{
													Intent intent = new Intent(Intent.ACTION_SEND); 
													String[] tos = { "chh1932383611@gmail.com" }; 
													intent.putExtra(Intent.EXTRA_EMAIL, tos); 
													intent.putExtra(Intent.EXTRA_TEXT, "Bug Report of FGYoke"); 
													intent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report"); 

													intent.putExtra(Intent.EXTRA_STREAM,new File(filename).toURI() ); 
													intent.setType("text/plain"); 
													intent.setType("message/rfc882"); 
													Intent.createChooser(intent, "Choose Email Client"); 
													ctx.startActivity(intent); 
													android.os.Process.killProcess ( android.os.Process.myPid ( ) );
													
													// TODO: Implement this method
												}
										} )
									.create ( ).show ( );
								Looper.loop ( );
							}
					}.start ( );

			}
	}


