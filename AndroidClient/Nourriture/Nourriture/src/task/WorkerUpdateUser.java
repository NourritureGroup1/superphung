package task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.superphung.nourriture.Globals;
import com.superphung.nourriture.helpers;

public class WorkerUpdateUser extends AsyncTask<String, Void, String> {
	private Context context;
	public static final String URL_API = "https://54.64.212.101";
	//public static final String URL_API = "https://192.168.0.103:8081";

	public WorkerUpdateUser(Context c_) {
		context = c_;
	}

	@Override
	protected void onPreExecute() {
	}


	@Override
	protected String doInBackground(String... params) {
		return updateUser();
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
	}

	@Override
	protected void onProgressUpdate(Void... values) {}


	public String updateUser()
	{
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			parameters.add(new BasicNameValuePair("badFood", Globals.MainActivityDatas.user.getBadFood()));
			parameters.add(new BasicNameValuePair("dislikes", Globals.MainActivityDatas.user.getDislikes()));
			parameters.add(new BasicNameValuePair("favoriteFood", Globals.MainActivityDatas.user.getFavoriteFood()));
			parameters.add(new BasicNameValuePair("followings", Globals.MainActivityDatas.user.getFollowings()));
			parameters.add(new BasicNameValuePair("likes", Globals.MainActivityDatas.user.getLikes()));
			parameters.add(new BasicNameValuePair("recipesCreated", Globals.MainActivityDatas.user.getRecipesCreated()));
			parameters.add(new BasicNameValuePair("restrictedFood", Globals.MainActivityDatas.user.getRestrictedFood()));
			parameters.add(new BasicNameValuePair("email", Globals.MainActivityDatas.user.getEmail()));
			parameters.add(new BasicNameValuePair("name", Globals.MainActivityDatas.user.getName()));
			parameters.add(new BasicNameValuePair("password", Globals.MainActivityDatas.user.getPassword()));
			parameters.add(new BasicNameValuePair("_id", Globals.MainActivityDatas.user.getId()));
			parameters.add(new BasicNameValuePair("role", Globals.MainActivityDatas.user.getRole()));
			String newUserJson = helpers.getDatas(Globals.MainActivityDatas.urls.get("PUT").get("user")+"/"+Globals.MainActivityDatas.user.getId(),parameters, "PUT");
			JSONObject newdatas = null;
			try {
				newdatas = new JSONObject(newUserJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Globals.MainActivityDatas.user.fillDatas(newdatas);
			return "success";
		}
		else
			return "error";
	}
}