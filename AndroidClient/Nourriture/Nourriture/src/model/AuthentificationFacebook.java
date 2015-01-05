package model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;


public class AuthentificationFacebook extends Authentification {

	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private Context context;
	private Bundle savedInstanceState;
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        System.out.println("logged in babe !");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	        System.out.println("logged out babe !");
	    }
	}
	
	public AuthentificationFacebook(Context context_,Bundle savedInstanceState_) {
		type = "facebook";
		isConnected = false;
		user = null;
		context = context_;
		savedInstanceState = savedInstanceState_;
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
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		uiHelper = new UiLifecycleHelper((Activity) context, callback);
	    uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void performactivityresult(int requestCode, int responseCode,
			Intent intent) {
		// TODO Auto-generated method stub
		uiHelper.onActivityResult(requestCode, responseCode, intent);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
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
