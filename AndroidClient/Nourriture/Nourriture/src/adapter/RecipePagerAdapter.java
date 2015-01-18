package adapter;

import java.util.List;

import model.Moment;
import model.Recipe;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.superphung.nourriture.R;

public class RecipePagerAdapter extends PagerAdapter {
	private List<Recipe> list;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private String type;

	public RecipePagerAdapter(List<Recipe> listRecipe, Context context_, ImageLoader imageLoader_, DisplayImageOptions options_, String type_) {
		list = listRecipe;
		context = context_;
		imageLoader = imageLoader_;
		options = options_;
		type = type_;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
		assert imageLayout != null;
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		TextView description = (TextView) imageLayout.findViewById(R.id.description);
		TextView name = (TextView) imageLayout.findViewById(R.id.name);
		
		imageLoader.displayImage(list.get(position).getImgUrl(), imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				@SuppressWarnings("unused")
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "Input/Output error";
					break;
				case DECODING_ERROR:
					message = "Image can't be decoded";
					break;
				case NETWORK_DENIED:
					message = "Downloads are denied";
					break;
				case OUT_OF_MEMORY:
					message = "Out Of Memory error";
					break;
				case UNKNOWN:
					message = "Unknown error";
					break;
				}
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (context == null)
					imageLoader.stop();
			}
		});
		
		view.addView(imageLayout, 0);
		description.setText(list.get(position).getDescription());
		name.setText(list.get(position).getName());
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}
