package com.island.libgdxutils;
import android.graphics.*;
import android.os.*;
import java.io.*;
import android.content.*;
import android.net.*;
public class TextureAtlas
{
	public static void texture(ContentResolver resolver,File file1,File file2,Uri[]files)throws IOException
	{
		FileOutputStream out=new FileOutputStream(file1);
		FileOutputStream out2=new FileOutputStream(file2);
		Bitmap[]bitmaps=new Bitmap[files.length];
		BitmapFactory.Options opt=new BitmapFactory.Options();
		opt.inScaled=false;
		int width=0;
		int height=0;
		for(int a=0;a<files.length;a++)
		{
			InputStream input=resolver.openInputStream(files[a]);
			bitmaps[a]=BitmapFactory.decodeStream(input,null,opt);
			input.close();
			width+=bitmaps[a].getWidth();
			height+=bitmaps[a].getHeight();
		}
		int pezzi=2;
		while(files.length>pezzi*pezzi)pezzi*=2;
		width/=files.length;
		height/=files.length;
		int x=pow(width);
		int y=pow(height);
		if(x<y)x=y;
		else y=x;
		Bitmap immagine=Bitmap.createBitmap(x*pezzi,y*pezzi,Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(immagine);
		StringBuilder f=new StringBuilder();
		f.ensureCapacity(76+92*files.length);
		f.append("atlas.png").append(System.lineSeparator());
		f.append("size:"+x*pezzi+","+y*pezzi).append(System.lineSeparator());
		f.append("format:RGBA8888").append(System.lineSeparator());
		f.append("filter:Nearest,Nearest").append(System.lineSeparator());
		f.append("repeat:none").append(System.lineSeparator());
		for(int a=0;a<bitmaps.length;a++)
		{
			Bitmap bit=bitmaps[a];
			int size;
			int spazioX=0;
			int spazioY=0;
			if(bit.getWidth()>bit.getHeight())
			{
				size=bit.getWidth();
				spazioY=(size-bit.getHeight())/2;
			}
			else
			{
				size=bit.getHeight();
				spazioX=(size-bit.getWidth())/2;
			}
			Bitmap nuovo=Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
			Canvas c=new Canvas(nuovo);
			c.drawBitmap(bit,spazioX,spazioY,null);
			bit.recycle();
			int xr=(a%pezzi)*x;
			int yr=(a/pezzi)*y;
			canvas.drawBitmap(nuovo,null,new Rect(xr,yr,xr+x,yr+y),null);
			nuovo.recycle();
			f.append(new File(files[a].getPath()).getName().substring(0,new File(files[a].getPath()).getName().length()-4)).append(System.lineSeparator());
			f.append(" rotate:false").append(System.lineSeparator());
			f.append(" xy:").append(xr).append(",").append(yr).append(System.lineSeparator());
			f.append(" size:").append(x).append(",").append(y).append(System.lineSeparator());
			f.append(" orig:").append(x).append(",").append(y).append(System.lineSeparator());
			f.append(" offset:0,0").append(System.lineSeparator());
			f.append(" index:-1").append(System.lineSeparator());
		}
		immagine.compress(Bitmap.CompressFormat.PNG,100,out);
		out.flush();
		out.close();
		out2.write(f.toString().getBytes());
		out2.flush();
		out2.close();
	}
	private static int pow(int value)
	{
		int powprec=1;
		int pow=2;
		while(pow<value)
		{
			powprec=pow;
			pow*=2;
		}
		if(value-powprec<pow-value)value=powprec;
		else value=pow;
		return value;
	}
}
