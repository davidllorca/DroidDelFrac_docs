package org.escoladeltreball.droiddelfrac.model.pojo;

/**
 * Modela una factura, es decir, el pago de un usuario a otro en un evento.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class Bill {
	private int idEvent;
	private int idPayer;
	private int idReceiver;
	private String namePayer;
	private String nameReceiver;
	private double quantity;
	private boolean isPhantomPayer;
	
	/**
	 * Constructor para la inserción de facturas.
	 * @param idEvent es la id del evento.
	 * @param idPayer es la id del usuario pagador.
	 * @param idBuyer es la id del usuario que recibe el pago.
	 * @param quantity es la cantidad.
	 */
	public Bill(int idEvent, int idPayer, int idBuyer, double quantity) {
		super();
		this.idEvent = idEvent;
		this.idPayer = idPayer;
		this.idReceiver = idBuyer;
		this.quantity = quantity;
	}
	
	
	/**
	 * Constructor para la recuperación de facturas.
	 * @param idEvent es la id del evento.
	 * @param idPayer es la id del usuario pagador.
	 * @param idReceiver es la id del usuario que recibe el pago.
	 * @param namePayer es el nombre del pagador.
	 * @param nameReceiver es el nombre del que recibe el pago.
	 * @param quantity es la cantidad.
	 * @param isPhantomPayer indica si el usuario pagador es "fantasma".
	 */
	public Bill(int idEvent, int idPayer, int idReceiver, double quantity, String namePayer, String nameReceiver, boolean isPhantomPayer) {
		super();
		this.idEvent = idEvent;
		this.idPayer = idPayer;
		this.idReceiver = idReceiver;		
		this.quantity = quantity;
		this.namePayer = namePayer;
		this.nameReceiver = nameReceiver;
		this.isPhantomPayer = isPhantomPayer;
	}


	public int getIdEvent() {
		return idEvent;
	}


	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}


	public int getIdPayer() {
		return idPayer;
	}


	public void setIdPayer(int idPayer) {
		this.idPayer = idPayer;
	}


	public int getIdReceiver() {
		return idReceiver;
	}


	public void setIdReceiver(int idReceiver) {
		this.idReceiver = idReceiver;
	}


	public String getNamePayer() {
		return namePayer;
	}


	public void setNamePayer(String namePayer) {
		this.namePayer = namePayer;
	}


	public String getNameReceiver() {
		return nameReceiver;
	}


	public void setNameReceiver(String nameReceiver) {
		this.nameReceiver = nameReceiver;
	}


	public double getQuantity() {
		return quantity;
	}


	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}


	public boolean isPhantomPayer() {
		return isPhantomPayer;
	}


	public void setPhantomPayer(boolean isPhantomPayer) {
		this.isPhantomPayer = isPhantomPayer;
	}


	@Override
	public String toString() {
		return "Bill [idEvent=" + idEvent + ", idPayer=" + idPayer
				+ ", idReceiver=" + idReceiver + ", namePayer=" + namePayer
				+ ", nameReceiver=" + nameReceiver + ", quantity=" + quantity
				+ ", isPhantomPayer=" + isPhantomPayer + "]";
	}		
}