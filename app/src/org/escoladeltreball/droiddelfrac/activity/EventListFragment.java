package org.escoladeltreball.droiddelfrac.activity;

import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.adapters.CustomExpandableListAdapter;
import org.escoladeltreball.droiddelfrac.model.pojo.Event;
import org.escoladeltreball.droiddelfrac.model.pojo.Group;
import org.escoladeltreball.droiddelfrac.model.pojo.ListEvents;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.Util;
import org.escoladeltreball.droiddelfrac.util.task.AsyncUserEvents;
import org.escoladeltreball.droiddelfrac.util.task.LaunchIntentTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Fragment que muestra tres listas expandibles con tres tipos de estado de 
 * eventos
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 *
 */
public class EventListFragment extends Fragment implements OnChildClickListener {

	private static final String TAG = "EventListFragment";
	
	/* Views */
	private View rootView;
	private ExpandableListView lvUpcoming;
	private ExpandableListView lvClosed;
	private ExpandableListView lvPending;
	private TextView textViewUpcomingEvents;
	private TextView textViewClosedEvents;
	private TextView textViewPendingEvents;
	private ImageView imageViewUpcomingEvents;
	private ImageView imageViewClosedEvents;
	private ImageView imageViewPendingEvents;
	private ImageView imageNoEvents;
	private TextView textNoEvents;
	
	/* Variables para mapear los objetos */
	private SparseArray<Group> groupsUpcoming;
	private SparseArray<Group> groupsClosed;
	private SparseArray<Group> groupsPending;
	
	/* Variables */
	private String idUser;

