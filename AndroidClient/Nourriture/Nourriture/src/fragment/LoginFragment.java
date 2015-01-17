package fragment;

import java.util.Arrays;

import model.AuthentificationFacebook;
import model.AuthentificationGoogle;
import model.AuthentificationLocal;
import model.MainDatas;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.MyApplication;
import com.superphung.nourriture.MyApplication.TrackerName;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

public class LoginFragment extends Fragment implements OnClickListener {
	private Context context;
	private View rootView;
	private Bundle savedInstanceState;

	public LoginFragment(){
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState_) 
	{
		rootView = inflater.inflate(R.layout.fragment_login, container, false);
		context = getActivity();
		savedInstanceState = savedInstanceState_;

		Button loginButton = (Button)rootView.findViewById(R.id.login_button);
		TextView registerButton = (TextView)rootView.findViewById(R.id.register_button);
		SignInButton btnSignInGoogle = (SignInButton)rootView.findViewById(R.id.btn_sign_in_google);
		LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
		
		authButton.setReadPermissions(Arrays.asList("public_profile"));
		authButton.setReadPermissions(Arrays.asList("email "));
		
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		btnSignInGoogle.setOnClickListener(this);
		authButton.setOnClickListener(this);
		return rootView;
	}


	/**
	 * Button on click listener
	 * */
	@Override
	public void onClick(View v) {
		System.out.println("JE COMPTE LE NB DE CLICK §");
		if (v.getId() == R.id.login_button)
			normalAuth();
		else if (v.getId() == R.id.btn_sign_in_google)
			googleAuth();
		else if (v.getId() == R.id.authButton)
			facebookAuth();
		else if (v.getId() == R.id.register_button)
			registration();
	}

	private void registration() {				
		Fragment fragment = null;
		fragment = new RegisterFragment();
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
			((Activity) context).setTitle(((Activity) context).getResources().getStringArray(R.array.nav_drawer_items)[1]);
		}
	}

	private void normalAuth() {
		if (((MainActivity) context).auth != null)
			if (((MainActivity) context).auth.isAuthenticationInProgress())
				return ;
		String email = ((EditText)rootView.findViewById(R.id.email)).getText().toString();
		String password = ((EditText)rootView.findViewById(R.id.password)).getText().toString();
		email = email.replace(" ", "");
		if (!helpers.isValidEmail(email)) {
			Toast toast = Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT);
			toast.show(); 
			return ;
		}
		else {
			((MainActivity) context).auth = new AuthentificationLocal(context, email, password);
			((MainActivity) context).auth.proceedAuthentication();
		}
	}

	private void googleAuth() {
		if (((MainActivity) context).auth != null)
			if (((MainActivity) context).auth.isAuthenticationInProgress())
				return ;
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (resultCode == ConnectionResult.SUCCESS) {
			((MainActivity)context).auth = new AuthentificationGoogle(context);
			((MainActivity)context).auth.init();
			((MainActivity)context).auth.start();
			((MainActivity)context).auth.proceedAuthentication();
		}
		else {
			Toast toast = Toast.makeText(context, "Google Play services are not available at the moment.", Toast.LENGTH_SHORT);
			toast.show(); 
		}
	}
	
	private void facebookAuth() {
		if (((MainActivity) context).auth != null)
			if (((MainActivity) context).auth.isAuthenticationInProgress())
				return ;
		((MainActivity)context).auth = new AuthentificationFacebook(context, savedInstanceState);
		((MainActivity)context).auth.init();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		/*Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Login Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());*/
	}
}
