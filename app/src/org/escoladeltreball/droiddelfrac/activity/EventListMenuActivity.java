package org.escoladeltreball.droiddelfrac.activity;

import java.util.ArrayList;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.navigationdrawer.DrawerItem;
import org.escoladeltreball.droiddelfrac.navigationdrawer.DrawerListAdapter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class EventListMenuActivity extends BaseActivity {

	protected DrawerLayout drawerLayout;
	protected ListView drawerList;
	protected ActionBarDrawerToggle drawerToggle;

	protected CharSequence activityTitle;
	protected CharSequence itemTitle;
	protected String[] tagTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		itemTitle = activityTitle = getTitle();
		tagTitles = getResources().getStringArray(R.array.tagsNavigationDrawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		/*
		 * PENDIENTE
		 */
		// Setear una sombra sobre el contenido principal cuando el drawer se
		// despliegue
		// drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
		// GravityCompat.START);

		// Crear elementos de la lista
		ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();
		items.add(new DrawerItem(true));
		items.add(new DrawerItem(tagTitles[0],
				R.drawable.ic_action_view_as_list));
		items.add(new DrawerItem(tagTitles[1], R.drawable.ic_action_edit));
		items.add(new DrawerItem(tagTitles[2], R.drawable.ic_action_refresh));
		items.add(new DrawerItem(tagTitles[3], R.drawable.ic_action_email));
		items.add(new DrawerItem(tagTitles[4], R.drawable.ic_action_settings));
		items.add(new DrawerItem(tagTitles[5], R.drawable.ic_action_back));

		// Relacionar el adaptador y la escucha de la lista del drawer
		drawerList.setAdapter(new DrawerListAdapter(this, items, idUser,
				nameUser, photo));
		drawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Habilitar el icono de la app por si hay algún estilo que lo
		// deshabilitó
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Crear ActionBarDrawerToggle para la apertura y cierre
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(itemTitle);

				/*
				 * Usa este método si vas a modificar la action bar con cada
				 * fragmento
				 */
				// invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(activityTitle);

				/*
				 * Usa este método si vas a modificar la action bar con cada
				 * fragmento
				 */
				// invalidateOptionsMenu();
			}
		};
		// Seteamos la escucha
		drawerLayout.setDrawerListener(drawerToggle);

		if (savedInstanceState == null) {
			selectItem(1);
		}

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.main, menu);
//		return super.onCreateOptionsMenu(menu);
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			// Toma los eventos de selección del toggle aquí
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/* La escucha del ListView en el Drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// Pasamos datos a los fragments
		Bundle bundle = new Bundle();
		bundle.putString("idUser", idUser);
		Log.i("idUser", idUser);

		FragmentManager fragmentmanager = getSupportFragmentManager();
		FragmentTransaction fragmenttransaction = fragmentmanager
				.beginTransaction();

		switch (position) {
		case 0:
			// Avatar usuario
			break;
		case 1:
			Fragment eventListFragment = new EventListFragment();
			eventListFragment.setArguments(bundle);
			fragmenttransaction.replace(R.id.content_frame, eventListFragment, "eventListFragment");
			break;
		case 2:
			 Fragment editProfileFragment = new EditProfileFragment();
			 fragmenttransaction
			 .replace(R.id.content_frame, editProfileFragment);
			break;
		case 3:
			Fragment syncContactsFragment = new SyncContactsFragment();
			fragmenttransaction.replace(R.id.content_frame,
					syncContactsFragment);
			break;
		case 4:
			// Fragment EventListFragment = new EventListFragment();
			// fragmenttransaction.replace(R.id.content_frame,
			// EventListFragment);
			break;
		case 5:

			break;
		case 6:
			// Pone la preferencia keepLogged en false.
			saveKeepLogged(false);
			// Sale de la aplicación.
			finish();
			break;
		default:
			break;
		}

		fragmenttransaction.addToBackStack(null);
		fragmenttransaction.commit();

		if (position != 0) {
			// Se actualiza el item seleccionado y el título, después de cerrar
			// el drawer
			drawerList.setItemChecked(position, true);
			setTitle(tagTitles[position - 1]);
			drawerLayout.closeDrawer(drawerList);
		}
	}

	/* Método auxiliar para setear el titulo de la action bar */
	@Override
	public void setTitle(CharSequence title) {
		itemTitle = title;
		getActionBar().setTitle(itemTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sincronizar el estado del drawer
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Cambiar las configuraciones del drawer si hubo modificaciones
		drawerToggle.onConfigurationChanged(newConfig);
	}

}
