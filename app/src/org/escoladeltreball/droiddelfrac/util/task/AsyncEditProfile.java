package org.escoladeltreball.droiddelfrac.util.task;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;


/**
 * Conecta con el servlet encargado de actualizar el perfil de usuario con una
 * foto de perfil y un nombre de usuario.
 *
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncEditProfile extends AsyncTask<String, Void, Void> {
	
	private Context context;

	public AsyncEditProfile (Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(String[] url) {
		// Envía la solicitud de login al servidor
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

				if (code.equals("200")) {
					showToast(context.getString(R.string.updateCorrect));
				} else if (code.equals("400")) {
					// Toast info el login ha sido incorrecto
					showToast(context.getString(R.string.badRequest));
				} else {
					// Toast info se ha producido otro error
					showToast(context.getString(R.string.badRequest));									
				}
			} catch (JSONException e) {
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
	private void showToast(final String infomessage){		
		Handler handler = new Handler(context.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(context, infomessage,
						Toast.LENGTH_LONG).show();
			}
		});
	}

}
