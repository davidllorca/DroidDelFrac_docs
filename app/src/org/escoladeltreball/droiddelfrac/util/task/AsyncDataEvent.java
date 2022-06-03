package org.escoladeltreball.droiddelfrac.util.task;

import java.util.ArrayList;
import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.GetData;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.escoladeltreball.droiddelfrac.model.pojo.ListEvExpenses;
import org.escoladeltreball.droiddelfrac.model.pojo.ListUsers;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

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

public class AsyncDataEvent extends AsyncTask<String, Void, List<Object>> {

	private static final String TAG = "AsyncDataEvent";
	private Activity activity;

	public AsyncDataEvent(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	protected List<Object> doInBackground(String[] url) {
		// Envía la solicitud al servidor
		Request req = new Request();
		
		String result = req.GET(url[0]);

		return handleResult(result);
	}

	private List<Object> handleResult(String result) {
		List<Object> dataEvent= null;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				// Log BORRAR
				Log.i(TAG, "code: " + code);
				if (code.equals("200")) {
					
					String expensesString = GetData.getStringFromJSON("data", "expense", result);
					String usersString = GetData.getStringFromJSON("data", "user", result);
					Log.i("RESULT EventUser", expensesString);
					ListEvExpenses expensesList = new Gson().fromJson(expensesString, ListEvExpenses.class);
					dataEvent= new ArrayList<>();
					dataEvent.add(expensesList);
					ListUsers usersList = new Gson().fromJson(usersString, ListUsers.class);
					dataEvent.add(usersList);

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
		return dataEvent;

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
