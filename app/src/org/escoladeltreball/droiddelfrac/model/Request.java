package org.escoladeltreball.droiddelfrac.model;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class Request {

	/**
	 * Envia una petición Http con el método GET
	 * 
	 * @param url: la url a la que se ha de hacer la petición
	 * @return la respuesta Http en formato String
	 */
	public String GET(String url) {

		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			return "protocol error";
		} catch (IOException e) {
			return "IO error";
		}
	}

}
