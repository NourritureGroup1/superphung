package com.superphung.nourriture;

import fragment.profileFragment;
import model.MainDatas;
import model.User;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {
	private MainDatas MainActivityDatas = new MainDatas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		setContentView(R.layout.activity_main);
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
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
		fragment = new profileFragment(this,MainActivityDatas);
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			
		}
	}
}