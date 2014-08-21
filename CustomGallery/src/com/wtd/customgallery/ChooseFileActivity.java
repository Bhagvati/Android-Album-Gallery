package com.wtd.customgallery;

import com.wtd.customgallery.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ChooseFileActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String mimeType = getIntent().getType();
		
		if(mimeType.equals("image")){
			Intent intent = new Intent(this,ImageAlbumListActivity.class);
			startActivityForResult(intent, Utility.CHOOSE_IMAGE_REQUEST);
		}else if(mimeType.equals("video")){
			Intent intent = new Intent(this,VideoAlbumListActivity.class);
			startActivityForResult(intent, Utility.CHOOSE_VIDEO_REQUEST);
		}else{
			Intent intent = new Intent(this,ImageAlbumListActivity.class);
			startActivityForResult(intent, Utility.CHOOSE_IMAGE_REQUEST);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case Utility.CHOOSE_IMAGE_REQUEST:
			if(resultCode==RESULT_OK){
				Bundle extra = data.getExtras();
				Log.e("SelectImage", "path "+extra.getString("path"));
				Intent intent = new Intent();
				intent.putExtra("path", extra.getString("path"));
				setResult(RESULT_OK, intent);
				finish();
				return;
			}
			setResult(RESULT_CANCELED);
			finish();
			return;
			
		case Utility.CHOOSE_VIDEO_REQUEST:
			if(resultCode==RESULT_OK){
				Bundle extra = data.getExtras();
//				Log.e("SelectImage", "path "+extra.getString("path"));
				Intent intent = new Intent();
//				intent.putExtra("path", extra.getString("path"));
//				intent.setData(data.getData());
				intent.putExtra("wtd_video", extra.getSerializable("wtd_video"));
				setResult(RESULT_OK, intent);
				finish();
				return;
			}
			setResult(RESULT_CANCELED);
			finish();
			return;
		}
	}
}
