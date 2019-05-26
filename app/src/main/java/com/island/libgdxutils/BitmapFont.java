package com.island.libgdxutils;
import android.graphics.*;
import android.net.*;
import java.io.*;
import android.content.*;
public class BitmapFont
{
	public static void execute(ContentResolver resolver,File file1,File file2,Uri[]files)throws IOException
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
		f.append("info").append(System.lineSeparator());
		f.append("common lineHeight=").append(y).append(" base=0").append(System.lineSeparator());
		f.append("page id=0 file=font.png").append(System.lineSeparator());
		f.append("chars count=").append(bitmaps.length).append(System.lineSeparator());
		for(int a=0;a<bitmaps.length;a++)
		{
			Bitmap bit=bitmaps[a];
			int xr=(a%pezzi)*x;
			int yr=(a/pezzi)*y;
			canvas.drawBitmap(bit,null,new Rect(xr,yr,xr+x,yr+y),null);
			bit.recycle();
			String nome=new File(files[a].getPath()).getName().substring(0,1);
			f.append("char id=").append((int)nome.charAt(0))
				.append(" x=").append(xr).append(" y=").append(yr).append(" width=").append(x).append(" height=").append(y)
				.append(" xoffset=0 yoffset=0").append(" xadvance=").append(x).append(System.lineSeparator());
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
