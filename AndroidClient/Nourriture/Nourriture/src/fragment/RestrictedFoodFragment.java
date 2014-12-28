package fragment;

import java.util.ArrayList;
import com.superphung.nourriture.MainActivity;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;
import java.util.List;


import model.AuthImageDownloader;
import model.Ingredient;
import model.MainDatas;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import task.WorkerRestrictedFood;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.superphung.nourriture.R;
import com.superphung.nourriture.helpers;

import adapter.RestrictedFoodAdapter;
import adapter.Holder.ViewHolder;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class RestrictedFoodFragment extends Fragment {
	ImageView selectedImage;  
	private Context context;
	private View rootView;
	private ImageLoader imageLoader;
	private MainDatas MainActivityDatas;

	public RestrictedFoodFragment(MainDatas MainActivityDatas_)
	{
		MainActivityDatas = MainActivityDatas_;
	}

	public RestrictedFoodFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		rootView = inflater.inflate(R.layout.grid, container, false);
		imageLoader = ImageLoader.getInstance();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().build();
		Builder configBuilder = new ImageLoaderConfiguration.Builder(getActivity());
		configBuilder.imageDownloader(new AuthImageDownloader(getActivity(), 100, 100));
		configBuilder.defaultDisplayImageOptions(defaultOptions);
		ImageLoaderConfiguration config=configBuilder.build();
		imageLoader.init(config);
		new WorkerRestrictedFood(context, rootView, imageLoader, MainActivityDatas).execute();
		Button addRestrictedIngredient = (Button) rootView.findViewById(R.id.addRestrictedIngredient);
		
		addRestrictedIngredient.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);

				alert.setTitle("Add new restricted ingredient");

				// Set an EditText view to get user input 
				final EditText input = new EditText(context);
				alert.setView(input);

				alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  //String value = input.getText();
				  // Do something with value!
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});

				alert.show();
			}
		});
		return rootView;
	}
}
