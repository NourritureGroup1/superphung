package fragment;

import java.util.List;

import model.AuthImageDownloader;
import model.Ingredient;
import model.MainDatas;
import task.WorkerIngredientFood;
import task.WorkerRestrictedFood;
import adapter.RestrictedFoodAdapter;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
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
import com.superphung.nourriture.MyApplication;
import com.superphung.nourriture.R;
import com.superphung.nourriture.MyApplication.TrackerName;

@SuppressLint("ValidFragment")
public class RestrictedFoodFragment extends Fragment {
	ImageView selectedImage;  
	private Context context;
	private View rootView;
	private ImageLoader imageLoader;
	private MainDatas MainActivityDatas;
	private RestrictedFoodAdapter adapteur_autocomplete;
	private List<Ingredient> list_autocomplete;

	public RestrictedFoodFragment(MainDatas MainActivityDatas_)
	{
		MainActivityDatas = MainActivityDatas_;
	}

	public RestrictedFoodFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		rootView = inflater.inflate(R.layout.grid, container, false);
		imageLoader = ImageLoader.getInstance();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().build();
		Builder configBuilder = new ImageLoaderConfiguration.Builder(getActivity());
		configBuilder.imageDownloader(new AuthImageDownloader(getActivity(), 100, 100));
		configBuilder.defaultDisplayImageOptions(defaultOptions);
		ImageLoaderConfiguration config=configBuilder.build();
		imageLoader.init(config);
		new WorkerRestrictedFood(context, rootView, imageLoader, MainActivityDatas).execute();
		RadioButton addRestrictedFood = (RadioButton)rootView.findViewById(R.id.add);
		RadioButton refreshRestrictedFood = (RadioButton)rootView.findViewById(R.id.refresh);

		addRestrictedFood.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new WorkerIngredientFood(context, rootView,imageLoader, MainActivityDatas,"add_restircted_food").execute();
			}
		});

		refreshRestrictedFood.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new WorkerRestrictedFood(context, rootView, imageLoader, MainActivityDatas).execute();
			}
		});

		//Button addRestrictedIngredient = (Button) rootView.findViewById(R.id.addRestrictedIngredient);
		/*list_autocomplete = new ArrayList<Ingredient>();
		adapteur_autocomplete = new RestrictedFoodAdapter(context, 0, 0, list_autocomplete);
		adapteur_autocomplete.setImageLoader(imageLoader);*/

		//final AutoCompleteTextView input = new AutoCompleteTextView(context);
		//	input.setAdapter(adapteur_autocomplete);
		//new WorkerIngredientFood(context, rootView, MainActivityDatas,adapteur_autocomplete).execute();
		//gallery.setAdapter(adapteur_autocomplete);



		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		/*Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Restricted food Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());*/
	}
}
