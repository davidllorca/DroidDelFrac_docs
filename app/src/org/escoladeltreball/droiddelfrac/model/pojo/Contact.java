package org.escoladeltreball.droiddelfrac.model.pojo;

/**
 * Modelo del objecto Contact. Contacto del usuario del propio dispositivo.
 *
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class Contact {

	// Atributos
	private String id;
	private String name;
	private String phoneNumber;
	private byte[] photo;
	/** Marca si el usuario está seleccionado en una ListView */
	private boolean isChecked;
	private boolean isPhantom;

	// Constructores

	public Contact(String id, String name, String phoneNumber) {
		super();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public Contact(String id, String name, String phoneNumber, byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.photo = photo;
	}

	public Contact(String name, String phoneNumber) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public Contact(String name, byte[] photo) {
		super();
		this.name = name;
		this.photo = photo;
	}

	public Contact(String id, String name, byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.photo = photo;
	}

	public Contact(String name) {
		super();
		this.name = name;
		this.isChecked = true;
		this.isPhantom = true;
	}

	// Getters y setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public boolean isPhantom() {
		return isPhantom;
	}

	public void setPhantom(boolean isPhantom) {
		this.isPhantom = isPhantom;
	}

	@Override
	public String toString() {
		return "Contact [idPhone=" + id + ", name=" + name + ", phoneNumber="
				+ phoneNumber + "]";
	}

}