package org.escoladeltreball.droiddelfrac.model;

import java.util.HashMap;
import java.util.Map;

import org.escoladeltreball.droiddelfrac.model.pojo.Expense;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
@SuppressWarnings("unused")
public class GetData {

	/**
	 * Obtiene los datos de un objeto JSON
	 * 
	 * @param tag: la etiqueta JSON que queremos obtner el valor
	 * @param response la cadena que contiene el objecto JSON
	 * @return result en formato String
	 */
	public static String getStringFromJSON(String array, String object,
			String response){

		try {
			JSONObject jObj = new JSONObject(response).getJSONObject(array);
			String result = String.valueOf(jObj.get(object));

			return result;
		} catch (JSONException e) {
			return e.getMessage();
		}
	}

	public static String getStringFromJSON(String tag, String arrayTag,
			String object, String response){

		try {
			String result = null;

			JSONObject json = new JSONObject(response).getJSONObject(tag);
			JSONObject obj = json.getJSONObject(arrayTag);
			for (int i = 0; i < obj.length(); i++) {
				result = obj.getString(object);
			}

			return result;
		} catch (JSONException e) {
			return e.getMessage();
		}
	}

	/**
	 * Obtiene los datos de un array de objectos JSON
	 * 
	 * @param arrayTag: la etiqueta con el nombre del array JSON
	 * @param keyValueTags: String[] con etiquetas JSON
	 * @param response la cadena que contiene el objecto JSON
	 * @return result en forma de map
	 */
	public static Map<Integer, String> getMapFromJSON(String arrayTag,
			String[] keyValueTags, String response) {

		Map<Integer, String> map = new HashMap<Integer, String>();
		try {
			JSONObject json = new JSONObject(response).getJSONObject("data");
			JSONArray array = json.getJSONArray(arrayTag);
			for (int i = 0; i < array.length(); i++) {
				int key = array.getJSONObject(i).getInt(keyValueTags[0]);
				String value = array.getJSONObject(i)
						.getString(keyValueTags[1]);
				map.put(key, value);
			}
		} catch (JSONException e) {
			e.getMessage();
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Busca en un map la Key de un determinado Value
	 * 
	 * @param map: el map a analizar
	 * @param value: el valor en forma de String que queremos saber la Key
	 * @return key : Key (Integer) que queremos obtener
	 */
	@SuppressWarnings("rawtypes")
	public static int getKeyFromValue(Map<Integer, String> map, String value) {
		for (Map.Entry entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				int key = (Integer) entry.getKey();
				return key;
			}
		}
		return -1;
	}
	
}
