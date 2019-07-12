package com.island.libgdxutils;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import java.io.*;
import android.widget.*;
public class DaeActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try
		{
			File file=Executer.prepareFile("model.obj");
			Uri uri=getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
			if(uri==null)uri=getIntent().getData();
			Dae2Obj.obj(getContentResolver(),file,uri);
			Dae2Obj.mtl(Executer.prepareFile("model.mtl"));
			Dae2Obj.png(Executer.prepareFile("model.png"));
			((TextView)findViewById(R.id.text)).setText(file.getPath());
		}
		catch(IOException e)
		{
			Toast.makeText(this,e.toString(),0).show();
		}
	}
}
