package com.superphung.nourriture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import task.AddIngredientTask;

import model.Ingredient;
import model.Moment;

import adapter.PicGridAdapter;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

public class AddIngredient extends Activity implements OnClickListener {
	private ImageLoader imageLoader;
	private Button save;
	private ImageView camera;
	private EditText name;
	private EditText description;
	private EditText categories;
	private EditText nutrients;
	private ImageView pic;
	private String current_pic_file = null;
	private Context context;

	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.add_ingredient);
		context = this;
		save = (Button) findViewById(R.id.create);
		camera = (ImageView) findViewById(R.id.camera);
		name = (EditText) findViewById(R.id.ingredient_name);
		categories = (EditText) findViewById(R.id.category);
		nutrients = (EditText) findViewById(R.id.nutrients);
		description = (EditText) findViewById(R.id.description);
		
		save.setOnClickListener(this);
		camera.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.create)
			create_ingredient();
		if (v.getId() == R.id.camera)
			selectImage();
	}

	private void selectImage() {
		// TODO Auto-generated method stub
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

	private void create_ingredient() {
		if (name.getText().toString().equals("")) {
			Toast toast = Toast.makeText(this, "The name cannot be empty", Toast.LENGTH_SHORT);
			toast.show(); 
			return;
		}
		if (description.getText().toString().equals("")) {
			Toast toast = Toast.makeText(this, "The description cannot be empty", Toast.LENGTH_SHORT);
			toast.show(); 
			return;
		}
		if (categories.getText().toString().equals("")) {
			Toast toast = Toast.makeText(this, "You must enter a least one category", Toast.LENGTH_SHORT);
			toast.show(); 
			return;
		}
		if (nutrients.getText().toString().equals("")) {
			Toast toast = Toast.makeText(this, "You must enter a least one nutrient", Toast.LENGTH_SHORT);
			toast.show(); 
			return;
		}
		if (current_pic_file == null) {
			Toast toast = Toast.makeText(this, "You must add a picture", Toast.LENGTH_SHORT);
			toast.show(); 
			return;
		}
		Ingredient tmp = new Ingredient("", current_pic_file, description.getText().toString(), name.getText().toString(), categories.getText().toString(), nutrients.getText().toString());
		//execute worker
		new AddIngredientTask(context, tmp).execute();
		name.setText("");
		description.setText("");
		categories.setText("");
		nutrients.setText("");
		current_pic_file = null;
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
                        camera.setImageBitmap(bm);
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
                camera.setImageBitmap(bm);
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
