package org.escoladeltreball.droiddelfrac.model.pojo;

import java.util.Arrays;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class BillItem {
	
	// Atributos
	private String idPayer;
	private byte[] avatarPayer;
	private String namePayer;
	private String idReceiver;
	private byte[] avatarReceiver;
	private String nameReceiver;
	private double amount;
	private boolean isPhantomPayer;
	
	// Contructores
	public BillItem() {
		super();
	}
		
	public BillItem(String idPayer, byte[] avatarPayer, String namePayer,
			String idReceiver, byte[] avatarReceiver, String nameReceiver,
			double amount) {
		super();
		this.idPayer = idPayer;
		this.avatarPayer = avatarPayer;
		this.namePayer = namePayer;
		this.idReceiver = idReceiver;
		this.avatarReceiver = avatarReceiver;
		this.nameReceiver = nameReceiver;
		this.amount = amount;
	}



	// Getters y setters
	public String getIdPayer() {
		return idPayer;
	}

	public void setIdPayer(String idPayer) {
		this.idPayer = idPayer;
	}

	public byte[] getAvatarPayer() {
		return avatarPayer;
	}

	public void setAvatarPayer(byte[] avatarPayer) {
		this.avatarPayer = avatarPayer;
	}

	public String getIdReceiver() {
		return idReceiver;
	}

	public void setIdReceiver(String idReceiver) {
		this.idReceiver = idReceiver;
	}

	public byte[] getAvatarReceiver() {
		return avatarReceiver;
	}

	public void setAvatarReceiver(byte[] avatarReceiver) {
		this.avatarReceiver = avatarReceiver;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isPhantomPayer() {
		return isPhantomPayer;
	}

	public void setPhantomPayer(boolean isPhantomPayer) {
		this.isPhantomPayer = isPhantomPayer;
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

	@Override
	public String toString() {
		return "BillItem [idPayer=" + idPayer + ", avatarPayer="
				+ Arrays.toString(avatarPayer) + ", namePayer=" + namePayer
				+ ", idReceiver=" + idReceiver + ", avatarReceiver="
				+ Arrays.toString(avatarReceiver) + ", nameReceiver="
				+ nameReceiver + ", amount=" + amount + ", isPhantomPayer="
				+ isPhantomPayer + "]";
	}
	
	
	
}
