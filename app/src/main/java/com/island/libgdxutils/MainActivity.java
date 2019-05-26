package com.island.libgdxutils;
import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.view.*;
public class MainActivity extends Activity 
{
	public static final int PERMISSION_RESULT_CODE=2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		permission();
    }
	public void textureatlas(View v)
	{
		open(Executer.TEXTURE_ATLAS);
	}
	public void bitmapfont(View v)
	{
		open(Executer.BITMAP_FONT);
	}
	public void texturein(View v)
	{
		instructions(R.layout.texturein);
	}
	public void fontin(View v)
	{
		instructions(R.layout.fontin);
	}
	public void daein(View v)
	{
		instructions(R.layout.daein);
	}
	public void open(int extra)
	{
		Intent i=new Intent(this,Executer.class);
		i.putExtra(Executer.EXTRA_TYPE,extra);
		startActivity(i);
	}
	public void instructions(int layout)
	{
		Intent i=new Intent(this,Instructions.class);
		i.putExtra(Instructions.EXTRA_LAYOUT,layout);
		startActivity(i);
	}
	public void mostra()
	{
		setContentView(R.layout.title);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode,String[]permissions,int[]grantResults)
	{
		if(requestCode==PERMISSION_RESULT_CODE)
		{
			if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
			{
				mostra();
			}
			else permission();
		}
	}
	public void permission(View view)
	{
		requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_RESULT_CODE);
	}
	public void permission()
	{
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
		{
			mostra();
		}
		else
		{
			if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
			{
				setContentView(R.layout.permission);
			}
			else
			{
				setContentView(R.layout.permission);
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_RESULT_CODE);
			}
		}
	}
}
