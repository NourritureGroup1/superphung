package task;

import java.util.ArrayList;
import java.util.List;

import model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.superphung.nourriture.Globals;
import com.superphung.nourriture.helpers;

public class DeleteIngredient extends AsyncTask<String, Void, String> {
	private Context context;
	private String id;
	public static final String URL_API = "https://54.64.212.101";
	//public static final String URL_API = "https://192.168.0.103:8081";

	public DeleteIngredient(Context c_, String id_) {
		context = c_;
		id = id_;
	}

	@Override
	protected void onPreExecute() {
	}


	@Override
	protected String doInBackground(String... params) {
		return delete();
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


	public String delete()
	{
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			parameters.add(new BasicNameValuePair("id", id));
			helpers.getDatas(Globals.MainActivityDatas.urls.get("POST").get("deleteIngredients"),parameters, "POST");
			return "success";
		}
		else
			return "error";
	}
}