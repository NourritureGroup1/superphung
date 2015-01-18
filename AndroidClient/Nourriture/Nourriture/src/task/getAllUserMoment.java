package task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Moment;
import model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import adapter.MomentAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;
import com.superphung.nourriture.SliderElements;
import com.superphung.nourriture.SliderElementsMoments;
import com.superphung.nourriture.helpers;

public class getAllUserMoment extends AsyncTask<String, Void, String> {
	private Context context;
	private View rootView;
	private User user;
	private ImageLoader imageLoader;
	private ArrayAdapter<Moment> adapter;
	private DisplayImageOptions options;
	private ArrayList<Moment> CustomListViewValuesArr;
	public static final String URL_API = "https://54.64.212.101";
	//	public static final String URL_API = "https://192.168.0.103:8081";
	private ProgressBar progress;


	public getAllUserMoment(Context c_, View rootView_, User user_,ImageLoader imageLoader_,DisplayImageOptions options_) {
		context = c_;
		rootView = rootView_;
		user = user_;
		CustomListViewValuesArr = new ArrayList<Moment>();
		imageLoader = imageLoader_;
		options = options_;
	}

	@Override
	protected void onPreExecute() {
		progress = (ProgressBar) rootView.findViewById(R.id.prossbar);
	}


	@Override
	protected String doInBackground(String... params) {
		return loadMore();
	}

	@Override
	protected void onPostExecute(String result) {
		progress.setVisibility(View.GONE);
		if (CustomListViewValuesArr.size() == 0)
		{
			Toast toast = Toast.makeText(context, user.getName()+" didnt share any moments yet.", Toast.LENGTH_SHORT);
			toast.show(); 
		}
		ListView list = (ListView) rootView.findViewById(R.id.listView);
		Globals.moments = CustomListViewValuesArr;
		adapter = new MomentAdapter(context, 0, 0, CustomListViewValuesArr,imageLoader,options);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(context, SliderElementsMoments.class);
				Bundle b = new Bundle();
				b.putInt("position", position);
				b.putString("type", "moments");
				myIntent.putExtras(b);
				context.startActivity(myIntent);	
			}
			
		});
	}

	@Override
	protected void onProgressUpdate(Void... values) {}


	@SuppressLint("SimpleDateFormat") @SuppressWarnings("unused")
	public String loadMore()
	{
		if (helpers.haveNetworkConnection(context))
		{
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			parameters.add(new BasicNameValuePair("creator", user.getId()));
			String readJSON = helpers.getDatas(URL_API+"/momentByCreator",parameters, "POST");
			System.out.println(readJSON);
			try{
				if (!helpers.isNumeric(readJSON)) {
					System.out.println(readJSON);
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
						String imgUrl = "";
						try {
							imgUrl = curr.getString("imgUrl");
						}
						catch (Exception e) {
							imgUrl = "";
						}
						String dtStart = curr.getString("date");  
						SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");  
						try {  
							System.out.println("je parse une date ");
						    Date date = format.parse(dtStart);  
							CustomListViewValuesArr.add(new Moment(curr.getString("_id"), curr.getString("description"), URL_API+imgUrl, curr.getString("creator"), date));
						} catch (ParseException e) {  
						    e.printStackTrace();  
						}
					}
				}
			} catch(Exception e){e.printStackTrace();}
			finally{System.out.println("Success");}
		}
		return "success";
	}
}