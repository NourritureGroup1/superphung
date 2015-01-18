package task;

import java.io.IOException;
import java.util.List;

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
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.helpers;

public class RegisterTask extends AsyncTask<String, String, String> {
	private Context context;
	private User user;
	private List<NameValuePair> parameters;
	private String type;
	private ProgressDialog progress;

	public RegisterTask(List<NameValuePair> parameters_, Context context_, String type_)
	{
		parameters = parameters_;
		context = context_;
		type = type_;
	}

	@Override
	protected  void onPreExecute()
	{
		//showing a dialog to tell the user we are authenticating him
		progress = new ProgressDialog(context);
		progress.setTitle("Wait a moment");
		progress.setMessage("Registration in progress...");
		progress.show();
	}

	@Override
	protected String doInBackground(String... params) {
		System.out.println("1type : "+type);
		if (helpers.haveNetworkConnection(context))
		{
			String readJSON = null;
			System.out.println("type : "+type);
			if (type == "google") 
				setAccessTokenGoogle();
			System.out.println("type2 : "+type);
			readJSON = helpers.getDatas(Globals.MainActivityDatas.urls.get("POST").get("signup"),parameters,"POST");
			System.out.println(readJSON);
			if (helpers.isNumeric(readJSON))
				return readJSON;
			try{
				System.out.println(readJSON);
				JSONObject datas = new JSONObject(readJSON);
				System.out.println(readJSON);
				user = new User(datas.get("_id").toString(),datas.get("name").toString(),datas.get("role").toString(),datas.get("email").toString(), "");
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
		if (result.equals("409"))
		{
			Toast toast = Toast.makeText(context, "This user already exist", Toast.LENGTH_SHORT);
			toast.show(); 
			return ;
		}			
		if (result.equals("error") || result.equals("404"))
		{
			Toast toast = Toast.makeText(context, "Fatal error: cannot connect.Try again later.", Toast.LENGTH_SHORT);
			toast.show(); 
			return ;
		}	
		user.setConnected(true);
		((MainActivity)context).loginUser(user);
	}

	private void setAccessTokenGoogle() {
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
		return;
	}
}
