package org.escoladeltreball.droiddelfrac.util.task;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.activity.EventListMenuActivity;
import org.escoladeltreball.droiddelfrac.activity.LoginActivity;
import org.escoladeltreball.droiddelfrac.activity.SplashScreenDialog;
import org.escoladeltreball.droiddelfrac.model.GetData;
import org.escoladeltreball.droiddelfrac.model.Request;
import org.escoladeltreball.droiddelfrac.model.pojo.ListEvents;
import org.escoladeltreball.droiddelfrac.model.pojo.ListUsers;
import org.escoladeltreball.droiddelfrac.model.pojo.User;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Realiza una llamada al servidor para realizar el login a la aplicación. En
 * caso de una respuesta correcte del servlet la id de usuario. En caso de error devolverá un -1.
 *
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncLoginkeepLogged extends AsyncTask<String, Void, User> {

	private static final String TAG = "AsyncLogin";
	private Activity activity;
	private ProgressDialog splash;

	public AsyncLoginkeepLogged(Activity activity) {
		this.activity = activity;
		splash = new ProgressDialog(activity);
	}
	
	@Override
	protected void onPreExecute() {
		splash.setMessage(activity.getResources().getText(R.string.loadData));
		splash.show();
	}
	
	@Override
	protected void onPostExecute(User user) {
		if (user != null) {
			String email = user.getEmail();
			String nameUser = user.getName();
			String idUser = String.valueOf(user.getId());
			((LoginActivity) activity).setIdUser(idUser);
			((LoginActivity) activity).setNameUser(nameUser);
			((LoginActivity) activity).setMailUser(email);
			((LoginActivity) activity).setPhoto(user.getPhoto());
			
			// Lanza la siguiente Activity
			lauchEventListActivity();
		} 
		
		if (splash.isShowing()) {
			splash.dismiss();
			Log.i(TAG, "oculta splash screen");
		}
	}

	@Override
	protected User doInBackground(String[] url) {
		// Envía la solicitud de login al servidor
		Request req = new Request();
		
		String result = req.GET(url[0]);

		return handleResult(result);
	}

	private User handleResult(String result) {
		ListUsers user = null;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				// Log BORRAR
				Log.i(TAG, "code: " + code);
				if (code.equals("200")) {
					
					String listString = GetData.getStringFromJSON("data", "user", result);
					user = new Gson().fromJson(listString, ListUsers.class);
					
					//showToast(activity.getString(R.string.loginCorrect));
					return user.getListUsers().get(0);
				} else if (code.equals("401")) {
					// Toast info el login ha sido incorrecto
					showToast(activity.getString(R.string.wrongLogin));
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
		return null;
	}
	
	/**
	 * Lanza la Activity EventListActivity.
	 */
	private void lauchEventListActivity() {
		Intent intent = new Intent(activity, EventListMenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(intent);
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
