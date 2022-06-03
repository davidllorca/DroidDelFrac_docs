/**
 * 
 */
package org.escoladeltreball.droiddelfrac.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.adapters.TabsPagerAdapter;
import org.escoladeltreball.droiddelfrac.model.GetData;
import org.escoladeltreball.droiddelfrac.model.pojo.Bill;
import org.escoladeltreball.droiddelfrac.model.pojo.BillItem;
import org.escoladeltreball.droiddelfrac.model.pojo.Event;
import org.escoladeltreball.droiddelfrac.model.pojo.Expense;
import org.escoladeltreball.droiddelfrac.model.pojo.Guest;
import org.escoladeltreball.droiddelfrac.model.pojo.ListBills;
import org.escoladeltreball.droiddelfrac.model.pojo.ListEvExpenses;
import org.escoladeltreball.droiddelfrac.model.pojo.ListUsers;
import org.escoladeltreball.droiddelfrac.model.pojo.User;
import org.escoladeltreball.droiddelfrac.sqlite.ContactsSQLiteHelper;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.task.AsyncDataEvent;
import org.escoladeltreball.droiddelfrac.util.task.AsyncListBills;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class EventActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static final String TAG = "EventActivity";

	/* Variables */
	private String idAdmin;
	private String idUser;
	private String idEvent;
	private String confirmet;
	private String status;
	private List<Expense> expenseList;
	private List<User> userList;
	private List<Guest> guestList;
	private List<BillItem> billItemsList;

	/* Views */
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		// Recuperamos los datos que nos pasan
		Bundle bundle = null;
		if (savedInstanceState == null) {
			bundle = getIntent().getExtras();
			Event event = bundle.getParcelable("event");
			idAdmin = event.getIdAdmin() + "";
			idUser = bundle.getString("idUser");
			idEvent = event.getId() + "";
			status = event.getStatus() + "";
			String result = bundle.getString("result");
			handleResult(result);
		}

		// Inicializa componentes
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), bundle);

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Añade los Tabs
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_info)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_guests)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_bill)
				.setTabListener(this));
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}
	/**
	 * Realiza las llamadas al servlet y asigna valores a las variables.
	 */
	public void loadData(int mode) {
		String url = Constants.URL_EVENT_USERS + "?eventid=" + this.idEvent;
		AsyncDataEvent task = new AsyncDataEvent(this);
		task.execute(new String[] { url });

		// Recuperamos los gastos asociados al evento y los usuarios invitados
		List<Object> data = null;
		try {
			data = task.get();
			expenseList = ((ListEvExpenses) data.get(0)).getListExpenses();
			userList = ((ListUsers) data.get(1)).getListUsers();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Realiza las llamadas al servlet y asigna valores a las variables.
	 */
	public void loadData() {
		String url = Constants.URL_EVENT_USERS + "?eventid=" + this.idEvent;
		AsyncDataEvent task = new AsyncDataEvent(this);
		task.execute(new String[] { url });

		// Recuperamos los gastos asociados al evento y los usuarios invitados
		List<Object> data = null;
		try {
			data = task.get();
			expenseList = ((ListEvExpenses) data.get(0)).getListExpenses();
			userList = ((ListUsers) data.get(1)).getListUsers();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		// Cotejamos los invitados con los contactos del usuario y selecciona el
		// nombre del contacto en caso de existir en la agenda del teléfono.
		guestList = getGuests(userList);

		// Recupera el estado conrfimet del usuario.
		for (int i = 0; i < userList.size() && confirmet == null; i++) {
			if ((userList.get(i).getId() + "").equals(idUser)) {
				confirmet = userList.get(i).getConfirmet() + "";
			}
		}
		// Notificar al adaptador que se han producido cambios en los guests.
		EventGuestsFragment page = (EventGuestsFragment) getSupportFragmentManager()
				.findFragmentByTag(
						"android:switcher:" + R.id.pager + ":"
								+ viewPager.getCurrentItem());
		Log.i("tag eventguestFragment", "android:switcher:" + R.id.pager + ":"
				+ viewPager.getCurrentItem());
		
		page.notifyChangues();

		// EventGuestsFragment fragment =
		// (EventGuestsFragment)getSupportFragmentManager().findFragmentByTag("guests");
		// fragment.getAdapter().notifyDataSetChanged();

		if (status.equals("2")) {
			url = Constants.URL_LIST_BILLS + "?eventid=" + idEvent + "&userid="
					+ idUser;// &eventid // &userid
			Log.i(TAG, url);

			AsyncListBills taskBills = new AsyncListBills(this);
			taskBills.execute(url);

			ListBills list = null;

			try {
				list = taskBills.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			List<Bill> bills = list.getListBills();

			billItemsList = getBillItems(bills);
		}
	}

	/**
	 * Compara la lista de usuarios participantes obtenida por AsyncEventUsers y
	 * busca en la base de datos SQLite si se tiene el usuario en la agenda de
	 * contactos del teléfono. En caso de existir se cambia el nombre del
	 * usuario por el nombre con el que se tiene guardado el contacto en la
	 * agenda.
	 * 
	 * @param listUsersGuests
	 * @return lista de Guest(participantes) al evento.
	 */
	private List<Guest> getGuests(List<User> listUsersGuests) {

		List<Guest> guestsEventList = new ArrayList<>();

		if (listUsersGuests != null) {
			// Abrimos la base de datos en modo escritura
			ContactsSQLiteHelper csdbh = new ContactsSQLiteHelper(this,
					"DBContactos", null, 1);

			SQLiteDatabase db = csdbh.getWritableDatabase();

			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				// Recorre la lista de participantes obtenida con
				// AsyncEventUsers
				Log.i("List size", "" + listUsersGuests.size());
				for (User user : listUsersGuests) {
					// Crea el objeto Guest
					Guest guest = null;
					
					// Comprueba si está en la base de datos SQLITE
					int id = user.getId();
					String name;
					byte[] photo;
					int weight;
					int confirmet;

					if (!user.isPhantom()) {
						String query = "SELECT * FROM contacts WHERE id=" + id;
						Cursor cursor = db.rawQuery(query, null);
						// En caso de existir coge el el nombre y la foto de la
						// bd.
						if (cursor.moveToFirst()) {
							name = cursor.getString(1);
							// photo = cursor.getBlob(3);
						} else {
							// En caso de no existir coge el id/nombre y foto de
							// la
							// lista
							// obtenida
							name = user.getName();
							// photo = user.getPhoto();
						}
						photo = user.getPhoto();
						// HARCODED weight
						weight = 1;
						confirmet = user.getConfirmet();
						Log.i("User","confirmet:" + user.getConfirmet());

						
						if (photo != null) {
							guest = new Guest(id + "", name, photo, weight,
									confirmet);
						} else {
							guest = new Guest(id + "", name, weight, confirmet);

						}
					} else {
						name = user.getName();
						guest = new Guest(name, true);
					}
					
					Log.i("USER", user.toString());
					Log.i("GUEST", guest.toString());
					// Añade el participante a la lista
					guestsEventList.add(guest);
				}
				db.close();
			}
			// Devuelve la nueva lista
			return guestsEventList;
		} else {
			return null;
		}
	}

	private List<BillItem> getBillItems(List<Bill> billsList) {
		List<BillItem> itemsBillList = null;

		// Abrimos la base de datos en modo escritura
		ContactsSQLiteHelper csdbh = new ContactsSQLiteHelper(this,
				"DBContactos", null, 1);

		SQLiteDatabase db = csdbh.getWritableDatabase();
		// Consulta si hay datos del usuario con id determinada

		if (billsList != null) {
			itemsBillList = new ArrayList<>();

			if (db != null) {
				for (Bill bill : billsList) {

					// Atributos del nuevo objeto

					// Datos del Payer
					String namePayer;
					byte[] photoPayer = null;

					String idPayer = bill.getIdPayer() + "";
					String query = "SELECT name, photo FROM contacts WHERE id="
							+ idPayer;
					Cursor cursor = db.rawQuery(query, null);

					// En caso de existir coge el el nombre y la foto de la
					// bd.
					if (cursor.moveToFirst()) {
						namePayer = cursor.getString(0);
						photoPayer = cursor.getBlob(1);
					} else {
						// En caso de no existir coge el nombre de la lista
						// obtenida
						namePayer = bill.getNamePayer();
					}

					// Datos del Receiver
					String nameReceiver;
					byte[] photoReceiver = null;

					String idReceiver = bill.getIdReceiver() + "";
					query = "SELECT name, photo FROM contacts WHERE id="
							+ idReceiver;
					cursor = db.rawQuery(query, null);

					// En caso de existir coge el el nombre y la foto de la
					// bd.
					if (cursor.moveToFirst()) {
						nameReceiver = cursor.getString(0);
						photoReceiver = cursor.getBlob(1);
					} else {
						// En caso de no existir coge el nombre de la lista
						// obtenida
						nameReceiver = bill.getNameReceiver();
					}

					// Crea un item nuevo
					BillItem item = new BillItem(bill.getIdPayer() + "",
							photoPayer, namePayer, bill.getIdReceiver() + "",
							photoReceiver, nameReceiver, bill.getQuantity());

					itemsBillList.add(item);
				}
			}
		}

		for (BillItem billItem : itemsBillList) {
			Log.i("ItemBill", billItem.toString());
		}
		return itemsBillList;

	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("onRestart", "");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("onstart", "");
	}

	// Getters y setters

	public String getIdAdmin() {
		return idAdmin;
	}

	public String getIdUser() {
		return idUser;
	}

	public String getIdEvent() {
		return idEvent;
	}

	public List<User> getUserList() {
		return userList;
	}

	public List<Expense> getExpenseList() {
		return expenseList;
	}

	public String getConfirmet() {
		return confirmet;
	}
	
	public void setConfirmet(String confirmet) {
		this.confirmet =confirmet;
	}

	public List<Guest> getGuestList() {
		return guestList;
	}

	public List<BillItem> getBillItemsList() {
		return billItemsList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private void handleResult(String result) {
		List<Object> dataEvent = null;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				// Log BORRAR
				Log.i(TAG, "code: " + code);
				if (code.equals("200")) {

					String expensesString = GetData.getStringFromJSON("data",
							"expense", result);
					String usersString = GetData.getStringFromJSON("data",
							"user", result);
					Log.i("RESULT EventUser", expensesString);
					ListEvExpenses expensesList = new Gson().fromJson(
							expensesString, ListEvExpenses.class);
					expenseList = expensesList.getListExpenses();
					ListUsers usersList = new Gson().fromJson(usersString,
							ListUsers.class);
					userList = usersList.getListUsers();

					guestList = getGuests(userList);

					// Recupera el estado conrfimet del usuario.
					for (int i = 0; i < userList.size() && confirmet == null; i++) {
						if ((userList.get(i).getId() + "").equals(idUser)) {
							confirmet = userList.get(i).getConfirmet() + "";
						}
					}

				} else {
					// Toast info se ha producido otro error
					showToast(getString(R.string.badRequest));
					finish();
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				showToast(getString(R.string.badRequest));
				finish();
			}
		} else {
			showToast(getString(R.string.badRequest));
			finish();
		}
	}

	/**
	 * Muestra un mensaje Toast en la UI que ha llamado al Asynctask
	 * 
	 * @param errorMessage
	 */
	private void showToast(final String infomessage) {
		Handler handler = new Handler(getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), infomessage,
						Toast.LENGTH_LONG).show();
			}
		});
	}

}
