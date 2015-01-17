package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import task.getFacebookUserTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.superphung.nourriture.MainActivity;


public class AuthentificationFacebook extends Authentification {
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private Context context;
	private Bundle savedInstanceState;
	private boolean authenticationProgress;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (!authenticationProgress)
				onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			if (isConnected == false) {
				Request.newMeRequest(session, new Request.GraphUserCallback() {
					// callback after Graph API response with user object
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							authenticationProgress = false;
							Log.i(TAG, "1: Logged in...");
							System.out.println("1: logged in babe !");
							List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
							parameters.add(new BasicNameValuePair("email", user.asMap().get("email").toString()));
							parameters.add(new BasicNameValuePair("name", user.getName()));
							parameters.add(new BasicNameValuePair("oauthID", user.getId()));
							((MainActivity)context).saveAuthenticator("facebook", null,null);
							new getFacebookUserTask(context,parameters).execute();
							isConnected = true;
						}
					}
				}).executeAsync();
			}
		} else if (state.isClosed()) {
			Log.i(TAG, "2: Logged out...");
			System.out.println("2: logged out babe !");
			authenticationProgress = false;
		}
	}

	public AuthentificationFacebook(Context context_,Bundle savedInstanceState_) {
		type = "facebook";
		isConnected = false;
		user = null;
		context = context_;
		savedInstanceState = savedInstanceState_;
		authenticationProgress = false;
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

	public  void proceedAuthentication() {}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Session session = Session.getActiveSession();
		if (session != null) {
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
			}
		} else {
			session = new Session(context);
			Session.setActiveSession(session);
			session.closeAndClearTokenInformation();
		}
	}

	@Override
	public void start() {}

	@Override
	public void init() {
		Log.i(TAG, "TEST: Logged out...");
		System.out.println("TEST: logged out babe !");
		authenticationProgress = true;
		uiHelper = new UiLifecycleHelper((Activity) context, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void performactivityresult(int requestCode, int responseCode,
			Intent intent) {
		uiHelper.onActivityResult(requestCode, responseCode, intent);
	}

	@Override
	public void onPause() {
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public boolean isAuthenticationInProgress() {
		// TODO Auto-generated method stub
		return authenticationProgress;
	}
}
