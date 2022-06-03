package org.escoladeltreball.droiddelfrac.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class ContactsSQLiteHelper extends SQLiteOpenHelper {
 
	//Sentencia SQL para crear la tabla de contactos registrados en la aplicación
    String sqlCreate = "CREATE TABLE contacts (id INTEGER PRIMARY KEY, name TEXT, phone TEXT, photo BLOB default null )";
 
    public ContactsSQLiteHelper(Context context, String nombre,CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
       
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS contacts");
 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }

}