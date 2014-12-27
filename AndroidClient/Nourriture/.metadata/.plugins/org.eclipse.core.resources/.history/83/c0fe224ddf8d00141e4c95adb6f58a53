package fragment;

import java.io.Console;

import model.MainDatas;

import task.LoginTask;
import task.RegisterTask;

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

public class RegisterFragment extends Fragment {
	private Context context;
	private View rootView;
	private MainDatas MainActivityDatas;
    
	public RegisterFragment(MainDatas mainDatas_){
		MainActivityDatas = mainDatas_;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
		context = getActivity();
		Button loginButton = (Button)rootView.findViewById(R.id.signup_button);
		/*TextView registerButton = (TextView)rootView.findViewById(R.id.register_button);
		
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				Fragment fragment = null;
				fragment = new RegisterFragment();
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
				}
			}
		});*/
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String email = ((EditText)rootView.findViewById(R.id.email)).getText().toString();
				String name = ((EditText)rootView.findViewById(R.id.name)).getText().toString();
				String password = ((EditText)rootView.findViewById(R.id.password)).getText().toString();
				Log.e("email",email);
				Log.e("password",password);
				email = email.replace(" ", "");
				if (!helpers.isValidEmail(email)) {
					  Toast toast = Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT);
					  toast.show(); 
					  return ;
				}
				else if (name.isEmpty()) {
					  Toast toast = Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT);
					  toast.show(); 
					  return ;
				}
				else
					new RegisterTask(email, name, password, context,MainActivityDatas).execute();
			}
		});
		
        return rootView;
    }
}
