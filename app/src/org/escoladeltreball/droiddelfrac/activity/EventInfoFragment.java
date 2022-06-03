package org.escoladeltreball.droiddelfrac.activity;

import java.util.ArrayList;
import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.adapters.EventInfoAdapter;
import org.escoladeltreball.droiddelfrac.model.pojo.Event;
import org.escoladeltreball.droiddelfrac.model.pojo.Expense;
import org.escoladeltreball.droiddelfrac.model.pojo.Guest;
import org.escoladeltreball.droiddelfrac.model.pojo.ListGuest;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.task.AsyncChangeEvent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class EventInfoFragment extends Fragment{

	private static final String TAG = "EventIngoFragment";
	private static final int CODE_ACTIVITY_RESULT = 1;
	
	/* Componentes */
	private ListView amountList;
	private ImageView photo;
	private TextView description;
	private TextView place;
	private TextView date;
	private TextView textViewBudgetAmount;
	private TextView textNoExp;
	private ImageView imageNoExp;

	/* Variables */
	private View rootView;
	private Event event;
	private List<Expense> listExpense;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_event_info, container, false);

		// Recuperamos los datos que nos mandan desde la activity
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			event = bundle.getParcelable("event");
		}
		
		// Ponemos el nombre del evento como titulo de la activity
		getActivity().setTitle(event.getName());
				
		// recuperamos los componentes
		initComponents();
		
		// Accedemos a los datos cargados en EventActivity
		listExpense = ((EventActivity) getActivity()).getExpenseList();
		
		// Cargamos los datos
		loadData();
		loadDataListView();
				
		// Hablitamos que se pueda cambiar el actionBar en cada fragment
		setHasOptionsMenu(true);

		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		
		// Si el evento está pendiente de pago no mostramos opciónes
		if (event.getStatus() == 2){
			inflater.inflate(R.menu.main, menu);
		} else {
			inflater.inflate(R.menu.menu_event_info, menu);
			
			// Si no eres el administrador del evento, se oculta el icono de cerrar
			if (!((EventActivity) getActivity()).getIdUser().equals(event.getIdAdmin() + "")){
				Log.d(TAG, "No es administrador de este evento");
				MenuItem itemPay = menu.findItem(R.id.actionPayEvent);
				itemPay.setVisible(false);
//				getActivity().invalidateOptionsMenu();
			}
		}
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String url = Constants.URL_CHANGE_EVENT_STATE + "?eventid=" + event.getId() + "&state=";
		AsyncChangeEvent task = new AsyncChangeEvent(getActivity());
		
		switch (item.getItemId()) {
		case R.id.actionNewExpense:
			
			if (((EventActivity) getActivity()).getConfirmet().equals("1")){
				// Recuperamos los invitados al evento
				List<Guest> guestList = getGuest();
				ListGuest listGuest = new ListGuest(String.valueOf(event.getId()), guestList);

				Bundle bundle = new Bundle();
				bundle.putParcelable("guesList", listGuest);

				Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, CODE_ACTIVITY_RESULT);
			} else {
				Toast.makeText(getActivity(), R.string.firstConfirmet,
						Toast.LENGTH_SHORT).show();
			}
			return false;
			
		case R.id.actionPayEvent:
				url += "2";
				
				task.execute(new String[] { url });
			
			return true;
			
		case R.id.actionCloseEvent:
			url += "3";
			
			task.execute(new String[] { url });
		
		return true;

		default:
			Log.e(TAG, "inválid button");
			return super.onOptionsItemSelected(item);
		}

	}
	
	private List<Guest> getGuest() {
		List<Guest> list = new ArrayList<Guest>();
		List<Guest> guestList = ((EventActivity) getActivity()).getGuestList();

		for (Guest guest : guestList) {
			boolean isPhantom = guest.isPhantom();
			int confirmet = guest.getConfirmet();

			if (!isPhantom && confirmet == 1) {
				list.add(guest);
			}
		}

		return list;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CODE_ACTIVITY_RESULT && resultCode == getActivity().RESULT_OK) {
			// Actualizamos datos
			((EventActivity) getActivity()).loadData(1);
			// Accedemos a los datos cargados en EventActivity
			listExpense = ((EventActivity) getActivity()).getExpenseList();
			loadDataListView();
			updateAmount();
		}
		
	}
	
	private void updateAmount(){
		double amount = 0;
		for (Expense expense : listExpense) {
			amount += expense.getQuantity();
		}
		textViewBudgetAmount.setText(String.valueOf(amount));
	}

	private void loadDataListView() {
		EventInfoAdapter adapter = new EventInfoAdapter(getActivity(), listExpense);
		amountList.setAdapter(adapter);
		
		if (listExpense.size()==0) {
			imageNoExp.setVisibility(View.VISIBLE);
			textNoExp.setVisibility(View.VISIBLE);
		} else {
			imageNoExp.setVisibility(View.GONE);
			textNoExp.setVisibility(View.GONE);
		}
	}

	
	private void loadData() {
		byte[] phot = event.getPhoto();
		String desc = event.getDescription();
		String sPlace = event.getPlace();
		String sDate = event.getDateEvent();
		String sAmount = event.getAmount();
		
		textViewBudgetAmount.setText(sAmount + " €");
		
		if (phot != null){
			Bitmap bitmap = BitmapFactory.decodeByteArray(phot, 0, phot.length);
	    	photo.setImageBitmap(bitmap);
		}
		
		if (desc != null){
			description.setText(desc);
		} else {
			description.setVisibility(View.GONE);
		}
		
		if (sPlace != null){
			place.setText(sPlace);
		} else {
			place.setVisibility(View.GONE);
		}
		
		if (sDate != null){
			date.setText(sDate);
		} else {
			date.setVisibility(View.GONE);
		}

	}
	
	private void initComponents() {
		// Referencia views
		photo = (ImageView) rootView.findViewById(R.id.imageInfoEvent);
		description = (TextView) rootView.findViewById(R.id.descriptionInfoEvent);
		place = (TextView) rootView.findViewById(R.id.placeInfoEvent);
		date = (TextView) rootView.findViewById(R.id.dateInfoEvent);
		textViewBudgetAmount = (TextView) rootView.findViewById(R.id.textViewBudgetAmount);
		amountList = (ListView) rootView.findViewById(R.id.amountList);
		
//		View empty = rootView.findViewById(R.id.emptyListView);
//		amountList.setEmptyView(empty);
		
		textNoExp = (TextView) rootView.findViewById(R.id.textNoExp);
		imageNoExp = (ImageView) rootView.findViewById(R.id.imageNoExp);
		
	}

}
