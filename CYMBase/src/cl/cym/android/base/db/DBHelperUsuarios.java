package cl.cym.android.base.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cl.cym.android.base.dto.UsuarioDTO;

public class DBHelperUsuarios extends SQLiteOpenHelper {

	//public static final String DATABASE_NAME = "MyDBName.db";
	public static final String TABLE_USUARIOS = "USUARIOS";
	public static final String TABLE_USUARIOS_ID = "ID";
	public static final String TABLE_USUARIOS_USUARIO = "USUARIO";
	public static final String TABLE_USUARIOS_CLAVE = "CLAVE";
	public static final String TABLE_USUARIOS_IMAGEN = "IMAGEN";
	public static final String TABLE_USUARIOS_CREACION = "CREACION";
	public static final String TABLE_USUARIOS_MODIFICACION = "MODIFICACION";
	
	private SimpleDateFormat sf;

	public DBHelperUsuarios(Context context, String databaseName) {
		super(context, databaseName, null, 1);
		sf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_USUARIOS + "("+
				TABLE_USUARIOS_ID + " integer primary key, " +
				TABLE_USUARIOS_USUARIO + " text, " +
				TABLE_USUARIOS_CLAVE + " text, " +
				TABLE_USUARIOS_IMAGEN + " integer, " +
				TABLE_USUARIOS_CREACION +" text, " +
				TABLE_USUARIOS_MODIFICACION + " text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
		onCreate(db);
	}

	public UsuarioDTO agregarUsuario(final String usuario, final String clave, final int imagen) {
		UsuarioDTO user = existeUsuario(usuario);
		if (user == null) {
			final String actual = sf.format(new Date());
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(TABLE_USUARIOS_USUARIO, usuario);
			contentValues.put(TABLE_USUARIOS_CLAVE, clave);
			contentValues.put(TABLE_USUARIOS_IMAGEN, imagen);
			contentValues.put(TABLE_USUARIOS_CREACION, actual);
			contentValues.put(TABLE_USUARIOS_MODIFICACION, actual);
			db.insert(TABLE_USUARIOS, null, contentValues);
			user = existeUsuario(usuario);
			return user;
		}
		return null;
	}

	public UsuarioDTO existeUsuario(final String usuario) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE " + 
				TABLE_USUARIOS_USUARIO + " = " + usuario, null);
		res.moveToFirst();
		UsuarioDTO user = null;
		if (!res.isAfterLast()) {
			user = new UsuarioDTO();
			user.setId(res.getLong(res.getColumnIndex(TABLE_USUARIOS_ID)));
			user.setUsuario(res.getString(res.getColumnIndex(TABLE_USUARIOS_USUARIO)));
			user.setClave(res.getString(res.getColumnIndex(TABLE_USUARIOS_CLAVE)));
			user.setImagen(res.getInt(res.getColumnIndex(TABLE_USUARIOS_IMAGEN)));
			user.setCreacion(res.getString(res.getColumnIndex(TABLE_USUARIOS_CREACION)));
			user.setModificacion(res.getString(res.getColumnIndex(TABLE_USUARIOS_MODIFICACION)));
		}
		return user;
	}
	
	public Cursor getData(final String usuario, final String clave) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE " + 
				TABLE_USUARIOS_USUARIO + " = " + usuario + " and " +
				TABLE_USUARIOS_CLAVE + " = " + clave + "",
				null);
		return res;
	}

	public int numberOfRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_USUARIOS);
		return numRows;
	}

	public boolean modificarUsuario(final String usuario, final String clave, final int imagen) {
		final String actual = sf.format(new Date());
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(TABLE_USUARIOS_CLAVE, clave);
		contentValues.put(TABLE_USUARIOS_IMAGEN, imagen);
		contentValues.put(TABLE_USUARIOS_MODIFICACION, actual);
		db.update(TABLE_USUARIOS, contentValues, TABLE_USUARIOS_USUARIO + " = ? ",
				new String[] { usuario });
		return true;
	}
	
	public boolean modificarImagenUsuario(final String usuario, final int imagen) {
		final String actual = sf.format(new Date());
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(TABLE_USUARIOS_IMAGEN, imagen);
		contentValues.put(TABLE_USUARIOS_MODIFICACION, actual);
		db.update(TABLE_USUARIOS, contentValues, TABLE_USUARIOS_USUARIO + " = ? ",
				new String[] { usuario });
		return true;
	}

	public Integer borrarUsuario(final String usuario) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TABLE_USUARIOS, TABLE_USUARIOS_USUARIO + " = ? ",
				new String[] { usuario });
	}

	public List<UsuarioDTO> getAllUsuario() {
		List<UsuarioDTO> array_list = new ArrayList<UsuarioDTO>();
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS, null);
			res.moveToFirst();
			while (res.isAfterLast() == false) {
				UsuarioDTO user = new UsuarioDTO();
				user.setId(res.getLong(res.getColumnIndex(TABLE_USUARIOS_ID)));
				user.setUsuario(res.getString(res.getColumnIndex(TABLE_USUARIOS_USUARIO)));
				user.setClave(res.getString(res.getColumnIndex(TABLE_USUARIOS_CLAVE)));
				user.setImagen(res.getInt(res.getColumnIndex(TABLE_USUARIOS_IMAGEN)));
				user.setCreacion(res.getString(res.getColumnIndex(TABLE_USUARIOS_CREACION)));
				user.setModificacion(res.getString(res.getColumnIndex(TABLE_USUARIOS_MODIFICACION)));
				
				array_list.add(user);
				res.moveToNext();
			}
		} catch (RuntimeException e) {
			//onUpgrade(db, db.getVersion(), db.getVersion()+1);
		}
		return array_list;
	}
	
	public boolean loginUsuario(final String usuario, final String clave) {
		Cursor res = getData(usuario, clave);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			return true;
		}
		return false;
	}
}