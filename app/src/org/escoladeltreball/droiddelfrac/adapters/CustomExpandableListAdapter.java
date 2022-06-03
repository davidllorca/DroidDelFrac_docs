package org.escoladeltreball.droiddelfrac.adapters;


import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.pojo.Event;
import org.escoladeltreball.droiddelfrac.model.pojo.Group;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adaptador que se encarga de asociar los datos recogidos con
 * los componentes gráficos del layout donde se quieren mostrar
 * dichos datos
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 *
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

	private final SparseArray<Group> groups;
	public LayoutInflater inflater;
	public Activity activity;

	public CustomExpandableListAdapter(Activity act, SparseArray<Group> groups) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}
	
	/**
	   * Patrón para crear el modelo que representará el ítem 
	   * del listView
	   */
	  static class ViewHolderGroup {
		  ImageView photo;
		  TextView name;
	  }
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		ViewHolderGroup holder = new ViewHolderGroup();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group_event_list_exp, null);

			holder.photo = (ImageView) convertView.findViewById(R.id.imageParent);
			holder.name = (TextView) convertView.findViewById(R.id.textParent);
			
			// Asociamos la estructura del layout con con la vista que retornaremos en el método
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderGroup) convertView.getTag();
		}
		
		Group group = (Group) getGroup(groupPosition);
		byte[] pho = group.getChildren().get(0).getPhoto();
		
		if (pho != null){
	    	Bitmap bitmap = BitmapFactory.decodeByteArray(pho, 0, pho.length);
	    	holder.photo.setImageBitmap(bitmap);
	    } else {
	    	holder.photo.setImageResource(R.drawable.ic_launcher);
	    }
	    holder.name.setText(group.getName());
		
		return convertView;
	}
	
	/**
	   * Patrón para crear el modelo que representará el ítem 
	   * del listView
	   */
	  static class ViewHolderChild {
		  ImageView photo;
		  TextView description;
		  TextView place;
		  TextView date;
		  TextView amount;
	  }
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		ViewHolderChild holder = new ViewHolderChild();
		
	    if (convertView == null) {
	      convertView = inflater.inflate(R.layout.child_event_list_exp, null);
	      
	      holder.photo = (ImageView) convertView.findViewById(R.id.imageChild);
	      holder.description = (TextView) convertView.findViewById(R.id.descriptionChild);
	      holder.place = (TextView) convertView.findViewById(R.id.placeChild);
	      holder.date = (TextView) convertView.findViewById(R.id.dateChild);
	      holder.amount = (TextView) convertView.findViewById(R.id.amountChild);

	      // Asociamos la estructura del layout con con la vista que retornaremos en el método
	      convertView.setTag(holder);
	    } else {
	    	holder = (ViewHolderChild) convertView.getTag();
	    }
	    
	    final Event children = (Event) getChild(groupPosition, childPosition);
	    
	    byte[] pho = children.getPhoto();
	    String desc = children.getDescription();
	    String place = children.getPlace();
	    String date = children.getDateEvent();
	    String amount = children.getAmount() + " €";
	    
	    if (pho != null){
	    	Bitmap bitmap = BitmapFactory.decodeByteArray(pho, 0, pho.length);
	    	holder.photo.setImageBitmap(bitmap);
	    } else {
	    	holder.photo.setImageResource(R.drawable.ic_launcher);
	    }
	    // Si un dato no lo tenemos, lo ocultamos para que no ocupe lugar
	    if (desc == null || desc.equals("")){
	    	holder.description.setVisibility(View.GONE);
	    } else {
	    	holder.description.setVisibility(View.VISIBLE);
	    	holder.description.setText(desc);
	    }
	    
	    if (place == null || desc.equals("")){
	    	holder.place.setVisibility(View.GONE);
	    } else {
	    	holder.place.setVisibility(View.VISIBLE);
	    	holder.place.setText(place);
	    }
	    
	    if (date == null){
	    	holder.date.setVisibility(View.GONE);
	    } else {
	    	holder.date.setVisibility(View.VISIBLE);
	    	holder.date.setText(date.toString());
	    }
	    holder.amount.setText(amount);

		
		return convertView;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// Solo queremos tener un child donde mostramos toda la información
	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
