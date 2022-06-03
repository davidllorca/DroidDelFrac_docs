package org.escoladeltreball.droiddelfrac.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.pojo.Contact;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.Util;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Dialog que permite al usuario añadir un gasto al evento.
 * 
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AddExpenseDialogFragment extends DialogFragment implements
		OnClickListener {

	private static final String TAG = "AddExpenseDialogFragment";
	
	/* Componentes */
	private View rootView;
	private EditText conceptExpense;
	private EditText quantityExpense;
	private ListView guest;
	
	/* Variables */
	private String idUser;
	private String idEvent;
	
	public AddExpenseDialogFragment(Bundle extra) {
		idUser = extra.getString("idUser");
		idEvent = extra.getString("idEvent");
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.dialog_add_expense, container);
		
		getDialog().setTitle(R.string.addExpense);

		// Recuperamos los componentes
		initComponents();
		
		// Cargamos los datos de los contactos
		List<Contact> contacts = Util.getContactsList(getActivity());
		String[] nameContacts = new String[contacts.size()];
		for (int i = 0; i < contacts.size(); i++) {
			nameContacts[i] = contacts.get(i).getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, nameContacts);
		guest.setAdapter(adapter);

		return rootView;
	}
	
	
	private void initComponents() {
		conceptExpense = (EditText) rootView.findViewById(R.id.edtNameExpense);
		quantityExpense = (EditText) rootView.findViewById(R.id.edtAmountExpense);
		Button addExpense = (Button) rootView.findViewById(R.id.btnAddExpense);
		Button cancelExpense = (Button) rootView.findViewById(R.id.btnCandelExpense);
		guest = (ListView) rootView.findViewById(R.id.lvGuest);
		
		// añadimos los listener
		addExpense.setOnClickListener(this);
		cancelExpense.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddExpense:
			// Recuperamos la información y tratamos los espacios
			String concept = conceptExpense.getText().toString().trim();
			String quantity = quantityExpense.getText().toString().trim();
			try {
				concept = URLEncoder.encode(concept, "latin-1");
				quantity = URLEncoder.encode(quantity, "latin-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// comprobamos que el usuario ha introducido un importe
			if (!quantity.equals("")) {

				String url = Constants.ADD_EXPENSE + "?id_event=" + idEvent
						+ "&id_user=" + idUser + "&concept=" + concept
						+ "&quantity=" + quantity;
				Log.i(TAG, url);
			}
			
		case R.id.btnCandelExpense:
			dismiss();
			break;
			
		default:
			Log.e(TAG, "invalid button");
			break;
		}
		
	}

}
