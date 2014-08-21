package com.wtd.customgallery.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtd.customgallery.CustomGalleryActivity;
import com.wtd.customgallery.R;
import com.wtd.customgallery.util.Utility;
import com.wtd.customgallery.util.model.Album;

public class AlbumListAdapter extends BaseAdapter {

	public static LayoutInflater inflater;
	public final Activity context;
	public List<Album> albumlist;
	int so;
	Bitmap bmp;
	int THUMBNAIL_SIZE = 100;
	public List<String> thumbList = new ArrayList<String>();
	public AlbumListAdapter(final Activity context, List<Album> albumlist) {
		this.context = context;
		this.albumlist = albumlist;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return albumlist.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.wtd_album_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) vi
					.findViewById(R.id.gcImageViewService);
			holder.textViewServiceTitle = (TextView) vi
					.findViewById(R.id.gcTextViewServiceTitle);
			holder.counterView = (TextView) vi
					.findViewById(R.id.gcTextCounter);
			holder.imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Album a = (Album) v.getTag();
					Intent i = new Intent(context, CustomGalleryActivity.class);
					i.putExtra("album", a);
						context.startActivityForResult(i,Utility.CHOOSE_IMAGE_REQUEST);
				}
			});
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		String path = thumbList.get(position);
		
		Log.e("imagename", "path " + path);
		if (path.isEmpty())
			return null;

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = Utility.calculateInSampleSize(opts, THUMBNAIL_SIZE,
				THUMBNAIL_SIZE);
		opts.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, opts);
		holder.imageView.setImageBitmap(bmp);
		holder.textViewServiceTitle.setText(albumlist.get(position).bucketname);
		holder.counterView.setText(albumlist.get(position).counter+"");
		holder.imageView.setTag(albumlist.get(position));
		return vi;
	}
	public void setThumbList(List<String> _thumbList){
		for(String thumbPath : _thumbList)
			thumbList.add(thumbPath);
	}
	private class ViewHolder {
		private ImageView imageView;
		private TextView textViewServiceTitle;
		private TextView counterView;
	}
}
