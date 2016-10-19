package org.FGPRC.yokeEmu;

public class Movement
	{
		private static float Y_AXIS_RANGE=20;
		private static float Z_AXIS_RANGE=25;
		private static final int digital=5;
		private static final int ratio=10000;
		private float[] result;
		private float mneturalz,mneturaly,tmp;
		public Movement ( float neturalz, float neturaly )
			{
				result = new float[2];
				init ( neturalz, neturaly );

			}
		public Movement ( float neturalz, float neturaly ,float yaxisrange,float zaxisrange)
			{
				Y_AXIS_RANGE=yaxisrange;
				Z_AXIS_RANGE=zaxisrange;
				result = new float[2];
				init ( neturalz, neturaly );

			}
		
		public void init ( float neturalz, float neturaly )
			{
				mneturalz = neturalz;
				mneturaly = neturaly;
			};
		public synchronized float[] calculate ( float currentz, float currenty )
			{
				/*Caluculate z axis (pitch)value */
				result [ 0 ] = ( currentz - mneturalz ) / Z_AXIS_RANGE;
				result [ 0 ] = result [ 0 ] - result [ 0 ] * 2;
				result[0]  =  (float)(Math.round(result[0]*10000))/10000;
				if(result[0]>1){result[0]=1;}
				else if(result[0]<-1){result[0]=-1;}
				/* calculate y axis (bank)value*/
				result [ 1 ] = ( currenty - mneturaly  ) / Y_AXIS_RANGE;
				result [ 1 ] = result [ 1 ] - result [ 1 ] * 2;
				result[1]  =  (float)(Math.round(result[1]*10000))/10000;
				if(result[1]>1){result[1]=1;}
				else if(result[1]<-1){result[1]=-1;}
				
				return result;
			}
	}
