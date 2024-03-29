package org.escoladeltreball.droiddelfrac.activity;

import org.escoladeltreball.droiddelfrac.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class NoContactSelectedDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
		// Set Dialog Icon
				.setIcon(R.drawable.ic_launcher_oval)
				// Set Dialog Title
				.setTitle(R.string.alertTitle)
				// Set Dialog Message
				.setMessage(R.string.noContactSelected)
				// Positive button
				.setPositiveButton(R.string.skip, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						notifyToTarget(Activity.RESULT_OK);
					}
				})

				// Negative Button
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
							}
						}).create();
	}
	
	 private void notifyToTarget(int code) {
	        Fragment targetFragment = getTargetFragment();
	        if (targetFragment != null) {
	            targetFragment.onActivityResult(getTargetRequestCode(), code, null);
	        }
	    }
}
