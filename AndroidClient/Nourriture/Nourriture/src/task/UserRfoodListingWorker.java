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

import fragment.UserRestrictedFoodFragment;

public class UserRfoodListingWorker  extends AsyncTask<String, Void, String> {
	private ProgressDialog progress;
	private Context context;
	private ArrayList<Ingredient> listUserRFood;
	private RestrictedFoodAdapter adapter;
	public static final String URL_API = "https://54.64.212.101";
	//public static final String URL_API = "https://192.168.0.103:8081";
	private ImageLoader imageLoader;
	private UserRestrictedFoodFragment rfoodfragment;
	private View rootview;

	public UserRfoodListingWorker(Context c_, View rootView_,ImageLoader imageLoader_, UserRestrictedFoodFragment rfoodfragment_) {
		context = c_;
		listUserRFood = new ArrayList<Ingredient>();
		rootview = rootView_;
		imageLoader = imageLoader_;
		rfoodfragment = rfoodfragment_;
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
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			String readJSONUserRestrictedFood = helpers.getDatas(URL_API+"/user/"+Globals.currentUser.getId()+"/rfood",parameters, "GET");
			try{
				if (!helpers.isNumeric(readJSONUserRestrictedFood)) {
					JSONArray jsonArrayUserRestrictedFood = new JSONArray(readJSONUserRestrictedFood);
					for(int i=0;i<jsonArrayUserRestrictedFood.length();i++)
					{
						JSONObject curr = jsonArrayUserRestrictedFood.getJSONObject(i);
						listUserRFood.add(new Ingredient(curr.getString("_id"),URL_API+curr.getString("imgUrl"),curr.getString("description"),curr.getString("name"),null,null));
					}
				}
			} catch(Exception e){e.printStackTrace();}
			finally{System.out.println("Success");}
		}
		else
			return "error";
		return "success";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progress.dismiss();
		if (result.equals("error"))
		{
			Toast toast = Toast.makeText(context, "no internet connection available", Toast.LENGTH_SHORT);
			toast.show();
			return ;
		}
		if (listUserRFood.size() == 0)
		{
			Toast toast = Toast.makeText(context, Globals.currentUser.getName()+" has no restricted food yet.", Toast.LENGTH_SHORT);
			toast.show(); 
		}
		GridView gallery = (GridView) rootview.findViewById(R.id.gridview);
		if (gallery != null) {
			adapter = new RestrictedFoodAdapter(context, 0, 0, listUserRFood, "");
			adapter.setImageLoader(imageLoader);
			adapter.notifyDataSetChanged();
			Globals.rfood = listUserRFood;
			gallery.setAdapter(adapter);
			gallery.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
					Intent myIntent = new Intent(context, SliderElements.class);
					Bundle b = new Bundle();
					b.putInt("position", position);
					myIntent.putExtras(b);
					rfoodfragment.startActivityForResult(myIntent,111);	
				}
			});
		}
	}

}
