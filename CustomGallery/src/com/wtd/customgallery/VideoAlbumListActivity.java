package com.wtd.customgallery;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.wtd.customgallery.adapters.VideoAlbumListAdapter;
import com.wtd.customgallery.util.MediaDAO;
import com.wtd.customgallery.util.Utility;
import com.wtd.customgallery.util.model.Album;

public class VideoAlbumListActivity extends Activity {

	private GridView gridViewServices;
	List<Album> albumlist;
	private VideoAlbumListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wtd_activity_album_list);
		
		DisplayImage d = new DisplayImage();
		d.execute();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Utility.CHOOSE_IMAGE_REQUEST:
			if(resultCode==RESULT_OK){
				Bundle extra = data.getExtras();
//				Log.e("ImageAlbumList", "path "+extra.getString("path"));
				Intent d = new Intent();
//				d.setDataAndType(extra.get, type)
//				d.setData(data.getData());
//				d.putExtra("path", extra.getString("path"));
				d.putExtra("wtd_video", extra.getSerializable("wtd_video"));
				setResult(RESULT_OK, d);
				finish();
			}
			break;
		}
	}
	public Activity getActivity() {
		return VideoAlbumListActivity.this;
	}

	public class DisplayImage extends AsyncTask<String, Void, String> {
		ProgressDialog pdilog;
		private List<String> thumbList;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			thumbList = new ArrayList<String>();
			pdilog = ProgressDialog.show(VideoAlbumListActivity.this, "",
					"loading data....");
		}

		@Override
		protected String doInBackground(String... params) {

			final String orderBy = MediaStore.Video.Media.BUCKET_ID;
			final ContentResolver resolver = getContentResolver();
			String[] projection = new String[] {
					MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
					MediaStore.Video.Media.BUCKET_ID };

			Cursor imagecursor = resolver.query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
					null, null, orderBy);

			long previousid = 0;

			int bucketColumn = imagecursor
					.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

			int bucketcolumnid = imagecursor
					.getColumnIndex(MediaStore.Video.Media.BUCKET_ID);
			albumlist = new ArrayList<Album>();
			Log.e("count",imagecursor.getCount()+"");
			if(imagecursor.getCount()<0)
			{
//				View view = findViewById(R.id.emptyview);
//				view.inflate(R.layout.emptyview, null);
//				view.setVisibility(View.VISIBLE);
//				
//				TextView emptyview = (TextView)view.findViewById(R.id.emptyview);
//				emptyview.setVisibility(View.VISIBLE);
				return "text";
			}
			else
			{
				gridViewServices = (GridView) findViewById(R.id.gcGridViewServices);
				while (imagecursor.moveToNext()) {

					long bucketid = imagecursor.getInt(bucketcolumnid);
					if (previousid != bucketid) {
						Album album = new Album();
						album.bucketid = bucketid;
						album.bucketname = imagecursor.getString(bucketColumn);
						album.counter++;
						albumlist.add(album);
						previousid = bucketid;
//						String path = MediaDAO.getLastPhotoThumbnailFromAllPhotosStr(
//								VideoAlbumListActivity.this,album.bucketid);
						String path = MediaDAO.getVideoMediaThumbnailsPathByBucket(
								VideoAlbumListActivity.this,album.bucketid);
						thumbList.add(path);
					}else{
						if(albumlist.size()>1)
							albumlist.get(albumlist.size()-1).counter++;
					}
				}
				imagecursor.close();
				return "";
			}
			
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pdilog != null)
				pdilog.dismiss();
			pdilog = null;

			if (result != null) {
				if (result.equals("")) {
					
					adapter = new VideoAlbumListAdapter(getActivity(), albumlist);
					adapter.setThumbList(thumbList);
					gridViewServices.setAdapter(adapter);
				}
			}
			thumbList.clear();
		}
	}

}
