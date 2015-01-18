package fragment;

import model.AuthImageDownloader;
import task.UserRfoodListingWorker;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.MyApplication;
import com.superphung.nourriture.MyApplication.TrackerName;
import com.superphung.nourriture.R;

@SuppressLint("ValidFragment")
public class UserRestrictedFoodFragment extends Fragment {
	ImageView selectedImage;  
	private Context context;
	private View rootView;
	private ImageLoader imageLoader;

	public UserRestrictedFoodFragment(){}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		rootView = inflater.inflate(R.layout.grid_user, container, false);
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
		new UserRfoodListingWorker(context, rootView, imageLoader, this).execute();
		Button return_profile = (Button) rootView.findViewById(R.id.add);
		return_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment fragment = null;
				fragment = new UserProfileFragment(context);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
					((Activity) context).setTitle(Globals.currentUser.getName()+"'s profile");
				}
			}
		});
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Another user's Restricted food Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());
	}
}
