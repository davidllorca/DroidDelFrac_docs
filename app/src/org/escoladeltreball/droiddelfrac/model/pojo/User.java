package org.escoladeltreball.droiddelfrac.model.pojo;

import java.util.Arrays;

/**
 * Modela un usuario para la base de datos.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class User {

	private int id;
	private String name;
	private String email;
	private String phone;
	private String pass;
	private String dataRegister;
	private boolean active;
	private byte[] photo;
	private int confirmet;
	/** Marca si el usuario está seleccionado en una ListView */
	private boolean isChecked;
	private boolean isPhantom;

	public User() {

	}

	/**
	 * 
	 * @param name
	 */
	public User(String name) {
		super();
		this.name = name;
	}

	/**
	 * Contructor para el login.
	 * 
	 * @param email
	 *            es el email del usuario.
	 * @param pass
	 *            es la contraseña del usuario.
	 */
	public User(String email, String pass) {
		this.email = email;
		this.pass = pass;
	}
	
	/**
	 * Constructor para listar datos del cliente tras el login.
	 * @param id es la id.
	 * @param name es el nombre de usuario.
	 * @param email es el email.
	 * @param photo es la foto
	 */
	public User (int id, String name, String email, byte[] photo) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.photo = photo;
	}

	/**
	 * Constructor para el registro.
	 * 
	 * @param email
	 *            es el email.
	 * @param phone
	 *            es el teléfono.
	 * @param pass
	 *            es la contraseña.
	 */
	public User(String email, String phone, String pass) {
		this.email = email;
		this.phone = phone;
		this.pass = pass;
	}
	
	/**
	 * Constructor para listar usuarios fantasma de un evento.
	 * @param id es la id.
	 * @param name es el nombre de usuario.
	 * @param email es el email.
	 * @param phone es el teléfono.
	 * @param confirmet es el código de asistencia (1 en este caso).
	 */
	public User (int id, String name, String email, String phone,int confirmet, boolean isPhantom) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.confirmet = confirmet;
		this.isPhantom = isPhantom;
	}

	/**
	 * Constructor para listar contactos del cliente o de un evento.
	 * 
	 * @param id
	 *            es la id.
	 * @param name
	 *            es el nombre de usuario.
	 * @param email
	 *            es el email.
	 * @param phone
	 *            es el teléfono.
	 * @param photo
	 *            es la foto
	 */
	public User(int id, String name, String email, String phone, byte[] photo) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.photo = photo;
	}

	/**
	 * Constructor usado para actualizar la información del usuario registrado.
	 * 
	 * @param id
	 *            es la id del usuario.
	 * @param name
	 *            es el nombre de usuario.
	 */
	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getDataRegister() {
		return dataRegister;
	}

	public void setDataRegister(String dataRegister) {
		this.dataRegister = dataRegister;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}	

	public int getConfirmet() {
		return confirmet;
	}

	public void setConfirmet(int confirmet) {
		this.confirmet = confirmet;
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
		return "User [id=" + id + ", name=" + name + ", email=" + email
				+ ", confirmet=" + confirmet + "]";
	}



	

	

}
