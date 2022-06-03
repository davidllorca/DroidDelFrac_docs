package org.escoladeltreball.droiddelfrac.util.task;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Realiza una llamada al servidor para solicitar los datos de un evento
 * determinado. Devuelve un objeto ExpenseManager y otro ListUsers con todos los gastos unitarios
 * del evento y los participantes de este respectivamente.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */

public class AsyncPayEvent extends AsyncTask<String, Void, Void> {

	private static final String TAG = "AsyncDataEvent";
	private Context context;

	public AsyncPayEvent(Context context) {
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(String[] url) {
		// Envía la solicitud al servidor
		Request req = new Request();

		String result = req.GET(url[0]);

		handleResult(result);
		return null;
	}

	private void handleResult(String result) {

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				// Log BORRAR
				Log.i(TAG, "code: " + code);
				if (!code.equals("200")) {
					// Toast info se ha producido otro error
					showToast(context.getString(R.string.badRequest));
				} 
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				showToast(context.getString(R.string.badRequest));
			}
		} else {
			showToast(context.getString(R.string.badRequest));
		}

	}

	/**
	 * Muestra un mensaje Toast en la UI que ha llamado al Asynctask
	 * 
	 * @param errorMessage
	 */
	private void showToast(final String infomessage) {
		Handler handler = new Handler(context.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(context, infomessage, Toast.LENGTH_LONG).show();
			}
		});
	}
}
