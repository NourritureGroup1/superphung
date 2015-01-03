package model;

import android.content.Intent;

import com.google.android.gms.common.SignInButton;

public abstract class Authentification  {
	protected String type;
	protected boolean isConnected;
	protected User user;
	public SignInButton btnSignIn;
	public boolean mIntentInProgress;
	public boolean mSignInClicked;
	public static final int RC_SIGN_IN = 0;
	
	public abstract String getType();
	public abstract boolean isConnected();
	public abstract User getUser();
	public abstract void proceedAuthentication();
	public abstract void finish();
	public abstract void start();
	public abstract void init();
	public abstract void performactivityresult(int requestCode, int responseCode, Intent intent);
}
