package org.escoladeltreball.droiddelfrac.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.pojo.Guest;
import org.escoladeltreball.droiddelfrac.model.pojo.ListGuest;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.task.AsyncAddExpense;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activity que permite al usuario añadir un gasto al evento.
 * 
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AddExpenseActivity extends Activity {

	private static final String TAG = "AddExpenseActivity";
	
	/* Components */
	private EditText amount;
	private EditText concept;
	private Spinner spUsers;
	
	/* Variables */
	private String idEvent;
	private List<Guest> guestList;
	private List<String> nameGuest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense);
		
		// Get extras
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			ListGuest list = extras.getParcelable("guesList");
			guestList = list.getGuestList();
			idEvent = list.getIdEvent();
		}
		
		initComponents();
		
		// Cargamos los participantes en el spinner
		loadGuestSpinner();
		
	}

	private void loadGuestSpinner() {
		// Obtenemos los nombres de los participantes
		nameGuest = new ArrayList<String>();
		for (Guest guest : guestList) {
			nameGuest.add(guest.getName());
		}
		Collections.sort(nameGuest);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameGuest);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spUsers.setAdapter(adapter);
	}

	private void initComponents() {
		spUsers = (Spinner) findViewById(R.id.spContactAddExpense);
		amount = (EditText) findViewById(R.id.edAmountAddExpense);
		concept = (EditText) findViewById(R.id.edConceptAddExpense);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_expense, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.actionOk:
			String idUser = getIdGuest(spUsers.getSelectedItem().toString());
			String concept = this.concept.getText().toString().trim();
			try {
				concept = URLEncoder.encode(concept, "latin-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String quantity = this.amount.getText().toString(); // pendiete comprobar que es un double y no un string
			String url = Constants.URL_ADD_EXPENSE + "?id_event=" + idEvent
					+ "&id_user=" + idUser + "&concept=" + concept + "&quantity="
					+ quantity;
			Log.i(TAG, url);
			AsyncAddExpense task = new AsyncAddExpense(getApplicationContext());
			task.execute(new String[] { url });
			
			try {
				if (task.get()){
					Toast.makeText(getApplicationContext(), "Añadiendo gasto...",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		default:
			finish();
			return super.onOptionsItemSelected(item);
		}
	}

	private String getIdGuest(String name) {
		Log.d(TAG, name);
		String idUser = null;
		for (Guest guest : guestList) {
			if (name.equals(guest.getName())){
				idUser = guest.getId();
			}
		}
		Log.d(TAG, idUser);
		return idUser;
	}

}
