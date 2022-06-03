package org.escoladeltreball.droiddelfrac.util.task;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.pojo.ListStrings;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Realiza una llamada al servlet, enviando las ids de los usuarios
 * a añadir al evento.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncAddUsers extends AsyncTask<List<String>, Void, Void> {
	
	String url = Constants.URL_ADD_USERS+"?eventid=";
	
	private Context context;
	private String eventid;

	public AsyncAddUsers(Context context, String eventid) {
		this.context = context;
		this.eventid = eventid;
	}

	@Override
	protected Void doInBackground(List<String>... params) {
		String result = null;
		ListStrings ls = new ListStrings (params[0]);		
		String list = new Gson().toJson(ls);
		
//		// la id del evento se añade la última al List
//		String eventid = params[0].get(params[0].size()-1);
		
		HttpGet get = new HttpGet(url+eventid);
		Log.i("url addusers", url+eventid);
		
		get.addHeader("lista", list);
		
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = client.execute(get);
			result = EntityUtils.toString(response.getEntity());
			handleResult(result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private void handleResult(String result) {

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));

				if (code.equals("200")) {
					//showToast(context.getString(R.string.addedContacts));
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
