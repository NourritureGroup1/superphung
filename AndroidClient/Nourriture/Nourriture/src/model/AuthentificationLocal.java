package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.Session;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.helpers;

public class AuthentificationLocal extends Authentification {
	private Context context;
	private String email;
	private String password;
	private User user;
	private String type;
	private boolean authenticationProgress;

	public AuthentificationLocal(Context context_, String email_, String password_) {
		type = "local";
		isConnected = false;
		user = null;
		context = context_;
		email = email_;
		password = password_;
		authenticationProgress = false;
	}

	public void init() {}

	public String getType() {
		return type;
	}
	public  boolean isConnected() {
		return isConnected;
	}
	public  User getUser() {
		return user;
	}

	public  void proceedAuthentication() {
		authenticationProgress = true;
		new loginLocale().execute();
	}

	public void finish() {}

	public void start() {}

	@Override
	public void performactivityresult(int requestCode, int responseCode,
			Intent intent) {}


	private class loginLocale extends AsyncTask<String, String, String> {
		private ProgressDialog progress;

		@Override
		protected  void onPreExecute()
		{
			progress = new ProgressDialog(context);
			progress.setTitle("Wait a moment");
			progress.setMessage("Authentication in progress...");
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			if (helpers.haveNetworkConnection(context))
			{
				List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
				parameters.add(new BasicNameValuePair("email", email));
				parameters.add(new BasicNameValuePair("password", password));
				String readJSON = null;
				readJSON = helpers.getDatas(Globals.MainActivityDatas.urls.get("POST").get("login"),parameters,"POST");
				System.out.println(readJSON);
				if (helpers.isNumeric(readJSON))
					return readJSON;
				try{
					JSONObject datas = new JSONObject(readJSON);
					user = new User(datas.get("_id").toString(),datas.get("name").toString(),datas.get("role").toString(),email, datas.get("password").toString());
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
			authenticationProgress = false;
			if (helpers.isNumeric(result))
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
			isConnected = true;
			System.out.println("le mail est : "+email);
			((MainActivity)context).saveAuthenticator("locale",email,password);
			((MainActivity)context).loginUser(user);
		}
	}

	@Override
	public void onPause() {}

	@Override
	public void onDestroy() {
		if (Session.getActiveSession() != null) {
		    Session.getActiveSession().closeAndClearTokenInformation();
		}
		Session.setActiveSession(null);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {}

	@Override
	public void onResume() {}

	@Override
	public boolean isAuthenticationInProgress() {
		return authenticationProgress;
	}
}

