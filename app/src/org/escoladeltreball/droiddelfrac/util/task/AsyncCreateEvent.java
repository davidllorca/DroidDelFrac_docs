package org.escoladeltreball.droiddelfrac.util.task;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.GetData;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Si el evento se crea satisfactoriamente devuelve el idEvento, en caso
 * contrario devuelve un -1.
 *
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncCreateEvent extends AsyncTask<String, Void, String> {

	private static final String TAG = "AsyncCreateEvent";
	private Context context;

	public AsyncCreateEvent(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String[] url) {
		Log.i(TAG, "doinbackground");
		// Envía la solicitud al servidor
		Request req = new Request();

		String result = req.GET(url[0]);
		Log.i(TAG, result);

		return handleResult(result);
	}

	private String handleResult(String result) {
		String idEvent = "-1";
		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				if (code.equals("200")) {
					// En caso de resultado satisfactorio, el servlet devuelve
					// la id del nuevo usuario.
					idEvent = GetData.getStringFromJSON("data", "id",
							result);
					Log.i(TAG, "idEvent: " + idEvent);
				} else if (code.equals("400")) {
					Log.e(TAG, "code: " + code);
					showToast(context.getString(R.string.badRequest));
				} else {
					Log.e(TAG, "code: " + code);
					showToast(context.getString(R.string.badRequest));
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
			}
		} else {
			showToast(context.getString(R.string.badRequest));
		}

		return idEvent;
	}

	/**
	 * Muestra un mensaje Toast en la UI que ha llamado al Asynctask
	 * 
	 * @param errorMessage
	 */
	private void showToast(final String infoMessage) {
		Handler handler = new Handler(context.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(context, infoMessage, Toast.LENGTH_LONG).show();
			}
		});
	}

}
