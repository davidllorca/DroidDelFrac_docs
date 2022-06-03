package org.escoladeltreball.droiddelfrac.util;
/**
 * Clase con todas las constantes de acceso a los datos del servlet.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class Constants {
	
	/* Servlets */
	public final static String URL = "http://servlet-droiddelfrac.rhcloud.com/servlet";
	public final static String LOGIN = "/login";
	public final static String REGISTER = "/register";
	public final static String USER_EVENTS = "/userevents";
	public final static String CREATE_EVENT = "/createevent";
	public final static String ADD_EXPENSE = "/addexpense";
	public final static String LIST_FRIENDS = "/listfriends";
	public final static String EVENT_USERS = "/eventusers";
	public final static String LIST_EXPENSES = "/listexpenses";
	public final static String REGISTER_UPDATE = "/registerupdate";
	public final static String UPLOAD_IMG = "/uploadimg";
	public final static String EXISTING_USERS = "/existingusers";
	public final static String ADD_USERS = "/addusers";
	public final static String USER_EVENT_STATE ="/usereventstate";
	public final static String ADD_USER_PHANTOM ="/eventaddphantom";
	public final static String LIST_BILLS = "/listbills";
	public final static String CHANGE_EVENT_STATE = "/changeeventstate";
		
	public final static String TOKEN = "?token=";
		
	/* URLs */
	public final static String URL_LOGIN = URL + LOGIN + TOKEN; //&email &pass
	public final static String URL_REGISTER = URL + REGISTER + TOKEN; //&email &phone &pass
	public final static String URL_USER_EVENTS = URL + USER_EVENTS; //&userid
	public final static String URL_CREATE_EVENT = URL + CREATE_EVENT; //&name &description &dataevent=yyyy-MM-dd &place &idadmin &photo &import
	public final static String URL_ADD_EXPENSE = URL + ADD_EXPENSE; //&id_event &id_user &concept &quantity
	public final static String URL_LIST_FRIENDS = URL + LIST_FRIENDS; //&userid
	public final static String URL_EVENT_USERS = URL + EVENT_USERS; //&eventid
	public final static String URL_LIST_EXPENSES = URL + LIST_EXPENSES; //&eventid
	public final static String URL_REGISTER_UPDATE = URL + REGISTER_UPDATE; //&userid &name (photo???)
	public final static String URL_UPLOAD_IMG = URL + UPLOAD_IMG; //&id
	public final static String URL_EXISTING_USERS = URL + EXISTING_USERS;
	public final static String URL_ADD_USERS = URL + ADD_USERS; //&eventid
	public final static String URL_USER_EVENT_STATE = URL + USER_EVENT_STATE; //&userid &eventid &state
	public final static String URL_ADD_USER_PHANTOM = URL + ADD_USER_PHANTOM; //&eventid &name
	public final static String URL_LIST_BILLS = URL + LIST_BILLS; //&eventid &userid
	public final static String URL_CHANGE_EVENT_STATE = URL + CHANGE_EVENT_STATE; //&eventid &state
}