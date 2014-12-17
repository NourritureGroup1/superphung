package task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.User;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.superphung.nourriture.helpers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, String, String> {
	private String email;
	private String password;
	private Context context;
	private User user;
	
	public LoginTask(String email_, String password_, Context context_)
	{
		email = email_;
		password = password_;
		context = context_;
	}
	
	@Override
	protected String doInBackground(String... params) {
		  if (helpers.haveNetworkConnection(context))
		  {
			  List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			  parameters.add(new BasicNameValuePair("email", email));
			  parameters.add(new BasicNameValuePair("password", password));
			  String readJSON = null;
			  readJSON = helpers.getDatas(context,"https://192.168.0.103:8081/login",parameters);
			  System.out.println("JE READ LE JSON");
			  System.out.println(readJSON);
			  if (readJSON == "error")
				  return "loginFailed";
		      try{
		            JSONObject datas = new JSONObject(readJSON);
		            user = new User(datas.get("name").toString(),datas.get("role").toString(),email, password);
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
		if (result.equals("loginFailed"))
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
      /*  ((MainActivity)context).setUser(user);
        new CurrentUserTask(user, context).execute();
        ((MainActivity)getActivity()).getUserMenu();
		Fragment fragment = null;
		fragment = new ProfileFragment();
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			
		}*/
	}
}
