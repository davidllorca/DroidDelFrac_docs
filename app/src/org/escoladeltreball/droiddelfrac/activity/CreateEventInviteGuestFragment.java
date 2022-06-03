package org.escoladeltreball.droiddelfrac.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.adapters.InviteContactsAdapter;
import org.escoladeltreball.droiddelfrac.model.pojo.Contact;
import org.escoladeltreball.droiddelfrac.sqlite.ContactsSQLiteHelper;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.Util;
import org.escoladeltreball.droiddelfrac.util.task.AsyncAddUserPhantom;
import org.escoladeltreball.droiddelfrac.util.task.AsyncAddUsers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class CreateEventInviteGuestFragment extends Fragment {

	private static final String TAG = "CreateEventInviteGuestFragment";
	private static final int REQ_CODE_SKIP = 0;
	private static final int REQ_CODE_SYNC = 1;
	private static final int REQ_CODE_SENT = 2;
	private static final int REQ_CODE_PHANTOM = 3;
	private static final int REQ_CODE_ADD_GUEST = 4;


	/* Views */
	private View rootView;
	private ListView listViewGuest;
	private EditText inputSearchEditText;
	private ImageView syncImage;
	private TextView textNoUsers;

	private InviteContactsAdapter adapter;

	/* Variables */
	private String idUser;
	private String idEvent;
	private List<Contact> contacts;
	private List<String> phantomGuests;
	

	public CreateEventInviteGuestFragment(Bundle bundle) {
		// Recuperamos los datos que nos mandan desde la activity
		if (bundle != null) {
			idEvent = bundle.getString("idEvent", "-1");
			Log.i("idEvent", idEvent);
		}
		// Inicaliza variables
		phantomGuests = new ArrayList<>();

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				R.layout.fragment_create_event_invite_guests, container, false);

		// Carga componentes
		initComponents();

		// Hablitamos que se pueda cambiar el actionBar en cada fragment
		setHasOptionsMenu(true);

		return rootView;
	}

	private void getContacts() {
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

			// En caso de encontrar algun contacto en la BD
			if (nContactsDB > 0) {
				// Seleccionamos todos los contactos de SQLITE
				String query = "SELECT * FROM contacts";
				c = db.rawQuery(query, null);

				contacts = new ArrayList<>();
				// Nos aseguramos de que existe al menos un registro
				if (c.moveToFirst()) {
					int i = 0;
					// Recorremos el cursor hasta que no haya más registros
					do {
						String id = c.getString(0);
						String name = c.getString(1);
						byte[] photo = c.getBlob(3);
						contacts.add(new Contact(id, name, photo));
						Log.i("cotnact", name);
						i++;
					} while (c.moveToNext());
				}

			} else {
				showSyncContactsDialog();
			}
			// Cerramos la base de datos
			db.close();
		}
	}

	/**
	 * Referencia los componentes y añade los listeners
	 */
	private void initComponents() {
		// Inicializamos contacts
		contacts = new ArrayList<>();

		syncImage = (ImageView) rootView.findViewById(R.id.imageViewSync);
		inputSearchEditText = (EditText) rootView
				.findViewById(R.id.editTextSearchUsers);
		textNoUsers = (TextView) rootView.findViewById(R.id.textNoUsers);

		// recuperar los contactos de la bd
		getContacts();

		if (contacts.size() > 0) {
			syncImage.setVisibility(View.GONE);
			textNoUsers.setVisibility(View.GONE);
			inputSearchEditText.setVisibility(View.VISIBLE);

			// recuperar la listview
			listViewGuest = (ListView) rootView
					.findViewById(R.id.listViewGuests);

			// Añadir adaptador
			adapter = new InviteContactsAdapter(getActivity(), contacts);
			listViewGuest.setAdapter(adapter);
			
			/*
			 * Filtro de busqueda
			 */
			inputSearchEditText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					adapter.getFilter().filter(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
				}
			});
		} else {
			inputSearchEditText.setVisibility(View.GONE);
			syncImage.setVisibility(View.VISIBLE);
			textNoUsers.setVisibility(View.VISIBLE);
		}

		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_send_invitations, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionSendInvitations:
			sendInvitations();
			return false;
		case R.id.actionAddPhantomGuest:
			AddGuestDialogFragment dialog = new AddGuestDialogFragment();
			dialog.setTargetFragment(this, REQ_CODE_PHANTOM);
			dialog.show(getFragmentManager(), "dialog_add_guests");
			return false;
		case R.id.actionSyncContacts:
			Util.insertContactsSQLite(getActivity());
			initComponents();
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void sendInvitations() {
		List<String> idContactsSelected = getIdContactsSelected();

		// Si exites algun contatcto seleccionado.
		if (!(idContactsSelected.size() + phantomGuests.size() == 0)) {
			// Si es un usuario virtual.
			if (!(phantomGuests.size() == 0)) {
				String url = Constants.URL_ADD_USER_PHANTOM + "?eventid="
						+ idEvent + "&name=";
//				AsyncAddUserPhantom task = new AsyncAddUserPhantom(
//						getActivity());
				for (String namePhantom : phantomGuests) {
					
					AsyncAddUserPhantom task = new AsyncAddUserPhantom(
							getActivity());
					task.execute(url + namePhantom.toString());
					try {
						task.get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					Log.i("phantom URL", url + namePhantom.toString());

				}
			}

			// Si es un usuario real
			if (!(idContactsSelected.size() == 0)) {
				AsyncAddUsers task = new AsyncAddUsers(getActivity(), idEvent);
				task.execute(idContactsSelected);

				try {
					task.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}

			showInvitationsSent();
			getActivity().setResult(Activity.RESULT_OK);
		} else {
			showNoContactSelectedDialog();
		}
	}

	/**
	 * Implementa una List con los ids de los items seleccionados.
	 * 
	 * @return list con los id de los items seleccionados.
	 */
	private List<String> getIdContactsSelected() {
		// Reset del EditText de búsqueda.
		if (!inputSearchEditText.getText().toString().equals("")) {
			inputSearchEditText.setText("");
		}
		// Recupera la listview y sus componentes.
		View view = null;
		CheckBox checkbox = null;
		// List<Contact> al que se desea enviar inivtaciones
		List<String> idContactsSelected = new ArrayList<>();
		// Recorrer la listview y selecciona los contactos seleccionados.

		if (listViewGuest != null) {
			if (listViewGuest.getCount() > 0) {
				for (int i = 0; i < listViewGuest.getChildCount(); i++) {
					view = listViewGuest.getChildAt(i);
					checkbox = (CheckBox) view
							.findViewById(R.id.checkInviteContacts);
					if (checkbox.isChecked()) {
						Contact contactToGuest = (Contact) listViewGuest
								.getItemAtPosition(i);
						// Si es un contacto con la apliación guardamos la id
						if (!contactToGuest.isPhantom()) {
							idContactsSelected.add(contactToGuest.getId());
						} else {
							// Sino guardamos el nombre del inivitado 'virtual'
							phantomGuests.add(contactToGuest.getName());
						}
					}
				}
			}
		}
		return idContactsSelected;
	}

	/**
	 * Muestra Dialog informando que no hay ningún contacto seleccionado. Desde
	 * el qual se puede saltar el paso de enviar invitaciones a participantes.
	 */
	private void showNoContactSelectedDialog() {
		NoContactSelectedDialogFragment dialog = new NoContactSelectedDialogFragment();
		dialog.setTargetFragment(this, REQ_CODE_SKIP);
		dialog.show(getFragmentManager(), "NoContactSelectedDialogFragment");
	}

	/**
	 * Muestra Dialog informando que no existen contactos. Desde el qual se
	 * puede realizar la acción de sincronizar usuarios con los contactos de la
	 * agenda del dispositivo.
	 */
	private void showSyncContactsDialog() {
		SyncContactsDialogFragment dialog = new SyncContactsDialogFragment();
		dialog.setTargetFragment(this, REQ_CODE_SYNC);
		dialog.show(getFragmentManager(), "SyncContactsDialogFragment");
	}

	/**
	 * Muestra Dialog informando que las invitaciones a los participantes se han
	 * enviado correctamente.
	 */
	private void showInvitationsSent() {
		InvitationsSentDialogFragment dialog = new InvitationsSentDialogFragment();
		dialog.setTargetFragment(this, REQ_CODE_SENT);
		dialog.show(getFragmentManager(), "InvitationsSentDialogFragment");
	}

	private void initListView() {
		listViewGuest = (ListView) rootView.findViewById(R.id.listViewGuests);
		adapter = new InviteContactsAdapter(getActivity(), contacts);
		listViewGuest.setAdapter(adapter);
		inputSearchEditText.setVisibility(View.VISIBLE);
		syncImage.setVisibility(View.GONE);
	}

	/**
	 * CallBack DialogFragments.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQ_CODE_SKIP:
				getActivity().finish();
				break;
			case REQ_CODE_SYNC:
				// Vuelve a cargar los componentes
				initComponents();
				break;
			case REQ_CODE_SENT:
				getActivity().finish();
				break;
			case REQ_CODE_PHANTOM:
				// Recoge el array de los extras, crea los items, los añade a la
				// List.
				Bundle extras = data.getExtras();

				String[] phantomGuestsArray = extras
						.getStringArray("phantomGuests");
				if (phantomGuestsArray.length > 0) {
					Log.i("phantomList", ">0");
					for (int j = 0; j < phantomGuestsArray.length; j++) {
						contacts.add(new Contact(phantomGuestsArray[j]));
					}
					Log.i("Phantom", contacts.toString());					
					initListView();
					
					// Notifica al adaptador que el contenido ha cambiado.
//					adapter.notifyDataSetChanged();
				}

				break;
			default:
				break;
			}
		}
	}	
}
