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
public class InvitationsSentDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
		// Set Dialog Icon
				.setIcon(R.drawable.ic_launcher_oval)
				// Set Dialog Title
				.setTitle(R.string.app_name)
				// Set Dialog Message
				.setMessage(R.string.invitationsSent)
				// Positive button
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						notifyToTarget(Activity.RESULT_OK);
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
