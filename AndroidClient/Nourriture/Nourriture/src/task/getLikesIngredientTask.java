package task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

public class getLikesIngredientTask extends AsyncTask<String, Void, String> {
	private Context context;
	private View rootView;
	private int position;
	public static final String URL_API = "https://54.64.212.101";
	//	public static final String URL_API = "https://192.168.0.103:8081";


	public getLikesIngredientTask(Context c_, View rootView_, int position_) {
		context = c_;
		rootView = rootView_;
		position = position_;
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
		final Button like = (Button) rootView.findViewById(R.id.like);
		final Button dislike = (Button) rootView.findViewById(R.id.dislike);
		boolean isliked = false;
		if (Globals.MainActivityDatas.user.doILikeIt(Globals.rfood.get(position).getId()) == true)
			isliked = true;
		if (isliked == true) {
			dislike.setVisibility(View.VISIBLE);
			System.out.println("je like le "+Globals.rfood.get(position).getName());
		}
		else {
			like.setVisibility(View.VISIBLE);	
			System.out.println("je dislike le "+Globals.rfood.get(position).getName());
		}
		
		like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				like.setVisibility(View.GONE);
				dislike.setVisibility(View.VISIBLE);
				String favFood = (Globals.MainActivityDatas.user.getFavoriteFood().equals("")) ? Globals.rfood.get(position).getId() : Globals.MainActivityDatas.user.getFavoriteFood() + ","+ Globals.rfood.get(position).getId();
				Globals.MainActivityDatas.user.setFavoriteFood(favFood);
				new WorkerUpdateUser(context).execute();
			}
		});
		dislike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				like.setVisibility(View.VISIBLE);
				dislike.setVisibility(View.GONE);
				String favFood = ( Globals.MainActivityDatas.user.getFavoriteFood().equals(Globals.rfood.get(position).getId())) ? "" : Globals.MainActivityDatas.user.getFavoriteFood().replace(","+ Globals.rfood.get(position).getId(), "");
				Globals.MainActivityDatas.user.setFavoriteFood(favFood);
				new WorkerUpdateUser(context).execute();
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
			String readJSON = helpers.getDatas(URL_API+"/user/"+Globals.MainActivityDatas.user.getId(),parameters, "GET");
			System.out.println(readJSON);
			try{
				if (!helpers.isNumeric(readJSON)) {
					JSONObject datas = new JSONObject(readJSON);
					Globals.MainActivityDatas.user.fillDatas(datas);
				}
			} catch(Exception e){e.printStackTrace();}
			finally{System.out.println("Success");}
		}
		return "success";
	}
}