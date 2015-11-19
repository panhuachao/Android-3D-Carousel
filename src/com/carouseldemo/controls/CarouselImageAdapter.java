package com.carouseldemo.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//Image adapter class for the Carousel
public class CarouselImageAdapter extends BaseAdapter {

		private Context mContext;
		private CarouselItem[] mImages;
		private List<Bitmap> imageList;
		
		public CarouselImageAdapter(Context c) {
			mContext = c;
		}
		
		public CarouselImageAdapter(Context _context,TypedArray images) {
			// TODO Auto-generated constructor stub
			this.mContext=_context;
			imageList=new ArrayList<Bitmap>();
			mImages = new CarouselItem[images.length()];
			for(int i = 0; i< images.length(); i++)
			{
				Bitmap originalImage = ((BitmapDrawable)images.getDrawable(i)).getBitmap();
				imageList.add(originalImage);
				
				CarouselItem item = new CarouselItem(_context);
				item.setIndex(i);
				item.setImageBitmap(originalImage);
				mImages[i] = item;
			}
		}
						
		@SuppressWarnings("unused")
		public void SetImages(TypedArray array, TypedArray names){
			SetImages(array, names, true);
		}
		
		public void SetImages(TypedArray array, TypedArray names, boolean reflected){
			
			if(names != null)
				if(array.length() != names.length())
					throw new RuntimeException("Images and names arrays length doesn't match");
			
			Drawable[] drawables = new Drawable[array.length()];
			List<Bitmap> bitmaps =new ArrayList<Bitmap>();
			List<String> nameslist=new ArrayList<String>();
			for(int i = 0; i< array.length(); i++)
			{
				drawables[i] = array.getDrawable(i);
				Bitmap originalImage = ((BitmapDrawable)drawables[i]).getBitmap();
				bitmaps.add(originalImage);
				nameslist.add(names.getString(i));
			}
			this.SetImages(bitmaps, nameslist, reflected);
		}
		
		/***
		 * 增加setimages 方法，用于adatper特定图片，名称设定
		 * @since 2015-11-19
		 * @author panhuachao
		 * @param bitmaps
		 * @param names
		 * @param reflected
		 */
		public void SetImages(List<Bitmap> bitmaps,List<String> names, boolean reflected)
		{

			final int reflectionGap = 4;
			mImages = new CarouselItem[bitmaps.size()];
			if(bitmaps != null)
				if(bitmaps.size() != names.size())
					throw new RuntimeException("Images and names arrays length doesn't match");
			
			for(int i = 0; i< bitmaps.size(); i++)
			{
				Bitmap originalImage = bitmaps.get(i);
				
				if(reflected){
					int width = originalImage.getWidth();
					int height = originalImage.getHeight();

					// This will not scale but will flip on the Y axis
					Matrix matrix = new Matrix();
					matrix.preScale(1, -1);
					
					// Create a Bitmap with the flip matrix applied to it.
					// We only want the bottom half of the image
					Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
							height / 2, width, height / 2, matrix, false);

					// Create a new bitmap with same width but taller to fit
					// reflection
					Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
							(height + height / 2), Config.ARGB_8888);

					// Create a new Canvas with the bitmap that's big enough for
					// the image plus gap plus reflection
					Canvas canvas = new Canvas(bitmapWithReflection);
					// Draw in the original image
					canvas.drawBitmap(originalImage, 0, 0, null);
					// Draw in the gap
					Paint deafaultPaint = new Paint();
					canvas.drawRect(0, height, width, height + reflectionGap,
							deafaultPaint);
					// Draw in the reflection
					canvas.drawBitmap(reflectionImage, 0, height + reflectionGap,
							null);

					// Create a shader that is a linear gradient that covers the
					// reflection
					Paint paint = new Paint();
					LinearGradient shader = new LinearGradient(0,
							originalImage.getHeight(), 0,
							bitmapWithReflection.getHeight() + reflectionGap,
							0x70ffffff, 0x00ffffff, TileMode.CLAMP);
					// Set the paint to use this shader (linear gradient)
					paint.setShader(shader);
					// Set the Transfer mode to be porter duff and destination in
					paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
					// Draw a rectangle using the paint with our linear gradient
					canvas.drawRect(0, height, width,
							bitmapWithReflection.getHeight() + reflectionGap, paint);
					
					originalImage = bitmapWithReflection;
				}
								
				CarouselItem item = new CarouselItem(mContext);
				item.setIndex(i);
				item.setImageBitmap(originalImage);
				if(names != null)
					item.setText(names.get(i));
				mImages[i] = item;
			}
		}

		public int getCount() {
			if(mImages == null)
				return 0;
			else
				return mImages.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return mImages[position];
		}

	}