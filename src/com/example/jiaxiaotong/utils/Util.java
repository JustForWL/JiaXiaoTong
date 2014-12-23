package com.example.jiaxiaotong.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

public class Util {
	public static BitmapDrawable getBackground(String iconAddr) {
		// TODO Auto-generated method stub
		BitmapDrawable bitmapDrawable = null;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory().getPath() + 
					"/jiaxiaotong/icon/" + iconAddr;
			File file = new File(filePath);
			if(file.canRead()) {
				bitmapDrawable = (BitmapDrawable) BitmapDrawable.createFromPath(filePath);
			}
		}
		return bitmapDrawable;
	}
	
	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(date);
	}
}
