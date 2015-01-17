package fragment;

import java.util.ArrayList;
import java.util.List;

import model.AuthImageDownloader;
import model.Ingredient;
import task.WorkerGetAllIngredients;
import adapter.RestrictedFoodAdapter;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;

public class BrowseIngredientsFragment extends Fragment {
	private Context context;
	private View rootView;
	private RestrictedFoodAdapter adapteur;
	private String query_search = "";

	public BrowseIngredientsFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.grid_browse, container, false);
		context = getActivity();
		final CheckBox rfoodonly = (CheckBox) rootView.findViewById(R.id.rfoodonly);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		.cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
		Builder configBuilder = new ImageLoaderConfiguration.Builder(getActivity());
		configBuilder.imageDownloader(new AuthImageDownloader(getActivity(), 100, 100));
		configBuilder.defaultDisplayImageOptions(options);
		ImageLoaderConfiguration config=configBuilder.build();
		imageLoader.init(config);
		adapteur = new RestrictedFoodAdapter(context, 0, 0, Globals.rfood_filtered, "browse");
		new WorkerGetAllIngredients(context, rootView,adapteur, imageLoader).execute();

		SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView1);
		int id = searchView.getContext()
				.getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) searchView.findViewById(id);
		textView.setTextColor(Color.WHITE);

		rfoodonly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<Ingredient> tmp_list = new ArrayList<Ingredient>();
				for (Ingredient value : Globals.rfood_filtered) {
					if (rfoodonly.isChecked()) {
						if (Globals.MainActivityDatas.user.isItRestrictedFood(value.getId())) {
							if (query_search.equals(""))
								tmp_list.add(value);						
							else {
								if (value.getName().contains(query_search))
									tmp_list.add(value);									
							}
						}
					}
					else {
						if (query_search.equals(""))
							tmp_list.add(value);						
						else {
							System.out.println("je  check "+value.getName()+ " avec "+query_search);
							if (value.getName().contains(query_search))
								tmp_list.add(value);							
						}
					}
				}
				Globals.rfood= tmp_list;
				adapteur.notifyDataSetChanged();
			}
		});

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				query_search = "";
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				query_search = newText;
				System.out.println("search for an ingredient");
				List<Ingredient> tmp_list = new ArrayList<Ingredient>();
				for (Ingredient value : Globals.rfood_filtered) {
					if (value.getName().contains(newText)) {
						if (rfoodonly.isChecked()) {
							if (Globals.MainActivityDatas.user.isItRestrictedFood(value.getId()))
								tmp_list.add(value);
						}
						else
							tmp_list.add(value);
					}
				}
				Globals.rfood= tmp_list;
				adapteur.notifyDataSetChanged();
				return false;
			}
		});

		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		/*Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Register Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());*/
	}
}
