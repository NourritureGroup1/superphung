package fragment;

import java.util.Arrays;

import model.AuthentificationFacebook;
import model.AuthentificationGoogle;
import model.AuthentificationLocale;
import model.MainDatas;
import task.LoginTask;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

public class LoginFragment extends Fragment implements OnClickListener {
	private Context context;
	private View rootView;
	private MainDatas MainActivityDatas;

	public LoginFragment(MainDatas mainDatas_){
		MainActivityDatas = mainDatas_;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) 
	{
		rootView = inflater.inflate(R.layout.fragment_login, container, false);
		context = getActivity();

		Button loginButton = (Button)rootView.findViewById(R.id.login_button);
		TextView registerButton = (TextView)rootView.findViewById(R.id.register_button);
		SignInButton btnSignInGoogle = (SignInButton)rootView.findViewById(R.id.btn_sign_in_google);
		LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("public_profile"));
		authButton.setReadPermissions(Arrays.asList("email "));
		authButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity)context).auth = new AuthentificationFacebook(context, savedInstanceState);
				((MainActivity)context).auth.init();
			}
		});
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		btnSignInGoogle.setOnClickListener(this);
		return rootView;
	}


	/**
	 * Button on click listener
	 * */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.login_button)
			normalAuth();
		else if (v.getId() == R.id.btn_sign_in_google)
			googleAuth();
		else if (v.getId() == R.id.register_button)
			registration();
	}

	private void registration() {				
		Fragment fragment = null;
		fragment = new RegisterFragment(MainActivityDatas);
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
			((Activity) context).setTitle(((Activity) context).getResources().getStringArray(R.array.nav_drawer_items)[1]);
		}
	}

	private void normalAuth() {
		String email = ((EditText)rootView.findViewById(R.id.email)).getText().toString();
		String password = ((EditText)rootView.findViewById(R.id.password)).getText().toString();
		email = email.replace(" ", "");
		if (!helpers.isValidEmail(email)) {
			Toast toast = Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT);
			toast.show(); 
			return ;
		}
		else {
			((MainActivity) context).auth = new AuthentificationLocale(context, MainActivityDatas, email, password);
			((MainActivity) context).auth.proceedAuthentication();
		}
		new LoginTask(email, password, context,MainActivityDatas).execute();
	}

	private void googleAuth() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (resultCode == ConnectionResult.SUCCESS) {
			((MainActivity)context).auth = new AuthentificationGoogle(context, MainActivityDatas);
			((MainActivity)context).auth.init();
			((MainActivity)context).auth.start();
			((MainActivity)context).auth.proceedAuthentication();
		}
		else {
			Toast toast = Toast.makeText(context, "Google Play services are not available at the moment.", Toast.LENGTH_SHORT);
			toast.show(); 
		}
	}
}
