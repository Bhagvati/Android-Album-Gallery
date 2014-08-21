package com.wtd.customgallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gc_activity_main);
		Button photos = (Button) findViewById(R.id.gcButtonChoosePhotos);
		Button videos = (Button) findViewById(R.id.gcButtonChooseVideo);
		Button media = (Button) findViewById(R.id.gcButtonChooseMedia);

		photos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						ImageAlbumListActivity.class);
				startActivity(i);
			}
		});
		videos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						VideoAlbumListActivity.class);
				startActivity(i);
			}
		});
		media.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						AllMediaActivity.class);
				startActivity(i);
			}
		});
	}
}
