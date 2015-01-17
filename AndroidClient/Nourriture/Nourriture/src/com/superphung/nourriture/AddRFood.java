package com.superphung.nourriture;

import java.util.ArrayList;
import java.util.List;

import model.AuthImageDownloader;
import model.Ingredient;
import task.WorkerAddRestrictedFood;
import task.AllowedFoodListingWorker;
import adapter.RestrictedFoodAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;

public class AddRFood extends Activity {
	private ImageLoader imageLoader;
	private RestrictedFoodAdapter adapteur_autocomplete;
	private List<Ingredient> list_autocomplete;

	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.grid_add);
		imageLoader = ImageLoader.getInstance();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().build();
		Builder configBuilder = new ImageLoaderConfiguration.Builder(this);
		configBuilder.imageDownloader(new AuthImageDownloader(this, 100, 100));
		configBuilder.defaultDisplayImageOptions(defaultOptions);
		ImageLoaderConfiguration config=configBuilder.build();
		imageLoader.init(config);
		new AllowedFoodListingWorker(this, getWindow().getDecorView().getRootView(),imageLoader, "rfood").execute();
		final RadioButton confirm = (RadioButton)findViewById(R.id.add);
		final RadioButton return_button = (RadioButton)findViewById(R.id.refresh);
		RadioButton delete = (RadioButton)findViewById(R.id.delete);

		return_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Globals.rfood = new ArrayList<Ingredient>();
				finish();
			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String arrayfood = "";
				if (Globals.rfood != null) {
					int i = 0;
					for(Ingredient food : Globals.rfood) {
						if (food.getChecked()) {
							String tmp = food.getId();
							if (!arrayfood.equals(""))
								arrayfood += ",";
							arrayfood += tmp;
							i++;
						}							
					}
					new WorkerAddRestrictedFood(getApplicationContext(), arrayfood).execute();
					finish();
				}
			}
		});
	}
}
