package org.escoladeltreball.droiddelfrac.model.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que modela un grupo de eventos
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class Group {

	private String name;
	private final List<Event> children = new ArrayList<Event>();

	public Group() {
	}

	public Group(String string) {
		this.name = string;
	}

	public void addChildren(Event children) {
		this.children.add(children);
	}

	public List<Event> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
