package fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Moment;
import task.ShareMomentTask;
import adapter.PicGridAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.MyApplication;
import com.superphung.nourriture.MyApplication.TrackerName;
import com.superphung.nourriture.R;

public class ProfileFragment extends Fragment implements OnClickListener {
	private Context context;
	private View rootView;
	private ImageView pic;
	private String current_pic_file = null;
	private GridView grid_pics;
	private PicGridAdapter adapter;

	public ProfileFragment(Context context_){
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
		pic = (ImageView)rootView.findViewById(R.id.camera);
		Button share_moment = (Button)rootView.findViewById(R.id.share_moment);
		Button restrictedFood = (Button)rootView.findViewById(R.id.rfood);
		Button my_moments = (Button)rootView.findViewById(R.id.my_moments);
		grid_pics = (GridView) rootView.findViewById(R.id.grid);
		name.setText("Profle of "+Globals.MainActivityDatas.user.getName());
		email.setText("Email: "+Globals.MainActivityDatas.user.getEmail());
		role.setText("Role: "+Globals.MainActivityDatas.user.getRole());
		restrictedFood.setOnClickListener(this);
		share_moment.setOnClickListener(this);
		my_moments.setOnClickListener(this);
		pic.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rfood)
			restrictedFood();
		if (v.getId() == R.id.share_moment)
			share_moment();
		if (v.getId() == R.id.my_moments)
			my_moments();
		if (v.getId() == R.id.camera)
			selectImage();
	}
	
	private void my_moments() {
		Fragment fragment = null;
		fragment = new ListMyMomentsFragment();
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
			((Activity) context).setTitle("My moments");
		}
	}

	private void share_moment() {
		EditText description = (EditText)rootView.findViewById(R.id.moment_desc);
		if (description.getText().toString().isEmpty()) {
			Toast toast = Toast.makeText(context, "You cannot share an empty moment", Toast.LENGTH_SHORT);
			toast.show(); 
			return;
		}
		if (current_pic_file == null) {
			Toast toast = Toast.makeText(context, "You need to post a picture with your moment", Toast.LENGTH_SHORT);
			toast.show(); 
			return;
		}
		String desc = description.getText().toString();
		description.setText("");
		Moment mmoment = new Moment(null, desc, null, Globals.MainActivityDatas.user.getId(), new Date());
		mmoment.setImgPath(current_pic_file);
		new ShareMomentTask(context, mmoment).execute();
		current_pic_file = null;
		List<Moment> list_moments = new ArrayList<Moment>();
		adapter = new PicGridAdapter(context,0,0,list_moments);	
		adapter.notifyDataSetChanged();	
		grid_pics.setAdapter(adapter);
		
	}

	public void restrictedFood() {
		Fragment fragment = null;
		fragment = new RestrictedFoodFragment();
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
			((Activity) context).setTitle("My restricted food");
		}
	}
	
	private void selectImage() {
		System.out.println("je clique sur la cam");
		current_pic_file = null;
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
 
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1888);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            111);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
	

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Tracker t = ((MyApplication) getActivity().getApplication()).getTracker(
			    TrackerName.APP_TRACKER);
			t.setScreenName("Current user profile Fragment");
			t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1888) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
 
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);
                    bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
                    File wallpaperDirectory = new File(android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix");
                 // have the object build the directory structure, if needed.
                    wallpaperDirectory.mkdirs(); 
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    current_pic_file = file.getAbsolutePath();
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        //pic.setImageBitmap(bm);
                		List<Moment> list_moments = new ArrayList<Moment>();
                		Moment test = new Moment("", "", "", "", new Date());
                		test.setImg(bm);
                		list_moments.add(test);
                		adapter = new PicGridAdapter(context,0,0,list_moments);	
                		grid_pics.setAdapter(adapter);
                		adapter.notifyDataSetChanged();
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 111) {
                Uri selectedImageUri = data.getData();
 
                String tempPath = getPath(selectedImageUri, (Activity) context);
                current_pic_file = tempPath;
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                btmapOptions.inSampleSize = 2;
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                //pic.setImageBitmap(bm);
        		List<Moment> list_moments = new ArrayList<Moment>();
        		Moment test = new Moment("", "", "", "", new Date());
        		test.setImg(bm);
        		list_moments.add(test);
        		adapter = new PicGridAdapter(context,0,0,list_moments);	
        		adapter.notifyDataSetChanged();	
        		grid_pics.setAdapter(adapter);
            }
        }
    }
	
	public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaColumns.DATA };
        @SuppressWarnings("deprecation")
		Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
