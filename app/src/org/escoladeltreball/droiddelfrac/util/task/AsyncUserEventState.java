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
 * Realiza una llamada al servidor para realizar un cambio en el estado de un
 * usuario en un evento determinado.
 *
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncUserEventState extends AsyncTask<String, Void, Boolean> {

	private static final String TAG = "AsyncUserEventState";
	private Context context;

	public AsyncUserEventState(Context context) {
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String[] url) {
		Log.i(TAG, "doinbackground");
		// Envía la solicitud al servidor
		Request req = new Request();

		String result = req.GET(url[0]);
		Log.i(TAG, result);

		return handleResult(result);
	}

	private Boolean handleResult(String result) {
		boolean changeStateDone = false;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				if (code.equals("200")) {
					// En caso de resultado satisfactorio, el servlet devuelve
					changeStateDone = true;
				} else {
					showToast(context.getString(R.string.badRequest));
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				showToast(context.getString(R.string.badRequest));
			}
		} else {
			showToast(context.getString(R.string.badRequest));

		}
		return changeStateDone;
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
