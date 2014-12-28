package fragment;

import java.util.ArrayList;
import java.util.List;


import model.AuthImageDownloader;
import model.Ingredient;
import model.MainDatas;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

import adapter.Holder.ViewHolder;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class RestrictedFoodFragment extends Fragment {
	ImageView selectedImage;  
	private Context context;
	private ArrayList<Ingredient> CustomListViewValuesArr;
	private ImageAdapter adapteur;
	private GridView gallery;
	private long firstImage = 0;
	private int current_categorie = 1;
	private boolean loading = false;
	private View rootView;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageLoaderConfiguration config;
	private MainDatas MainActivityDatas;
	public static final String URL_API = "https://192.168.0.103:8081";

	public RestrictedFoodFragment(MainDatas MainActivityDatas_)
	{
		CustomListViewValuesArr = new ArrayList<Ingredient>();
		MainActivityDatas = MainActivityDatas_;
	}

	public RestrictedFoodFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		rootView = inflater.inflate(R.layout.grid, container, false);
		if (config == null)
		{
			imageLoader = ImageLoader.getInstance();

		    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().build();

		    Builder configBuilder = new ImageLoaderConfiguration.Builder(getActivity());
		    configBuilder.imageDownloader(new AuthImageDownloader(getActivity(), 100, 100));
		    configBuilder.defaultDisplayImageOptions(defaultOptions);
		    ImageLoaderConfiguration config=configBuilder.build();

		    imageLoader.init(config);
			
			/*ImageLoader.getInstance().init(config);
			imageLoader = ImageLoader.getInstance(); 	*/
		}
		gallery = (GridView) rootView.findViewById(R.id.gridview);
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				/*	if (CustomListViewValuesArr.get(position).getType().equals("categorie"))
            	{
                	current_categorie = CustomListViewValuesArr.get(position).getId();
                    fetchRestrictedFood();            		
            	}
            	else
            	{
            		imageLoader.stop();
    				Fragment fragment = null;
    				fragment = new SliderPhotosFragment(position, CustomListViewValuesArr, current_categorie);
    				if (fragment != null) {
    					FragmentManager fragmentManager = getFragmentManager();
    					fragmentManager.beginTransaction()
    							.replace(R.id.frame_container, fragment).commit();
    				}
            	}*/
			}
		});
		adapteur = new ImageAdapter(context, CustomListViewValuesArr);
		gallery.setAdapter(adapteur);
		fetchRestrictedFood();
		return rootView;
	}

	private class Worker extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;

		@Override
		protected String doInBackground(String... params) {
			loading = true;
			return loadMore();
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("error"))
			{
				Toast toast = Toast.makeText(context, "no internet connection available", Toast.LENGTH_SHORT);
				toast.show();
				return ;
			}
			firstImage += 50;
			loading = false;
			progress.dismiss(); 
			adapteur = new ImageAdapter(context, CustomListViewValuesArr);
			gallery.setAdapter(adapteur);
		}

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(context);
			progress.setTitle("Loading");
			progress.setMessage("Wait while loading...");
			progress.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {}
	}


	public void fetchRestrictedFood()
	{
		if (loading == false)
		{
			/*firstImage = 0;
			CustomListViewValuesArr = null;
			adapteur = null;
	        CustomListViewValuesArr = new ArrayList<Ingredient>();*/
			new Worker().execute();
		}
	}

	public String loadMore()
	{
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			parameters.add(new BasicNameValuePair("categorie", String.valueOf(current_categorie)));
			parameters.add(new BasicNameValuePair("start", String.valueOf(firstImage)));
			String readJSON = helpers.getDatas(URL_API+"/user/"+MainActivityDatas.user.getId()+"/rfood",parameters, "GET");
			try{
				JSONArray jsonArray = new JSONArray(readJSON);
				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject curr = jsonArray.getJSONObject(i);
					Ingredient tmp = new Ingredient(curr.getString("_id"),curr.getString("imgUrl"),curr.getString("description"),curr.getString("name"),null,null);	
					CustomListViewValuesArr.add(tmp);				
				}
			} catch(Exception e){e.printStackTrace();}
			finally{System.out.println("Success");}
		}
		else
			return "error";
		return "success";
	}


	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<Ingredient> CustomListViewValuesArr = new ArrayList<Ingredient>();
		@SuppressWarnings("unused")
		private ViewHolder holder;  

		public ImageAdapter(Context c, ArrayList<Ingredient> CustomListViewValuesArr_) {
			context = c;
			CustomListViewValuesArr = CustomListViewValuesArr_;
		}
		public int getCount() {
			return CustomListViewValuesArr.size();
		}
		public Object getItem(int position) {
			return CustomListViewValuesArr.get(position);
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater)
						context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.img = (ImageView) view.findViewById(R.id.image);
				holder.name = (TextView) view.findViewById(R.id.name);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (CustomListViewValuesArr.get(position).getImg() != null)
			{
				holder.progressBar.setVisibility(View.GONE);
				holder.img.setVisibility(View.VISIBLE);    	
				holder.img.setImageBitmap(CustomListViewValuesArr.get(position).getImg()); 	
			}
			else
			{
				holder.img.setVisibility(View.VISIBLE);
				if (CustomListViewValuesArr.get(position).getImg() == null)
				{
					imageLoader.displayImage(CustomListViewValuesArr.get(position).getImgUrl(), holder.img, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
							CustomListViewValuesArr.get(position).setImg(loadedImage);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri, View view, int current,
								int total) {
							holder.progressBar.setProgress(Math.round(100.0f * current / total));
						}
					}
							);
				}
				else
				{
					holder.progressBar.setVisibility(View.GONE);
					holder.img.setImageBitmap(CustomListViewValuesArr.get(position).getImg());
				}
			}
			holder.name.setText(CustomListViewValuesArr.get(position).getName());
			return view;
		}
	}
}
