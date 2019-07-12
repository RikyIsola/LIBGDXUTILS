package com.island.libgdxutils;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.widget.*;
import java.io.*;
public class Executer extends Activity
{
	public static final int TEXTURE_ATLAS=0;
	public static final int BITMAP_FONT=1;
	public static final int FILE_RESULT_CODE=1;
	public static final String EXTRA_TYPE="type";
	public static final String path="LibgdxUtils/";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		file();
	}
	public void file()
	{
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("android.content.extra.SHOW_ADVANCED",true);
		intent.putExtra("android.content.extra.FANCY",true);
		intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
		intent.putExtra("android.content.extra.SHOW_FILESIZE",true);
		startActivityForResult(Intent.createChooser(intent,"Choose File"),FILE_RESULT_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		try
		{
			if(requestCode==FILE_RESULT_CODE)
			{
				if(resultCode==Activity.RESULT_OK)
				{
					setContentView(R.layout.main);
					ClipData clip=data.getClipData();
					Uri[]uri;
					if(clip==null)uri=new Uri[]{data.getData()};
					else
					{
						uri=new Uri[clip.getItemCount()];
						for(int a=0;a<uri.length;a++)uri[a]=clip.getItemAt(a).getUri();
					}
					final ContentResolver resolver=getContentResolver();
					final int id=getIntent().getIntExtra(EXTRA_TYPE,-1);
					final File file;
					if(id==TEXTURE_ATLAS)
					{
						file=prepareFile("atlas.png");
						File file2=prepareFile("atlas.atlas");
						TextureAtlas.texture(resolver,file,file2,uri);
					}
					else if(id==BITMAP_FONT)
					{
						file=prepareFile("font.png");
						File file2=prepareFile("font.fnt");
						BitmapFont.execute(resolver,file,file2,uri);
					}
					else throw new UnsupportedOperationException();
					((TextView)findViewById(R.id.text)).setText(file.getPath());
				}
				else finish();
			}
		}
		catch(IOException e)
		{
			Toast.makeText(this,e.toString(),0).show();
		}
	}
	public static File prepareFile(String dove)
	{
		File file=new File(Environment.getExternalStorageDirectory(),path+dove);
		file.getParentFile().mkdirs();
		return file;
	}
}
