package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import task.getGoogleUserTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.superphung.nourriture.MainActivity;

public class AuthentificationGoogle extends Authentification implements ConnectionCallbacks, OnConnectionFailedListener {
	private static final int RC_SIGN_IN = 0;
	private static final String TAG = "MainActivity";
	private static final int PROFILE_PIC_SIZE = 400;
	private GoogleApiClient mGoogleApiClient;
	private Context context;
	private MainDatas MainActivityDatas;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;

	public AuthentificationGoogle(Context context_,MainDatas MainActivityDatas_) {
		type = "google";
		isConnected = false;
		user = null;
		context = context_;
		MainActivityDatas = MainActivityDatas_;
		System.out.println("constructeur ok");
	}

	public void init() {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		System.out.println("init() ok");
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
		signInWithGplus();
	}

	private void signInWithGplus() {
		System.out.println("signinwith");
		if (!(mGoogleApiClient.isConnecting())) {
			System.out.println("signinwith resolve");
			mSignInClicked = true;
			resolveSignInError();
		}
	}


	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		System.out.println("signinerror");
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
			new getGoogleUserTask(context,MainActivityDatas,parameters).execute();
		} else {
			Toast.makeText(context.getApplicationContext(),
					"Person information is null", Toast.LENGTH_LONG).show();
		}
	}

	public void finish() {
		System.out.println("finish");
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
	}

	public void start() {
		System.out.println("start");
		mGoogleApiClient.connect();
	}

	public void performactivityresult(int requestCode, int responseCode,
			Intent intent) {
		System.out.println("performance");
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != Activity.RESULT_OK) {
				mSignInClicked = false;
			}
			mIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		System.out.println("onconnected");
		mSignInClicked = false;
		Toast.makeText(context, "User is connected!", Toast.LENGTH_LONG).show();
		init_datas_google();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("connectionsuspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub				System.out.println("je suis totor mwhahahaha");
		System.out.println("connectionfailed");
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
}
