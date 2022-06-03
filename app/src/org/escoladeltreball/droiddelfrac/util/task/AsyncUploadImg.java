package org.escoladeltreball.droiddelfrac.util.task;

import java.io.File;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.escoladeltreball.droiddelfrac.model.Request;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Conecta con el servlet para subir y recibir la foto de perfil del usuario cliente.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class AsyncUploadImg extends AsyncTask<String, Void, Void> {
	
	final static String URL = "http://servlet-droiddelfrac.rhcloud.com/servlet/uploadimg?id=";

	@Override
	protected Void doInBackground(String[]params) {
		try {
			uploadPictureToServer(params[0], params[1], params[2]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void uploadPictureToServer(String i_file, String id, String type) throws ClientProtocolException, IOException {


	    File file = new File(i_file);

	    FileBody fileBody = new FileBody(file);
	    
	    Log.i("idEvent", id);

	    HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(URL+id+"&type="+type);
	    
	    Log.i("Type", type);
	    post.setHeader("enctype", "multipart/form-data");

	    MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
	    multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	    multipartEntity.addPart("userfile", fileBody);
	    post.setEntity(multipartEntity.build());
	    

	    HttpResponse response = client.execute(post);
	    String responseBody = EntityUtils.toString(response.getEntity());
	    
	    // borramos la foto del dispositivo	    
	    if (file.exists()) {
	    	file.delete();
	    }

	}

}
