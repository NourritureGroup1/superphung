package fragment;

import task.getAllProfileTask;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.superphung.nourriture.MyApplication;
import com.superphung.nourriture.MyApplication.TrackerName;
import com.superphung.nourriture.R;

public class ListUserFragment extends Fragment implements OnClickListener {
	private Context context;
	private View rootView;
	public ListUserFragment(){
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState_) 
	{
		rootView = inflater.inflate(R.layout.listview, container, false);
		context = getActivity();
		new getAllProfileTask(context, rootView).execute();
		return rootView;
	}

	/**
	 * Button on click listener
	 * */
	@Override
	public void onClick(View v) {
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("List users Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());
	}
}
