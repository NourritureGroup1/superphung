package com.superphung.nourriture;

import adapter.MomentPagerAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class SliderElementsMoments extends Activity {

	private ImageLoader imageLoader;
	private ViewPager pager;
	private int position = 0;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		position = b.getInt("position");
		type = b.getString("type");
		setContentView(R.layout.ac_image_pager);
		imageLoader = ImageLoader.getInstance();
		pager = (ViewPager) findViewById(R.id.pager);
		Button back_button = (Button) findViewById(R.id.back_button);
		back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		//.showImageForEmptyUri(R.drawable.no_avatar)
		//.showImageOnFail(R.drawable.no_avatar)
		.resetViewBeforeLoading(true)
		.cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
		pager.setAdapter(new MomentPagerAdapter(Globals.moments, getApplicationContext(), imageLoader, options, type));
		//pager.setAdapter(new adapter.ImagePagerAdapter(Globals.rfood, getApplicationContext(),imageLoader,options, type));
		pager.setCurrentItem(position);
	}
}
