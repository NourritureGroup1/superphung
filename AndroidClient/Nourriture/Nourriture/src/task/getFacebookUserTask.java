package task;

import java.util.List;

import model.User;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.superphung.nourriture.Globals;
import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.helpers;

public class getFacebookUserTask extends AsyncTask<String, String, String> {
	private Context context;
	private User user;
	private List<NameValuePair>parameters;
	private ProgressDialog progress;

	public getFacebookUserTask(Context context_, List<NameValuePair>params_)
	{
		context = context_;
		parameters = params_;
	}

	@Override
	protected  void onPreExecute()
	{
		//showing a dialog to tell the user we are authenticating him
		progress = new ProgressDialog(context);
		progress.setTitle("Wait a moment");
		progress.setMessage("Authentication in progress...");
		progress.show();
	}

	@Override
	protected String doInBackground(String... params) {
		if (helpers.haveNetworkConnection(context))
		{
			String readJSON = null;
			readJSON = helpers.getDatas(Globals.MainActivityDatas.urls.get("POST").get("signin_oauth"),parameters,"POST");
			if (helpers.isNumeric(readJSON))
				return readJSON;
			try{
				JSONObject datas = new JSONObject(readJSON);
				user = new User(datas.get("_id").toString(), datas.get("name").toString(),
						datas.get("role").toString(),datas.get("email").toString(), datas.get("oauthID").toString(), "facebook");
				user.fillDatas(datas);
				return "success";
			} catch(Exception e){e.printStackTrace();}
			finally{}
		}
		else
			return "error";
		return "error";
	}

	@Override
	protected void onPostExecute(String result) {
		progress.dismiss();
		if (result.equals("loginFailed") || result.equals("404") || result.equals("409"))
		{
			Toast toast = Toast.makeText(context, "Cannot find the user with the password/username you typed", Toast.LENGTH_SHORT);
			toast.show(); 
			return ;
		}			
		if (result.equals("error"))
		{
			Toast toast = Toast.makeText(context, "Fatal error: cannot connect.Try again later.", Toast.LENGTH_SHORT);
			toast.show(); 
			return ;
		}
		user.setConnected(true);
		((MainActivity)context).loginUser(user);
	}
}
