package org.escoladeltreball.droiddelfrac.adapters;


import org.escoladeltreball.droiddelfrac.activity.EventBillsFragment;
import org.escoladeltreball.droiddelfrac.activity.EventGuestsFragment;
import org.escoladeltreball.droiddelfrac.activity.EventInfoFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

	private Bundle bundle;
	
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public TabsPagerAdapter(FragmentManager fm,	Bundle bundle) {
		super(fm);
		this.bundle = bundle;
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// EventInfoFragment activity
			EventInfoFragment details = new EventInfoFragment();
			details.setArguments(bundle);
			return details;
		case 1:
			// EventGuestsFragment activity
			return new EventGuestsFragment();
		case 2:
			// EventChatFragment activity
			return new EventBillsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
