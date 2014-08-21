package com.wtd.customgallery;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

public class CustomGalleryActivity extends Activity {
	private static final String TAG = CustomGalleryActivity.class
			.getSimpleName();
	private int count;
	private boolean[] thumbnailsselection;
	private String[] arrPath;
	private ImageAdapter imageAdapter;
	private Album album;
	private CustomGalleryActivity activity;
	GridView imagegrid;
	private List<WTDVideo> list = new ArrayList<WTDVideo>();
	private List<WTDVideo> newselectionlist = new ArrayList<WTDVideo>();
	private List<WTDVideo> newsendselectionlist = new ArrayList<WTDVideo>();

	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
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
					Toast.makeText(CustomGalleryActivity.this,
							"Total images : " + newsendselectionlist.size(),
							1000).show();
					// Intent i = new
					// Intent(CustomGalleryActivity.this,ChooseFileActivity.class);
					// i.putParcelableArrayListExtra("list", (ArrayList<?
					// extends Parcelable>) newsendselectionlist);
					// startActivity(i);
				} else {
					Toast.makeText(CustomGalleryActivity.this,
							"please select Image", 1000).show();
				}

			}
		});
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		album = (Album) b.getSerializable("album");
		new DisplayImage().execute();
	}

	public class DisplayImage extends AsyncTask<String, Void, String> {
		private ProgressDialog pdilog;
		private int THUMBNAIL_SIZE = 100;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdilog = ProgressDialog.show(CustomGalleryActivity.this, "",
					"loading data....");
		}

		@Override
		protected String doInBackground(String... params) {
			final ContentResolver resolver = getContentResolver();
			final String[] projection = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
			final String[] selectionArgs = { album.bucketid + "" };

			Cursor imagecursor = resolver.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
					selection, selectionArgs, null);

			int image_column_index = imagecursor
					.getColumnIndex(MediaStore.Images.Media._ID);
			count = imagecursor.getCount();
			arrPath = new String[count];
			thumbnailsselection = new boolean[count];
			int i = 0;
			while (imagecursor.moveToNext()) {
				int id = imagecursor.getInt(image_column_index);
//				int dataColumnIndex = imagecursor
//						.getColumnIndex(MediaStore.Images.Media.DATA);
//				String path = imagecursor.getString(dataColumnIndex);
				String thumbPath = MediaDAO.getImageMediaThumbnailsPathByID(
						CustomGalleryActivity.this, id);
				WTDVideo detail = Utility.getImageDetailsModel(imagecursor,
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
					imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
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

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.wtd_galleryitem, null);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.thumbImage);
				holder.imageViewTick = (ImageView) convertView
						.findViewById(R.id.gcImageViewTick);
				holder.imageVewVideo = (ImageView) convertView
						.findViewById(R.id.gcImageViewVideo);
				holder.imageViewTick.setId(position);
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
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			WTDVideo media = list.get(position);
			if (media.isSelected) {
				holder.imageViewTick.setVisibility(View.VISIBLE);

			} else {
				holder.imageViewTick.setVisibility(View.INVISIBLE);
			}
			holder.imageview.setId(position);
			Bitmap bmp = BitmapFactory.decodeFile(media.thumbnailPath);
			if (bmp == null) {

				Bitmap thumb = ThumbnailUtils.createVideoThumbnail(media.path,
						MediaStore.Images.Thumbnails.MINI_KIND);
				holder.imageview.setImageBitmap(thumb);
			} else {
				holder.imageview.setImageBitmap(bmp);
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