package org.escoladeltreball.droiddelfrac.util.task;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.activity.EventActivity;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Realiza una llamada al servidor para cambiar el estado de un evento. En
 * caso de una respuesta correcte del servlet la id de usuario. En caso de error devolverá un -1.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncChangeEvent extends AsyncTask<String, Void, Boolean> {

	private static final String TAG = "AsyncLogin";
	private Activity activity;
	private ProgressDialog splash;

	public AsyncChangeEvent(Activity activity) {
		this.activity = activity;
		splash = new ProgressDialog(activity);
	}
	
	@Override
	protected void onPreExecute() {
		splash.setMessage(activity.getResources().getText(R.string.loadData));
		splash.show();
	}
	
	@Override
	protected void onPostExecute(Boolean data) {
		if (data) {
			((EventActivity) activity).finish();
		}
		
		if (splash.isShowing()) {
			splash.dismiss();
		}
	}

	@Override
	protected Boolean doInBackground(String[] url) {
		// Envía la solicitud de login al servidor
		Request req = new Request();
		
		String result = req.GET(url[0]);

		return handleResult(result);
	}

	private Boolean handleResult(String result) {
		boolean isChangeStatus = false;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				if (code.equals("200")) {
					
					isChangeStatus = true;
					
				} else {
					// Toast info se ha producido otro error
					showToast(activity.getString(R.string.badRequest));									
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				showToast(activity.getString(R.string.badRequest));
			}
		}else{
			showToast(activity.getString(R.string.badRequest));
		}
		return isChangeStatus;
	}
		
	/**
	 * Muestra un mensaje Toast en la UI que ha llamado al Asynctask
	 * 
	 * @param errorMessage
	 */
	private void showToast(final String infomessage){		
		Handler handler = new Handler(activity.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(activity, infomessage,
						Toast.LENGTH_LONG).show();
			}
		});
	}
}
