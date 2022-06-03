package org.escoladeltreball.droiddelfrac.util.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncSendContacts extends AsyncTask<String, Void, String> {
	
	private JSONArray arrayPhones;
	
	public AsyncSendContacts(JSONArray arrayPhones){
		this.arrayPhones = arrayPhones;
	}
	@Override
	protected String doInBackground(String[] url) {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://servlet-droiddelfrac.rhcloud.com/servlet/searchusers");
		StringEntity se = null;
		try {
			se = new StringEntity(arrayPhones.toString());
//			Log.i("JSONarray", arrayPhones.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		post.setEntity(se);
		try {
			client.execute(post);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}