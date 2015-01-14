package com.superphung.nourriture;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;

import model.Authentification;
import model.AuthentificationFacebook;
import model.AuthentificationGoogle;
import model.AuthentificationLocal;
import model.MainDatas;
import model.User;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import fragment.ProfileFragment;

public class MainActivity extends Activity {
	private MainDatas MainActivityDatas = new MainDatas();
	public Authentification auth;
	private Bundle savedI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		setContentView(R.layout.activity_main);
		//((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
		savedI = savedInstanceState;
		MainActivityDatas.init(this,savedInstanceState);
		loadAuthenticator();
	}

	public void saveAuthenticator(String auth_type, String email, String Password) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString("auth_type", auth_type);
		editor.putString("email", email);
		editor.putString("password", Password);
		editor.commit();
	}
	
	private void loadAuthenticator() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String auth_type = sharedPreferences.getString("auth_type", null);
		String email = sharedPreferences.getString("email", null);
		String password = sharedPreferences.getString("password", null);
		if (auth_type != null) {
			System.out.println("mon dernier auth est :"+auth_type);
			System.out.println("mon dernier auth est :"+auth_type);
			System.out.println("mon dernier email est :"+email);
			System.out.println("mon dernier password est :"+password);
			if (auth_type.contains("facebook")) {
				auth = new AuthentificationFacebook(this, savedI, MainActivityDatas);
				auth.init();
			}
			else if (auth_type.contains("google")) {
				int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
				if (resultCode == ConnectionResult.SUCCESS) {
					auth = new AuthentificationGoogle(this, MainActivityDatas);
					auth.init();
					auth.start();
					auth.proceedAuthentication();
				}
				else {
					Toast toast = Toast.makeText(this, "Google Play services are not available at the moment.", Toast.LENGTH_SHORT);
					toast.show(); 
				}
			}
			else if (auth_type.contains("locale")) {
				auth = new AuthentificationLocal(this, MainActivityDatas, email, password);
				auth.init();
				auth.proceedAuthentication();
			}
		}
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
		auth.finish();
		saveAuthenticator(null,null,null);
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
		if (auth != null)
			auth.start();
		//GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	public void onStop() {
		super.onStop();
		//GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (auth != null)
			auth.performactivityresult(requestCode, responseCode, intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (auth != null)
			auth.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (auth != null)
			auth.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (auth != null)
			auth.onSaveInstanceState(outState);
	}
	@Override
	public void onResume() {
		super.onResume();
		if (auth != null)
			auth.onResume();
	}
}
