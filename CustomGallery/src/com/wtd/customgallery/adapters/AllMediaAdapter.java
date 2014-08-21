package com.wtd.customgallery.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wtd.customgallery.R;
import com.wtd.customgallery.util.Utility;
import com.wtd.customgallery.util.model.MediaSelection;
import com.wtd.customgallery.util.model.WTDVideo;

public class AllMediaAdapter extends BaseAdapter {

	public static LayoutInflater inflater;
	public final Activity context;
	public List<WTDVideo> list;
	int so;
	Bitmap bmp;
	int THUMBNAIL_SIZE = 100;
	public AllMediaAdapter(final Activity context, List<WTDVideo> albumlist) {
		this.context = context;
		this.list = albumlist;
		
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return list.size();
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
			vi = inflater.inflate(R.layout.wtd_galleryitem, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) vi
					.findViewById(R.id.thumbImage);
			holder.gcImageViewVideo = (ImageView) vi
					.findViewById(R.id.gcImageViewVideo);
			holder.imageViewTick = (ImageView) vi
					.findViewById(R.id.gcImageViewTick);	
			holder.imageViewTick.setId(position);
			
			holder.imageView.setOnClickListener(new OnClickListener() {

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

			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		WTDVideo media = list.get(position);
		if(media.isImage)
		{
			holder.gcImageViewVideo.setVisibility(View.INVISIBLE);
		}
		else {
			holder.gcImageViewVideo.setVisibility(View.VISIBLE);
		}
		if (media.isSelected) {
			holder.imageViewTick.setVisibility(View.VISIBLE);

		} else {
			holder.imageViewTick.setVisibility(View.INVISIBLE);
		}
		holder.imageView.setId(position);
		Bitmap bmp = BitmapFactory.decodeFile(media.thumbnailPath);
		if (bmp == null) {

			Bitmap thumb = ThumbnailUtils.createVideoThumbnail(media.path,
					MediaStore.Images.Thumbnails.MINI_KIND);
			holder.imageView.setImageBitmap(thumb);
		} else {
			holder.imageView.setImageBitmap(bmp);
		}
		holder.imageViewTick.setTag(media);
		holder.imageView.setTag(holder.imageViewTick);
		Log.e("tag", media.path);
	
		return vi;
	}
	private class ViewHolder {
		ImageView imageView;
		ImageView gcImageViewVideo;
		public ImageView imageViewTick;
		
	}
}
