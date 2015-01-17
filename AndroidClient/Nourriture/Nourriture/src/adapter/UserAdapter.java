package adapter;

import java.util.List;

import model.User;
import adapter.Holder.ViewHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.superphung.nourriture.Globals;
import com.superphung.nourriture.R;

public class UserAdapter extends ArrayAdapter<User> implements Filterable {
	private Context context;
	@SuppressWarnings("unused")
	private ViewHolder holder;  


	public UserAdapter(Context context_, int resource,
			int textViewResourceId, List<User> objects) {
		super(context_, resource, textViewResourceId, objects);
		context = context_;
		Globals.users = objects;
	}
	
	public int getCount() {
		return Globals.users.size();
	}
	public User getItem(int position) {
		return Globals.users.get(position);
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
			holder.name = (TextView) view.findViewById(R.id.name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.name.setText(Globals.users.get(position).getName());
		return view;
	}
}
