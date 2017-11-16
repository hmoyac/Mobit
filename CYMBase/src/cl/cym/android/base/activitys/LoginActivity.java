package cl.cym.android.base.activitys;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cl.cym.android.base.BaseActivity;
import cl.cym.android.base.R;
import cl.cym.android.base.db.DBHelperUsuarios;
import cl.cym.android.base.dto.RolDTO;
import cl.cym.android.base.dto.UsuarioDTO;
import cl.cym.android.base.layout.CustomGrid;
import cl.cym.android.base.util.CryptoUtil;
import cl.cym.android.base.util.GlobalAppVar;
import cl.cym.android.base.util.PhotoUtils;

public class LoginActivity extends Activity {
	
	GridView grid;
	
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;	
	private DBHelperUsuarios userHelper = null;
	private AlertDialog _deleteUserDialog;
	
	private List<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
	private int posicion = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		guardarConfiguracion();
		
		grid = (GridView)findViewById(R.id.grid);
		
		usuarios = cargarGrilla(grid);
        
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                	
                	if (usuarios.get(position).getUsuario().compareTo(GlobalAppVar.DEFAUL_USER) == 0) {
                		Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
                		startActivityForResult(intent, GlobalAppVar.REQUEST_NEW_USER);
                	} else {
                	   	showDialogPassword(usuarios.get(position).getUsuario(), usuarios.get(position).getImagen());
                	   	//final GlobalAppVar globalVariable = (GlobalAppVar) getApplicationContext();
                        
                        // Get name and email from global/application context
                        //final String name  = globalVariable.getName();
                        //final String email = globalVariable.getEmail();
                        
                        
                	}
                    //Toast.makeText(LoginActivity.this, "You Clicked at " +nombreUsuarioArreglo[position], Toast.LENGTH_SHORT).show();
                }
            });
        
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	posicion = position;
            	if(!deleteUserDialog().isShowing() && !isFinishing()) {
            		deleteUserDialog().show();
            	}
	            return true;
            }
        });
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == GlobalAppVar.REQUEST_NEW_USER && resultCode == RESULT_OK) {
	    	grid = (GridView)findViewById(R.id.grid);
	    	Intent intent = getIntent();
	        UsuarioDTO user = (UsuarioDTO) intent.getSerializableExtra("usuarioDTO");
	    	((CustomGrid)grid.getAdapter()).addItem(user);
	    }
	    
	}
	
	private List<UsuarioDTO> cargarGrilla(GridView gridView) {
		// leemos los usuarios registrados, en caso de vacio aparece el usuario administrador
		userHelper = new DBHelperUsuarios(this, BaseActivity.DATABASE_NAME);
		List<UsuarioDTO> usuarios = userHelper.getAllUsuario();
		if (usuarios.isEmpty()) {
			RolDTO rolAdmin = new RolDTO(GlobalAppVar.DEFAUL_USER, GlobalAppVar.DEFAUL_ROL);
			List<RolDTO> roles = new ArrayList<RolDTO>();
			roles.add(rolAdmin);
			usuarios.add(new UsuarioDTO(GlobalAppVar.DEFAUL_USER, "", 0, roles));
		}
		grid.setAdapter(new CustomGrid(LoginActivity.this, usuarios));
		return usuarios;
	}
	
	private AlertDialog deleteUserDialog() {
        if (_deleteUserDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.label_eliminar_user);
            
            builder
			.setCancelable(false)
			.setPositiveButton("Si", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							int cant = userHelper.borrarUsuario(usuarios.get(posicion).getUsuario());
							if (cant > 0) {
								((CustomGrid)grid.getAdapter()).deleteItem(posicion);
								Toast.makeText(LoginActivity.this, R.string.label_eliminado, Toast.LENGTH_SHORT).show();
							}
						}
					})
			.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int id) {
							dialog.cancel();
						}
					});
            
            _deleteUserDialog = builder.create();
 
        }
        return _deleteUserDialog;
 
    }
	
	//guardar configuración aplicación Android usando SharedPreferences
    public void guardarConfiguracion()
    {
    	SharedPreferences prefs = 
    		getSharedPreferences("AjpdSoftMonitorWifi", Context.MODE_PRIVATE);
    	 
    	SharedPreferences.Editor editor = prefs.edit();
    	editor.putBoolean("GuardarSDCard", true);
    	editor.putString("Fichero", "txtFichero.getText().toString()");
    	editor.commit();
    }

    //cargar configuración aplicación Android usando SharedPreferences
    public void cargarConfiguracion()
    {    	
      //SharedPreferences prefs = getSharedPreferences("AjpdSoftMonitorWifi", Context.MODE_PRIVATE);
    	 
      //txtFichero.setText(prefs.getString("Fichero", "wifi.txt"));
   	  //opUbicacionFichero.setChecked(prefs.getBoolean("GuardarSDCard", true));   	
    }
    
    @SuppressLint("InflateParams") 
    private void showDialogPassword(final String usuario, final int conImagen) {
		if (mAuthTask != null) {
			return;
		}
		
		// get password_dialog.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(this);

		View promptView = layoutInflater.inflate(R.layout.password_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set prompts.xml to be the layout file of the alertdialog builder
		alertDialogBuilder.setView(promptView);

		final ImageView imgUsuario = (ImageView) promptView.findViewById(R.id.imgUsuario);
		switch (conImagen) {
			case 0:
				imgUsuario.setImageResource(R.drawable.ic_usuario);
				break;
			case 1:
				Bitmap bitmap = PhotoUtils.cargarImagen(usuario, getApplicationContext());
				imgUsuario.setImageBitmap(bitmap);
				break;
		}
				
		final TextView txtUsuario = (TextView) promptView.findViewById(R.id.TxtUsuario);
		txtUsuario.setText(usuario);
		
		final EditText input = (EditText) promptView.findViewById(R.id.userInput);
		
		final CheckBox chkMostrar = (CheckBox) promptView.findViewById(R.id.chkMostrar);
		chkMostrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                        // show password
                	input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                        // hide password
                	input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
		
		
		

		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// get user input and set it to result
								mAuthTask = new UserLoginTask(usuario, input.getText().toString());
								mAuthTask.execute((Void) null);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alertD = alertDialogBuilder.create();

		alertD.show();
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String mUsuario;
		private final String mClave;
		private ProgressDialog progressDialog;

		UserLoginTask(String usuario, String clave) {
			mUsuario = usuario;
			mClave = clave;
			if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			boolean isUserOk = userHelper.loginUsuario(mUsuario, CryptoUtil.md5(mClave));
			
			if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
			
			return isUserOk;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

			final GlobalAppVar globalVariable = (GlobalAppVar) getApplicationContext();
			if (success) {
				//Toast.makeText(LoginActivity.this, "CONECTADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
		        //Set name and email in global/application context
		        globalVariable.setUsername(mUsuario);
		        globalVariable.setConectado(true);
		        
		        Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
        		startActivity(intent);
				//finish();
			} else {
				//Toast.makeText(LoginActivity.this, R.string.error_password_label, Toast.LENGTH_SHORT).show();
				globalVariable.setUsername("");
		        globalVariable.setConectado(false);
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_user, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.mnu_new_user_aceptar:
	        	Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
        		startActivity(intent);
	        	
	        	//Log.i("ActionBar", "Nuevo!");
	            return true;
	        /*
	        case R.id.menu_save:
	            Log.i("ActionBar", "Guardar!");;
	            return true;
	        case R.id.menu_settings:
	            Log.i("ActionBar", "Settings!");;
	            return true;
	        */
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
		
}
