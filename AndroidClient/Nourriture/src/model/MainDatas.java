package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.superphung.nourriture.R;

import fragment.LoginFragment;

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
	public Map<String, String> urls = new HashMap<String, String>();
	public User user;
	private Context context;
	
	
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
		urls.put("login", "https://192.168.0.103:8081/login");
	}
	
	private void getUserMenu() {
		navMenuIcons = context.getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) ((Activity) context).findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) ((Activity) context).findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(0, -1)));
		
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
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(0, -1)));
		
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
		switch (position) {
			case 0:
				fragment = new LoginFragment(this);
				break;
			default:
				break;
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
