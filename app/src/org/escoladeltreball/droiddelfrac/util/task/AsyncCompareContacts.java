package org.escoladeltreball.droiddelfrac.util.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.GetData;
import org.escoladeltreball.droiddelfrac.model.pojo.ListStrings;
import org.escoladeltreball.droiddelfrac.model.pojo.ListUsers;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Envía una lista de los teléfonos de los contactos del usuario al servlet
 * y recibe una lista de los que ya son usuarios de la aplicación.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncCompareContacts extends AsyncTask<List<String>, Void, ListUsers> {
	
	String url = Constants.URL_EXISTING_USERS;
	
	private Context context;

	public AsyncCompareContacts(Context context) {
		this.context = context;
	}

	@Override
	protected ListUsers doInBackground(List<String>... params) {
		String result = null;
		ListStrings ls = new ListStrings (params[0]);		
		String list = new Gson().toJson(ls);
		
		HttpGet get = new HttpGet(url);
		get.addHeader("lista", list);
		
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = client.execute(get);
			result = EntityUtils.toString(response.getEntity());
			return handleResult(result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	private ListUsers handleResult(String result) {
		ListUsers userList = null;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				if (code.equals("200")) {
					String listString = GetData.getStringFromJSON("data", "user", result);
					Log.i("RESULT UserList", listString);
					userList = new Gson().fromJson(listString, ListUsers.class);

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
		return userList;

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
