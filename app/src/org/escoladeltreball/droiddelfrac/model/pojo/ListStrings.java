package org.escoladeltreball.droiddelfrac.model.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class ListStrings {
	List<String> listStrings = new ArrayList<String>();

	public ListStrings(List<String> listStrings) {
		super();
		this.listStrings = listStrings;
	}

	public List<String> getListStrings() {
		return listStrings;
	}

}
