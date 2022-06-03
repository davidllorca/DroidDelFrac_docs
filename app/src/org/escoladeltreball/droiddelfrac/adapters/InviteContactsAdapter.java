package org.escoladeltreball.droiddelfrac.adapters;

import java.util.ArrayList;
import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.pojo.Contact;
import org.escoladeltreball.droiddelfrac.model.pojo.User;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Adaptador que se encarga de asociar los datos recogidos con los componentes
 * gráficos del layout donde se quieren mostrar dichos datos.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 * 
 *         This is free software, licensed under the GNU General Public License
 *         v3. See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class InviteContactsAdapter extends BaseAdapter implements Filterable {

	private final List<Contact> objects;
	private final LayoutInflater layoutInflater;
	private ItemFilter mFilter = new ItemFilter();
	private List<Contact> filteredObjects;

	// private boolean[] checkBoxState;

	public InviteContactsAdapter(Context context, List<Contact> contacts) {
		super();
		this.objects = contacts;
		this.filteredObjects = contacts;
		this.layoutInflater = LayoutInflater.from(context);
		// this.checkBoxState=new boolean[contacts.size()];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filteredObjects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return filteredObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * Patrón para crear el modelo que representará el ítem del listView
	 */
	static class ViewHolder {
		ImageButton icPerfilInvitePerson;
		TextView nameContactListInvitePersonEvent;
		CheckBox checkInviteContacts;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// Usamos nuestro holder para asociar los datos con los componentes del
		// layout
		ViewHolder holder = new ViewHolder();

		// Reutilizamos los views para no usar mas memoria que la necesaria
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_invite_contacts,
					null);

			// Recuperamos los componentes y los asociamos al holder
			holder.icPerfilInvitePerson = (ImageButton) convertView
					.findViewById(R.id.icPerfilInvitePerson);
			holder.nameContactListInvitePersonEvent = (TextView) convertView
					.findViewById(R.id.nameContactListInvitePersonEvent);
			holder.checkInviteContacts = (CheckBox) convertView
					.findViewById(R.id.checkInviteContacts);

			// Asociamos la estructura del layout con con la vista que
			// retornaremos en el método
			convertView.setTag(holder);
		} else {
			Contact c = (Contact) getItem(position);
			holder = (ViewHolder) convertView.getTag();
		}

		// Asignamos los datos a los componentes del holder
		if (filteredObjects.get(position).getPhoto() != null) {
			holder.icPerfilInvitePerson.setImageBitmap(BitmapFactory.decodeByteArray(
					filteredObjects.get(position).getPhoto(), 0, filteredObjects.get(position)
							.getPhoto().length));
		} else {
			holder.icPerfilInvitePerson.setImageResource(R.drawable.ic_launcher);
		}
		
		holder.nameContactListInvitePersonEvent.setText(""
				+ filteredObjects.get(position).getName().toString());
		// Para los datos del filtro
		// originalData.add(objects.get(position).getName().toString());
		// Log.i("name", objects.get(position).getName().toString());
		// holder.checkInviteContacts.setText(objects.get(position).getTelephoneNumber());

		if (filteredObjects.get(position).isChecked()) {
			holder.checkInviteContacts.setChecked(true);
		} else {
			holder.checkInviteContacts.setChecked(false);
		}

		// Control CheckBox
		// holder.checkInviteContacts.setChecked(checkBoxState[position]);

		holder.checkInviteContacts
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Contact contact = (Contact) getItem(position);
						int index = filteredObjects.indexOf(contact);

						if (((CheckBox) v).isChecked())
							filteredObjects.get(index).setChecked(true);
						// checkBoxState[position] = true;
						else
							// checkBoxState[position] = false;
							filteredObjects.get(index).setChecked(false);
					}
				});

		return convertView;
	}

	@Override
	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			final List<Contact> list = objects;

			int count = list.size();
			final ArrayList<Contact> nlist = new ArrayList<Contact>(count);

			String filterableString;

			for (int i = 0; i < count; i++) {
				filterableString = list.get(i).getName();
				if (filterableString.toLowerCase().contains(filterString)) {
					// nlist.add(filterableString);
					nlist.add(list.get(i));
				}
			}

			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredObjects = (ArrayList<Contact>) results.values;
			notifyDataSetChanged();

		}

	}
	
	// Getters y setters
	
	public List<Contact> getFilteredObjects() {
		return filteredObjects;
	}
}