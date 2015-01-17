package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.NavDrawerListAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.R;

import fragment.BrowseIngredientsFragment;
import fragment.ListMyMomentsFragment;
import fragment.ListUserFragment;
import fragment.LoginFragment;
import fragment.ProfileFragment;
import fragment.RegisterFragment;

public class MainDatas {
	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	public ActionBarDrawerToggle mDrawerToggle;
	public CharSequence mDrawerTitle;
	public CharSequence mTitle;
	public String[] navMenuTitles;
	public TypedArray navMenuIcons;
	public ArrayList<NavDrawerItem> navDrawerItems;
	public NavDrawerListAdapter adapter;
	public Map<String, Map<String, String>> urls = new HashMap<String, Map<String, String>>();
	public User user = new User("", "", "", "", "");
	private Context context;

	public String API_URL = "https://54.64.212.101";	
	//public String API_URL = "https://192.168.0.103:8081";


	public void init(Context context_,Bundle savedInstanceState) {
		context = context_;
		init_urls();
		mTitle = ((Activity) context).getTitle();
		mDrawerTitle = ((Activity) context).getTitle();
		navMenuTitles = ((Activity) context).getResources().getStringArray(R.array.nav_drawer_items);
		getUserMenu();
		mDrawerToggle = new ActionBarDrawerToggle((Activity) context, mDrawerLayout,R.drawable.ic_launcher,R.string.app_name,R.string.app_name) {
			public void onDrawerClosed(View view) {
				((Activity) context).getActionBar().setTitle(mTitle);
				((Activity) context).invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) {
				((Activity) context).getActionBar().setTitle(mDrawerTitle);
				((Activity) context).invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			displayView(0);
		}
	}

	private void init_urls() {
		Map<String, String> url_request_post = new HashMap<String, String>();
		Map<String, String> url_request_put = new HashMap<String, String>();
		Map<String, String> url_request_get = new HashMap<String, String>();
		Map<String, String> url_request_delete = new HashMap<String, String>();

		url_request_post.put("login", API_URL+"/login");
		url_request_post.put("user", API_URL+"/user");
		url_request_post.put("signup", API_URL+"/signup"); 
		url_request_post.put("signin_oauth", API_URL+"/user/social"); 
		url_request_post.put("share_moment", API_URL+"/moment"); 
		
		url_request_put.put("user", API_URL+"/user"); 
		url_request_get.put("user", API_URL+"/user"); 
		url_request_get.put("ingredient", API_URL+"/ingredient"); 

		urls.put("POST", url_request_post);
		urls.put("PUT", url_request_put);
		urls.put("GET", url_request_get);
		urls.put("DELETE", url_request_delete);
	}

	private void getUserMenu() {
		navMenuIcons = context.getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) ((Activity) context).findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) ((Activity) context).findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(3, -1)));

		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		adapter = new NavDrawerListAdapter(context,
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
	}

	public void getUserMenuConnected() {
		navMenuIcons = context.getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) ((Activity) context).findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) ((Activity) context).findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(2, -1)));

		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		adapter = new NavDrawerListAdapter(context,
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
	}

	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			displayView(position);
		}
	}

	public void displayView(int position) {
		Fragment fragment = null;
		if (user.getConnected() == true) {
			Log.e("MainActivity", "JE  SUIS  CO");
			switch (position) {
			case 0:
				fragment = new ProfileFragment(context);
				break;
			case 1:
				fragment = new ListMyMomentsFragment();
				break;
			case 2:
				fragment = new BrowseIngredientsFragment();
				break;
			case 3:
				fragment = new ListUserFragment();
				break;
			case 4:
				((MainActivity) context).restartActivity();
				//fragment = new LoginFragment(this);
				break;
			default:
				break;
			}			
		}
		else {
			Log.e("MainActivity", "JE NE SUIS PAS CO");
			switch (position) {
			case 0:
				fragment = new LoginFragment();
				break;
			case 1:
				fragment = new RegisterFragment();
				break;
			default:
				break;
			}			
		}
		if (fragment != null) {
			FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			((Activity) context).setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
}
