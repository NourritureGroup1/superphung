package fragment;

import model.AuthImageDownloader;
import task.RfoodListingWorker;
import task.WorkerRestrictedFood;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.superphung.nourriture.AddRFood;
import com.superphung.nourriture.MyApplication;
import com.superphung.nourriture.MyApplication.TrackerName;
import com.superphung.nourriture.R;
import com.superphung.nourriture.RemoveRFood;

@SuppressLint("ValidFragment")
public class RestrictedFoodFragment extends Fragment {
	ImageView selectedImage;  
	private Context context;
	private View rootView;
	private ImageLoader imageLoader;

	public RestrictedFoodFragment(){}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		rootView = inflater.inflate(R.layout.grid, container, false);
		imageLoader = ImageLoader.getInstance();
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
		/*DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().build();*/
		Builder configBuilder = new ImageLoaderConfiguration.Builder(getActivity());
		configBuilder.imageDownloader(new AuthImageDownloader(getActivity(), 100, 100));
		configBuilder.defaultDisplayImageOptions(options);
		ImageLoaderConfiguration config=configBuilder.build();
		imageLoader.init(config);
		new RfoodListingWorker(context, rootView, imageLoader, this).execute();
		final RadioButton confirm = (RadioButton)rootView.findViewById(R.id.add);
		final RadioButton return_button = (RadioButton)rootView.findViewById(R.id.refresh);
		RadioButton delete = (RadioButton)rootView.findViewById(R.id.delete);
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(context, RemoveRFood.class);
				startActivityForResult(myIntent,111);
			}
		});
		
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(context, AddRFood.class);
				startActivityForResult(myIntent,111);			
			}
		});

		return_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new WorkerRestrictedFood(context, rootView, imageLoader).execute();
			}
		});
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Restricted food Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*for (int i = 0; i < Globals.rfood.size();i++) {
			Globals.rfood.get(i).setChecked(false);
		}*/
		new RfoodListingWorker(context, rootView, imageLoader, this).execute();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
