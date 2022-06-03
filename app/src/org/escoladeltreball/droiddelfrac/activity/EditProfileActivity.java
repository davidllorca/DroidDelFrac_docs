package org.escoladeltreball.droiddelfrac.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.task.AsyncEditProfile;
import org.escoladeltreball.droiddelfrac.util.task.AsyncUploadImg;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Completa el registro del usuario con la posibilidad de añadir una foto
 * de perfil y un nombre de usuario.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class EditProfileActivity extends BaseActivity implements OnClickListener{
	
	private ImageButton avatarImageButton;
	private EditText userNameEditText;
	private Button finishRegisterButton;
	private Uri mImageCaptureUri;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		initComponents();
		this.photo = null;
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.imageButtonAddAvatar:
			// Abre un dialog donde escogemos añadir una imagen de la galeria 
			// o un intent a la camara de fotos.
			String[] items = getResources().getStringArray(R.array.select_photo_dialog);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.select_dialog_item, items);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.photoDialogTitle);
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) { 
					// nueva foto desde la cámara
					if (item == 0) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						mImageCaptureUri = Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), "tmp_avatar_"
								+ String.valueOf(System.currentTimeMillis())
								+ ".jpg"));
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
								mImageCaptureUri);
						try {
							intent.putExtra("return-data", true);
							startActivityForResult(intent, PICK_FROM_CAMERA);
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						}
					// foto existente, desde la galería
					} else {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent,
								"Complete action using"), PICK_FROM_FILE);
					}
				}
			});
			final AlertDialog dialog = builder.create();
			dialog.show();				
			break;
			
		case R.id.buttonUpdateProfileChanges:
			//Inserta los datos que faltan en la base de datos(UPDATE)
			AsyncEditProfile aep = new AsyncEditProfile(this);
			String name = userNameEditText.getText().toString();
			this.nameUser = name;
			name = name.trim();
			try {
				name = URLEncoder.encode(name, "latin-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			aep.execute(Constants.URL_REGISTER_UPDATE + "?userid=" + idUser
					+ "&name=" + name);

			// Lanza la activity principal de la aplicación
			
			Intent intent = new Intent(this, EventListMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);					
			break;			
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("onActivityResult requestCode: ", requestCode+"");
		Log.i("onActivityResult resultCode: ", resultCode+"");
		
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case PICK_FROM_CAMERA:
			doCrop();
			break;
		case PICK_FROM_FILE:
			mImageCaptureUri = data.getData();
			doCrop();
			break;
		case CROP_FROM_CAMERA:
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				
				// Guarda la foto en la variable de BaseActivity
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
				this.photo = stream.toByteArray();
				
				avatarImageButton.setBackgroundColor(Color.WHITE);
				avatarImageButton.setImageBitmap(photo);

				// Guarda la foto recortada en memoria interna

				ContextWrapper cw = new ContextWrapper(getApplicationContext());

				String filename = "avatar" + idUser + ".jpg";

				File directory = cw.getDir("images", Context.MODE_PRIVATE);
				// Create imageDir
				File file = new File(directory, filename);

				FileOutputStream fos = null;
				try {

					fos = new FileOutputStream(file);

					// Use the compress method on the BitMap object to write
					// image to the OutputStream
					photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String path = directory.getAbsolutePath();
				if (file.exists()) {
					file.delete();
				}
				Log.i("avatarPAth", directory.getAbsolutePath());
				try {
					FileOutputStream out = new FileOutputStream(file);
					photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
					out.flush();
					out.close();
					AsyncUploadImg aui = new AsyncUploadImg();
					String[] params = {path+"/"+filename, this.idUser, "user"};
//					Toast.makeText(this, path+"/"+filename, Toast.LENGTH_LONG).show();
					aui.execute(params);
					File f = new File(mImageCaptureUri.getPath());
					if (f.exists()) {
						f.delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			break;
		}
	}
	
	/**
	 * Recorta la foto seleccionada con la aplicación por defecto.
	 */
	private void doCrop() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);
		int size = list.size();
		if (size == 0) {
			Toast.makeText(this, "Can not find image crop app",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.setData(mImageCaptureUri);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);
				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));
				startActivityForResult(i, CROP_FROM_CAMERA);
			
		}
	}
	
	/**
	 * Referencia los componentes y añade los listeners
	 */
	private void initComponents() {
		// Carga el layout
		setContentView(R.layout.activity_edit_profile);
	
		// Referencia views
		avatarImageButton = (ImageButton)findViewById(R.id.imageButtonAddAvatar);
		userNameEditText = (EditText)findViewById(R.id.editTextUserName);
		finishRegisterButton = (Button)findViewById(R.id.buttonUpdateProfileChanges);
		
		// Añade listeners
		avatarImageButton.setOnClickListener(this);
		finishRegisterButton.setOnClickListener(this);		
	}

}