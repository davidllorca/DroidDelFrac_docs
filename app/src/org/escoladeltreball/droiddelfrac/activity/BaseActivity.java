package org.escoladeltreball.droiddelfrac.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Activity padre con variables y métodos que heredarán el resto
 * de las activitys de la aplicación 
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class BaseActivity extends FragmentActivity {
	
	/* Variables */
	protected static String idUser;
	protected static String mailUser;
	protected static String nameUser;
	protected static byte[] photo;
	
	/* URLs Servlet */
	protected static final String URL = "http://servlet-droiddelfrac.rhcloud.com/servlet";
	protected static final String TOKEN = "?token=12345678";
	// http://servlet-droidelfrac.rhcloud.com/servlet/login?token=12345678&email=rdios@a.es&pass=12345678
	
	/* SharedPreferences */
	protected SharedPreferences prefs;
	protected final static String PREF_NAME = "DroidDelFracPreferences";
	protected final static String KEEP_LOGGED = "keepLogged";

	

	/**
	 * Muestra un mensaje Toast.
	 * 
	 * @param text 
	 *            el mensaje a mostrar
	 */
	protected void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * Comprueba si hay conexión a internet.
	 * 
	 * @return true si hay conexión a internet, falso en caso contrario.
	 */
	protected boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null);
	}

	/**
	 * Guarda el par key/value de Strings en un SharedPreferences
	 * 
	 * @param context
	 * @param key
	 *            string
	 * @param value
	 *            string
	 */
	public void savePreferences(String key, String value) {

		prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Obtiene el valor de la preferencia cuya key pasamos por parámetro
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public String obtainStringPreferences(String key) {

		prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return prefs.getString(key, "");
	}

	/**
	 * Obtiene el valor de la preferencia cuya key pasamos por parámetro
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public boolean obtainBooleanPreferences(String key) {

		prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return prefs.getBoolean(key, false);
	}

	/**
	 * Guarda el par key/value de String/boolean en un SharedPreferences
	 * 
	 * @param context
	 * @param key
	 *            string
	 * @param value
	 *            boolean
	 */
	public void savePreferences(String key, boolean value) {

		prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * Guarda en un SharedPreferences si el usuario desea mantenerse logueado.
	 * 
	 * @param keepLogged
	 *            true si el usuario desea mantener la sesión, false en caso
	 *            contrario.
	 */
	public void saveKeepLogged(boolean keepLogged) {
		savePreferences(KEEP_LOGGED, keepLogged);
	}

	/**
	 * Obtiene el valor de la preferencia KEEP_LOGGED.
	 * 
	 * @return
	 */
	protected boolean keepLogged() {
		return obtainBooleanPreferences(KEEP_LOGGED);
	}
	
	/**
	 * Devuelve el numero de teléfono del dispositivo o en su defecto en número de SIM.
	 * @return
	 */
	protected String getMyPhoneNumber() {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		if(mTelephonyMgr.getLine1Number().equals("")){
			return mTelephonyMgr.getSimSerialNumber();
		}
		return mTelephonyMgr.getLine1Number();
	}
	
	/**
	 * Cambia los espacios de una cadena por '%'.
	 * 
	 * @param url
	 * @return url con los caracteres substituidos.
	 */
	protected String prepareUrl(String url){
		url = url.replace(" ", "%");
		return url;
		
	}

	public static String getIdUser() {
		return idUser;
	}

	public static void setIdUser(String idUser) {
		BaseActivity.idUser = idUser;
	}

	public static String getMailUser() {
		return mailUser;
	}

	public static void setMailUser(String mailUser) {
		BaseActivity.mailUser = mailUser;
	}

	public static String getNameUser() {
		return nameUser;
	}

	public static void setNameUser(String nameUser) {
		BaseActivity.nameUser = nameUser;
	}

	public static byte[] getPhoto() {
		return photo;
	}

	public static void setPhoto(byte[] photo) {
		BaseActivity.photo = photo;
	}
}
