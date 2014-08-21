package com.wtd.customgallery;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.wtd.customgallery.util.MediaDAO;
import com.wtd.customgallery.util.Utility;
import com.wtd.customgallery.util.model.Album;
import com.wtd.customgallery.util.model.WTDVideo;

public class VideoGalleryActivity extends Activity {
	private static final String TAG = VideoGalleryActivity.class.getSimpleName();
	private int count;
	private Bitmap[] thumbnails;
	private boolean[] thumbnailsselection;
//	private String[] arrPath;
	private List<WTDVideo> list = new ArrayList<WTDVideo>();
	private ImageAdapter imageAdapter;
	private Album album;
	private VideoGalleryActivity activity;
	public List<String> selection;
	 ViewHolder holder;
	 private List<WTDVideo> newselectionlist = new ArrayList<WTDVideo>();
		private List<WTDVideo> newsendselectionlist = new ArrayList<WTDVideo>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wtd_activity_custom_gallery);
		activity = this;

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
					Toast.makeText(VideoGalleryActivity.this,
							"Total images : " + newsendselectionlist.size(),
							1000).show();
					// Intent i = new
					// Intent(CustomGalleryActivity.this,ChooseFileActivity.class);
					// i.putParcelableArrayListExtra("list", (ArrayList<?
					// extends Parcelable>) newsendselectionlist);
					// startActivity(i);
				} else {
					Toast.makeText(VideoGalleryActivity.this,
							"please select Image", 1000).show();
				}

			}
		});
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		album = (Album)b.getSerializable("album");
		new DisplayImage().execute(); 
	}

	public class DisplayImage extends AsyncTask<String, Void, String> {
		private ProgressDialog pdilog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdilog = ProgressDialog.show(VideoGalleryActivity.this, "",
					"loading data....");
		}
		@Override
		protected String doInBackground(String... params) {
			final ContentResolver resolver = getContentResolver();
		    final String selection = MediaStore.Video.Media.BUCKET_ID + " = ?";
		    final String[] selectionArgs = {album.bucketid+""};
		   
			Cursor imagecursor = resolver.query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
					 selection, selectionArgs, null);
			
			int image_column_index = imagecursor.getColumnIndex(MediaStore.Video.Media._ID);
			count = imagecursor.getCount();
			thumbnails = new Bitmap[count];
			thumbnailsselection = new boolean[count];
			int i=0;
			while(imagecursor.moveToNext()){
				int id = imagecursor.getInt(image_column_index);
				int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Video.Media.DATA);
				Bitmap b = MediaStore.Video.Thumbnails.getThumbnail(resolver, id,
						MediaStore.Video.Thumbnails.MINI_KIND, null);
				
				String path = imagecursor.getString(dataColumnIndex);
				String thumbPath = MediaDAO.getVideoMediaThumbnailsPathByID(
						VideoGalleryActivity.this, id);
				WTDVideo detail = Utility.getVideoDetailsModel(imagecursor,
						thumbPath);

				list.add(detail);

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
					GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
					imagegrid.setVerticalScrollBarEnabled(false);
					imagegrid.setHorizontalScrollBarEnabled(false);
					imagegrid.setNumColumns(3);
					imageAdapter = new ImageAdapter();
					imagegrid.setAdapter(imageAdapter);
				}
			}
		}
	}
	
	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, final ViewGroup parent) {
			
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.wtd_galleryitem, null);
				holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
				holder.imageViewTick = (ImageView) convertView
						.findViewById(R.id.gcImageViewTick);
				holder.imageVewVideo = (ImageView) convertView
						.findViewById(R.id.gcImageViewVideo);
				holder.imageViewTick.setId(position);
				selection = new ArrayList<String>();
				holder.imageview.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ImageView selectimage = (ImageView) v.getTag();
						WTDVideo media = (WTDVideo) selectimage.getTag();
						media.isSelected = !media.isSelected;
						if (media.isSelected) {
							selectimage.setVisibility(View.VISIBLE);

						} else {
							selectimage.setVisibility(View.INVISIBLE);
						}
					}
				});

				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			WTDVideo media = list.get(position);
			if(media.isImage)
			{
				holder.imageVewVideo.setVisibility(View.INVISIBLE);
			}
			else {
				holder.imageVewVideo.setVisibility(View.VISIBLE);
			}
			if (media.isSelected) {
				holder.imageViewTick.setVisibility(View.VISIBLE);

			} else {
				holder.imageViewTick.setVisibility(View.INVISIBLE);
			}
			holder.imageViewTick.setTag(media);
			holder.imageview.setTag(holder.imageViewTick);
			Log.e("tag", media.path);
			holder.id = position;
			return convertView;
		}
	}
	class ViewHolder {
		ImageView imageview;
		int id;
		public ImageView imageViewTick;
		public ImageView imageVewVideo;
	}
}