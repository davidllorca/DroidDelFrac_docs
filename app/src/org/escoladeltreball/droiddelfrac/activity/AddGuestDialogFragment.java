package org.escoladeltreball.droiddelfrac.activity;

import java.util.ArrayList;
import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.adapters.AddGuestsDialogAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Dialog que permite al usuario añadir un nuevo participante.
 * 
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 * 
 * @see http://android-developers.blogspot.com.es/2012/05/using-dialogfragments.html
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AddGuestDialogFragment extends DialogFragment implements
		OnClickListener {

	/* Views */
	private EditText nameEditText;
	private ImageButton addGuestImageButton;
	private ListView phantomGuestListView;
	private Button addButton;
	private Button cancelButton;

	/* Variables */
	// private List<Guest> phantomGuestList;
	private List<Object> phantomGuestList;
	private AddGuestsDialogAdapter adapter;

	public AddGuestDialogFragment() {
		this.phantomGuestList = new ArrayList<>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.dialog_add_guest, container);

		// Título del diálogo
		getDialog().setTitle(R.string.addGuestTitle);

		// Referencia componentes
		nameEditText = (EditText) view.findViewById(R.id.editTextGuestName);
		addGuestImageButton = (ImageButton) view
				.findViewById(R.id.imageButtonAddGuestToList);
		addGuestImageButton.setOnClickListener(this);

		addButton = (Button) view.findViewById(R.id.buttonAddPhantomGuest);
		cancelButton = (Button) view
				.findViewById(R.id.buttonCancelAddPhantomGuest);
		addButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		phantomGuestListView = (ListView) view
				.findViewById(R.id.listViewNewGuests);
		adapter = new AddGuestsDialogAdapter(getActivity(), phantomGuestList);
		phantomGuestListView.setAdapter(adapter);

		return view;
	}

	private void addGuestToList() {
		if (!nameEditText.getText().toString().trim().equals("")) {
			// phantomGuestList.add(new Guest(nameEditText.getText().toString(),
			// true));
			phantomGuestList.add(nameEditText.getText().toString());
			nameEditText.setText(null);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButtonAddGuestToList:
			addGuestToList();
			break;
		case R.id.buttonAddPhantomGuest:
			Intent intent = new Intent();
			// Crea un array con los nombres de los usuarios 'virtuales' a añadir al evento.
			String[] phantomGuestsArray = new String[phantomGuestList.size()];
			for (int i = 0; i < phantomGuestsArray.length; i++) {
				phantomGuestsArray[i] = phantomGuestList.get(i).toString();
			}
			intent.putExtra("phantomGuests", phantomGuestsArray);
			// Notificamos en el Fragment principal
			getTargetFragment().onActivityResult(getTargetRequestCode(),
					Activity.RESULT_OK, intent);
			dismiss();
			break;
		case R.id.buttonCancelAddPhantomGuest:
			getTargetFragment().onActivityResult(getTargetRequestCode(),
					Activity.RESULT_CANCELED, null);
			dismiss();
			break;
		default:
			break;
		}
	}
}