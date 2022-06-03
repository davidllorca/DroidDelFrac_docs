package org.escoladeltreball.droiddelfrac.activity;

import org.escoladeltreball.droiddelfrac.R;
import org.escoladeltreball.droiddelfrac.model.GetData;
import org.escoladeltreball.droiddelfrac.model.pojo.ListUsers;
import org.escoladeltreball.droiddelfrac.model.pojo.User;
import org.escoladeltreball.droiddelfrac.util.task.AsyncLogin;
import org.escoladeltreball.droiddelfrac.util.task.AsyncLoginkeepLogged;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "LoginActivity";

	/* Views */
	private EditText emailEditText;
	private EditText passwordEditText;
	private CheckBox checkBox;
	private Button loginButton;
	private Button registerButton;

	private User user;
	private String email;
	private String password;

	/* URLs Servlet */
	public final static String LOGIN = "/login";
	public final static String URL_LOGIN = URL + LOGIN + TOKEN; // &email &pass

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// PENDIENTE: borrar el login automático Implementar el logout ene l
		// menu
		// saveKeepLogged(false);

		// Si el usuario tiene configurado mantener la conexión lanzaremos directamente EventListActivity
		mailUser = obtainStringPreferences("mailUser");
		if (keepLogged()) {
			idUser = obtainStringPreferences("idUser");
//			mailUser = obtainStringPreferences("mailUser");
			login(mailUser, obtainStringPreferences("password"));
		}

		// Carga componentes
		initComponents();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buttonLogin:
			login();
			break;
		case R.id.buttonRegister:
			register();
			break;
		default:
			break;
		}
	}

	/**
	 * Realiza el login a la aplicación.
	 */
	private void login() {

		email = emailEditText.getText().toString();
		password = passwordEditText.getText().toString();

		if (!email.equals("") && !password.equals("")) {

			if (isNetworkConnected()) {
				// Construye la cadena.
				String url = URL + LOGIN + TOKEN + "&email=" + email + "&pass="
						+ password;
				Log.i(TAG + " url", url);
				AsyncLogin task = new AsyncLogin(this);

				idUser = "-1";
				task.execute(new String[] { url });
			}
		} else {
			// Si los campos estan vacíos, muestra un mensaje.
			showToast(this.getString(R.string.emptyFields));
		}

	}

	/**
	 * Realiza el login a la aplicación cuando la preferencia de keepLogged es true.
	 */
	private void login(String email, String password){
		if (isNetworkConnected()) {
			// Construye la cadena.
			String url = URL + LOGIN + TOKEN + "&email=" + email + "&pass="
					+ password;
			Log.i(TAG + " url", url);
			AsyncLoginkeepLogged task = new AsyncLoginkeepLogged(this);

			idUser = "-1";
			task.execute(new String[] { url });
		}
	}

	/**
	 * Resgistro de un nuevo usuario.
	 */
	private void register() {
		launchRegisterIniActivity();
	}

	/**
	 * Lanza la Activity RegsiterIniActivity.
	 */
	private void launchRegisterIniActivity() {
		Intent intent = new Intent(this, RegisterActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * Referencia los componentes y añade los listeners
	 */
	private void initComponents() {
		// Carga el layout
		setContentView(R.layout.activity_login);

		// Referencia views
		emailEditText = (EditText) findViewById(R.id.editTextUser);
		emailEditText.setText(mailUser);
		passwordEditText = (EditText) findViewById(R.id.editTextPassword);
		checkBox = (CheckBox) findViewById(R.id.checkBoxLogged);
		loginButton = (Button) findViewById(R.id.buttonLogin);
		registerButton = (Button) findViewById(R.id.buttonRegister);

		// Añade listeners
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	private User handleResult(String result) {
		ListUsers user = null;

		// Verificamos que el result no es un error
		if (!result.equals("protocol error") && !result.equals("IO error")) {
			try {
				String code = String
						.valueOf(new JSONObject(result).get("code"));
				// Log BORRAR
				Log.i(TAG, "code: " + code);
				if (code.equals("200")) {

					String listString = GetData.getStringFromJSON("data", "user", result);
					user = new Gson().fromJson(listString, ListUsers.class);

					//showToast(activity.getString(R.string.loginCorrect));
					return user.getListUsers().get(0);
				} else if (code.equals("401")) {
					// Toast info el login ha sido incorrecto
					showToast(getString(R.string.wrongLogin));
				} else {
					// Toast info se ha producido otro error
					showToast(getString(R.string.badRequest));									
				}
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
				showToast(getString(R.string.badRequest));
			}
		}else{
			showToast(getString(R.string.badRequest));
		}
		return null;
	}

	public void setTextPasswordEditText(String text) {
		this.passwordEditText.setText(text);

	}

}
