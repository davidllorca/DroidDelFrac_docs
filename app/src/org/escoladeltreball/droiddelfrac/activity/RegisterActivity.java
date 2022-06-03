package org.escoladeltreball.droiddelfrac.activity;

import java.util.concurrent.ExecutionException;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.util.task.AsyncRegisterUser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity que registra un nuevo usuario.
 * 
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 * 
 *         This is free software, licensed under the GNU General Public License
 *         v3. See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "RegisterActivity";

	/* Views */
	private EditText mailEditText;
	private EditText passwordEditText;
	private EditText repeatPasswordEditText;
	private TextView showPassword;
	private EditText phoneEditText;
	private Button nextButton;

	/* URLs Servlet */
	private final static String REGISTER = "/register";
	public final static String URL_REGISTER = URL + REGISTER + TOKEN; // &email
																		// &phone
																		// &pass

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Carga componentes
		initComponents();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textViewShowpassword:
			// hace la contraseña visible para que la visualice el usuario.
			if (showPassword.getText().toString()
					.equals(this.getString(R.string.showPassword))) {

				passwordEditText
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
				repeatPasswordEditText
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
				showPassword.setText(this.getString(R.string.hidePassword));
			} else {
				// vuelve la contreña invisible otra vez.
				passwordEditText
						.setTransformationMethod(PasswordTransformationMethod
								.getInstance());
				repeatPasswordEditText
						.setTransformationMethod(PasswordTransformationMethod
								.getInstance());
				showPassword.setText(this.getString(R.string.showPassword));
			}
			break;
		case R.id.buttonNext:
			String email = mailEditText.getText().toString();
			String password = passwordEditText.getText().toString();
			String repeatPassword = repeatPasswordEditText.getText().toString();
			String phone = phoneEditText.getText().toString().trim();

			if (!email.equals("") && !password.equals("")
					&& !repeatPassword.equals("") && !phone.equals("")) {
				if (isValidEmail(email)) {
					if (phone.matches("[1-9]{1}\\d{8}")) {
						// Si las constraseñas introducidas son iguales.
						if (password.equals(repeatPassword)) {

							if (isNetworkConnected()) {
								/*
								 * TEST
								 */
								// int number = 1 + (int) (Math.random() *
								// 999999999);
								// phone = number + "";

								Log.i("myPhone", phone);
								// Construye la cadena.
								String url = URL + REGISTER + TOKEN + "&email="
										+ email + "&phone=" + phone + "&pass="
										+ password;
								Log.i(TAG, url);
								AsyncRegisterUser task = new AsyncRegisterUser(
										this);

								task.execute(new String[] { url });
								try {
									idUser = task.get();
								} catch (InterruptedException
										| ExecutionException e) {
									Log.e(TAG + " task.get()", e.toString());
								}

								// Si el registro se ha producido
								// satisfactoriamente.
								if (!idUser.equals("-1")) {
									// BORRAR
									Log.i(TAG, "idUser:" + idUser);
									showToast("registro satisfactorio");
									mailUser = email;
									launchRegisterFinishActivity();
								} else {
									resetPasswordFields();
								}

							} else {
								showToast(this
										.getString(R.string.notConnectionFound));
							}

						} else {
							// En caso contrario mostramos un mensaje info
							showToast(this
									.getString(R.string.diferentPasswords));
							// Resetea los campos contraseña
							resetPasswordFields();
						}
					} else {
						showToast(this.getString(R.string.invalidPhone));
						phoneEditText.setText("");
					}
				} else {
					showToast(this.getString(R.string.invalidEmail));
					mailEditText.setText("");
				}

			} else {
				// Si los campos estan vacíos, muestra un mensaje.
				showToast(this.getString(R.string.emptyFields));
			}
			break;
		default:
			break;
		}

	}

	/**
	 * Resetea los campos de la contraseña.
	 */
	private void resetPasswordFields() {
		passwordEditText.setText("");
		repeatPasswordEditText.setText("");
	}

	/**
	 * Lanza la Activity EditProfileActivity. Segunda parte del registro de
	 * usuario.
	 */
	private void launchRegisterFinishActivity() {
		Intent intent = new Intent(this, EditProfileActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	/**
	 * Referencia los componentes y añade los listeners
	 */
	private void initComponents() {
		// Carga el layout
		setContentView(R.layout.activity_registrer);

		// Referencia views
		mailEditText = (EditText) findViewById(R.id.editTextRegisterMail);
		passwordEditText = (EditText) findViewById(R.id.editTextRegisterPassword);
		repeatPasswordEditText = (EditText) findViewById(R.id.editTextRepeatPassword);
		showPassword = (TextView) findViewById(R.id.textViewShowpassword);
		phoneEditText = (EditText) findViewById(R.id.editTextPhone);
		nextButton = (Button) findViewById(R.id.buttonNext);

		// Añade listeners
		nextButton.setOnClickListener(this);
		showPassword.setOnClickListener(this);
	}

	/**
	 * Valida si el e-mail cumple el patrón correcto.
	 * 
	 * @param target
	 *            String a validar;
	 * @return true si el formato del e-mail es correcto, falso en caso
	 *         contrario.
	 */
	public static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}
}
