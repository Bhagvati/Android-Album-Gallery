package com.wtd.customgallery;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.GridView;

import com.wtd.customgallery.adapters.AlbumListAdapter;
import com.wtd.customgallery.util.MediaDAO;
import com.wtd.customgallery.util.Utility;
import com.wtd.customgallery.util.model.Album;

public class ImageAlbumListActivity extends Activity {

	private GridView gridViewServices;
	List<Album> albumlist;
	private AlbumListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wtd_activity_album_list);
		gridViewServices = (GridView) findViewById(R.id.gcGridViewServices);
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
				Log.e("ImageAlbumList", "path "+extra.getString("path"));
				Intent d = new Intent();
				d.putExtra("path", extra.getString("path"));
				setResult(RESULT_OK, d);
				finish();
			}
			break;
		}
	}
	public Activity getActivity() {
		return ImageAlbumListActivity.this;
	}

	public class DisplayImage extends AsyncTask<String, Void, String> {
		ProgressDialog pdilog;
		private List<String> thumbList;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			thumbList = new ArrayList<String>();
			pdilog = ProgressDialog.show(ImageAlbumListActivity.this, "",
					"loading data....");
		}

		@Override
		protected String doInBackground(String... params) {

			final String orderBy = MediaStore.Images.Media.BUCKET_ID;
			final ContentResolver resolver = getContentResolver();
			String[] projection = new String[] {
					MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
					MediaStore.Images.Media.BUCKET_ID };

			Cursor imagecursor = resolver.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
					null, null, orderBy);

			long previousid = 0;

			int bucketColumn = imagecursor
					.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

			int bucketcolumnid = imagecursor
					.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
			albumlist = new ArrayList<Album>();
			while (imagecursor.moveToNext()) {

				long bucketid = imagecursor.getInt(bucketcolumnid);
				if (previousid != bucketid) {
					Album album = new Album();
					album.bucketid = bucketid;
					album.bucketname = imagecursor.getString(bucketColumn);
					album.counter++;
					albumlist.add(album);
					previousid = bucketid;
					String path = MediaDAO.getLastPhotoThumbnailFromAllPhotosStr(
							ImageAlbumListActivity.this,album.bucketid);
					thumbList.add(path);
				}else{
					if(albumlist.size()>1)
						albumlist.get(albumlist.size()-1).counter++;
				}
			}
			imagecursor.close();
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pdilog != null)
				pdilog.dismiss();
			pdilog = null;

			if (result != null) {
				if (result.equals("")) {
					adapter = new AlbumListAdapter(getActivity(), albumlist);
					adapter.setThumbList(thumbList);
					gridViewServices.setAdapter(adapter);
				}
			}
			thumbList.clear();
		}
	}

}
