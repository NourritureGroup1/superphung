package task;

import java.util.ArrayList;
import java.util.List;

import model.Ingredient;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.BrowseFoodAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;
import com.superphung.nourriture.SliderElements;
import com.superphung.nourriture.helpers;

public class WorkerGetAllIngredients extends AsyncTask<String, Void, String> {
	private Context context;
	private View rootview;
	private ImageLoader imageLoader;
	private BrowseFoodAdapter adapteur;
	public static final String URL_API = "https://54.64.212.101";
	//public static final String URL_API = "https://192.168.0.103:8081";
	private ArrayList<Ingredient> listAllIngredients;

	public WorkerGetAllIngredients(Context c_, View rootview_, BrowseFoodAdapter adapteur_, ImageLoader imageLoader_) {
		context = c_;
		rootview = rootview_;
		listAllIngredients = new ArrayList<Ingredient>();
		imageLoader = imageLoader_;
		adapteur = adapteur_;
	}

	@Override
	protected void onPreExecute() {
	}


	@Override
	protected String doInBackground(String... params) {
		return getAll();
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("result =>"+result);
		if (result.equals("error"))
		{
			Toast toast = Toast.makeText(context, "no internet connection available", Toast.LENGTH_SHORT);
			toast.show();
			return ;
		}
		Globals.rfood = listAllIngredients;
		Globals.rfood_filtered = listAllIngredients;
		GridView gallery = (GridView) rootview.findViewById(R.id.gridview);
		if (gallery != null) {
			gallery.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				}
			});
			adapteur.setImageLoader(imageLoader);
			gallery.setAdapter(adapteur);
			adapteur.notifyDataSetChanged();
			gallery.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
					Intent myIntent = new Intent(context, SliderElements.class);
					Bundle b = new Bundle();
					b.putInt("position", position);
					b.putString("type", "browse");
					myIntent.putExtras(b);
					context.startActivity(myIntent);	
				}
			});
			if (Globals.MainActivityDatas.user.getRole().equals("gastronomist")) {
				gallery.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						alertMessage(position);
						return true;
					}
				});

			}
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {}
	public void alertMessage(final int position) { 
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) { 
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE: // Yes button clicked 
					new DeleteIngredient(context, Globals.rfood.get(position).getId()).execute();
					Globals.rfood.remove(position);
					adapteur.notifyDataSetChanged();
					break; 
				case DialogInterface.BUTTON_NEGATIVE: // No button clicked 
					break; 
				}
			} 
		}; 
		AlertDialog.Builder builder = new AlertDialog.Builder(context); 
		builder.setMessage("Are you sure you want to delete this ingredient ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show(); 
	}

	public String  getAll()
	{
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			String response = helpers.getDatas(Globals.MainActivityDatas.urls.get("GET").get("ingredient"),parameters, "GET");
			if (!helpers.isNumeric(response)) {
				try {
					JSONArray allIngredientsjson = new JSONArray(response);
					for(int i=0;i<allIngredientsjson.length();i++)
					{
						JSONObject curr_all = allIngredientsjson.getJSONObject(i);
						Ingredient tmp_all = new Ingredient(curr_all.getString("_id"),URL_API+curr_all.getString("imgUrl"),curr_all.getString("description"),curr_all.getString("name"),null,null);							
						listAllIngredients.add(tmp_all);		
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return "success";
		}
		else
			return "error";
	}
}