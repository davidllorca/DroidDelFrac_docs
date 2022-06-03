package org.escoladeltreball.droiddelfrac.model.pojo;

import java.sql.Date;

import org.escoladeltreball.droiddelfrac.util.Util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase que modela un Evento
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 *
 */
public class Event implements Parcelable{

	private int id;
	private String name;
	private String description;
	/*
	 * status 1 = evento creado. Se pueden agregar gastos
	 * status 2 = evento pendiente de pago. No se pueden notificar mas gastos
	 * status 3 = evento cerrado. Todos los cobros realizados
	 */
	private int status;
	/*
	 * userStatus 0 = en espera de confirmación
	 * userStatus 1 = confirmado
	 * userStatus 2 = rechazado
	 */
	private int userStatus;
	private String amount;
	private String dateEvent;
	private String place;
	private int idAdmin;
	private byte[] photo;
	
	/* Constructores */
	
	public Event() {}
	
	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param dateEvent
	 * @param place
	 * @param idAdmin
	 * @param status
	 * @param userStatus
	 * @param amount
	 */
	public Event(int id, String name, String description, String dateEvent,
			String place, int idAdmin, int status, int userStatus,
			String amount) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.dateEvent = dateEvent;
		this.place = place;
		this.idAdmin = idAdmin;
		this.status = status;
		this.userStatus = userStatus;
		this.amount = amount;
	}
	
	/**
	 * Constructor para CreateEvent, la creación de un nuevo evento.
	 * @param name es el nombre del evento.
	 * @param description es la descripción.
	 * @param data_event es la fecha de celebración.
	 * @param place es el lugar de celebración.
	 * @param id_admin es la id del usuario administrador.
	 * @param photo es la foto del evento.
	 */
	public Event(String name, String description, String place, int idAdmin, byte[] photo) {
		this.name = name;
		this.description = description;
		this.place = place;
		this.idAdmin = idAdmin;
		this.photo = photo;
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param status
	 * @param userStatus
	 * @param amount
	 * @param place
	 * @param idAdmin
	 * @param photo
	 */
	public Event(int id, String name, String description, int status,
			int userStatus, String amount, String place, int idAdmin,
			byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.userStatus = userStatus;
		this.amount = amount;
		this.place = place;
		this.idAdmin = idAdmin;
		this.photo = photo;
	}

	public Event(Parcel in){
	    readFromParcel(in);
	}
	
	public static final Parcelable.Creator<Event> CREATOR = new Creator<Event>() {  
		 public Event createFromParcel(Parcel in) {
	            return new Event(in);
	        }

		@Override
		public Event[] newArray(int size) {
			return new Event[size];
		}  
		
	};
	
	/* Metodos */
	
	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel source) {  
		//Event event = new Event();
		this.id = source.readInt();
		this.name = source.readString();
		this.description = source.readString();
		this.status = source.readInt(); 
		this.userStatus = source.readInt(); 
		this.amount = source.readString();
		this.dateEvent = source.readString();
		this.place = source.readString();
		this.idAdmin = source.readInt();
		int lengthPhoto = source.readInt();
		if (lengthPhoto > 0){
			this.photo = new byte[lengthPhoto]; 
			source.readByteArray(this.photo);
		}
		
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(id);  
		parcel.writeString(name);  
		parcel.writeString(description);
		parcel.writeInt(status);
		parcel.writeInt(userStatus);
		parcel.writeString(amount);
		parcel.writeString(dateEvent);
		parcel.writeString(place);
		parcel.writeInt(idAdmin);
		if (photo != null){
			parcel.writeInt(photo.length);
			parcel.writeByteArray(photo);
		}
	}
	
	/* Getters & Setters */

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDateEvent() {
		return dateEvent;
	}

	public void setDataEvent(String dateEvent) {
		this.dateEvent = dateEvent;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getIdAdmin() {
		return idAdmin;
	}

	public void setIdAdmin(int idAdmin) {
		this.idAdmin = idAdmin;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public void setDateEvent(String dateEvent) {
		this.dateEvent = dateEvent;
	}
	
}
