package org.escoladeltreball.droiddelfrac.util;

import java.util.ArrayList;
import java.util.List;

import org.escoladeltreball.droiddelfrac.model.pojo.Contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.escoladeltreball.droiddelfrac.model.pojo.Contact;
import org.escoladeltreball.droiddelfrac.model.pojo.ListUsers;
import org.escoladeltreball.droiddelfrac.model.pojo.User;
import org.escoladeltreball.droiddelfrac.sqlite.ContactsSQLiteHelper;
import org.escoladeltreball.droiddelfrac.util.task.AsyncCompareContacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class Util {

	/**
	 * Ajusta la altura del layout en función de los items
	 * expandidos para conseguir que se pueda hacer scroll
	 * en todo el layout en lugar de solo en una lista
	 * 
	 * @param listView
	 */
	public static void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	/**
	 * Ajusta la altura del layout en función de los items
	 * expandidos para conseguir que se pueda hacer scroll
	 * en todo el layout en lugar de solo en una lista
	 * 
	 * @param listView
	 * @param group
	 */
	public static void setListViewHeight(ExpandableListView listView,
			int group) {
		ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.EXACTLY);
		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			View groupItem = listAdapter.getGroupView(i, false, null, listView);
			groupItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

			totalHeight += groupItem.getMeasuredHeight();

			if (((listView.isGroupExpanded(i)) && (i != group))
					|| ((!listView.isGroupExpanded(i)) && (i == group))) {
				for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
					View listItem = listAdapter.getChildView(i, j, false, null,
							listView);
					listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

					totalHeight += listItem.getMeasuredHeight();

				}
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
		if (height < 10)
			height = 200;
		params.height = height;
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
	
	/**
	 * Recupera todos los contactos del ContentProvider Contacts.
	 * 
	 * @return List<Contacts>
	 */
	public static List<Contact> getContactsList(Context context) {

		List<Contact> contactsList = new ArrayList<>();

		ContentResolver cr = context.getContentResolver();

		// Obtiene toda la informarciónd de los contactos
		Cursor cursor = context.getContentResolver().query(
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
	public static List<User> compareContacts(Context context) {
		List<User> users = null;
		List<Contact> contactsList = getContactsList(context);
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
		AsyncCompareContacts acc = new AsyncCompareContacts(context);
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
	
	

	public static void insertContactsSQLite(Context context) {

		List<User> list = compareContacts(context);

		if (list.size() > 0) {
			// Abrimos la base de datos en modo escritura
			ContactsSQLiteHelper csdbh = new ContactsSQLiteHelper(
					context, "DBContactos", null, 1);

			SQLiteDatabase db = csdbh.getWritableDatabase();
			// Reseteamos la tabla contactos
			csdbh.onUpgrade(db, 1, 1);
			Log.i("RESET", "drop table contacts");

			// Si hemos abierto correctamente la base de datos
			if (db != null) {

				List<Contact> contacts = getContactsList(context);
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

//					String sql = "INSERT INTO contacts (id,name, phone) "
//							+ "VALUES (" + id + ",'" + name + "', '" + phone
//							+ "')";
					String sql ="";
					if (photo != null) {
						sql = "INSERT INTO contacts (id, name, phone, photo) VALUES(?,?,?,?)";
						SQLiteStatement st = db.compileStatement(sql);
						st.clearBindings();
						st.bindDouble(1, id);
						st.bindString(2, name);
						st.bindString(3, phone);
						st.bindBlob(4, photo);
						st.executeInsert();
					} else {
						sql = "INSERT INTO contacts (id, name, phone) VALUES(?,?,?)";
						SQLiteStatement st = db.compileStatement(sql);
						st.clearBindings();
						st.bindDouble(1, id);
						st.bindString(2, name);
						st.bindString(3, phone);
						st.executeInsert();
					}
//					db.execSQL(sql);
				}
			}
			// Cerramos la base de datos
			db.close();
		}
	}
}
