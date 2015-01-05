package fragment;

import java.io.Console;
import java.io.InputStream;
import java.util.Arrays;

import model.AuthentificationGoogle;
import model.AuthentificationLocale;
import model.MainDatas;

import task.LoginTask;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment implements OnClickListener {
	private Context context;
	private View rootView;
	private MainDatas MainActivityDatas;
	
	public LoginFragment(MainDatas mainDatas_){
		MainActivityDatas = mainDatas_;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
	{
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
		context = getActivity();
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		
		Button loginButton = (Button)rootView.findViewById(R.id.login_button);
		TextView registerButton = (TextView)rootView.findViewById(R.id.register_button);
		
		
		//if (resultCode != ConnectionResult.SUCCESS) {
			final SignInButton btnSignIn = (SignInButton)rootView.findViewById(R.id.btn_sign_in_google);
			final LoginFragment currentClass = this;
			btnSignIn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/*((MainActivity)context).mGoogleApiClient = new GoogleApiClient.Builder(context)
					.addConnectionCallbacks(currentClass)
			        .addOnConnectionFailedListener(currentClass).addApi(Plus.API, Plus.PlusOptions.builder().build())
			        .addScope(Plus.SCOPE_PLUS_LOGIN).build();
					((MainActivity)context).mGoogleApiClient.connect();*/
					//((MainActivity)context).auth = new AuthentificationGoogle(context);
					System.out.println("je cree un authgoogle");
					((MainActivity)context).auth = new AuthentificationGoogle(context, MainActivityDatas);
					System.out.println("je vais faire un init google");
					((MainActivity)context).auth.init();
					System.out.println("je fais un start google");
					((MainActivity)context).auth.start();
					System.out.println("je fais un proceedauth google");
					((MainActivity)context).auth.proceedAuthentication();
					System.out.println("j'ai fini ce onclick");
					//System.out.println("j'ai cliqué ici");
				}
			});
		//}
		
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("email "));
        return rootView;
    }

 
    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button)
            normalAuth();
       // else if (v.getId() == R.id.btn_sign_in_google)
      //  	signInWithGplus();
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
	
	


	/*fb connect*/
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i("nourriture", "Logged in...");
	        //((MainActivity)context).auth = new AuthentificationFacebook();
	    } else if (state.isClosed()) {
	        Log.i("nourriture", "Logged out...");
	    }
	}
}
