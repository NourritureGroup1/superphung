package com.superphung.nourriture;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

import fragment.ProfileFragment;
import model.MainDatas;
import model.User;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private MainDatas MainActivityDatas = new MainDatas();
	public GoogleApiClient mGoogleApiClient;
	public boolean mIntentInProgress;
	public boolean mSignInClicked;
	public ConnectionResult mConnectionResult;
	public SignInButton btnSignIn;
	public static final int RC_SIGN_IN = 0;
	public static final String TAG = "MainActivity";
	public static final int PROFILE_PIC_SIZE = 400;
	public ImageView imgProfilePic;
	public boolean googleplayAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		setContentView(R.layout.activity_main);
		MainActivityDatas = new MainDatas();
		MainActivityDatas.init(this,savedInstanceState);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (MainActivityDatas.mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == R.id.action_settings)
			return true;
		else
			return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen;
		drawerOpen = MainActivityDatas.mDrawerLayout.isDrawerOpen(MainActivityDatas.mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    

	public void restartActivity() {
		if (MainActivityDatas.user.getType() == "google") {
	      /*  if (mGoogleApiClient.isConnected()) {
	            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	            mGoogleApiClient.disconnect();
	            mGoogleApiClient.connect();
	        }*/
	        if (mGoogleApiClient.isConnected()) {
	            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
	                    .setResultCallback(new ResultCallback<Status>() {
	                        @Override
	                        public void onResult(Status arg0) {
	                            mGoogleApiClient.connect();
	                        }
	 
	                    });
	        }
		}
	    Intent intent = getIntent();
	    finish();
	    startActivity(intent);
	}

	@Override
	public void setTitle(CharSequence title) {
		MainActivityDatas.mTitle = title;
		getActionBar().setTitle(title);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		MainActivityDatas.mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		MainActivityDatas.mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void loginUser(User user_) {
		MainActivityDatas.user = user_;
		MainActivityDatas.getUserMenuConnected();
	    //new CurrentUserTask(user, context).execute();
        //((MainActivity)getActivity()).getUserMenu();
		Fragment fragment = null;
		fragment = new ProfileFragment(this,MainActivityDatas);
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			setTitle(getResources().getStringArray(R.array.nav_drawer_items)[2]);
			
		}
	}
	

	public void onStart() {
	    super.onStart();
	    mGoogleApiClient.connect();
	}
	 
	public void onStop() {
	  super.onStop();
	    if (mGoogleApiClient.isConnected()) {
	    	mGoogleApiClient.disconnect();
	    }
	}
}
