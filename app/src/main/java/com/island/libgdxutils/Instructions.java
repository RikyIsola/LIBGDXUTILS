package com.island.libgdxutils;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
public class Instructions extends Activity
{
	public static final String EXTRA_LAYOUT="layout";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(getIntent().getIntExtra(EXTRA_LAYOUT,0));
	}
	public void playstore(View view)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=jquinn.qubism.android"));
		startActivity(intent);
	}
}
