package com.dms.almacensoft.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dms.almacensoft.R;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * {@link UtilMethods} contiene métodos de ayuda que se utilizan en el aplicativo, pueden ser llamados
 * desde cualquier clase.
 */

public class UtilMethods {

    private static Toast toast;

    private static BluetoothAdapter bluetoothAdapter;

    private UtilMethods (){

    }

    /**
     * Esconde el teclado virtual
     * @param context es el contexto correcto
     */
    public static void hideKeyboard(AppCompatActivity context){
        InputMethodManager inputMethodManager = (InputMethodManager)  context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Copia la base de datos (ROOM) hacia la carpeta "Downloads" (DB_ALMACEN)
     */
    public static void copyAppDbToDownloadFolder(Context context) {
        String[] databasefiles = new String []{"DB_ALMACEN","DB_ALMACEN-shm","DB_ALMACEN-wal"};
        Executors.newSingleThreadExecutor().execute(() -> {
            for (String currentfile : databasefiles) {
                try {
                    File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), currentfile); // for example "my_data_backup.db"
                    File currentDB = context.getApplicationContext().getDatabasePath(currentfile); //databaseName=your current application database name, for example "my_data.db"
                    if (currentDB.exists()) {
                        FileInputStream fis = new FileInputStream(currentDB);
                        FileOutputStream fos = new FileOutputStream(backupDB);
                        fos.getChannel().transferFrom(fis.getChannel(), 0, fis.getChannel().size());
                        fis.close();
                        fos.close();
                        Log.d("Database successfully", "copied " + currentfile + " to download folder");
                        new Handler(Looper.getMainLooper()).post(() -> UtilMethods.showToast(context,"Base de datos copiada"));
                    } else Log.d("Copying Database", " fail, database not found");
                } catch (IOException e) {
                    Log.d("Copying Database", "fail copying" + currentfile + "reason:", e);
                    new Handler(Looper.getMainLooper()).post(() -> UtilMethods.showToast(context,"Base de datos vacía"));
                }
            }
        });
    }

    /**
     * Muestra un toast único a través de la aplicación
     * @param context es el contexto correcto
     * @param message es el menaje a mostrar
     */
    public static void showToast(Context context, String message) {
        if (toast == null){
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(message);
            toast.show();
        }
    }

    /**
     * Devuelve la fecha formateada ("19841203") de un objeto Date.
     */
    public static String formatDateDetalle(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(dateObject);
    }

    /**
     * Devuelve la fecha formateada ("2019-04-30 17:15:22") de un objeto Date.
     */
    public static String formatCurrentDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(dateObject);
    }

    /**
     * Devuelve la fecha formateada ("20190430171522") de un objeto Date. Será usada como número de documento
     */
    public static String formatNroDocumento(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(dateObject);
    }

    /**
     * Verifica conexión a internet
     * @param context es el contexto correcto
     * @return true - hay conexión
     *         false - no hay conexión
     */
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) { // connected to the internet
            return true;
        } else {
            return false;
        }
    }

    /**
     * Devuelve el diálogo de error de conexión en el que la acción es configurable
     * @param context es el contexto correcto
     * @param iButton es la acción a realizar
     */
    public static CustomDialog getInternetError(Context context, CustomDialog.IButton iButton){
        return new CustomDialog.Builder(context)
                .setMessage(context.getString(R.string.movil_sin_conexion))
                .setIcon(R.drawable.ic_alert)
                .setCancelable(false)
                .setPositiveButtonLabel(context.getString(R.string.reintentar))
                .setPositiveButtonlistener(iButton)
                .setTheme(R.style.AppTheme_Dialog_Error)
                .build();
    }

    /**
     * @return el objeto BluetoothAdapter devuelto por el BluetoothManager de Android
     */
    public static BluetoothAdapter getBluetoothAdapter(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter;
    }
}
