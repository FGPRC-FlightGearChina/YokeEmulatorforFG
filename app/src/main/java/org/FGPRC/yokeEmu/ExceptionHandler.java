package org.FGPRC.yokeEmu;

import android.content.*;
import android.os.*;
import android.app.*;
import android.view.View.*;
import java.io.*;
import android.view.*;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler
{private static ExceptionHandler exceptionHandler;
    private ExceptionHandler(){}
    public static ExceptionHandler getInstance()
    {
        if(exceptionHandler==null)
            exceptionHandler=new ExceptionHandler();
        return exceptionHandler;
    }
    private boolean inited=false;
    private Context ctx,ce;
    private Thread.UncaughtExceptionHandler defaultHandler;
    public void init(Context ctx)
    {   ce= ctx;
        if(inited)return;
        this.ctx=ctx;
        defaultHandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread p1,Throwable p2)
    {
     
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        p2.printStackTrace(printWriter);
        String filename=Long.toHexString(System.currentTimeMillis())+".log";
        File parent=ctx.getExternalFilesDir("err-log");
        final File file=new File(parent,filename);
        try{
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(stringWriter.toString().getBytes());
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                String s ="ERROR:\n";
                new AlertDialog.Builder(ctx).setTitle("ERROR")
                .setCancelable(false)
                .setMessage(s+file.getAbsolutePath()+". ").setNeutralButton("OK", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .create().show();
                Looper.loop();
            }
        }.start();
       
    }
}


