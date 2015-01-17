package fragment;

import task.FollowUser;
import task.UnfollowUser;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;

public class UserProfileFragment extends Fragment implements OnClickListener {
	private Context context;
	private View rootView;
	private View unfollow;
	private View follow;

	public UserProfileFragment(Context context_){
		context = context_;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		context = getActivity();
		TextView name = (TextView)rootView.findViewById(R.id.profileof);
		TextView email = (TextView)rootView.findViewById(R.id.email);
		TextView role = (TextView)rootView.findViewById(R.id.role);
		Button restrictedFood = (Button)rootView.findViewById(R.id.rfood);
		LinearLayout momentlayout = (LinearLayout)rootView.findViewById(R.id.moment);
		Button user_moment = (Button)rootView.findViewById(R.id.my_moments);
		momentlayout.setVisibility(View.GONE);
		follow = (Button)rootView.findViewById(R.id.follow);
		unfollow = (Button)rootView.findViewById(R.id.unfollow);
		name.setText("Profle of "+Globals.currentUser.getName());
		email.setText("Email: "+Globals.currentUser.getEmail());
		role.setText("Role: "+Globals.currentUser.getRole());
		restrictedFood.setOnClickListener(this);
		if (Globals.MainActivityDatas.user.areWeFriends(Globals.currentUser.getId()))
			unfollow.setVisibility(View.VISIBLE);
		else
			follow.setVisibility(View.VISIBLE);
		
		follow.setOnClickListener(this);
		unfollow.setOnClickListener(this);
		user_moment.setText(Globals.currentUser.getName()+"'s moments");
		user_moment.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rfood)
			restrictedFood();
		if (v.getId() == R.id.unfollow)
			unfollow();
		if (v.getId() == R.id.follow)
			follow();
		if (v.getId() == R.id.my_moments)
			user_moments();
	}

	private void user_moments() {
		Fragment fragment = null;
		fragment = new ListUserMomentsFragment();
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
			((Activity) context).setTitle(Globals.currentUser+"'s moments");
		}
	}

	private void follow() {
		new FollowUser(context, Globals.currentUser).execute();
		follow.setVisibility(View.GONE);
		unfollow.setVisibility(View.VISIBLE);
	}

	private void unfollow() {
		new UnfollowUser(context, Globals.currentUser).execute();
		unfollow.setVisibility(View.GONE);
		follow.setVisibility(View.VISIBLE);
	}

	public void restrictedFood() {
		Fragment fragment = null;
		fragment = new UserRestrictedFoodFragment();
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
			((Activity) context).setTitle(Globals.currentUser.getName()+"'s restructed food");
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		/*Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Current user profile Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());*/
	}
}
