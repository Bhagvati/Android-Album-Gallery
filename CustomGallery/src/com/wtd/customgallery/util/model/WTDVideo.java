package com.wtd.customgallery.util.model;

import java.io.Serializable;

public class WTDVideo implements Serializable{
	
	private static final long serialVersionUID = -352949483095264794L;
	
	public long _ID = 0;
	public String title = "";
	public String path = "";
	public String bucketName = "";
	public String bucketID = "";
	public String thumbnailPath = "";
	public String duration = "";
	public String size = "";
	public boolean isSelected = false;
	public boolean isImage = true;
	
}
