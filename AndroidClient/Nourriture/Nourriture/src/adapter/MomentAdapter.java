package adapter;

import java.util.List;
import java.util.Locale;

import model.Moment;

import org.ocpsoft.prettytime.PrettyTime;

import adapter.Holder.ViewHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.superphung.nourriture.R;

public class MomentAdapter extends ArrayAdapter<Moment> implements Filterable {
	private Context context;
	@SuppressWarnings("unused")
	private ViewHolder holder;  
	private List<Moment> list;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;


	public MomentAdapter(Context context_, int resource,
			int textViewResourceId, List<Moment> objects,ImageLoader imageLoader_,DisplayImageOptions options_) {
		super(context_, resource, textViewResourceId, objects);
		context = context_;
		list = objects;
		imageLoader = imageLoader_;
		options = options_;
	}
	
	public int getCount() {
		return list.size();
	}
	public Moment getItem(int position) {
		return list.get(position);
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
			view = inflater.inflate(R.layout.row_list_moments, parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.description = (TextView) view.findViewById(R.id.description);
			holder.img = (ImageView) view.findViewById(R.id.list_image);
			holder.date = (TextView) view.findViewById(R.id.date);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.description.setText(list.get(position).getDescription());
		PrettyTime p = new PrettyTime(new Locale("en"));
		holder.date.setText(p.format(list.get(position).getDate()));
		imageLoader.displayImage(list.get(position).getImgUrl(), holder.img, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (context == null)
					imageLoader.stop();
			}
		});
		return view;
	}
}
