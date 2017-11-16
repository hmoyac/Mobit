package cl.cym.android.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

public class PhotoUtils {
    private static Context mContext;
    private Options generalOptions;
 
    public PhotoUtils(Context context) {
        mContext = context;
    }
 
    public static File createTemporaryFile(String part, String ext,
            Context myContext) throws Exception {
        String path = myContext.getExternalCacheDir().getAbsolutePath()
                + "/temp/";
        File tempDir = new File(path);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }
    
    public static boolean guardarImagen(String usuario, Bitmap bitmap,
            Context myContext) {
        try {
        	String archivo = Environment.getExternalStorageDirectory() + "/" + mContext.getApplicationInfo().name + "/foto/" + usuario + ".jpg";
        	File file = new File(archivo);
            if (!file.exists()) {
            	file.mkdir();
            }
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static Bitmap cargarImagen(String usuario, Context myContext) {
    	Bitmap imagen = null;
    	try {
        	String archivo = Environment.getExternalStorageDirectory() + "/" + mContext.getApplicationInfo().name + "/foto/" + usuario + ".jpg";
        	File file = new File(archivo);
        	BitmapFactory.Options options = new BitmapFactory.Options();
        	options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2
        	 
        	FileInputStream fileInputStream = new FileInputStream(file);
        	imagen = BitmapFactory.decodeStream(fileInputStream, new Rect(0, 0, 0, 0), options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagen;
    }
 
    public void getImage(Uri uri, GlobalInterface photoSetter) {
        Object[] object = new Object[2];
        object[0] = (Object) uri;
        object[1] = (Object) photoSetter;
        new DownloadTask().execute(object); 
    }
    
    public Bitmap getImage(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = null;
        try {
            is = mContext.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(is, null, options);
            is.close();
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.generalOptions = options;
        return scaleImage(options, uri, 300);
    }
    
    public static int nearest2pow(int value) {
        return value == 0 ? 0
                : (32 - Integer.numberOfLeadingZeros(value - 1)) / 2;
    }
 
    public Bitmap scaleImage(BitmapFactory.Options options, Uri uri,
            int targetWidth) {
        if (options == null)
            options = generalOptions;
        Bitmap bitmap = null;
        double ratioWidth = ((float) targetWidth) / (float) options.outWidth;
        double ratioHeight = ((float) targetWidth) / (float) options.outHeight;
        double ratio = Math.min(ratioWidth, ratioHeight);
        int dstWidth = (int) Math.round(ratio * options.outWidth);
        int dstHeight = (int) Math.round(ratio * options.outHeight);
        ratio = Math.floor(1.0 / ratio);
        int sample = nearest2pow((int) ratio);
 
        options.inJustDecodeBounds = false;
        if (sample <= 0) {
            sample = 1;
        }
        options.inSampleSize = (int) sample;
        options.inPurgeable = true;
        try {
            InputStream is;
            is = mContext.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (sample > 1)
                bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight,
                        true);
            is.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
        return bitmap;
    }
    
    private class DownloadTask extends AsyncTask<Object,Integer, Bitmap>{
        private GlobalInterface photoSetter;
        @Override
        protected Bitmap doInBackground(Object...objects){
            Uri uri = (Uri)objects[0];
            this.photoSetter = (GlobalInterface)objects[1];
            Bitmap result = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream is = null;
            try {
                is = mContext.getContentResolver().openInputStream(uri);
                result = BitmapFactory.decodeStream(is, null, options);
                is.close();
     
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(Bitmap result){
            photoSetter.onPhotoDownloaded(result);
        }
    }
    
}