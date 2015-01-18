package adapter;

import java.util.List;

import model.Ingredient;
import adapter.Holder.ViewHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

public class RestrictedFoodAdapter extends ArrayAdapter<Ingredient> implements Filterable {
	private Context context;
	@SuppressWarnings("unused")
	private ViewHolder holder;  
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private boolean checked;
	private String type;


	public RestrictedFoodAdapter(Context context_, int resource,
			int textViewResourceId, List<Ingredient> objects, String type_) {
		super(context_, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		context = context_;
		Globals.rfood = objects;
		checked = false;
		type = type_;
		//imageLoader = imageLoader_;
	}
	
	public void setImageLoader(ImageLoader imageLoader_) {
		imageLoader = imageLoader_;
	}
	
	public int getCount() {
		return Globals.rfood.size();
	}
	public Ingredient getItem(int position) {
		return Globals.rfood.get(position);
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
		if (Globals.rfood.get(position).getImg() != null)
		{
			holder.progressBar.setVisibility(View.GONE);
			holder.img.setVisibility(View.VISIBLE);    	
			holder.indicator.setVisibility(View.VISIBLE);    	
			holder.img.setImageBitmap(Globals.rfood.get(position).getImg()); 
			holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.uncheckbox));		
		}
		else
		{
			if (Globals.rfood.get(position).getImg() == null)
			{
				imageLoader.displayImage(Globals.rfood.get(position).getImgUrl(), holder.img, options, new SimpleImageLoadingListener() {
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
						Globals.rfood.get(position).setImg(loadedImage);
						holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.uncheckbox));		
						holder.progressBar.setVisibility(View.GONE);
						holder.img.setVisibility(View.VISIBLE);
						if (!type.equals("browse"))
							holder.indicator.setVisibility(View.VISIBLE);   
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
				if (!type.equals("browse"))
					holder.indicator.setVisibility(View.VISIBLE);   
				holder.img.setImageBitmap(Globals.rfood.get(position).getImg());
			}
		}		
		holder.name.setText(Globals.rfood.get(position).getName());
		if (!type.equals("browse"))
		{
			holder.indicator.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (checked) {
						holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.uncheckbox));					
						checked = false;
						Globals.rfood.get(position).setChecked(false);
					}
					else {
						holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.checkbox));					
						checked = true;
						Globals.rfood.get(position).setChecked(true);
					}
				}
			});			
		}
		return view;
	}
}
