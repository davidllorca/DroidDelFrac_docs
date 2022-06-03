package org.escoladeltreball.droiddelfrac.adapters;

import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.pojo.Expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class EventInfoAdapter extends BaseAdapter {

	private final List<Expense> expenses;
	private final LayoutInflater layoutInflater;

	public EventInfoAdapter(Context context, List<Expense> expenses) {
		super();
		this.expenses = expenses;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return expenses.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return expenses.get(position);
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
		TextView nameConceptEventInfo;
		TextView nameContactEventInfo;
		TextView importEventInfo;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Usamos nuestro holder para asociar los datos con los componentes del layout
		ViewHolder holder = new ViewHolder();
		
		// Reutilizamos los views para no usar mas memoria que la necesaria
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_list_event_info, null);
			
			// Recuperamos los componentes y los asociamos al holder
			holder.nameConceptEventInfo = (TextView) convertView.findViewById(R.id.nameConceptEventInfo);
			holder.nameContactEventInfo = (TextView) convertView.findViewById(R.id.nameContactEventInfo);
			holder.importEventInfo = (TextView) convertView.findViewById(R.id.importEventInfo);
			
			// Asociamos la estructura del layout con con la vista que retornaremos en el método
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Asignamos los datos a los componentes del holder
		holder.nameConceptEventInfo.setText(expenses.get(position).getConcept());
		holder.nameContactEventInfo.setText(expenses.get(position).getUsername());
		holder.importEventInfo.setText(String.valueOf(expenses.get(position).getQuantity()) + " €");

		return convertView;
	}

}
