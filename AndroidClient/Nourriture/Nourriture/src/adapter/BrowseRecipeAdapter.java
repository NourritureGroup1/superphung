package adapter;

import java.util.List;

import model.Ingredient;
import model.Recipe;
import adapter.Holder.ViewHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;

public class BrowseRecipeAdapter extends ArrayAdapter<Recipe> implements Filterable {
	private Context context;
	@SuppressWarnings("unused")
	private ViewHolder holder;  
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private boolean checked;
	private String type;


	public BrowseRecipeAdapter(Context context_, int resource,
			int textViewResourceId, List<Recipe> objects, String type_) {
		super(context_, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		context = context_;
		Globals.listRecipe = objects;
		checked = false;
		type = type_;
		//imageLoader = imageLoader_;
	}

	public void setImageLoader(ImageLoader imageLoader_) {
		imageLoader = imageLoader_;
	}

	public int getCount() {
		return Globals.listRecipe.size();
	}
	public Recipe getItem(int position) {
		return Globals.listRecipe.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater)
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_grid_image, parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.img = (ImageView) view.findViewById(R.id.image);
			holder.indicator = (ImageView) view.findViewById(R.id.checkindicator);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (Globals.listRecipe.get(position).getImg() != null)
		{
			holder.progressBar.setVisibility(View.GONE);
			holder.img.setVisibility(View.VISIBLE);    	 	
			holder.img.setImageBitmap(Globals.listRecipe.get(position).getImg()); 	
		}
		else
		{
			if (Globals.listRecipe.get(position).getImg() == null)
			{
				imageLoader.displayImage(Globals.listRecipe.get(position).getImgUrl(), holder.img, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						holder.progressBar.setProgress(0);
						holder.progressBar.setVisibility(View.VISIBLE);
					}
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						holder.progressBar.setVisibility(View.GONE);
					}
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						Globals.listRecipe.get(position).setImg(loadedImage);	
						holder.progressBar.setVisibility(View.GONE);
						holder.img.setVisibility(View.VISIBLE);
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current,
							int total) {
						holder.progressBar.setProgress(Math.round(100.0f * current / total));
					}
				}
						);
			}
			else
			{
				holder.progressBar.setVisibility(View.GONE);
				holder.img.setImageBitmap(Globals.listRecipe.get(position).getImg());
			}
		}		
		holder.name.setText(Globals.listRecipe.get(position).getName());
		holder.indicator.setVisibility(View.GONE);    
		return view;
	}
}
