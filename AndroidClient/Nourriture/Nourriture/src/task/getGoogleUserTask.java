package task;

import java.io.IOException;
import java.util.List;

import model.MainDatas;
import model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.helpers;

public class getGoogleUserTask extends AsyncTask<String, String, String> {
	private Context context;
	private User user;
	private MainDatas mainDatas;
	private List<NameValuePair>parameters;

	public getGoogleUserTask(Context context_, MainDatas mainActivityDatas,List<NameValuePair>params_)
	{
		context = context_;
		mainDatas = mainActivityDatas;
		parameters = params_;
	}

	@Override
	protected  void onPreExecute()
	{
		//showing a dialog to tell the user we are authenticating him
		mainDatas.progress = new ProgressDialog(context);
		mainDatas.progress.setTitle("Wait a moment");
		mainDatas.progress.setMessage("Authentication in progress...");
		mainDatas.progress.show();
	}

	@Override
	protected String doInBackground(String... params) {
		if (helpers.haveNetworkConnection(context))
		{
			String readJSON = null;
			try {
				String accountID = GoogleAuthUtil.getAccountId(context, parameters.get(0).getValue());
				parameters.add(new BasicNameValuePair("oauthID", accountID));
			} catch (IOException e) {
				Log.e("error:",e.getMessage());
			} catch (UserRecoverableAuthException e) {
				Log.e("error:", e.getMessage());
			} catch (GoogleAuthException e) {
				Log.e("error:", e.getMessage());
			}
			readJSON = helpers.getDatas(mainDatas.urls.get("POST").get("signin_oauth"),parameters,"POST");
			if (helpers.isNumeric(readJSON))
				return readJSON;
			try{
				JSONObject datas = new JSONObject(readJSON);
				user = new User(datas.get("_id").toString(), datas.get("name").toString(),
						datas.get("role").toString(),datas.get("email").toString(), datas.get("oauthID").toString(), "google");
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
		mainDatas.progress.dismiss();
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
