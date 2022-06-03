package org.escoladeltreball.droiddelfrac.adapters;

import java.util.List;

import org.escoladeltreball.droiddelfrac.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
 */
public class ConfirmPersonEventAdapter extends BaseAdapter {

	private final List<Object> objects;
	private final LayoutInflater layoutInflater;

	public ConfirmPersonEventAdapter(Context context, List<Object> contacts) {
		super();
		this.objects = contacts;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return objects.get(position);
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
		ImageButton icPerfilConfirmPerson;
		TextView itemEventName;
		ImageButton icStatus;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Usamos nuestro holder para asociar los datos con los componentes del layout
		ViewHolder holder = new ViewHolder();
		
		// Reutilizamos los views para no usar mas memoria que la necesaria
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_list_event_view, null);
			
			// Recuperamos los componentes y los asociamos al holder
			holder.icPerfilConfirmPerson = (ImageButton) convertView.findViewById(R.id.icPerfilConfirmPerson);
			holder.itemEventName = (TextView) convertView.findViewById(R.id.itemEventName);
			holder.icStatus = (ImageButton) convertView.findViewById(R.id.icStatus);
			
			// Asociamos la estructura del layout con con la vista que retornaremos en el método
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Asignamos los datos a los componentes del holder
//		holder.icPerfilConfirmPerson.setText("" + objects.get(position).getId().toString());
//		holder.itemEventName.setText(""+objects.get(position).getName().toString());
//		holder.icStatus.setText(objects.get(position).getTelephoneNumber());

		return convertView;
	}

}
