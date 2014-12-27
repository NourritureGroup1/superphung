package fragment;

import java.io.Console;

import model.MainDatas;

import task.LoginTask;

import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginGooglePlusFragment extends Fragment {
	private Context context;
	private View rootView;
	private MainDatas MainActivityDatas;
    
	public LoginGooglePlusFragment(MainDatas mainDatas_){
		MainActivityDatas = mainDatas_;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
		context = getActivity();
		Button loginButton = (Button)rootView.findViewById(R.id.login_button);
	//	Button gPlusButton = (Button)rootView.findViewById(R.id.btn_google);
		TextView registerButton = (TextView)rootView.findViewById(R.id.register_button);
		
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				Fragment fragment = null;
				fragment = new RegisterFragment(MainActivityDatas);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
				}
			}
		});
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String email = ((EditText)rootView.findViewById(R.id.email)).getText().toString();
				String password = ((EditText)rootView.findViewById(R.id.password)).getText().toString();
				Log.e("email",email);
				Log.e("password",password);
				email = email.replace(" ", "");
				if (!helpers.isValidEmail(email)) {
					  Toast toast = Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT);
					  toast.show(); 
					  return ;
				}
				else
					new LoginTask(email, password, context,MainActivityDatas).execute();
			}
		});

		
	/*	gPlusButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Fragment fragment = null;
				fragment = new LoginGooglePlusFragment(MainActivityDatas);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
				}
			}
		});*/
        return rootView;
    }
}
