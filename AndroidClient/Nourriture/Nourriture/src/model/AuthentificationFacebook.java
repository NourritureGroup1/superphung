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


public class AuthentificationFacebook extends Authentification {
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private Context context;
	private Bundle savedInstanceState;
	private MainDatas MainActivityDatas;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			if (isConnected == false) {
				Log.i(TAG, "Logged in...");
				System.out.println("logged in babe !");
				Request.newMeRequest(session, new Request.GraphUserCallback() {
					// callback after Graph API response with user object
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
							parameters.add(new BasicNameValuePair("email", user.asMap().get("email").toString()));
							parameters.add(new BasicNameValuePair("name", user.getName()));
							parameters.add(new BasicNameValuePair("oauthID", user.getId()));
							new getFacebookUserTask(context,MainActivityDatas,parameters).execute();
							isConnected = true;
						}
					}
				}).executeAsync();
			}
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
			System.out.println("logged out babe !");
		}
	}

	public AuthentificationFacebook(Context context_,Bundle savedInstanceState_, MainDatas MainActivityDatas_) {
		type = "facebook";
		isConnected = false;
		user = null;
		context = context_;
		savedInstanceState = savedInstanceState_;
		MainActivityDatas = MainActivityDatas_;
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
}
