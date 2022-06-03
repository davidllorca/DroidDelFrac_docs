package org.escoladeltreball.droiddelfrac.adapters;

import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.R.drawable;
import org.escoladeltreball.droiddelfrac.model.pojo.Guest;
import org.escoladeltreball.droiddelfrac.model.pojo.User;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adaptador que se encarga de asociar los datos recogidos con
 * los componentes gráficos del layout donde se quieren mostrar
 * dichos datos.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class EventGuestAdapter extends BaseAdapter {

	private final List<Guest> guests;
	private final LayoutInflater layoutInflater;

	public EventGuestAdapter(Context context, List<Guest> guests) {
		super();
		this.guests = guests;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return guests.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return guests.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	/**
	 * Patrón para crear el modelo que representará el ítem 
	 * del listView
	 */
	static class ViewHolder {
		ImageButton photoEventGuest;
		TextView nameEventGuest;
		ImageView state;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("GUEST in adapter", guests.get(position).toString());
		// Usamos nuestro holder para asociar los datos con los componentes del layout
		ViewHolder holder = new ViewHolder();
		
		// Reutilizamos los views para no usar mas memoria que la necesaria
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_event_guest, null);
			
			// Recuperamos los componentes y los asociamos al holder
			holder.photoEventGuest = (ImageButton) convertView.findViewById(R.id.photoEventGuest);
			holder.nameEventGuest = (TextView) convertView.findViewById(R.id.nameEventGuest);
			holder.state = (ImageView) convertView.findViewById(R.id.imageViewStateUser);
			
			// Asociamos la estructura del layout con con la vista que retornaremos en el método
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Asignamos los datos a los componentes del holder
		//Avatar
		if (guests.get(position).getPhoto() != null) {
			holder.photoEventGuest.setImageBitmap(BitmapFactory.decodeByteArray(
					guests.get(position).getPhoto(), 0, guests.get(position)
							.getPhoto().length));
		} else{
			holder.photoEventGuest.setImageResource(R.drawable.ic_launcher);
		}
		// Nombre del participante
		holder.nameEventGuest.setText(guests.get(position).getName());
		// Estado 
		switch (guests.get(position).getConfirmet()) {
		case 0:
			holder.state.setImageResource(R.drawable.ic_pending_confirm);
			Log.i(guests.get(position).getName(), "?");
			break;
		case 1:
			holder.state.setImageResource(R.drawable.ic_accept_partipation);
			Log.i(guests.get(position).getName(), "accept");
			break;
		default:
			holder.state.setImageResource(R.drawable.ic_action_bad);
			Log.i(guests.get(position).getName(), "not accept");
			break;
		}
		
		return convertView;
	}

}