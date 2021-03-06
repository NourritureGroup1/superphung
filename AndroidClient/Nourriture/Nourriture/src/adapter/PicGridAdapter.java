package adapter;

import java.util.List;

import model.Moment;
import adapter.Holder.ViewHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.superphung.nourriture.R;

public class PicGridAdapter extends ArrayAdapter<Moment> implements Filterable {
	private Context context;
	@SuppressWarnings("unused")
	private ViewHolder holder;  
	private List<Moment> list;


	public PicGridAdapter(Context context_, int resource,
			int textViewResourceId, List<Moment> objects) {
		super(context_, resource, textViewResourceId, objects);
		context = context_;
		list = objects;
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
			view = inflater.inflate(R.layout.item_grid_image, parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.img = (ImageView) view.findViewById(R.id.image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (list.get(position).getImg() == null)
			System.out.println("IT IS NULL");
		else
			System.out.println("IT NOT IS NULL");
		holder.img.setImageBitmap(list.get(position).getImg());
		holder.img.setVisibility(View.VISIBLE);
		return view;
	}
}
