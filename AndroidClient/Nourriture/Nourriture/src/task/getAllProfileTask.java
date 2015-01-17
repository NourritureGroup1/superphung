package task;

import java.util.ArrayList;
import java.util.List;

import model.Ingredient;
import model.MainDatas;
import model.User;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import adapter.RestrictedFoodAdapter;
import adapter.UserAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

import fragment.UserProfileFragment;

public class getAllProfileTask extends AsyncTask<String, Void, String> {
	private Context context;
	private View rootView;
	private ArrayList<User> CustomListViewValuesArr;
	public static final String URL_API = "https://54.64.212.101";
	//	public static final String URL_API = "https://192.168.0.103:8081";


	public getAllProfileTask(Context c_, View rootView_) {
		context = c_;
		rootView = rootView_;
		CustomListViewValuesArr = new ArrayList<User>();
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
		ListView list = (ListView) rootView.findViewById(R.id.listView);
		ArrayAdapter adapter = new UserAdapter(context, 0, 0, CustomListViewValuesArr);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Globals.currentUser = CustomListViewValuesArr.get(position);
				Fragment fragment = new UserProfileFragment(context);
				FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
				fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();
				((Activity) context).setTitle(CustomListViewValuesArr.get(position).getName()+"'s profile");
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
			String readJSON = helpers.getDatas(URL_API+"/user",parameters, "GET");
			System.out.println(readJSON);
			try{
				if (!helpers.isNumeric(readJSON)) {
					JSONArray jsonArrayUserRestrictedFood = new JSONArray(readJSON);
					for(int i=0;i<jsonArrayUserRestrictedFood.length();i++)
					{
						JSONObject curr = jsonArrayUserRestrictedFood.getJSONObject(i);
						String password = "";
						try {
							password = curr.getString("password");
						}
						catch (Exception e) {
							password = "";
						}
						CustomListViewValuesArr.add(new User(curr.getString("_id"), curr.getString("name"), curr.getString("role"), curr.getString("email"), password));
					}
				}
			} catch(Exception e){e.printStackTrace();}
			finally{System.out.println("Success");}
		}
		return "success";
	}
}