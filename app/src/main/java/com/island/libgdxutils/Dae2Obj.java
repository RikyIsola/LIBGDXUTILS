package com.island.libgdxutils;
import android.graphics.*;
import java.io.*;
import java.util.*;
import android.content.*;
import android.net.*;

public class Dae2Obj
{
	public static void obj(ContentResolver resolver,File file,Uri uri)throws IOException
	{
		Scanner in=new Scanner(resolver.openInputStream(uri));
		boolean mesh=false;
		FileOutputStream out=new FileOutputStream(file);
		int lunghezza=0;
		int totale=0;
		int ultimoTotale=0;
		StringBuilder pezzo1=new StringBuilder();
		StringBuilder pezzo2=new StringBuilder();
		StringBuilder pezzo3=new StringBuilder();
		while(in.hasNextLine())
		{
			String letto=in.nextLine();
			if(letto.contains("</mesh>"))
			{
				mesh=false;
				lunghezza=0;
			}
			else if(mesh)
			{
				if(letto.contains("_position_array\" count"))
				{
					int inizio=letto.indexOf(">");
					int fine=letto.lastIndexOf("<");
					String nuovo=letto.substring(inizio+1,fine)+" ";
					float x=0,y=0,z=0;
					for(int n=0;n<nuovo.length();n++)
					{
						char c=nuovo.charAt(n);
						if(c==" ".charAt(0))
						{
							if(lunghezza%3==0)
							{
								x=Float.valueOf(pezzo1.toString())/256;
							}
							else if(lunghezza%3==1)
							{
								y=Float.valueOf(pezzo2.toString())/256;
							}
							else
							{
								z=Float.valueOf(pezzo3.toString())/256;
								new Vertice(x,y,z);
								pezzo1.setLength(0);
								pezzo2.setLength(0);
								pezzo3.setLength(0);
								totale++;
							}
							lunghezza++;
						}
						else
						{
							if(lunghezza%3==0)pezzo1.append(c);
							else if(lunghezza%3==1)pezzo2.append(c);
							else pezzo3.append(c);
						}
					}
				}
				else if(letto.contains("_normal_array\" count"))
				{
					int inizio=letto.indexOf(">");
					int fine=letto.lastIndexOf("<");
					String nuovo=letto.substring(inizio+1,fine)+" ";
					char[]caratteri=nuovo.toCharArray();
					float x=0,y=0,z=0;
					for(char c:caratteri)
					{
						if(c==" ".charAt(0))
						{
							if(lunghezza%3==0)
							{
								x=Float.valueOf(pezzo1.toString());
							}
							else if(lunghezza%3==1)
							{
								y=Float.valueOf(pezzo2.toString());
							}
							else
							{
								z=Float.valueOf(pezzo3.toString());
								new Normal(x,y,z);
								pezzo1.setLength(0);
								pezzo2.setLength(0);
								pezzo3.setLength(0);
							}
							lunghezza++;
						}
						else
						{
							if(lunghezza%3==0)pezzo1.append(c);
							else if(lunghezza%3==1)pezzo2.append(c);
							else pezzo3.append(c);
						}
					}
				}
				else if(letto.contains("\"mat_"))
				{
					int inizio=letto.indexOf("mat_");
					int fine=letto.lastIndexOf("\"");
					String nuovo=letto.substring(inizio+4,fine)+" ";
					/*float a=arrotonda(Integer.parseInt(nuovo.substring(0,1),16)/15.0f,1);
					 float x=(int)(a*9);*/
					float r=arrotonda(Integer.parseInt(nuovo.substring(2,3),16)/15.0f,1);
					float x=(int)(r*9)*0.001f;
					float g=arrotonda(Integer.parseInt(nuovo.substring(4,5),16)/15.0f,1);
					x+=(int)(g*9)*0.01f;
					float b=arrotonda(Integer.parseInt(nuovo.substring(6,7),16)/15.0f,1);
					x+=(int)(b*9)*0.1f;
					x+=0.0005f;
					for(int n=ultimoTotale;n<totale;n++)new Texture(arrotonda(x,4));
					ultimoTotale=totale;
				}
			}
			else if(letto.contains("<mesh>"))mesh=true;
		}
		StringBuilder finale=new StringBuilder();
		StringBuilder vertici=Vertice.salva();
		StringBuilder normali=Normal.salva();
		StringBuilder texture=Texture.salva();
		finale.ensureCapacity(30+vertici.length()*2+normali.length()*2+texture.length()*2);
		finale.append("mtllib model.mtl").append(System.lineSeparator());
		finale.append(Vertice.salva());
		finale.append(Normal.salva());
		finale.append(Texture.salva());
		finale.append("usemtl model").append(System.lineSeparator());
		for(int a=0;a<Vertice.lista.size();a+=3)
		{
			finale.append("f ");
			Vertice.ottieni(finale,a+2);
			finale.append("/");
			Texture.ottieni(finale,a+2);
			finale.append("/");
			Normal.ottieni(finale,a+2);
			finale.append(" ");
			Vertice.ottieni(finale,a+1);
			finale.append("/");
			Texture.ottieni(finale,a+1);
			finale.append("/");
			Normal.ottieni(finale,a+1);
			finale.append(" ");
			Vertice.ottieni(finale,a);
			finale.append("/");
			Texture.ottieni(finale,a);
			finale.append("/");
			Normal.ottieni(finale,a);
			finale.append(System.lineSeparator());
		}
		out.write(finale.toString().getBytes());
		in.close();
		out.flush();
		out.close();
	}
	public static void mtl(File f)throws IOException
	{
		FileOutputStream out=new FileOutputStream(f);
		StringBuilder file=new StringBuilder();
		file.append("newmtl model").append(System.lineSeparator()).append("map_Kd model.png");
		out.write(file.toString().getBytes());
		out.flush();
		out.close();
	}
	public static void png(File f)throws IOException
	{
		FileOutputStream out=new FileOutputStream(f);
		Bitmap b=Bitmap.createBitmap(1000,1,Bitmap.Config.ARGB_8888);
		b.eraseColor(Color.TRANSPARENT);
		Canvas c=new Canvas(b);
		Paint p=new Paint();
		for(int r=0;r<10;r++)for(int g=0;g<10;g++)for(int blu=0;blu<10;blu++)
				{
					p.setColor(Color.rgb(r*256/10,g*256/10,blu*256/10));
					c.drawRect(r+g*10+blu*100,0,r+g*10+blu*100+1,c.getHeight(),p);
				}
		b.compress(Bitmap.CompressFormat.PNG,100,out);
		out.flush();
		out.close();
	}
	private static float arrotonda(float val,int p)
	{
		int pow=(int)Math.pow(10,p);
		val*=pow;
		val=Math.round(val);
		return val/pow;
	}
	private static class Normal
	{
		public static ArrayList<Normal>lista=new ArrayList<Normal>(Vertice.cache);
		public Normal(float x,float y,float z)
		{
			this.x=x;
			this.y=y;
			this.z=z;
			int copia=lista.indexOf(this);
			if(lista.size()==0)scalature=0;
			else scalature=lista.get(lista.size()-1).scalature;
			if(copia!=-1)
			{
				scalature++;
				ref=lista.get(copia);
			}
			lista.add(this);
			index=lista.size();
		}
		float x,y,z;
		Normal ref;
		int index,scalature;
		public boolean copia()
		{
			return ref!=null;
		}
		public boolean equals(Object obj)
		{
			if(obj instanceof Normal)
			{
				Normal v=(Normal)obj;
				return x==v.x&&y==v.y&&z==v.z;
			}
			return super.equals(obj);
		}
		public void salva(StringBuilder sb)
		{
			if(!copia())
			{
				sb.append("vn ");
				if(x==(int)x)sb.append((int)x);
				else sb.append(x);
				sb.append(" ");
				if(y==(int)y)sb.append((int)y);
				else sb.append(y);
				sb.append(" ");
				if(z==(int)z)sb.append((int)z);
				else sb.append(z);
				sb.append(System.lineSeparator());
			}
		}
		public static StringBuilder salva()
		{
			StringBuilder sb=new StringBuilder();
			sb.ensureCapacity(lista.size()*13);
			for(Normal v:lista)v.salva(sb);
			return sb;
		}
		public static void ottieni(StringBuilder sb,int n)
		{
			Normal v=lista.get(n);
			while(v.copia())v=v.ref;
			sb.append(v.index-v.scalature);
		}
	}
	private static class Texture
	{
		public static ArrayList<Texture>lista=new ArrayList<Texture>(Vertice.cache/10);
		public Texture(float t)
		{
			this.t=t;
			int copia=lista.indexOf(this);
			if(lista.size()==0)scalature=0;
			else scalature=lista.get(lista.size()-1).scalature;
			if(copia!=-1)
			{
				scalature++;
				ref=lista.get(copia);
			}
			lista.add(this);
			index=lista.size();
		}
		float t;
		Texture ref;
		int index,scalature;
		public boolean copia()
		{
			return ref!=null;
		}
		public boolean equals(Object obj)
		{
			if(obj instanceof Texture)
			{
				Texture v=(Texture)obj;
				return t==v.t;
			}
			return super.equals(obj);
		}
		public void salva(StringBuilder sb)
		{
			if(!copia())
			{
				sb.append("vt ");
				if(t==(int)t)sb.append((int)t);
				else sb.append(t);
				sb.append(" 1");
				sb.append(System.lineSeparator());
			}
		}
		public static StringBuilder salva()
		{
			StringBuilder sb=new StringBuilder();
			sb.ensureCapacity(lista.size()*13);
			for(Texture v:lista)v.salva(sb);
			return sb;
		}
		public static void ottieni(StringBuilder sb,int n)
		{
			Texture v=lista.get(n);
			while(v.copia())v=v.ref;
			sb.append(v.index-v.scalature);
		}
	}
	private static class Vertice
	{
		public static final int cache=100;
		public static ArrayList<Vertice>lista=new ArrayList<Vertice>(cache);
		public Vertice(float x,float y,float z)
		{
			this.x=x;
			this.y=y;
			this.z=z;
			int copia=lista.indexOf(this);
			if(lista.size()==0)scalature=0;
			else scalature=lista.get(lista.size()-1).scalature;
			if(copia!=-1)
			{
				scalature++;
				ref=lista.get(copia);
			}
			lista.add(this);
			index=lista.size();
		}
		float x,y,z;
		Vertice ref;
		int index,scalature;
		public boolean copia()
		{
			return ref!=null;
		}
		public boolean equals(Object obj)
		{
			if(obj instanceof Vertice)
			{
				Vertice v=(Vertice)obj;
				return x==v.x&&y==v.y&&z==v.z;
			}
			return super.equals(obj);
		}
		public void salva(StringBuilder sb)
		{
			if(!copia())
			{
				sb.append("v ");
				if(x==(int)x)sb.append((int)x);
				else sb.append(x);
				sb.append(" ");
				if(y==(int)y)sb.append((int)y);
				else sb.append(y);
				sb.append(" ");
				if(z==(int)z)sb.append((int)z);
				else sb.append(z);
				sb.append(System.lineSeparator());
			}
		}
		public static StringBuilder salva()
		{
			StringBuilder sb=new StringBuilder();
			sb.ensureCapacity(lista.size()*13);
			for(Vertice v:lista)v.salva(sb);
			return sb;
		}
		public static void ottieni(StringBuilder sb,int n)
		{
			Vertice v=lista.get(n);
			while(v.copia())v=v.ref;
			sb.append(v.index-v.scalature);
		}
	}
}
