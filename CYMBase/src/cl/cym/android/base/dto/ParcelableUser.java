package cl.cym.android.base.dto;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableUser extends UsuarioDTO implements Parcelable {

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeLong(super.getId());
		dest.writeString(super.getUsuario());
		dest.writeString(super.getClave());		
		dest.writeString(super.getCreacion());
		dest.writeString(super.getModificacion());
		if (super.getRoles() != null && super.getRoles().size() > 0) {
			dest.writeInt(super.getRoles().size());
			for (RolDTO rol : super.getRoles()) {
				dest.writeLong(rol.getId());
				dest.writeString(rol.getUsuario());
				dest.writeString(rol.getRol());
			}
		} else {
			dest.writeInt(0);
		}
	}

	public ParcelableUser(Parcel source) {
		super.setId(source.readLong());
		super.setUsuario(source.readString());
		super.setClave(source.readString());
		super.setCreacion(source.readString());
		super.setModificacion(source.readString());
		List<RolDTO> listaRoles = new ArrayList<RolDTO>();
		int cont = source.readInt();
		for (int i = 0; i < cont; i++) {
			RolDTO rol = new RolDTO(source.readLong(), source.readString(), source.readString());
			listaRoles.add(rol);
		}
	}

	public static final Parcelable.Creator<ParcelableUser> CREATOR = new Parcelable.Creator<ParcelableUser>() {
			
		@Override
		public ParcelableUser createFromParcel(Parcel source) {
			return new ParcelableUser(source);
		}
		
		@Override
		public ParcelableUser[] newArray(int size) {
			return new ParcelableUser[size];
		}

	};
	
}
