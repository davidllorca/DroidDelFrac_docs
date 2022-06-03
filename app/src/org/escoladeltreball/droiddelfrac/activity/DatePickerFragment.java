package org.escoladeltreball.droiddelfrac.activity;

import java.util.Calendar;

import org.escoladeltreball.droiddelfrac.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Configura la fecha actual por defecto en el picker.
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		EditText dateEditText = (EditText) getActivity().findViewById(
				R.id.editTextEventDate);
		
		// Crontol de formato dd-mm-yyyy
		String d = (day < 10) ? "0" + day : day + "";
		month += 1;
		String m = (month < 10) ? "0" + month : month + "";
		
		dateEditText.setText(d + "-" + m + "-" + year);		
	}
}
