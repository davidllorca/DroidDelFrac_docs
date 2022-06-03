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
 * Realiza una llamada al servidor para realizar un registro. Devuelve -1 si el
 * registro no se ha producido satisfactoriamente, false en caso contrario.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncRegisterUser extends AsyncTask<String, Void, String> {

	private static final String TAG = "AsyncRegisterUser";
	private Context context;

	public AsyncRegisterUser(Context context) {
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
		String idUser = "-1";

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				if (code.equals("200")) {
					// En caso de resultado satisfactorio, el servlet devuelve
					// la id del nuevo usuario.
					idUser = GetData.getStringFromJSON("data", "id_user",
							result);
				} else if (code.equals("401")) {
					showToast("error 401");
				} else if (code.equals("400")) {
					// Obtiene el tipo de error
					String errorType = GetData.getStringFromJSON("error", "type", result);
					switch (errorType) {
					case "mail_unique":
						showToast(context.getString(R.string.mailUnique));
						break;
					case "phone_unique":
						showToast(context.getString(R.string.phoneUnique));
						break;
					case "Bad request":
						showToast(context.getString(R.string.badRequest));
						break;
					default:
						break;
					}
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				showToast(context.getString(R.string.badRequest));
			}
		}else{
			showToast(context.getString(R.string.badRequest));

		}
		return idUser;
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
