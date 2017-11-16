package cl.cym.android.base.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = -1811909146899860994L;

	/**
	 * Identificador único del registro. 
	 */
	private long id;
	
	/**
	 * Nombre del usuario en el sistema, debe ser único.
	 */
	private String usuario;
	
	/**
	 * clave de acceso al sistema asociada al usuario.
	 */
	private String clave;
	
	/**
	 * Imagen asociada al usuario.
	 */
	private int imagen;
	
	/**
	 * Lista de roles asociados al usuario.
	 */
	private List<RolDTO> roles;
	
	/**
	 * Fecha de creación del registro. 
	 */
	private String creacion;
	
	/**
	 * Fecha de modificacion del registro.
	 */
	private String modificacion;
	
	//////////////////////////////////////////////////////////////////
	////// Constructores /////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	
	public UsuarioDTO() {
		super();
		this.usuario = "";
		this.clave = "";
		this.imagen = 0;
		roles = new ArrayList<RolDTO>();
		this.creacion = "";
		this.modificacion = "";
	}

	public UsuarioDTO(String usuario, String clave, int imagen, List<RolDTO> roles,
			String creacion, String modificacion) {
		super();
		this.usuario = usuario;
		this.clave = clave;
		this.imagen = imagen;
		this.roles = roles;
		this.creacion = creacion;
		this.modificacion = modificacion;
	}
	
	public UsuarioDTO(String usuario, String clave, int imagen, List<RolDTO> roles) {
		super();
		this.usuario = usuario;
		this.clave = clave;
		this.imagen = imagen;
		this.roles = roles;
	}
	
	public UsuarioDTO(long id, String usuario, String clave, int imagen, List<RolDTO> roles,
			String creacion, String modificacion) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.clave = clave;
		this.imagen = imagen;
		this.roles = roles;
		this.creacion = creacion;
		this.modificacion = modificacion;
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

	public final String getClave() {
		return clave;
	}

	public final void setClave(String clave) {
		this.clave = clave;
	}

	public final int getImagen() {
		return imagen;
	}

	public final void setImagen(int imagen) {
		this.imagen = imagen;
	}

	public final List<RolDTO> getRoles() {
		return roles;
	}

	public final void setRoles(List<RolDTO> roles) {
		this.roles = roles;
	}

	public final String getCreacion() {
		return creacion;
	}

	public final void setCreacion(String creacion) {
		this.creacion = creacion;
	}

	public final String getModificacion() {
		return modificacion;
	}

	public final void setModificacion(String modificacion) {
		this.modificacion = modificacion;
	}
	
	
	
	
}
