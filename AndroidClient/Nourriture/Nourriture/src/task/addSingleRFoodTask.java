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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

public class addSingleRFoodTask extends AsyncTask<String, Void, String> {
	private Context context;
	private View rootView;
	private int position;
	private ArrayList<Ingredient> CustomListViewValuesArr;
	public static final String URL_API = "https://54.64.212.101";
	//	public static final String URL_API = "https://192.168.0.103:8081";


	public addSingleRFoodTask(Context c_, View rootView_, int position_) {
		context = c_;
		rootView = rootView_;
		position = position_;
		CustomListViewValuesArr = new ArrayList<Ingredient>();
	}

	@Override
	protected void onPreExecute() {
	}


	@Override
	protected String doInBackground(String... params) {
		return loadMore();
	}

	@Override
	protected void onPostExecute(String result) {
		final Button addrfood = (Button) rootView.findViewById(R.id.addrfood);
		final Button removerfood = (Button) rootView.findViewById(R.id.removerfood);
		boolean isliked = false;
		System.out.println("is restrictedfood ? "+Globals.rfood.get(position).getName());
		if (Globals.MainActivityDatas.user.isItRestrictedFood(Globals.rfood.get(position).getId()) == true)
			isliked = true;
		if (isliked == true) {
			removerfood.setVisibility(View.VISIBLE);
			System.out.println("je like le "+Globals.rfood.get(position).getName());
		}
		else {
			addrfood.setVisibility(View.VISIBLE);	
			System.out.println("je dislike le "+Globals.rfood.get(position).getName());
		}
		
		addrfood.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addrfood.setVisibility(View.GONE);
				removerfood.setVisibility(View.VISIBLE);
				String favFood = (Globals.MainActivityDatas.user.getRestrictedFood().equals("")) ? Globals.rfood.get(position).getId() : Globals.MainActivityDatas.user.getRestrictedFood() + ","+ Globals.rfood.get(position).getId();
				Globals.MainActivityDatas.user.setRestrictedFood(favFood);
				new WorkerUpdateUser(context).execute();
			}
		});
		
		removerfood.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addrfood.setVisibility(View.VISIBLE);
				removerfood.setVisibility(View.GONE);
				System.out.println();
				String favFood = ( Globals.MainActivityDatas.user.getRestrictedFood().equals(Globals.rfood.get(position).getId())) ? "" : Globals.MainActivityDatas.user.getRestrictedFood().replace(Globals.rfood.get(position).getId(), "");
				Globals.MainActivityDatas.user.setRestrictedFood(favFood);
				new WorkerUpdateUser(context).execute();
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
			String readJSON = helpers.getDatas(URL_API+"/user/"+Globals.MainActivityDatas.user.getId(),parameters, "GET");
			System.out.println(readJSON);
			try{
				if (!helpers.isNumeric(readJSON)) {
					JSONObject datas = new JSONObject(readJSON);
					Globals.MainActivityDatas.user.fillDatas(datas);
				}
			} catch(Exception e){e.printStackTrace();}
			finally{System.out.println("Success");}
		}
		return "success";
	}
}