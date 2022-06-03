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
 * Añade un gasto a un evento. Devuelve true en caso que se haya añadido el
 * gasto unitario con éxito o false en caso contrario.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncAddExpense extends AsyncTask<String, Void, Boolean> {

	private static final String TAG = "AsyncAddExpense";
	private Context context;

	public AsyncAddExpense(Context context) {
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String[] url) {
		// Envía la solicitud de login al servidor
		Request req = new Request();

		String result = req.GET(url[0]);

		return handleResult(result);
	}

	private Boolean handleResult(String result) {
		boolean expenseAdded = false;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				// Log BORRAR
				Log.i(TAG, "code: " + code);
				if (code.equals("200")) {
					expenseAdded = true;
					showToast(context.getString(R.string.addedExpense));

				} else {
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
		return expenseAdded;

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
