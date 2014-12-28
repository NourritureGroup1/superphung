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

import adapter.IngredientAutocompleteAdapter;
import adapter.RestrictedFoodAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WorkerIngredientFood extends AsyncTask<String, Void, String> {
	private ProgressDialog progress;
	private Context context;
	private ArrayList<Ingredient> CustomListViewValuesArr;
	private IngredientAutocompleteAdapter adapteur;
	public static final String URL_API = "https://192.168.0.103:8081";
	private MainDatas MainActivityDatas;
	private View rootView;
	
	public WorkerIngredientFood(Context c_, View rootView_, MainDatas MainActivityDatas_, IngredientAutocompleteAdapter adapteur_autocomplete_) {
		context = c_;
		MainActivityDatas = MainActivityDatas_;
		CustomListViewValuesArr = new ArrayList<Ingredient>();
		adapteur = adapteur_autocomplete_;
		rootView = rootView_;
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
		if (result.equals("error"))
		{
			Toast toast = Toast.makeText(context, "no internet connection available", Toast.LENGTH_SHORT);
			toast.show();
			return ;
		}
		progress.dismiss(); 
		adapteur = new IngredientAutocompleteAdapter(context, 0, 0, CustomListViewValuesArr);
		Button addRestrictedIngredient = (Button) rootView.findViewById(R.id.addRestrictedIngredient);
		final AutoCompleteTextView input = new AutoCompleteTextView(context);
		input.setAdapter(adapteur);
		input.setThreshold(1);
		addRestrictedIngredient.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);

				alert.setTitle("Add new restricted ingredient");

				// Set an EditText view to get user input 
//				final EditText input = new EditText(context);
				alert.setView(input);

				alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  //String value = input.getText();
				  // Do something with value!
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});

				alert.show();
			}
		});
	}

	@Override
	protected void onProgressUpdate(Void... values) {}
	
	
	public String loadMore()
	{
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			String readJSON = helpers.getDatas(URL_API+"/ingredient",parameters, "GET");
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
}