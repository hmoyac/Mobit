package cl.cym.android.base.activitys;

import java.io.File;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import cl.cym.android.base.BaseActivity;
import cl.cym.android.base.R;
import cl.cym.android.base.db.DBHelperUsuarios;
import cl.cym.android.base.dto.UsuarioDTO;
import cl.cym.android.base.util.CryptoUtil;
import cl.cym.android.base.util.GlobalInterface;
import cl.cym.android.base.util.PhotoUtils;

public class NewUserActivity extends Activity {

	private DBHelperUsuarios userHelper = null;
	private CrearUserTask mAuthTask = null;
	//private ModificarUserTask mEditAuthTask = null;
	
	private AlertDialog _photoDialog;
	private AlertDialog _deletePhotoDialog;
    private Uri mImageUri;
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040;//, ACTIVITY_SHARE = 1030;
    private PhotoUtils photoUtils;
    private boolean usuarioConImagen = false;
    private UsuarioDTO myUser = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
		
		photoUtils = new PhotoUtils(getApplicationContext());		
		
		/*
		Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                fromShare = true;
            } else if (type.startsWith("image/")) {
                fromShare = true;
                mImageUri = (Uri) intent
                        .getParcelableExtra(Intent.EXTRA_STREAM);
                getImage(mImageUri);
            }
        }
        */
		
		
		final ImageView imgUsuario = (ImageView) findViewById(R.id.imgUsuario);
		//imgUsuario.setImageBitmap(bpmUsuario);
		
		userHelper = new DBHelperUsuarios(this, BaseActivity.DATABASE_NAME);
		
		
		getPhotoDialog();
		imgUsuario.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v){
	            if(!getPhotoDialog().isShowing() && !isFinishing())
	                getPhotoDialog().show();
	        }
	    });
		imgUsuario.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
	        public boolean onLongClick(View v){
	            if(!deletePhotoDialog().isShowing() && !isFinishing())
	            	deletePhotoDialog().show();
	            return true;
	        }
	    });
	}
	
	private AlertDialog deletePhotoDialog() {
        if (_deletePhotoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.label_eliminar_imagen);
            
            builder
			.setCancelable(false)
			.setPositiveButton("Si", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// get user input and set it to result
							//mEditAuthTask = new ModificarUserTask(usuario, 0);
							//mEditAuthTask.execute((Void) null);
							resetImage();
						}
					})
			.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int id) {
							dialog.cancel();
						}
					});
            
            _deletePhotoDialog = builder.create();
 
        }
        return _deletePhotoDialog;
 
    }
	
	private AlertDialog getPhotoDialog() {
        if (_photoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.label_source_image);
            builder.setPositiveButton(R.string.label_camara, new DialogInterface.OnClickListener() {
 
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    File photo = null;
                    try {
                        // place where to store camera taken picture
                        photo = PhotoUtils.createTemporaryFile("picture", ".jpg", NewUserActivity.this);
                        photo.delete();
                    } catch (Exception e) {
                        Log.v(getClass().getSimpleName(),
                                "Can't create file to take picture!");
                    }
                    mImageUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
 
                }
 
            });
            builder.setNegativeButton(R.string.label_galeria, new DialogInterface.OnClickListener() {
 
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
                }
 
            });
            _photoDialog = builder.create();
 
        }
        return _photoDialog;
 
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
	        mImageUri = data.getData();
	        getImage(mImageUri);
	        usuarioConImagen = true;
	    } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
	            && resultCode == RESULT_OK) {
	        getImage(mImageUri);
	        usuarioConImagen = true;
	    }
	    
	}
	
	public void getImage(Uri uri) {
		photoUtils.getImage(uri, new GlobalInterface(){
	        @Override
	        public void onPhotoDownloaded(Bitmap bitmap){
	            setImageBitmap(bitmap);
	        }
	    });
	}
	
	private void setImageBitmap(Bitmap bitmap){
		ImageView imgUsuario = (ImageView) findViewById(R.id.imgUsuario);
		imgUsuario.setImageBitmap(bitmap);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState.containsKey("Uri")) {
	        mImageUri = Uri.parse(savedInstanceState.getString("Uri"));
	    }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ok_user, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.mnu_ok_user_aceptar:
	        	final EditText inputName = (EditText) this.findViewById(R.id.newUserName);
	        	final EditText inputClave = (EditText) this.findViewById(R.id.newUserClave);
	        	
	        	if (usuarioConImagen) {
		        	final ImageView inputImage = (ImageView) this.findViewById(R.id.imgUsuario);
		        	Bitmap bitmap = inputImage.getDrawingCache(true);
		        	mAuthTask = new CrearUserTask(inputName.getText().toString(), inputClave.getText().toString(), bitmap);
	        	} else {
	        		mAuthTask = new CrearUserTask(inputName.getText().toString(), inputClave.getText().toString(), null);
	        	}
				mAuthTask.execute((Void) null);
				try {
					usuarioConImagen = false;
					myUser = mAuthTask.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
	        	//Log.i("ActionBar", "Nuevo!");
	            break;
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
	    if (myUser != null) {
	    	Intent intent = getIntent();
	    	
			setResult(RESULT_OK, intent);
			finish();
	    }
	    
	}
	
	private void resetImage() {
    	final ImageView imgUsuario = (ImageView)findViewById(R.id.imgUsuario);
		imgUsuario.setImageResource(R.drawable.ic_usuario);
		usuarioConImagen = false;
    }
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class CrearUserTask extends AsyncTask<Void, Void, UsuarioDTO> {

		private final String mUsuario;
		private final String mClave;
		private final Bitmap mImagen;
		public UsuarioDTO user = null;
		
		private ProgressDialog progressDialog;

		CrearUserTask(String usuario, String clave, Bitmap imagen) {
			mUsuario = usuario;
			mClave = clave;
			mImagen = imagen;
			if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(NewUserActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
		}

		@Override
		protected UsuarioDTO doInBackground(Void... params) {
			// attempt authentication against a network service.

			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return false;
			}
			*/

			UsuarioDTO isUserAdd = null;
			if (mImagen != null) {
				PhotoUtils.guardarImagen(mUsuario, mImagen, getApplicationContext());
				isUserAdd = userHelper.agregarUsuario(mUsuario, CryptoUtil.md5(mClave), 1);
			} else {
				isUserAdd = userHelper.agregarUsuario(mUsuario, CryptoUtil.md5(mClave), 0);
			}
			
			return isUserAdd;
		}

		@Override
		protected void onPostExecute(final UsuarioDTO usuario) {
			user = usuario;
			mAuthTask = null;
			if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

			
			//if (success) {
				//Toast.makeText(NewUserActivity.this, "CONECTADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
				//finish();
			//} else {
				//Toast.makeText(NewUserActivity.this, R.string.error_password_label, Toast.LENGTH_SHORT).show();
			//}
		}

		@Override
		protected void onCancelled() {
			user = null;
			mAuthTask = null;
			if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
		}
	}
	
	public class ModificarUserTask extends AsyncTask<Void, Void, Boolean> {

		private final String mUsuario;
		int mConFoto = 0;
		
		ModificarUserTask(String usuario, int conFoto) {
			mUsuario = usuario;
			mConFoto = conFoto;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return userHelper.modificarImagenUsuario(mUsuario, mConFoto);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			//mEditAuthTask = null;
		}

		@Override
		protected void onCancelled() {
			//mEditAuthTask = null;
		}
	}
}
