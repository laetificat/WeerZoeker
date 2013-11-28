package com.kevin.weerzoeker;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	   ImageView bmImage;

	   public DownloadImageTask(ImageView bmImage) {
	      this.bmImage = bmImage;
	   }

	   protected Bitmap doInBackground(String... urls) {
	      String url = urls[0];
	      Bitmap bmpIcon = null;
	      try {
	         InputStream in = new java.net.URL(url).openStream();
	         bmpIcon = BitmapFactory.decodeStream(in);
	      } 
	      catch (Exception e) {
	         Log.e("Error", e.getMessage());
	         e.printStackTrace();
	      }
	      return bmpIcon;
	   }

	   protected void onPostExecute(Bitmap result) {
	      bmImage.setImageBitmap(result);
	   }
	}