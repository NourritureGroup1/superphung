package task;

import java.util.ArrayList;
import java.util.List;

import model.Ingredient;
import model.MainDatas;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

import adapter.RestrictedFoodAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WorkerIngredientFood extends AsyncTask<String, Void, String> {
	private ProgressDialog progress;
	private Context context;
	private ArrayList<Ingredient> listAllIngredients;
	private RestrictedFoodAdapter adapteur;
	public static final String URL_API = "https://192.168.0.103:8081";
	private MainDatas MainActivityDatas;
	private ImageLoader imageLoader;
	private String type;
	private View rootview;
	
	public WorkerIngredientFood(Context c_, View rootView_,ImageLoader imageLoader_, MainDatas MainActivityDatas_, String type_) {
		context = c_;
		MainActivityDatas = MainActivityDatas_;
		listAllIngredients = new ArrayList<Ingredient>();
		type = type_;
		rootview = rootView_;
		imageLoader = imageLoader_;
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
		if (result.equals("error"))
		{
			Toast toast = Toast.makeText(context, "no internet connection available", Toast.LENGTH_SHORT);
			toast.show();
			return ;
		}
		GridView gallery = (GridView) rootview.findViewById(R.id.gridview);
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
			adapteur = new RestrictedFoodAdapter(context, 0, 0, listAllIngredients);
			adapteur.setImageLoader(imageLoader);
			gallery.setAdapter(adapteur);
			adapteur.notifyDataSetChanged();
		}
		progress.dismiss();
	}

	@Override
	protected void onProgressUpdate(Void... values) {}
	
	
	public String loadMore()
	{
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			String readJSONIngredients = helpers.getDatas(URL_API+"/ingredient",parameters, "GET");
			String readJSONUserRestrictedFood = helpers.getDatas(URL_API+"/user/"+MainActivityDatas.user.getId()+"/rfood",parameters, "GET");
			try{
				JSONArray jsonArrayIngredients = new JSONArray(readJSONIngredients);
				for(int i=0;i<jsonArrayIngredients.length();i++)
				{
					JSONObject curr = jsonArrayIngredients.getJSONObject(i);
					Ingredient tmp = new Ingredient(curr.getString("_id"),curr.getString("imgUrl"),curr.getString("description"),curr.getString("name"),null,null);	
					listAllIngredients.add(tmp);				
				}
				JSONArray jsonArrayUserRestrictedFood = new JSONArray(readJSONUserRestrictedFood);
				ArrayList<Ingredient> listAllIngredientsTmp = new ArrayList<Ingredient>();
				for(int i=0;i<jsonArrayUserRestrictedFood.length();i++)
				{
					JSONObject curr = jsonArrayUserRestrictedFood.getJSONObject(i);
					Ingredient tmp = new Ingredient(curr.getString("_id"),curr.getString("imgUrl"),curr.getString("description"),curr.getString("name"),null,null);	
					
					for(int index=0;index < listAllIngredients.size();++index) {
						if (!tmp.getId().equals(listAllIngredients.get(index).getId())) {
							System.out.println("je compare le "+tmp.getName()+"et le "+listAllIngredients.get(index).getName());
							listAllIngredientsTmp.add(listAllIngredients.get(index));
						}
					}
				}
				listAllIngredients = listAllIngredientsTmp;
			} catch(Exception e){e.printStackTrace();}
			finally{System.out.println("Success");}
		}
		else
			return "error";
		return "success";
	}
}