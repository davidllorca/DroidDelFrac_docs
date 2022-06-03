package org.escoladeltreball.droiddelfrac.util.task;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.GetData;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.escoladeltreball.droiddelfrac.model.pojo.ListBills;
import org.escoladeltreball.droiddelfrac.model.pojo.ListEvExpenses;
import org.escoladeltreball.droiddelfrac.model.pojo.ListUsers;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Realiza una llamada al servidor para solicitar las cuentas y movimientos
 * entre invitados a un evento determinado. Devuelve un objeto ListBills con
 * todos los movimientos para saldar cuentas del evento.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 * 
 *         This is free software, licensed under the GNU General Public License
 *         v3. See http://www.gnu.org/licenses/gpl.html for more information.
 */

public class AsyncListBills extends AsyncTask<String, Void, ListBills> {

	private static final String TAG = "AsyncListBills";
	private Context context;

	public AsyncListBills(FragmentActivity context) {
		this.context = context;
	}

	@Override
	protected ListBills doInBackground(String[] url) {
		// Envía la solicitud al servidor
		Request req = new Request();

		String result = req.GET(url[0]);

		return handleResult(result);
	}

	private ListBills handleResult(String result) {
		ListBills billList = null;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				// Log BORRAR
				Log.i(TAG, "code: " + code);
				if (code.equals("200")) {

					String listString = GetData.getStringFromJSON("data",
							"bill", result);
					Log.i("RESULT Bills", listString);
					billList = new Gson().fromJson(listString,
							ListBills.class);

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
		return billList;

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
