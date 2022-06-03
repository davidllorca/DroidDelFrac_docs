package org.escoladeltreball.droiddelfrac.navigationdrawer;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.escoladeltreball.droiddelfrac.R;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerItem> {

	private static final String TAG = "DrawerListAdap";
	
	Context context;
	List<DrawerItem> drawerItemList;
	String idUser;
	String nameUser;
	byte[] photo;

	public DrawerListAdapter(Context context, List<DrawerItem> objects, String idUser, String mailUser,
			byte[] photo) {
		super(context, 0, objects);
		this.context = context;
		this.drawerItemList = objects;	
		this.idUser = idUser;
		this.nameUser = mailUser;
		this.photo = photo;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		LayoutInflater inflater = (LayoutInflater) parent.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.drawer_list_item, null);



		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);
		
		ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
		TextView name = (TextView) convertView.findViewById(R.id.name);

		if (!dItem.isAvatar()) {
			
			DrawerItem item = getItem(position);

			icon.setImageResource(item.getIconId());
			name.setText(item.getName());
		} else {
			inflater = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drawer_list_avatar_item, null);
			
			ImageView avatar = (ImageView) convertView.findViewById(R.id.imageAvatar);
			TextView userName = (TextView) convertView.findViewById(R.id.textViewUserName);
			
			
			DrawerItem item = getItem(position);
			
			try {
				ContextWrapper cw = new ContextWrapper(context);
		         // path to /data/data/yourapp/app_data/imageDir
		        File directory = cw.getDir("images", Context.MODE_PRIVATE);
		        
		        File f=new File(directory.getAbsolutePath(), "avatar" + idUser + ".jpg");
//		        Log.i("file f", "avatar" + idUser + ".jpg" );
		        if (f.exists()){
		        	Log.i("photo","f.exists()");
		        	Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
		        	avatar.setImageBitmap(b);
		        } else if (photo != null){
		        	Log.i("photo","!= null");
		        	Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
		        	avatar.setImageBitmap(bitmap);
		        } else {
		        	Log.i("photo","default");
		        	avatar.setImageResource(R.drawable.ic_launcher_oval);
		        }
		        
		    } 
		    catch (FileNotFoundException e){
		        Log.e(TAG, e.toString());
		    }
			userName.setText(nameUser);
		}

		return convertView;
	}
}
