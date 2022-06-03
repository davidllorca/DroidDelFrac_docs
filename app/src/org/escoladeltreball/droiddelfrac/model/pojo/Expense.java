package org.escoladeltreball.droiddelfrac.model.pojo;

/**
 * Modela un nuevo gasto en un evento.
 *
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class Expense {
	
	private int id;
	private int id_event;
	private int id_user;
	private String username;
	private String concept;
	private double quantity;
	
	/**
	 * Constructor usado para añadir gasto.
	 * @param id_event es la id del evento.
	 * @param id_user es la id del usuario que realiza el gasto.
	 * @param concept es el concepto.
	 * @param quantity es la cantidad.
	 */
	public Expense(int id_event, int id_user, String concept, double quantity) {
		super();
		this.id_event = id_event;
		this.id_user = id_user;
		this.concept = concept;
		this.quantity = quantity;
	}
	
	/**
	 * Constructor usado para listar los gastos de un evento.
	 * 
	 * @param id
	 * @param id_event
	 * @param id_user
	 * @param concept
	 * @param quantity
	 * @param username
	 */
	public Expense(int id, int id_event, int id_user, String concept,
			double quantity, String username) {
		super();
		this.id = id;
		this.id_event = id_event;
		this.id_user = id_user;
		this.username = username;
		this.concept = concept;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_event() {
		return id_event;
	}

	public void setId_event(int id_event) {
		this.id_event = id_event;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}