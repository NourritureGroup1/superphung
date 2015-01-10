package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import task.getGoogleUserTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.superphung.nourriture.MainActivity;

public class AuthentificationGoogle extends Authentification implements ConnectionCallbacks, OnConnectionFailedListener {
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private Context context;
	private MainDatas MainActivityDatas;
	private boolean mIntentInProgress;
	private ConnectionResult mConnectionResult;
	private boolean authenticationProgress;

	public AuthentificationGoogle(Context context_,MainDatas MainActivityDatas_) {
		type = "google";
		isConnected = false;
		user = null;
		context = context_;
		MainActivityDatas = MainActivityDatas_;
		authenticationProgress = false;
	}

	public void init() {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	public String getType() {
		return type;
	}
	public  boolean isConnected() {
		return isConnected;
	}
	public  User getUser() {
		return user;
	}

	public  void proceedAuthentication() {
		authenticationProgress = true;
		signInWithGplus();
	}

	private void signInWithGplus() {
		if (!(mGoogleApiClient.isConnecting())) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}


	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			System.out.println("signinerror if");
			try {
				System.out.println("signinerror try");
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult((Activity) context, RC_SIGN_IN);
			} catch (SendIntentException e) {
				System.out.println("signinerror catch");
				mIntentInProgress = false;
			}
		}
	}

	private void init_datas_google() {
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			String personName = currentPerson.getDisplayName();
			String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			parameters.add(new BasicNameValuePair("email", email));
			parameters.add(new BasicNameValuePair("name", personName));
			AsyncTask<String, String, String> googleUser = new getGoogleUserTask(context,MainActivityDatas,parameters);
			((MainActivity)context).saveAuthenticator("google", null,null);
			googleUser.execute();
			authenticationProgress = false;
			/*try {
				//googleUser.wait();
				authenticationProgress = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				authenticationProgress = false;
			}*/
		} else {
			Toast.makeText(context.getApplicationContext(),
					"Person information is null", Toast.LENGTH_LONG).show();
		}
	}

	public void finish() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
	}

	public void start() {
		if (!mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();			
		}
	}

	public void performactivityresult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) { 
			if (responseCode != Activity.RESULT_OK) {
				mSignInClicked = false;
			}
			else {
				mIntentInProgress = false;
				if (!mGoogleApiClient.isConnecting()) {
					mGoogleApiClient.connect();
				}
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		init_datas_google();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity) context,
					0).show();
			return;
		}
		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;
			//if (mSignInClicked) {
			// The user has already clicked 'sign-in' so we attempt to
			// resolve all
			// errors until the user is signed in, or they cancel.
			resolveSignInError();
			//}
		}
	}

	@Override
	public void onPause() {}

	@Override
	public void onDestroy() {}

	@Override
	public void onSaveInstanceState(Bundle outState) {}

	@Override
	public void onResume() {}

	@Override
	public boolean isAuthenticationInProgress() {
		// TODO Auto-generated method stub
		return authenticationProgress;
	}
}
