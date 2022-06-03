package org.escoladeltreball.droiddelfrac.util.task;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.escoladeltreball.droiddelfrac.model.pojo.Event;
import org.escoladeltreball.droiddelfrac.util.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class LaunchIntentTask extends AsyncTask<Intent, Void, String> {
	
	private Activity activity;
	private ProgressDialog splash;
	
	public LaunchIntentTask(Activity activity) {
		this.activity = activity;
		splash = new ProgressDialog(activity);
	}

	@Override
	/**
	 * Check for internet connection before start  new activity
	 */
	protected void onPreExecute() {
		splash.setMessage(activity.getResources().getText(R.string.loadData));
		splash.show();
	}

	/**
	 * Launch Activity
	 */
	@Override
	protected String doInBackground(Intent... intent) {
		Intent i = intent[0];
		Request req = new Request();
		Event event = i.getExtras().getParcelable("event");
		String url = Constants.URL_EVENT_USERS + "?eventid=" + event.getId();
		String result = req.GET(url);
		i.putExtra("result", result);
		activity.startActivity(i);
		return null;
	}

	/**
	 * Show progress bar
	 */
	@Override
	protected void onPostExecute(String result) {
		if (splash.isShowing()) {
			splash.dismiss();
		}
	}
}
