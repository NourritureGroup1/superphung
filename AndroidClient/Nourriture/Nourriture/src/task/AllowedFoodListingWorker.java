package task;

import java.util.ArrayList;
import java.util.List;

import model.Ingredient;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import adapter.RestrictedFoodAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;
import com.superphung.nourriture.SliderElements;
import com.superphung.nourriture.helpers;

public class AllowedFoodListingWorker extends AsyncTask<String, Void, String> {
	private ProgressDialog progress;
	private Context context;
	private ArrayList<Ingredient> listAllIngredients;
	private RestrictedFoodAdapter adapteur;
	public static final String URL_API = "https://54.64.212.101";
	//public static final String URL_API = "https://192.168.0.103:8081";
	private ImageLoader imageLoader;
	private View rootview;
	private String type;

	public AllowedFoodListingWorker(Context c_, View rootView_,ImageLoader imageLoader_, String type_) {
		context = c_;
		listAllIngredients = new ArrayList<Ingredient>();
		rootview = rootView_;
		imageLoader = imageLoader_;
		type = type_;
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
				}
			});
			adapteur = new RestrictedFoodAdapter(context, 0, 0, listAllIngredients, type);
			adapteur.setImageLoader(imageLoader);
			gallery.setAdapter(adapteur);
			adapteur.notifyDataSetChanged();
			gallery.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
					Intent myIntent = new Intent(context, SliderElements.class);
					Bundle b = new Bundle();
					b.putInt("position", position);
					b.putString("type", type);
					myIntent.putExtras(b);
					context.startActivity(myIntent);	
				}
			});
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {}


	public String loadMore()
	{
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			String readJSONIngredients = helpers.getDatas(URL_API+"/ingredient",parameters, "GET");
			String readJSONUserRestrictedFood = helpers.getDatas(URL_API+"/user/"+Globals.MainActivityDatas.user.getId()+"/rfood",parameters, "GET");
			try{
				JSONArray jsonArrayIngredients = new JSONArray(readJSONIngredients);
				if (!helpers.isNumeric(readJSONUserRestrictedFood)) {
					JSONArray jsonArrayUserRestrictedFood = new JSONArray(readJSONUserRestrictedFood);
					for(int i=0;i<jsonArrayIngredients.length();i++)
					{
						JSONObject curr_all = jsonArrayIngredients.getJSONObject(i);
						Ingredient tmp_all = new Ingredient(curr_all.getString("_id"),curr_all.getString("imgUrl"),curr_all.getString("description"),curr_all.getString("name"),null,null);							
						boolean isforbidden = false;
						for(int index=0;index < jsonArrayUserRestrictedFood.length();index++)
						{
							JSONObject curr = jsonArrayUserRestrictedFood.getJSONObject(index);
							Ingredient tmp = new Ingredient(curr.getString("_id"),curr.getString("imgUrl"),curr.getString("description"),curr.getString("name"),null,null);	
							if (tmp_all.getId().equals(tmp.getId())) {
								isforbidden = true;
							}
						}
						if (isforbidden == false) {
							listAllIngredients.add(tmp_all);							
						}
					}
				}
				else {
					for(int i=0;i<jsonArrayIngredients.length();i++)
					{
						JSONObject curr_all = jsonArrayIngredients.getJSONObject(i);
						Ingredient tmp_all = new Ingredient(curr_all.getString("_id"),curr_all.getString("imgUrl"),curr_all.getString("description"),curr_all.getString("name"),null,null);							
						listAllIngredients.add(tmp_all);	
					}
				}
			} catch(Exception e){e.printStackTrace();}
			finally{System.out.println("Success");}
		}
		else
			return "error";
		return "success";
	}
}