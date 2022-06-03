package org.escoladeltreball.droiddelfrac.util.task;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.activity.EventListFragment;
import org.escoladeltreball.droiddelfrac.activity.EventListMenuActivity;
import org.escoladeltreball.droiddelfrac.model.GetData;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.escoladeltreball.droiddelfrac.model.pojo.ListEvents;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Realiza una llamada al servidor para solicitar los eventos en los que participa el usuario.
 * Devuelve una ListEvent que contiene uns List con todos los eventos.
 * 
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncUserEvents extends
		AsyncTask<String, Void, ListEvents> {

	private static final String TAG = "AsyncUsersEvent";
	private Activity activity;
	private ProgressDialog splash;

	public AsyncUserEvents(Activity activity) {
		this.activity = activity;
		splash = new ProgressDialog(activity);
	}
	
	@Override
	protected void onPreExecute() {
		splash.setMessage(activity.getResources().getText(R.string.loadData));
		splash.show();
	}
	
	@Override
	protected void onPostExecute(ListEvents result) {
		FragmentManager fm = ((EventListMenuActivity)activity).getSupportFragmentManager();
		EventListFragment fragment = (EventListFragment) fm.findFragmentByTag("eventListFragment");
		fragment.workData(result);

		if (splash.isShowing()) {
			splash.dismiss();
			Log.i(TAG, "oculta splash screen");
		}
	}

	@Override
	protected ListEvents doInBackground(String[] url) {
		// Envía la solicitud de login al servidor
		Request req = new Request();

		String result = req.GET(url[0]);

		return handleResult(result);
	}

	private ListEvents handleResult(String result) {
		ListEvents eventList = null;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				// Log BORRAR
				Log.i(TAG, "code: " + code);
				if (code.equals("200")) {

					String listString = GetData.getStringFromJSON("data", "event", result);
					eventList = new Gson().fromJson(listString, ListEvents.class);

				} else {
					// Toast info se ha producido otro error
					showToast(activity.getString(R.string.badRequest));
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				showToast(activity.getString(R.string.badRequest));
			}
		} else {
			showToast(activity.getString(R.string.badRequest));
		}
		return eventList;

	}

	/**
	 * Muestra un mensaje Toast en la UI que ha llamado al Asynctask
	 * 
	 * @param errorMessage
	 */
	private void showToast(final String infomessage) {
		Handler handler = new Handler(activity.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(activity, infomessage, Toast.LENGTH_LONG).show();
			}
		});
	}
}
