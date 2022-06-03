package org.escoladeltreball.droiddelfrac.activity;

import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.adapters.EventBillsAdapter;
import org.escoladeltreball.droiddelfrac.model.pojo.BillItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class EventBillsFragment extends Fragment {

	private static final String TAG = "EventBillsFragment";

	/* Views */
	private View rootView;
	private Button button;
	private ListView billListView;
	private ImageView imageNoBills;
	private TextView textNoBills;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_event_bills, container,
				false);

		initComponents();

		return rootView;
	}

	/**
	 * Referencia los componentes y añade los listeners
	 */
	private void initComponents() {
		// Cargamos las facturas en la List de EventActivity
		((EventActivity) getActivity()).loadData();
		
		billListView = (ListView) rootView.findViewById(R.id.listViewBills);
		imageNoBills = (ImageView) rootView.findViewById(R.id.imageNoBills);
		textNoBills = (TextView) rootView.findViewById(R.id.textNoBills);
		
		List<BillItem> billItemsList = ((EventActivity) getActivity()).getBillItemsList();

		if (((EventActivity) getActivity()).getStatus().equals("2") && billItemsList.size() > 0) {
			EventBillsAdapter adapter = new EventBillsAdapter(getActivity(),
					billItemsList,
					((EventActivity) getActivity()).getIdUser());
			billListView.setAdapter(adapter);
		} else if (((EventActivity) getActivity()).getStatus().equals("1")) {
			imageNoBills.setVisibility(View.VISIBLE);
			textNoBills.setVisibility(View.VISIBLE);
		} 
		else {
			imageNoBills.setVisibility(View.VISIBLE);
			imageNoBills.setImageResource(R.drawable.splash);
			textNoBills.setVisibility(View.VISIBLE);
			textNoBills.setText("No tienes ninguna cuenta pendiente");
		}
	}

}
