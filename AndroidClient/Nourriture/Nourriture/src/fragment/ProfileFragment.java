package fragment;

import task.LoginTask;

import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

import model.MainDatas;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
	private Context context;
	private View rootView;
	private MainDatas MainActivityDatas;
	
	public ProfileFragment(Context context_,MainDatas mainDatas_){
		MainActivityDatas = mainDatas_;
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
		name.setText("Profle of "+MainActivityDatas.user.getName());
		email.setText("Email: "+MainActivityDatas.user.getEmail());
		role.setText("Role: "+MainActivityDatas.user.getRole());
		
		restrictedFood.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment fragment = null;
				fragment = new RestrictedFoodFragment(MainActivityDatas);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
					((Activity) context).setTitle(((Activity) context).getResources().getStringArray(R.array.nav_drawer_items)[4]);
				}
			}
		});
		
        return rootView;
    }
}
