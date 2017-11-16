package cl.cym.android.base.util;

import android.app.Application;
import android.graphics.Bitmap;

public class GlobalAppVar extends Application {

	public static final String DEFAUL_USER = "Administrador";
	public static final String DEFAUL_ROL = "ADMIN";
	
	public static final int REQUEST_NEW_USER = 1;
	
	private String username = "";
	
	private Bitmap userImage = null;
	
	private boolean conectado = false;

	
	
	public final Bitmap getUserImage() {
		return userImage;
	}

	public final void setUserImage(Bitmap userImage) {
		this.userImage = userImage;
	}

	public final String getUsername() {
		return username;
	}

	public final void setUsername(String username) {
		this.username = username;
	}

	public final boolean isConectado() {
		return conectado;
	}

	public final void setConectado(boolean conectado) {
		this.conectado = conectado;
	}
	
}
