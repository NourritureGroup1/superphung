package fragment;

import model.AuthImageDownloader;
import task.getAllProfileTask;
import task.getAllUserMoment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;

public class ListUserMomentsFragment extends Fragment implements OnClickListener {
	private Context context;
	private View rootView;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public ListUserMomentsFragment(){
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState_) 
	{
		rootView = inflater.inflate(R.layout.listview, container, false);
		context = getActivity();
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
		new getAllUserMoment(context, rootView, Globals.currentUser,imageLoader, options).execute();
		return rootView;
	}

	/**
	 * Button on click listener
	 * */
	@Override
	public void onClick(View v) {
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		/*Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Login Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());*/
	}
}
