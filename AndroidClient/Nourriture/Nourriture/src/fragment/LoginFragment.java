package fragment;

import java.io.Console;
import java.io.InputStream;

import model.MainDatas;

import task.LoginTask;

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

public class LoginFragment extends Fragment implements OnClickListener,
ConnectionCallbacks, OnConnectionFailedListener {
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
	//	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		
		Button loginButton = (Button)rootView.findViewById(R.id.login_button);
		TextView registerButton = (TextView)rootView.findViewById(R.id.register_button);
		
		
		//if (resultCode != ConnectionResult.SUCCESS) {
			SignInButton btnSignIn = (SignInButton)rootView.findViewById(R.id.btn_sign_in_google);
			((MainActivity)context).mGoogleApiClient = new GoogleApiClient.Builder(context)
			.addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
	        .addScope(Plus.SCOPE_PLUS_LOGIN).build();
			btnSignIn.setOnClickListener(this);
		//}
		
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		
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
        	signInWithGplus();
        else if (v.getId() == R.id.register_button)
            registration();
    }
    
	private void signInWithGplus() {
	    if (!((MainActivity)context).mGoogleApiClient.isConnecting()) {
	    	((MainActivity)context).mSignInClicked = true;
	        resolveSignInError();
	    }
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
		else
			new LoginTask(email, password, context,MainActivityDatas).execute();
	}
	
	
	
	/*google authentication*/
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
	    if (!result.hasResolution()) {
	        GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity) context,
	                0).show();
	        return;
	    }
	    if (!((MainActivity)context).mIntentInProgress) {
	        // Store the ConnectionResult for later usage
	    	((MainActivity)context).mConnectionResult = result;
	        if (((MainActivity)context).mSignInClicked) {
	            // The user has already clicked 'sign-in' so we attempt to
	            // resolve all
	            // errors until the user is signed in, or they cancel.
	            resolveSignInError();
	        }
	    }
	 
	}
	 
	@Override
	public void onActivityResult(int requestCode, int responseCode,
	        Intent intent) {
	    if (requestCode == ((MainActivity)context).RC_SIGN_IN) {
	        if (responseCode != Activity.RESULT_OK) {
	        	((MainActivity)context).mSignInClicked = false;
	        }
	        ((MainActivity)context).mIntentInProgress = false;
	       /* if (!((MainActivity)context).mGoogleApiClient.isConnecting()) {
	        	((MainActivity)context).mGoogleApiClient.connect();
	        }*/
	    }
	}
	 
	@Override
	public void onConnected(Bundle arg0) {
		((MainActivity)context).mSignInClicked = false;
	    Toast.makeText(context, "User is connected!", Toast.LENGTH_LONG).show();
	    MainActivityDatas.mGoogleApiClient = ((MainActivity)context).mGoogleApiClient;
	    MainActivityDatas.init_datas_google();
	    // Get user's information
	  //  getProfileInformation();
	 
	    // Update the UI after signin
	//    updateUI(true);
	 
	}
	 
	@Override
	public void onConnectionSuspended(int arg0) {
		//((MainActivity)context).mGoogleApiClient.connect();
	   // updateUI(false);
	}
	 
	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
	    if (isSignedIn) {
	    } else {
	    }
	}
	 
	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
	    if (((MainActivity)context).mConnectionResult.hasResolution()) {
	        try {
	        	((MainActivity)context).mIntentInProgress = true;
	        	((MainActivity)context).mConnectionResult.startResolutionForResult((Activity) context, ((MainActivity)context).RC_SIGN_IN);
	        } catch (SendIntentException e) {
	        	((MainActivity)context).mIntentInProgress = false;
	            //((MainActivity)context).mGoogleApiClient.connect();
	        }
	    }
	}
}
