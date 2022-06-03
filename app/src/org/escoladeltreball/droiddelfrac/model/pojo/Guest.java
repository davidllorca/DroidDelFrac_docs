package org.escoladeltreball.droiddelfrac.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * Modelo del objecto Guest. Contacto del usuario del propio dispositivo.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class Guest  implements Parcelable{

	// Atributos
	private String id;
	private String name;
	private byte[] photo;
	private int weight;
	private int confirmet;
	private boolean isPhantom;

	// Constructores

	public Guest(String id, String name, int weight, int confirmet) {
		super();
		this.id = id;
		this.name = name;
		this.weight = weight;
		this.confirmet = confirmet;
	}

	public Guest(String id, String name, byte[] photo, int weight, int confirmet) {
		super();
		this.id = id;
		this.name = name;
		this.photo = photo;
		this.weight = weight;
		this.confirmet = confirmet;
	}

	public Guest(String name, boolean isPhantom) {
		super();
		this.name = name;
		this.confirmet = 1;
		this.isPhantom = isPhantom;
	}
		
	/* Parcelable */
	
	public Guest(Parcel in){
		readFromParcel(in);
	}
	
	public static final Parcelable.Creator<Guest> CREATOR = new Creator<Guest>() {  
		 public Guest createFromParcel(Parcel in) {
	            return new Guest(in);
	        }

		@Override
		public Guest[] newArray(int size) {
			return new Guest[size];
		}  
		
	};
	
	public void readFromParcel(Parcel source) {
		this.id = source.readString();
		this.name = source.readString();
		int lengthPhoto = source.readInt();
		if (lengthPhoto > 0){
			this.photo = new byte[lengthPhoto]; 
			source.readByteArray(this.photo);
		}
		this.weight = source.readInt();
		this.confirmet = source.readInt();
		this.isPhantom = source.readByte() != 0; //myBoolean == true if byte != 0
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(this.id);
		out.writeString(this.name);
		if (this.photo != null){
			out.writeInt(this.photo.length);
			out.writeByteArray(this.photo);
		}
		out.writeInt(this.weight);
		out.writeInt(this.confirmet);
		out.writeByte((byte) (isPhantom ? 1 : 0)); //if myBoolean == true, byte == 1
		
	}
	
	@Override
	public int describeContents() {
		return 0;
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

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getConfirmet() {
		return confirmet;
	}

	public void setConfirmet(int confirmet) {
		this.confirmet = confirmet;
	}

	public boolean isPhantom() {
		return isPhantom;
	}

	@Override
	public String toString() {
		return "Guest [id=" + id + ", name=" + name + ", confirmet="
				+ confirmet + ", isPhantom=" + isPhantom + "]";
	}
	
	

}