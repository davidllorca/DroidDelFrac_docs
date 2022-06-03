package org.escoladeltreball.droiddelfrac.activity;

import org.escoladeltreball.droiddelfrac.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class InviteGuestsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_invite_guests);

//		// Recogemos datos del intent, idEvent PENDIENTE
		String idEvent = (String) getIntent().getExtras().get("idEvent");
		Bundle bundle = new Bundle();
		bundle.putString("idEvent", idEvent);
		
		FragmentManager fragmentmanager = getSupportFragmentManager();
		FragmentTransaction fragmenttransaction = fragmentmanager
				.beginTransaction();

		Fragment createEventInviteGuestFragment = new CreateEventInviteGuestFragment(bundle);
		fragmenttransaction.replace(R.id.invite_guests_content_frame,
				createEventInviteGuestFragment);
		fragmenttransaction.commit();

	}
		
}