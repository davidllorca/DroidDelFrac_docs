package org.escoladeltreball.droiddelfrac.model.pojo;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class ListGuest implements Parcelable{
	
	/* Atributos */
	
	String idEvent;
	List<Guest> guestList;
	
	/* Constructores */
	
	public ListGuest(String idEvent, List<Guest> guestList) {
		super();
		this.idEvent = idEvent;
		this.guestList = guestList;
	}
	
	/* Parcelable */
	
	public ListGuest(Parcel in){
		this();
		readFromParcel(in);
	}
	
	public ListGuest() {
		this.guestList = new ArrayList<Guest>();
	}

	public static final Parcelable.Creator<ListGuest> CREATOR = new Creator<ListGuest>() {  
		 public ListGuest createFromParcel(Parcel in) {
	            return new ListGuest(in);
	        }

		@Override
		public ListGuest[] newArray(int size) {
			return new ListGuest[size];
		}  
		
	};
	
	public void readFromParcel(Parcel in) {
		this.idEvent = in.readString();
		in.readList(guestList, Guest.class.getClassLoader());
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(this.idEvent);
		out.writeList(guestList);		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	/* Getters & Setters*/

	public String getIdEvent() {
		return idEvent;
	}

	public List<Guest> getGuestList() {
		return guestList;
	}
	
	

}
