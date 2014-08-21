package com.wtd.customgallery.util;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Media;

import com.wtd.customgallery.util.model.WTDVideo;

public class Utility {
	public static final int CHOOSE_IMAGE_REQUEST = 5656;
	public static final int CHOOSE_VIDEO_REQUEST = 5757;
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    
	    if (height > reqHeight || width > reqWidth) {

	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;

	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	    return inSampleSize;
	}
	
	public static WTDVideo getVideoDetailsModel(Cursor c, String thumbPath){
		WTDVideo details = new WTDVideo();
		details._ID = c.getLong(c.getColumnIndex(MediaStore.Video.Media._ID));
		details.bucketID = c.getString(c.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
		details.bucketName = c.getString(c.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
		details.title = c.getString(c.getColumnIndex(MediaStore.Video.Media.TITLE));
		details.path = c.getString(c.getColumnIndex(MediaStore.Video.Media.DATA));
//		details.uri = Uri.parse("file://"+details.path);
		details.thumbnailPath = thumbPath;
		details.duration = c.getString(c.getColumnIndex(MediaStore.Video.Media.DURATION));
		details.size = c.getString(c.getColumnIndex(MediaStore.Video.Media.SIZE));
		details.isImage = false;
		return details;
	}
	public static WTDVideo getImageDetailsModel(Cursor c, String thumbPath){
		WTDVideo details = new WTDVideo();
		details._ID = c.getLong(c.getColumnIndex(MediaStore.Images.Media._ID));
		details.bucketID = c.getString(c.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
		details.bucketName = c.getString(c.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
		details.title = c.getString(c.getColumnIndex(MediaStore.Images.Media.TITLE));
		details.path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
		details.thumbnailPath = thumbPath;
		details.size = c.getString(c.getColumnIndex(MediaStore.Images.Media.SIZE));
		details.isImage = true;
		return details;
	}
}
