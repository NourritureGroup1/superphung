package fragment;

import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EditProfileFragment extends Fragment {
	private Context context;
	private View rootView;

	public EditProfileFragment(Context context_){
		context = context_;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_editprofile, container, false);
		context = getActivity();
		TextView name = (TextView)rootView.findViewById(R.id.profileof);
		TextView email = (TextView)rootView.findViewById(R.id.email);
		TextView role = (TextView)rootView.findViewById(R.id.role);
		ImageView edit = (ImageView)rootView.findViewById(R.id.editprofile);
		Button restrictedFood = (Button)rootView.findViewById(R.id.rfood);
		name.setText("Profle of "+Globals.MainActivityDatas.user.getName());
		email.setText("Email: "+Globals.MainActivityDatas.user.getEmail());
		role.setText("Role: "+Globals.MainActivityDatas.user.getRole());
		//restrictedFood.setOnClickListener(this);
		//edit.setOnClickListener(this);
		return rootView;
	}
}
