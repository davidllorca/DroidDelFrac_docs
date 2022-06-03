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
public class ListBills {
	
	List<Bill> listBills = new ArrayList<Bill>();

	public ListBills(List<Bill> listBills) {
		super();
		this.listBills = listBills;
	}

	public List<Bill> getListBills() {
		return listBills;
	}

}
