package task;

import java.util.ArrayList;
import java.util.List;

import model.Ingredient;
import model.MainDatas;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import adapter.RestrictedFoodAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

public class WorkerRestrictedFood extends AsyncTask<String, Void, String> {
	private ProgressDialog progress;
	private Context context;
	private GridView gallery;
	private View rootView;
	private ImageLoader imageLoader;
	private ArrayList<Ingredient> CustomListViewValuesArr;
	private RestrictedFoodAdapter adapteur;
	public static final String URL_API = "https://54.64.212.101";
	private MainDatas MainActivityDatas;

	public WorkerRestrictedFood(Context c_, View rootView_, ImageLoader imageLoader_, MainDatas MainActivityDatas_) {
		context = c_;
		rootView = rootView_;
		imageLoader = imageLoader_;
		MainActivityDatas = MainActivityDatas_;
		CustomListViewValuesArr = new ArrayList<Ingredient>();
	}

	@Override
	protected void onPreExecute() {
		progress = new ProgressDialog(context);
		progress.setTitle("Loading");
		progress.setMessage("Wait while loading...");
		progress.show();
	}


	@Override
	protected String doInBackground(String... params) {
		return loadMore();
	}

	@Override
	protected void onPostExecute(String result) {
		progress.dismiss(); 
		System.out.println("result =>"+result);
		if (result.equals("error"))
		{
			Toast toast = Toast.makeText(context, "no internet connection available", Toast.LENGTH_SHORT);
			toast.show();
			return ;
		}
		gallery = (GridView) rootView.findViewById(R.id.gridview);
		if (gallery != null) {
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
			adapteur = new RestrictedFoodAdapter(context, 0, 0, CustomListViewValuesArr);
			adapteur.setImageLoader(imageLoader);
			gallery.setAdapter(adapteur);
			adapteur.notifyDataSetChanged();
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {}


	public String loadMore()
	{
		System.out.println("je load 1");
		if (helpers.haveNetworkConnection(context))
		{
			System.out.println("je load 2");
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			String readJSON = helpers.getDatas(URL_API+"/user/"+MainActivityDatas.user.getId()+"/rfood",parameters, "GET");
			System.out.println("je load 3");
			try{
				System.out.println(readJSON);
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
}