package org.escoladeltreball.droiddelfrac.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.adapters.ContactAdapter;
import org.escoladeltreball.droiddelfrac.model.pojo.Contact;
import org.escoladeltreball.droiddelfrac.model.pojo.ListUsers;
import org.escoladeltreball.droiddelfrac.model.pojo.User;
import org.escoladeltreball.droiddelfrac.sqlite.ContactsSQLiteHelper;
import org.escoladeltreball.droiddelfrac.util.task.AsyncCompareContacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 * 
 *         This is free software, licensed under the GNU General Public License
 *         v3. See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class SyncContactsFragment extends Fragment {

	public static final String CONTACTS = "org.escoladeltreball.contentprovidercontact.CONTACTS";

	/* Views */
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_sync_contacts, container,
				false);

		// Carga de los componntes
		initComponents();

		return rootView;
	}

	/**
	 * Recupera todos los contactos del ContentProvider Contacts.
	 * 
	 * @return List<Contacts>
	 */
	private List<Contact> getContactsList() {

		List<Contact> contactsList = new ArrayList<>();

		ContentResolver cr = getActivity().getContentResolver();

		// Obtiene toda la informarciónd de los contactos
		Cursor cursor = getActivity().getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null,
				"display_name");
		// Obtiene el número de la columnas que queremos almacenar.
		int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		int displayNameIndex = cursor
				.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			// Comprueba que tenga numero de teléfono
			if (Integer
					.parseInt(cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
				String id = cursor.getString(idIndex);
				String name = cursor.getString(displayNameIndex);
				String phone = null;

				String selectionArgs = Phone.CONTACT_ID + " = ? AND "
						+ Phone.TYPE + "= " + Phone.TYPE_MOBILE;

				Cursor pCur = cr.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						new String[] { Phone.NUMBER }, selectionArgs,
						new String[] { id }, null);

				if (pCur.moveToFirst()) {
					phone = pCur.getString(0);

				}
				pCur.close();

				// Crea el objeto contact
				Contact contact = new Contact(id, name, phone);
				contactsList.add(contact);
			}

		}

		return contactsList;

	}

	/**
	 * Envía los números de teléfono de los contactos de la agenda al servlet
	 * para compararlos con los de la base de datos.
	 * 
	 * @return la lista de usuarios que ya tienen la app.
	 */
	private List<User> compareContacts() {
		List<User> users = null;
		List<Contact> contactsList = getContactsList();
		List<String> nums = new ArrayList<String>();
		for (Contact c : contactsList) {
			String num = c.getPhoneNumber();
			if (num != null) {
				num = num.replace("+34", "");
				num = num.replaceAll(" ", "");
				num = num.replace("-", "");
				nums.add(num);
			}
		}
		AsyncCompareContacts acc = new AsyncCompareContacts(getActivity());
		acc.execute(nums);
		ListUsers list = null;
		try {
			list = acc.get();
			users = list.getListUsers();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

	private void insertContactsSQLite() {

		List<User> list = compareContacts();

		for (User user : list) {
			Log.i("User", user.toString());
		}
		if (list.size() > 0) {
			// Abrimos la base de datos en modo escritura
			ContactsSQLiteHelper csdbh = new ContactsSQLiteHelper(
					getActivity(), "DBContactos", null, 1);

			SQLiteDatabase db = csdbh.getWritableDatabase();
			// Reseteamos la tabla contactos
			csdbh.onUpgrade(db, 1, 1);
			Log.i("RESET", "drop table contacts");

			// Si hemos abierto correctamente la base de datos
			if (db != null) {

				List<Contact> contacts = getContactsList();
				Map<String, String> mapNames = new HashMap<>();

				// Cambia el formato del teléfono
				for (Contact contact : contacts) {
					String num = contact.getPhoneNumber();
					if (num != null) {
						num = num.replace("+34", "");
						num = num.replaceAll(" ", "");
						num = num.replace("-", "");
						mapNames.put(num, contact.getName());
					}
				}

				for (User u : list) {
					int id = u.getId();
					String phone = u.getPhone();
					String name = mapNames.get(phone);
					byte[] photo = u.getPhoto();

					// String sql = "INSERT INTO contacts (id,name, phone) "
					// + "VALUES (" + id + ",'" + name + "', '" + phone
					// + "')";
					String sql = "INSERT INTO contacts (id, name, phone, photo) VALUES(?,?,?,?)";
					SQLiteStatement st = db.compileStatement(sql);
					st.clearBindings();
					st.bindDouble(1, id);
					st.bindString(2, name);
					st.bindString(3, phone);
					if (photo != null) {
						st.bindBlob(4, photo);
					}
					st.executeInsert();
					// db.execSQL(sql);
				}
			}

			String queryExistInAgenda = "SELECT id FROM contacts WHERE id="
					+ Double.parseDouble(((EventListMenuActivity) getActivity()).idUser);
			Cursor cursor = db.rawQuery(queryExistInAgenda, null);

			// En caso de no existir lo inserta

			if (!cursor.moveToFirst()) {
				// Inserta al propio usuario en la base de datos.
				String sqlInserOwnUser = "INSERT INTO contacts (id, name, phone, photo) VALUES(?,?,?,?)";
				SQLiteStatement st = db.compileStatement(sqlInserOwnUser);
				st.clearBindings();
				st.bindDouble(
						1,
						Double.parseDouble(((EventListMenuActivity) getActivity()).idUser));
				st.bindString(2,
						((EventListMenuActivity) getActivity()).nameUser);
				st.bindBlob(4, ((EventListMenuActivity) getActivity()).photo);
				st.executeInsert();
			}

			// Cerramos la base de datos
			db.close();
		}
	}

	/**
	 * Referencia los componentes y añade los listeners
	 */
	private void initComponents() {

		// Inserta los contactos en DBContactos
		insertContactsSQLite();

		// Abrimos la base de datos en modo escritura
		ContactsSQLiteHelper csdbh = new ContactsSQLiteHelper(getActivity(),
				"DBContactos", null, 1);

		SQLiteDatabase db = csdbh.getWritableDatabase();

		// Si hemos abierto correctamente la base de datos
		if (db != null) {

			// Comprueba si existe algun registro
			String queryNConctacts = "SELECT COUNT(name) FROM contacts";
			Cursor c = db.rawQuery(queryNConctacts, null);
			int nContactsDB = 0;
			if (c != null) {
				c.moveToFirst();
				nContactsDB = c.getInt(0);
				Log.i("ncontacts", nContactsDB + "");
			}

			if (nContactsDB > 0) {
				// Seleccionamos todos los contactos de SQLITE
				String query = "SELECT * FROM contacts";
				c = db.rawQuery(query, null);

				List<Contact> contacts = new ArrayList<>();
				// Nos aseguramos de que existe al menos un registro
				if (c.moveToFirst()) {
					int i = 0;
					// Recorremos el cursor hasta que no haya más registros
					do {
						String name = c.getString(1);
						byte[] photo = c.getBlob(3);
						contacts.add(new Contact(name, photo));

						i++;
					} while (c.moveToNext());
				}

				// Crea adaptador para la vista
				ContactAdapter adapter = new ContactAdapter(getActivity(),
						contacts);
				ListView listView = (ListView) rootView
						.findViewById(R.id.listViewContacts);
				listView.setAdapter(adapter);
			} else {
				Toast.makeText(getActivity(),
						getActivity().getString(R.string.noContacts),
						Toast.LENGTH_SHORT).show();
			}
			// Cerramos la base de datos
			db.close();

		}
	}
}
