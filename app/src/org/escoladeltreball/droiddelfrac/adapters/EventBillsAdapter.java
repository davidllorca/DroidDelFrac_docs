package org.escoladeltreball.droiddelfrac.adapters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.R.drawable;
import org.escoladeltreball.droiddelfrac.model.pojo.BillItem;
import org.escoladeltreball.droiddelfrac.model.pojo.Guest;
import org.escoladeltreball.droiddelfrac.model.pojo.User;
import org.escoladeltreball.droiddelfrac.sqlite.ContactsSQLiteHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adaptador que se encarga de asociar los datos recogidos con los componentes
 * gráficos del layout donde se quieren mostrar dichos datos.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 * 
 *         This is free software, licensed under the GNU General Public License
 *         v3. See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class EventBillsAdapter extends BaseAdapter {

	private final List<BillItem> bills;
	private String idUser;
	private final LayoutInflater layoutInflater;
	private Context context;

	public EventBillsAdapter(Context context, List<BillItem> bills,
			String idUser) {
		super();
		this.context = context;
		this.bills = bills;
		this.idUser = idUser;
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bills.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bills.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * Patrón para crear el modelo que representará el ítem del listView
	 */
	static class ViewHolder {
		ImageView avatarLeft;
		TextView guestLeft;
		ImageView avatarRight;
		TextView guestRight;
		TextView amount;
		ImageView arrow;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Usamos nuestro holder para asociar los datos con los componentes del
		// layout
		ViewHolder holder = new ViewHolder();

		// Reutilizamos los views para no usar mas memoria que la necesaria
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_bill_receive,
					null);

			// Recuperamos los componentes y los asociamos al holder

			holder.avatarLeft = (ImageView) convertView
					.findViewById(R.id.imageViewAvatarGuestLeft);
			holder.guestLeft = (TextView) convertView
					.findViewById(R.id.textViewGuestLeft);
			holder.avatarRight = (ImageView) convertView
					.findViewById(R.id.imageViewAvatarGuestRight);
			holder.guestRight = (TextView) convertView
					.findViewById(R.id.textViewGuestRight);
			holder.amount = (TextView) convertView
					.findViewById(R.id.textViewAmount);
			holder.arrow = (ImageView) convertView
					.findViewById(R.id.imageViewArrow);

			// Asociamos la estructura del layout con con la vista que
			// retornaremos en el método
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (bills.get(position).getIdPayer().equals(idUser)) {

			// Asignamos los datos a los componentes del holder
			// Avatar Left
			if (bills.get(position).getAvatarPayer() != null) {
				Log.i("avatar payer", "not null");
				holder.avatarLeft.setImageBitmap(BitmapFactory.decodeByteArray(
						bills.get(position).getAvatarPayer(), 0,
						bills.get(position).getAvatarPayer().length));
			} else {
				Log.i("avatar payer", "null");
				holder.avatarLeft.setImageResource(R.drawable.ic_launcher);
			}
			// Nombre del participante
			holder.guestLeft.setText(bills.get(position).getNamePayer());

			// Avatar Right
			if (bills.get(position).getAvatarReceiver() != null) {
				holder.avatarRight.setImageBitmap(BitmapFactory
						.decodeByteArray(bills.get(position)
								.getAvatarReceiver(), 0, bills.get(position)
								.getAvatarReceiver().length));
			} else {
				holder.avatarRight.setImageResource(R.drawable.ic_launcher);
			}
			// Nombre del participante
			holder.guestRight.setText(bills.get(position).getNameReceiver());
			holder.arrow.setImageResource(R.drawable.ic_bill_pay);
			holder.amount.setText(round(bills.get(position).getAmount(), 2) + " €");

		} else {
			// Asignamos los datos a los componentes del holder
			// Avatar Left
			if (bills.get(position).getAvatarPayer() != null) {
				Log.i("avatar payer", "not null");
				holder.avatarRight.setImageBitmap(BitmapFactory.decodeByteArray(
						bills.get(position).getAvatarPayer(), 0,
						bills.get(position).getAvatarPayer().length));
			} else {
				Log.i("avatar payer", "null");
				holder.avatarRight.setImageResource(R.drawable.ic_launcher);
			}
			// Nombre del participante
			holder.guestRight.setText(bills.get(position).getNamePayer());

			// Avatar Right
			if (bills.get(position).getAvatarReceiver() != null) {
				holder.avatarLeft.setImageBitmap(BitmapFactory
						.decodeByteArray(bills.get(position)
								.getAvatarReceiver(), 0, bills.get(position)
								.getAvatarReceiver().length));
			} else {
				holder.avatarLeft.setImageResource(R.drawable.ic_launcher);
			}
			// Nombre del participante
			holder.guestLeft.setText(bills.get(position).getNameReceiver());
			holder.arrow.setImageResource(R.drawable.ic_bill_receive);
			holder.amount.setText(round(bills.get(position).getAmount(), 2) + " €");
		}

		return convertView;
	}

	/**
	 * Redondea doubles.
	 * 
	 * @param value
	 *            double a redondear.
	 * @param places
	 *            número de dígitos decimales.
	 * @return valor redondeado.
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}