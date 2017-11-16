package cl.cym.android.base.dto;

import java.io.Serializable;

public class RolDTO implements Serializable {

	private static final long serialVersionUID = -8804431205720112196L;

	/**
	 * Identificador del rol.
	 */
	private long id;
	
	/**
	 * Nombre de usuario asociado al rol.
	 */
	private String usuario;
	
	/**
	 * nombre del rol en el sistema.
	 */
	private String rol;

	//////////////////////////////////////////////////////////////////
	////// Constructores /////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	
	public RolDTO() {
		super();
		this.usuario = "";
		this.rol = "";
	}
	
	public RolDTO(String usuario, String rol) {
		super();
		this.usuario = usuario;
		this.rol = rol;
	}
	
	public RolDTO(long id, String usuario, String rol) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.rol = rol;
	}

	//////////////////////////////////////////////////////////////////
	////// Getters y Setters /////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	
	public final long getId() {
		return id;
	}

	public final void setId(long id) {
		this.id = id;
	}

	public final String getUsuario() {
		return usuario;
	}

	public final void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public final String getRol() {
		return rol;
	}

	public final void setRol(String rol) {
		this.rol = rol;
	}
	
	
	
}
