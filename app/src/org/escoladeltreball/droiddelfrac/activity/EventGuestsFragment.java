package org.escoladeltreball.droiddelfrac.activity;

import java.util.concurrent.ExecutionException;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.adapters.EventGuestAdapter;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.task.AsyncUserEventState;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class EventGuestsFragment extends Fragment implements
		OnItemLongClickListener, OnClickListener {

	private static final String TAG = "EventGuestFragment";

	private static final int REQ_CODE_ADD_GUEST = 4;
	private static final int REQ_CODE_EXIT_EVENT = 5;

	/* Views */
	private View rootView;
	private ListView listViewContacts;
	private Button discardParticipateButton;
	private Button pendingConfirmationParticipateButton;
	private Button confirmParticipateButton;

	/* Variable */
	// private List<Guest> listGuests;
	private int positionUserListView;
	private EventGuestAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("EventGuestFragment", "onCreateView");

		rootView = inflater.inflate(R.layout.fragment_event_guests, container,
				false);

		initComponents();

		// Hablitamos que se pueda cambiar el actionBar en cada fragment
		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onClick(View v) {

		String idUser = ((EventActivity) getActivity()).getIdUser();
		String idEvent = ((EventActivity) getActivity()).getIdEvent();
		String url = Constants.URL_USER_EVENT_STATE + "?userid=" + idUser
				+ "&eventid=" + idEvent + "&state=";
		
		Log.i("url state", url);
		AsyncUserEventState task = new AsyncUserEventState(getActivity());
		
		String state = null;
		boolean result = false;
		
		switch (v.getId()) {
		case R.id.buttonConfirmParticipation:
			// confirmParticipateButton.setBackgroundColor(getResources()
			// .getColor(R.color.green_light));
			// pendingConfirmationParticipateButton
			// .setBackgroundColor(getResources().getColor(
			// R.color.no_selected_button_background));
			// discardParticipateButton.setBackgroundColor(getResources()
			// .getColor(R.color.no_selected_button_background));
			state = "1";
			task.execute(url + state);
			
			try {
				result = task.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// url = url + "&state=" + 1;
			// Log.i("user_state", "1");
			break;
		case R.id.buttonPendingConfirmParticipation:
			// pendingConfirmationParticipateButton
			// .setBackgroundColor(getResources().getColor(
			// R.color.green_light));
			// confirmParticipateButton.setBackgroundColor(getResources()
			// .getColor(R.color.no_selected_button_background));
			// discardParticipateButton.setBackgroundColor(getResources()
			// .getColor(R.color.no_selected_button_background));

			// url = url + "&state=" + 0;
			// Log.i("user_state", "0");
			state = "0";
			task.execute(url + state);
			
			try {
				result = task.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.buttonDiscardParticipation:
			// discardParticipateButton.setBackgroundColor(getResources()
			// .getColor(R.color.green_light));
			// confirmParticipateButton.setBackgroundColor(getResources()
			// .getColor(R.color.no_selected_button_background));
			// pendingConfirmationParticipateButton
			// .setBackgroundColor(getResources().getColor(
			// R.color.no_selected_button_background));
			showExitEventDialog();
//			state = "2";
			// url = url + "&state=" + 2;
			break;
		default:
			break;
		}

		Log.i("result", result + "");
		if (result) {
			((EventActivity) getActivity()).setConfirmet(state);
			((EventActivity) getActivity()).loadData();
			notifyChangues();
		}
	}

	/*
	 * PENDIENTE
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// Toast.makeText(getActivity(), "position: " + position,
		// Toast.LENGTH_SHORT).show();
		// listViewContacts.getChildAt(position).setSelected(true);
		// listViewContacts.getChildAt(position).setBackgroundColor(
		// getResources().getColor(R.color.green_light));
		//
		// // @see
		// //
		// http://hmkcode.com/android-menu-handling-click-events-changing-menu-items-at-runtime/
		return false;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Si el evento está pendiente de pago no mostramos opciónes
		String status = ((EventActivity) getActivity()).getStatus();
		if (status.equals("2")) {
			inflater.inflate(R.menu.main, menu);
		} else {
			inflater.inflate(R.menu.menu_event_guest_fragment, menu);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.actionAddGuest:
			Intent intent = new Intent(getActivity(),
					InviteGuestsActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("idEvent",
					((EventActivity) getActivity()).getIdEvent());
			startActivityForResult(intent, REQ_CODE_ADD_GUEST);
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_ADD_GUEST:
			if (resultCode == Activity.RESULT_OK) {
				((EventActivity) getActivity()).loadData();
				notifyChangues();
			}
			break;
		case REQ_CODE_EXIT_EVENT:
			if (resultCode == Activity.RESULT_OK) {
				String idUser = ((EventActivity) getActivity()).getIdUser();
				String idEvent = ((EventActivity) getActivity()).getIdEvent();
				String url = Constants.URL_USER_EVENT_STATE + "?userid=" + idUser
						+ "&eventid=" + idEvent + "&state=";
				
				Log.i("url state", url);
				AsyncUserEventState task = new AsyncUserEventState(getActivity());
				String state = "2";
				task.execute(url + state);
				boolean result = false;
				try {
					result = task.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(result){
					getActivity().finish();
				}
			}
			break;
		default:
			Log.i("onActivityResult", "default");
			break;
		}
	}
	
	/**
	 * Muestra Dialog informando que no existen contactos. Desde el qual se
	 * puede realizar la acción de sincronizar usuarios con los contactos de la
	 * agenda del dispositivo.
	 */
	private void showExitEventDialog() {
		ExitEventDialogFragment dialog = new ExitEventDialogFragment();
		dialog.setTargetFragment(this, REQ_CODE_EXIT_EVENT);
		dialog.show(getFragmentManager(), "ExitEventDialogFragment");
	}
	
	/**
	 * Referencia los componentes y añade los listeners
	 */
	private void initComponents() {

		Log.i("status event", ((EventActivity) getActivity()).getStatus());

		if (!((EventActivity) getActivity()).getStatus().equals("2")) {
			// Cargamos los componentes

			discardParticipateButton = (Button) rootView
					.findViewById(R.id.buttonDiscardParticipation);
			pendingConfirmationParticipateButton = (Button) rootView
					.findViewById(R.id.buttonPendingConfirmParticipation);
			confirmParticipateButton = (Button) rootView
					.findViewById(R.id.buttonConfirmParticipation);
			discardParticipateButton.setOnClickListener(this);
			pendingConfirmationParticipateButton.setOnClickListener(this);
			confirmParticipateButton.setOnClickListener(this);

			// Cambia del fondo del botón correspondiente según el cofirmet
			String confirmet = ((EventActivity) getActivity()).getConfirmet();
			Log.i("initComponents", "confirmet: " + confirmet);
			switch (confirmet) {
			case "0":
				pendingConfirmationParticipateButton
						.setBackgroundColor(getResources().getColor(
								R.color.green_light));
				confirmParticipateButton.setBackgroundColor(getResources()
						.getColor(R.color.no_selected_button_background));
				discardParticipateButton.setBackgroundColor(getResources()
						.getColor(R.color.no_selected_button_background));
				break;
			case "1":
				confirmParticipateButton.setBackgroundColor(getResources()
						.getColor(R.color.green_light));
				pendingConfirmationParticipateButton
						.setBackgroundColor(getResources().getColor(
								R.color.no_selected_button_background));
				discardParticipateButton.setBackgroundColor(getResources()
						.getColor(R.color.no_selected_button_background));

				break;
			case "2":
				discardParticipateButton.setBackgroundColor(getResources()
						.getColor(R.color.green_light));
				confirmParticipateButton.setBackgroundColor(getResources()
						.getColor(R.color.no_selected_button_background));
				pendingConfirmationParticipateButton
						.setBackgroundColor(getResources().getColor(
								R.color.no_selected_button_background));

				break;
			default:
				break;
			}
			// Llamada a la variable de EventActivity y conversión a objetos
			// Guest
			// listGuests = getGuests(((EventActivity)
			// getActivity()).getUserList());
		} else {
			LinearLayout confirmPanel = (LinearLayout) rootView
					.findViewById(R.id.linearLayoutConfirmParticipation);
			confirmPanel.setVisibility(View.GONE);
		}

		listViewContacts = (ListView) rootView
				.findViewById(R.id.listViewContacts);
		// Añadimos los datos a la listView
		adapter = new EventGuestAdapter(getActivity(),
				((EventActivity) getActivity()).getGuestList());
		Log.i("GUEST ListView", ((EventActivity) getActivity()).getGuestList().toString());
		listViewContacts.setAdapter(adapter);

		// Añade listener
		listViewContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listViewContacts.setOnItemLongClickListener(this);
	}

	public void notifyChangues() {
		Log.i("EventGuests", "notify changes");
		// adapter.notifyDataSetChanged();
		initComponents();
	}

	public EventGuestAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}
