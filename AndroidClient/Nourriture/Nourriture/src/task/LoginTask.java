package task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.MainDatas;
import model.User;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.helpers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, String, String> {
	private String email;
	private String password;
	private Context context;
	private User user;
	private MainDatas mainDatas;
	
	public LoginTask(String email_, String password_, Context context_, MainDatas mainActivityDatas)
	{
		email = email_;
		password = password_;
		context = context_;
		mainDatas = mainActivityDatas;
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
			  List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			  parameters.add(new BasicNameValuePair("email", email));
			  parameters.add(new BasicNameValuePair("password", password));
			  String readJSON = null;
			  readJSON = helpers.getDatas(mainDatas.urls.get("POST").get("login"),parameters,"POST");
			  if (helpers.isNumeric(readJSON))
				  return readJSON;
		      try{
		            JSONObject datas = new JSONObject(readJSON);
		            user = new User(datas.get("_id").toString(),datas.get("name").toString(),datas.get("role").toString(),email, password);
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
        ((MainActivity)context).loginUser(user);
	}
}