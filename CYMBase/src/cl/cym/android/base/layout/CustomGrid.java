package cl.cym.android.base.layout;

import java.util.ArrayList;
import java.util.List;

import cl.cym.android.base.R;
import cl.cym.android.base.dto.RolDTO;
import cl.cym.android.base.dto.UsuarioDTO;
import cl.cym.android.base.util.GlobalAppVar;
import cl.cym.android.base.util.PhotoUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter {
	private Context mContext;
	private final List<UsuarioDTO> usuarios;

	public CustomGrid(Context c, List<UsuarioDTO> usuarios) {
		mContext = c;
		this.usuarios = usuarios;
	}

	public void deleteItem(int position) {
		usuarios.remove(position);
		if (usuarios.isEmpty()) {
			RolDTO rolAdmin = new RolDTO(GlobalAppVar.DEFAUL_USER, GlobalAppVar.DEFAUL_ROL);
			List<RolDTO> roles = new ArrayList<RolDTO>();
			roles.add(rolAdmin);
			usuarios.add(new UsuarioDTO(GlobalAppVar.DEFAUL_USER, "", 0, roles));
		}
		notifyDataSetChanged();
	}
	
	public void addItem(UsuarioDTO user) {
		usuarios.add(user);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return usuarios.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			grid = new View(mContext);
			grid = inflater.inflate(R.layout.grid_single, null);
		} else {
			grid = (View) convertView;
		}
		
		TextView textView = (TextView) grid.findViewById(R.id.grid_text);
		ImageView imageView = (ImageView) grid
				.findViewById(R.id.grid_image);
		textView.setText(usuarios.get(position).getUsuario());
		switch (usuarios.get(position).getImagen()) {
			case 0:
				imageView.setImageResource(R.drawable.ic_usuario);
				break;
			case 1:
				Bitmap bitmap = PhotoUtils.cargarImagen(usuarios.get(position).getUsuario(), mContext);
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				} else {
					imageView.setImageResource(R.drawable.ic_usuario);
				}
				break;
		}
		return grid;
	}
}