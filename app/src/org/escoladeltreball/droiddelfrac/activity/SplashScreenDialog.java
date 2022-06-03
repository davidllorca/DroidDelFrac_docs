package org.escoladeltreball.droiddelfrac.activity;

import org.escoladeltreball.droiddelfrac.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class SplashScreenDialog extends Dialog{

	public SplashScreenDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
	}

}
