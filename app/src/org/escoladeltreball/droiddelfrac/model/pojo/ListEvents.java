package org.escoladeltreball.droiddelfrac.model.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que modela una lista de eventos. Ésta servirá para recibir
 * o enviar listas de eventos en formato JSON
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class ListEvents {
	
	List<Event> listEvents = new ArrayList<Event>();

	public ListEvents(List<Event> listEvents) {
		super();
		this.listEvents = listEvents;
	}

	public List<Event> getListEvents() {
		return listEvents;
	}
	
	

}
