package adapter;

import java.util.ArrayList;
import java.util.List;

import model.Ingredient;
import adapter.Holder.ViewHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.superphung.nourriture.R;

public class RestrictedFoodAdapter extends ArrayAdapter<Ingredient> implements Filterable {
	private Context context;
	private List<Ingredient> CustomListViewValuesArr = new ArrayList<Ingredient>();
	@SuppressWarnings("unused")
	private ViewHolder holder;  
	private DisplayImageOptions options;
	private ImageLoader imageLoader;


	public RestrictedFoodAdapter(Context context_, int resource,
			int textViewResourceId, List<Ingredient> objects) {
		super(context_, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		context = context_;
		CustomListViewValuesArr = objects;
		//imageLoader = imageLoader_;
	}
	
	public void setImageLoader(ImageLoader imageLoader_) {
		imageLoader = imageLoader_;
	}
	
	/*public RestrictedFoodAdapter(Context c, ArrayList<Ingredient> CustomListViewValuesArr_,ImageLoader imageLoader_) {
		context = c;
		CustomListViewValuesArr = CustomListViewValuesArr_;
		imageLoader = imageLoader_;
	}*/
	public int getCount() {
		return CustomListViewValuesArr.size();
	}
	public Ingredient getItem(int position) {
		return CustomListViewValuesArr.get(position);
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
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (CustomListViewValuesArr.get(position).getImg() != null)
		{
			holder.progressBar.setVisibility(View.GONE);
			holder.img.setVisibility(View.VISIBLE);    	
			holder.img.setImageBitmap(CustomListViewValuesArr.get(position).getImg()); 	
		}
		else
		{
			holder.img.setVisibility(View.VISIBLE);
			if (CustomListViewValuesArr.get(position).getImg() == null)
			{
				imageLoader.displayImage(CustomListViewValuesArr.get(position).getImgUrl(), holder.img, options, new SimpleImageLoadingListener() {
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
						holder.progressBar.setVisibility(View.GONE);
						CustomListViewValuesArr.get(position).setImg(loadedImage);
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
				holder.img.setImageBitmap(CustomListViewValuesArr.get(position).getImg());
			}
		}
		holder.name.setText(CustomListViewValuesArr.get(position).getName());
		return view;
	}
}