	public EventListFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_eventlist, container,
				false);
		
		// Recuperamos los componentes
		initComponents();

		// Recuperamos los datos que nos mandan desde la activity
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			idUser = bundle.getString("idUser", "-1");
			Log.i(TAG, "iduser: " + idUser);
		}
		
		callServer();
		
		// Hablitamos que se pueda cambiar el actionBar en cada fragment
		setHasOptionsMenu(true);
		
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		callServer();
	}
	
	public void callServer() {
		Log.i("Eventlist", "onstart");

		// Realizamos la petición al servidor
		String url = Constants.URL_USER_EVENTS + "?userid=" + idUser;
		AsyncUserEvents task = new AsyncUserEvents(getActivity());
		task.execute(new String[] { url });
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i("Eventlist", "onresume");
	}
	
	public void workData(ListEvents events){
		if (events.getListEvents().size()==0 || events==null) {
			imageNoEvents.setVisibility(View.VISIBLE);
			textNoEvents.setVisibility(View.VISIBLE);
		} else {
			imageNoEvents.setVisibility(View.GONE);
			textNoEvents.setVisibility(View.GONE);
		}

		// Separamos los eventos por su estado para mostrarlo en una u otra lista
		if (events!=null) {
			organizeEvents(events);
		}
				
		// Cargamos los datos u ocultamos las vistas si no serán usadas
		if (groupsUpcoming.size() != 0){
			loadData(lvUpcoming, groupsUpcoming);
			textViewUpcomingEvents.setVisibility(View.VISIBLE);
			imageViewUpcomingEvents.setVisibility(View.VISIBLE);
			lvUpcoming.setVisibility(View.VISIBLE);
		} else {
			textViewUpcomingEvents.setVisibility(View.GONE);
			imageViewUpcomingEvents.setVisibility(View.GONE);
			lvUpcoming.setVisibility(View.GONE);
		}

		if (groupsClosed.size() != 0){
			loadData(lvClosed, groupsClosed);
			textViewClosedEvents.setVisibility(View.VISIBLE);
			imageViewClosedEvents.setVisibility(View.VISIBLE);
			lvClosed.setVisibility(View.VISIBLE);
		} else {
			textViewClosedEvents.setVisibility(View.GONE);
			imageViewClosedEvents.setVisibility(View.GONE);
			lvClosed.setVisibility(View.GONE);
		}

		if (groupsPending.size() != 0){
			loadData(lvPending, groupsPending);
			textViewPendingEvents.setVisibility(View.VISIBLE);
			imageViewPendingEvents.setVisibility(View.VISIBLE);
			lvPending.setVisibility(View.VISIBLE);
		} else {
			textViewPendingEvents.setVisibility(View.GONE);
			imageViewPendingEvents.setVisibility(View.GONE);
			lvPending.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_event_exp_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.actionNewevent:
			launchCreateEventActivity();
			return false;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Event event = null;
		switch (parent.getId()) {
		case R.id.expandableListViewUpcomingEvents:
			event = groupsUpcoming.get(groupPosition).getChildren().get(childPosition);
			break;

		case R.id.expandableListViewClosedEvents:
			event = groupsClosed.get(groupPosition).getChildren().get(childPosition);
			break;

		case R.id.expandableListViewPendingEvents:
			event = groupsPending.get(groupPosition).getChildren().get(childPosition);
			break;

		default:
			Log.e(TAG, "invalid button");
			break;
		}
		
		launchEventActivity(event);
		return true;
	}

	/**
	 * Lanza la Activity CreateEventActivity. Crear un evento nuevo.
	 */
	private void launchCreateEventActivity() {
		Intent intent = new Intent(getActivity(), CreateEventActivity.class);
		startActivity(intent);
	}

	/**
	 * Lanza la Activity EventActivity. Info de un evento en concreto.
	 * @param event 
	 */
	private void launchEventActivity(Event event) {
		Intent intent = new Intent(getActivity(), EventActivity.class);
		Bundle mBundle = new Bundle();  
        mBundle.putParcelable("event", event);
        mBundle.putString("idUser", idUser);
		intent.putExtras(mBundle);
//		startActivity(intent);		
		new LaunchIntentTask(getActivity()).execute(intent);
	}
	
	/**
	 * Método que carga los datos mediante un adaptador a las expandable list
	 */
	private void loadData(final ExpandableListView expandableList, SparseArray<Group> groups) {
		// Cargamos los datos en las ExpandableListView
		CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(getActivity(), groups);
		expandableList.setAdapter(adapter);
		
		// Ajustamos la altura de la pantalla cada vez que se expande un item,
		// de esta manera solventamos probelamas con el scroll y las vistas
		Util.setListViewHeight(expandableList);
		expandableList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				Util.setListViewHeight(parent, groupPosition);
				return false;
			}
		});
		
		expandableList.setOnGroupExpandListener(new OnGroupExpandListener() {
			private int previousGroup = -1;
			@Override
			public void onGroupExpand(int groupPosition) {
				if(groupPosition != previousGroup)
					expandableList.collapseGroup(previousGroup);
	            previousGroup = groupPosition;
			}
		});
		
	}

	/**
	 * Referencia los componentes y añade los listeners
	 */
	private void initComponents() {

		// Referencia views
		textViewUpcomingEvents = (TextView) rootView.findViewById(R.id.textViewUpcomingEvents);
		textViewClosedEvents = (TextView) rootView.findViewById(R.id.textViewClosedEvents);
		textViewPendingEvents = (TextView) rootView.findViewById(R.id.textViewPendingEvents);
		
		imageViewUpcomingEvents = (ImageView) rootView.findViewById(R.id.imageViewUpcomingEvents);
		imageViewClosedEvents = (ImageView) rootView.findViewById(R.id.imageViewClosedEvents);
		imageViewPendingEvents = (ImageView) rootView.findViewById(R.id.imageViewPendingEvents);
		
		lvUpcoming = (ExpandableListView) rootView.findViewById(R.id.expandableListViewUpcomingEvents);
		lvClosed = (ExpandableListView) rootView.findViewById(R.id.expandableListViewClosedEvents);
		lvPending = (ExpandableListView) rootView.findViewById(R.id.expandableListViewPendingEvents);
		
		imageNoEvents = (ImageView) rootView.findViewById(R.id.imageNoEvents);
		textNoEvents = (TextView) rootView.findViewById(R.id.textNoEvents);
		
		// Añade listeners
		lvUpcoming.setOnChildClickListener(this);
		lvClosed.setOnChildClickListener(this);
		lvPending.setOnChildClickListener(this);
	}
	
	/**
	 * Organiza los eventos por status del evento o status del usuario
	 * 
	 * @param listEvents
	 */
	private void organizeEvents(ListEvents listEvents) {
		groupsClosed = new SparseArray<Group>();
		groupsUpcoming = new SparseArray<Group>();
		groupsPending = new SparseArray<Group>();
		int i = 0, j = 0, k = 0;
		
		List<Event> events = listEvents.getListEvents();
		for (Event event : events) {
			Group group = new Group();
			if (event.getStatus() == 2) {
				group.setName(event.getName());
				group.addChildren(event);
				groupsClosed.append(i, group);
				i++;
			} else if (event.getUserStatus() == 1) {
				group.setName(event.getName());
				group.addChildren(event);
				groupsUpcoming.append(j, group);
				j++;
			} else {
				group.setName(event.getName());
				group.addChildren(event);
				groupsPending.append(k, group);
				k++;
			}
		}
	}

}
