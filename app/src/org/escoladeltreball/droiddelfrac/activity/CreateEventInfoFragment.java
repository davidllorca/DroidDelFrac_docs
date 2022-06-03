package org.escoladeltreball.droiddelfrac.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.util.Constants;
import org.escoladeltreball.droiddelfrac.util.task.AsyncCreateEvent;
import org.escoladeltreball.droiddelfrac.util.task.AsyncUploadImg;

import android.app.Activity;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class CreateEventInfoFragment extends Fragment implements
		OnClickListener {

	private static final String TAG = "CreateEventInfoFragment";

	/* Views */
	private View rootView;

	private ImageButton avatarImageButton;
	private ImageButton calenImageButton;
	private EditText nameEditText;
	private EditText descriptionEditText;
	private EditText dateEditText;
	private EditText placeEditText;
	private Button createEventButton;

	/* Variables */
	private String idUser;
	private String idEvent;
	private String day;
	private String month;
	private String year;
	
	private Uri mImageCaptureUri;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;


	/* URLs Servlet */
	public final static String CREATE_EVENT = "/createevent?";
	public final static String URL_CREATE_EVENT = Constants.URL + CREATE_EVENT; // &name

	// &description
	// &dataevent=yyyy-MM-dd
	// &place
	// &idadmin
	// &photo
	// &import

	public CreateEventInfoFragment() {
		idUser = ((CreateEventActivity)getActivity()).idUser;
		Log.i("IDUsER", idUser);
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_create_event_info,
				container, false);

		// Carga componentes
		initComponent();

		return rootView;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imageButtonImageEvent:
			// PENDIENTE: lanzar intent a la camara
			// Abre un dialog donde escogemos añadir una imagen de la galeria
			// o un intent a la camara de fotos.
			String[] items = getResources().getStringArray(
					R.array.select_photo_dialog);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.select_dialog_item, items);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.photoDialogTitle);
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					// nueva foto desde la cámara
					if (item == 0) {
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						mImageCaptureUri = Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), "tmp_avatar_"
								+ String.valueOf(System.currentTimeMillis())
								+ ".jpg"));
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
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
		case R.id.imageButtonPickCalendar:
			showDatePickerDialog();
			break;
		case R.id.buttonCreateEventNext:
			if (!nameEditText.getText().toString().equals("")) {
				String name = nameEditText.getText().toString();
				String description = descriptionEditText.getText().toString();
				String date = dateEditText.getText().toString();
				if (!date.equals("")) {
					year = date.substring(6, 10);
					month = date.substring(3, 5);
					day = date.substring(0, 2);
					date = year + "-" + month + "-" + day;
				}

				String place = placeEditText.getText().toString();
				// control de espacios
				name = name.trim();
				description = description.trim();
				place = place.trim();
				
				//codificacion caracteres raros
				try {
					name = URLEncoder.encode(name, "latin-1");
					description = URLEncoder.encode(description, "latin-1");
					place = URLEncoder.encode(place, "latin-1");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Log.i(TAG + " date:", date);
				

				String url = URL_CREATE_EVENT + "name=" + name
						+ "&description=" + description + "&dataevent=" + date
						+ "&place=" + place + "&idadmin=" + idUser + "&photo="
						+ "&import=";
				
				url = ((CreateEventActivity)getActivity()).prepareUrl(url);
				
				Log.i(TAG, "url:" + url);
				idEvent = "-1";
				AsyncCreateEvent task = new AsyncCreateEvent(getActivity());
				task.execute(new String[] { url });

				try {
					idEvent = task.get();
				} catch (InterruptedException | ExecutionException e) {
					Log.e(TAG + " task.get()", e.toString());
				}

				if (!idEvent.equals("-1")) {
					// foto a servidor
					AsyncUploadImg aui = new AsyncUploadImg();
					ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
					String filename = "event" + idUser + ".jpg";
					File directory = cw.getDir("images", Context.MODE_PRIVATE);
					String path = directory.getAbsolutePath();
					String[] params = {path+"/"+filename, idEvent, "event"};
					Toast.makeText(getActivity(), path+"/"+filename, Toast.LENGTH_LONG).show();
					aui.execute(params);

					FragmentManager fragmentmanager = getActivity()
							.getSupportFragmentManager();
					FragmentTransaction fragmenttransaction = fragmentmanager
							.beginTransaction();

					Bundle b = new Bundle();
					b.putString("idEvent", idEvent);// AÑADIR BUNDLE
					Fragment createEventInviteGuestFragment = new CreateEventInviteGuestFragment(
							b);
					fragmenttransaction.replace(
							R.id.create_event_content_frame,
							createEventInviteGuestFragment);
					fragmenttransaction.commit();
					
					//PENDIENTE
					// launchEventActivity();

				}

			} else {
				Toast.makeText(getActivity(),
						this.getString(R.string.nameRequired),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

	}

	public void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getActivity().getSupportFragmentManager(),
				"datePicker");
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getActivity().getSupportFragmentManager(),
				"timePicker");
	}

	/**
	 * Lanza la Activity EventActivity.
	 */
	private void launchEventActivity() {
		Intent intent = new Intent(getActivity(), EventActivity.class);
		Bundle extras = new Bundle();
		extras.putString("idEvent", idEvent);
		intent.putExtras(extras);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
		// | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("onActivityResult requestCode: ", requestCode+"");
		Log.i("onActivityResult resultCode: ", resultCode+"");
		
		if (resultCode != Activity.RESULT_OK)
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
				avatarImageButton.setBackgroundColor(Color.WHITE);
				avatarImageButton.setImageBitmap(photo);
				// // guardamos la foto recortada en memoria externa
				// String root =
				// Environment.getExternalStorageDirectory().toString();
				// File myDir = new File(root + "/DroidDelFrac");
				// myDir.mkdirs();
				// // String fname = "user" + this.idUser + ".jpg";
				// String fname = "userAvatar.jpg";
				// File file = new File(myDir, fname);
				// if (file.exists()) {
				// file.delete();
				// }

				// Guarda la foto recortada en memoria interna

				ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());

				String filename = "event" + idUser + ".jpg";

				File directory = cw.getDir("images", Context.MODE_PRIVATE);
				// Create imageDir
				File file = new File(directory, filename);

				FileOutputStream fos = null;
				try {

					fos = new FileOutputStream(file);

					// Use the compress method on the BitMap object to write
					// image to the OutputStream
					photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
		List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(
				intent, 0);
		int size = list.size();
		if (size == 0) {
			Toast.makeText(getActivity(), "Can not find image crop app",
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
	private void initComponent() {

		// Referencia views
		avatarImageButton = (ImageButton) rootView
				.findViewById(R.id.imageButtonImageEvent);
		nameEditText = (EditText) rootView.findViewById(R.id.editTextEventName);
		descriptionEditText = (EditText) rootView
				.findViewById(R.id.editTextEventDescription);
		dateEditText = (EditText) rootView.findViewById(R.id.editTextEventDate);
		calenImageButton = (ImageButton) rootView
				.findViewById(R.id.imageButtonPickCalendar);
		placeEditText = (EditText) rootView
				.findViewById(R.id.editTextEventPlace);
		createEventButton = (Button) rootView
				.findViewById(R.id.buttonCreateEventNext);

		// Añade listeners
		avatarImageButton.setOnClickListener(this);
		calenImageButton.setOnClickListener(this);
		createEventButton.setOnClickListener(this);
	}
}
