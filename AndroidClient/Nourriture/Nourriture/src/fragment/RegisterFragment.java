package fragment;

import java.util.ArrayList;
import java.util.List;

import model.MainDatas;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import task.RegisterTask;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.superphung.nourriture.MyApplication;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;
import com.superphung.nourriture.MyApplication.TrackerName;

public class RegisterFragment extends Fragment implements OnClickListener {
	private Context context;
	private View rootView;
	private MainDatas MainActivityDatas;

	public RegisterFragment(MainDatas mainDatas_){
		MainActivityDatas = mainDatas_;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_register, container, false);
		context = getActivity();
		Button registerButton = (Button)rootView.findViewById(R.id.signup_button);
		registerButton.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		String email = ((EditText)rootView.findViewById(R.id.email)).getText().toString();
		String name = ((EditText)rootView.findViewById(R.id.name)).getText().toString();
		String password = ((EditText)rootView.findViewById(R.id.password)).getText().toString();
		email = email.replace(" ", "");
		if (!helpers.isValidEmail(email)) {
			Toast toast = Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT);
			toast.show(); 
			return ;
		}
		else if (name.isEmpty()) {
			Toast toast = Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT);
			toast.show(); 
			return ;
		}
		else {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			parameters.add(new BasicNameValuePair("email", email));
			parameters.add(new BasicNameValuePair("name", name));
			parameters.add(new BasicNameValuePair("password", password));
			parameters.add(new BasicNameValuePair("role", "consumer"));
			new RegisterTask(parameters, context,MainActivityDatas,"local").execute();
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Register Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());
	}
}
