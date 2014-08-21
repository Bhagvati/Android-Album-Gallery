package com.wtd.customgallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.wtd.customgallery.CustomGalleryActivity.DisplayImage;
import com.wtd.customgallery.CustomGalleryActivity.ImageAdapter;
import com.wtd.customgallery.adapters.AlbumListAdapter;
import com.wtd.customgallery.adapters.AllMediaAdapter;
import com.wtd.customgallery.util.MediaDAO;
import com.wtd.customgallery.util.Utility;
import com.wtd.customgallery.util.model.MediaSelection;
import com.wtd.customgallery.util.model.WTDVideo;

public class AllMediaActivity extends Activity {
	List<MediaSelection> allmedialist;
	List<MediaSelection> Imagemedialist;
	List<MediaSelection> Videomedialist;
	GridView gridViewServices;
	MediaSelection allmediaobj;
	private List<WTDVideo> list = new ArrayList<WTDVideo>();
	private List<WTDVideo> imagelist = new ArrayList<WTDVideo>();
	private List<WTDVideo> videolist = new ArrayList<WTDVideo>();
	private List<WTDVideo> newselectionlist = new ArrayList<WTDVideo>();
	private List<WTDVideo> newsendselectionlist = new ArrayList<WTDVideo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wtd_activity_custom_gallery);

		allmedialist = new ArrayList<MediaSelection>();
		Imagemedialist = new ArrayList<MediaSelection>();
		Videomedialist = new ArrayList<MediaSelection>();
		View includedLayout = findViewById(R.id.customactionbarid);
		Button usebutton = (Button) includedLayout
				.findViewById(R.id.imageButton);
		usebutton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				newselectionlist.clear();
				newselectionlist.addAll(list);
				Log.e("listsize", newselectionlist.size() + "");

				for (int i = 0; i < newselectionlist.size(); i++) {
					if (newselectionlist.get(i).isSelected) {
						newsendselectionlist.add(newselectionlist.get(i));
						Log.e("newlist", newselectionlist.get(i).path);
					}
				}

				if (newsendselectionlist.size() > 0) {
					Toast.makeText(AllMediaActivity.this,
							"Total images : " + newsendselectionlist.size(),
							1000).show();
					// Intent i = new
					// Intent(CustomGalleryActivity.this,ChooseFileActivity.class);
					// i.putParcelableArrayListExtra("list", (ArrayList<?
					// extends Parcelable>) newsendselectionlist);
					// startActivity(i);
				} else {
					Toast.makeText(AllMediaActivity.this,
							"please select Image", 1000).show();
				}

			}
		});
		new DisplayImage().execute();
		

	}
	public class DisplayImage extends AsyncTask<String, Void, String> {
		private ProgressDialog pdilog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdilog = ProgressDialog.show(AllMediaActivity.this, "",
					"loading data....");
		}

		@Override
		protected String doInBackground(String... params) {
			final ContentResolver cresolver = getContentResolver();
			Cursor vediocursor = cresolver.query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
					null);
			int video_column_index = vediocursor
					.getColumnIndex(MediaStore.Video.Media._ID);
			while (vediocursor.moveToNext()) {
				int id = vediocursor.getInt(video_column_index);

				String thumbPath = MediaDAO.getVideoMediaThumbnailsPathByID(
						AllMediaActivity.this, id);
				WTDVideo detail = Utility.getVideoDetailsModel(vediocursor,
						thumbPath);


				videolist.add(detail);

			}
			vediocursor.close();
			list.addAll(videolist);
			
			final ContentResolver resolver = getContentResolver();
			Cursor imagecursor = resolver.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
					null);
			int image_column_index = imagecursor
					.getColumnIndex(MediaStore.Images.Media._ID);
			while (imagecursor.moveToNext()) {
				int id = imagecursor.getInt(image_column_index);

				String thumbPath = MediaDAO.getImageMediaThumbnailsPathByID(
						AllMediaActivity.this, id);
				WTDVideo detail = Utility.getImageDetailsModel(imagecursor,
						thumbPath);

				imagelist.add(detail);

			}
			imagecursor.close();
			
			list.addAll(imagelist);
			
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
					AllMediaAdapter adapter = new AllMediaAdapter(AllMediaActivity.this,
							list);

					gridViewServices = (GridView) findViewById(R.id.PhoneImageGrid);
					gridViewServices.setAdapter(adapter);
				}
			}
		}
	}

}
