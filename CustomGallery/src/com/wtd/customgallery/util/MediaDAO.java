package com.wtd.customgallery.util;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class MediaDAO {
	public static Uri getLastPhotoThumbnailFromAllPhotos(final Context context,
			final long id) {
		Cursor allMediaPhotos = getAllMediaThumbnails(context, id);
		Uri uri = getLastImageThumbnailUri(allMediaPhotos);
		safelyCloseCursor(allMediaPhotos);
		if (uri == null) {
			return Uri.parse("");
		}
		return uri;
	}

	public static String getLastPhotoThumbnailFromAllPhotosStr(final Context context,
			final long id) {
		String path = getAllMediaThumbnailsPath(context, id);
		return path;
	}
	
	private static String getAllMediaThumbnailsPath(Context context, long id) {
		String path = "";
		String selection = MediaStore.Images.Media.BUCKET_ID+ " = ?";
		String bucketid = String.valueOf(id);
		Log.e("id", bucketid + "");
		String[] selectionArgs = { bucketid };
		
		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Cursor c = context.getContentResolver().query(images, null,
				selection, selectionArgs,null);
		Log.e("id", "c count "+c.getCount());
		if(c.moveToNext()){
			selection = MediaStore.Images.Thumbnails.IMAGE_ID+ " = ?";
			String photoID = c.getString(c.getColumnIndex(MediaStore.Images.Media._ID));
//			Log.e("MediaDAO", "photoid "+photoID);
			selectionArgs = new String[]{ photoID};
			
			images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
			Cursor cursor = context.getContentResolver().query(images, null,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToNext()) {
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
			}else
				path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
			cursor.close();
		}else{
			Log.e("id", "from else");
		}
		
		c.close();
		return path;
	}
	public static Cursor getAllMediaVideos(final Context context) {

		final String[] projection = new String[] { MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DATA };
		final Uri videos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		return context.getContentResolver().query(videos, projection, null,
				null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
		
		
		
	}
	public static String getVideoMediaThumbnailsPathByBucket(Context context, long id) {
		String path = "";
		String selection = MediaStore.Video.Media.BUCKET_ID+ " = ?";
		String bucketid = String.valueOf(id);
		Log.e("id", bucketid + "");
		String[] selectionArgs = { bucketid };
		
		Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		Cursor c = context.getContentResolver().query(uri, null,
				selection, selectionArgs,null);
		Log.e("id", "c count "+c.getCount());
		if(c.moveToNext()){
			selection = MediaStore.Video.Thumbnails.VIDEO_ID+ " = ?";
			String _ID = c.getString(c.getColumnIndex(MediaStore.Video.Media._ID));
			selectionArgs = new String[]{ _ID};
			
			uri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
			Cursor cursor = context.getContentResolver().query(uri, null,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToNext()) {
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
			}else
				path = c.getString(c.getColumnIndex(MediaStore.Video.Media.DATA));
			cursor.close();
		}else{
			Log.e("id", "from else");
		}
		
		c.close();
		return path;
	}
	public static String getVideoMediaThumbnailsPathByID(Context context, long id) {
		String path = "";
		String selection = MediaStore.Video.Media._ID+ " = ?";
		String[] selectionArgs = { id+"" };
		Log.e("id",id+"");
			selection = MediaStore.Video.Thumbnails.VIDEO_ID+ " = ?";
			
			Uri uri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
			Cursor cursor = context.getContentResolver().query(uri, null,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToNext()) {
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
			}
			cursor.close();
		return path;
	}
	public static String getImageMediaThumbnailsPathByID(Context context, long id) {
		String path = "";
		String selection = MediaStore.Images.Media._ID+ " = ?";
		String[] selectionArgs = { id+"" };
			selection = MediaStore.Images.Thumbnails.IMAGE_ID+ " = ?";
			
			Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
			Cursor cursor = context.getContentResolver().query(uri, null,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToNext()) {
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
			}
			cursor.close();
		return path;
	}
	public static Cursor getAllMediaThumbnails(final Context context,
			final long id) {

		String selection = MediaStore.Images.Media.BUCKET_ID+ " = ?";
		String bucketid = String.valueOf(id);
		Log.e("id", bucketid + "");
		String[] selectionArgs = { bucketid };
		
		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Cursor c = context.getContentResolver().query(images, null,
				selection, selectionArgs,null);
		Log.e("id", "c count "+c.getCount());
		if(c.moveToNext()){
			selection = MediaStore.Images.Thumbnails.IMAGE_ID+ " = ?";
			String photoID = c.getString(c.getColumnIndex(MediaStore.Images.Media._ID));
//			Log.e("MediaDAO", "photoid "+photoID);
			selectionArgs = new String[]{ photoID};
			
			images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
			Cursor cursor = context.getContentResolver().query(images, null,
					selection, selectionArgs, null);
			return context.getContentResolver().query(images, null,
					selection, selectionArgs, null);
		}else{
			Log.e("id", "from else");
		}
		
		c.close();
		return null;
	}

	private static Uri getLastImageThumbnailUri(Cursor cursor) {
		if (cursor != null && cursor.moveToFirst()) {
			return Uri.fromFile(new File(cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Thumbnails.DATA))));
		}
		return null;
	}

	private static String getLastImageThumbnailPath(Cursor cursor) {
		if (cursor != null && cursor.moveToNext()) {
//			return Uri.fromFile(new File());
			return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
//			return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		}
		return "";
	}
	public static Cursor getAllMediaPhotos(final Context context) {
		
		final String[] projection = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		return context.getContentResolver().query(images, projection, null,
				null, MediaStore.Images.Media.DATE_ADDED + " DESC");
		
		
	}
	
	public static void safelyCloseCursor(final Cursor c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
