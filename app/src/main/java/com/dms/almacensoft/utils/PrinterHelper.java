package com.dms.almacensoft.utils;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;

import java.util.HashMap;
import java.util.List;

/**
 * {@link PrinterHelper} clase de ayuda que contiene métodos para realizar impresión por Bluetooth
 * con impresoras ZEBRA, testeado con impresora ZEBRA QLN220
 */

public class PrinterHelper {

    private PrinterHelper(){

    }

    private static boolean result = false;

    /**
     * Imprime una cadena de caracteres
     * @param printAddress es la MAC de la impresora a utilizar
     * @param label es la cadena de caracteres
     * @param numeroEtiquetas es la cantidad de copias a imprimir
     */
    public static void printBluetooth(String printAddress, String label, int numeroEtiquetas){
        new Thread(() -> {
            try {
                Connection mPrinterConnection = new BluetoothConnection(printAddress);
                mPrinterConnection.open();

                if (mPrinterConnection.isConnected()) {
                    result = true;
                    PrinterStatus printerStatus = null;
                    try {
                        ZebraPrinter mPrinter = ZebraPrinterFactory.getInstance(mPrinterConnection);
                        printerStatus = mPrinter.getCurrentStatus();

                        for (int i = 0; i < numeroEtiquetas; i++) {
                            mPrinter.printStoredFormat(label, new HashMap<>());
                        }

                        Thread.sleep(500); //Give the printer time to receive the data before closing the connection
                        mPrinterConnection.close();
                    } catch (ConnectionException e) {
                        result = false;
                    }
                } else {
                    result = false;
                }
            } catch (Exception exc) {
                result = false;
            }
        }).start();
    }

    /**
     * Imprime una lista de cadena de caracteres
     * @param printAddress es la MAC de la impresora a utilizar
     * @param labels es la lista que contiene las cadenas de caracteres
     * @param numeroEtiquetas es la cantidad de copias a imprimir
     */
    public static void printBluetooth(String printAddress, List<String> labels, int numeroEtiquetas){
        new Thread(() -> {
            try {
                Connection mPrinterConnection = new BluetoothConnection(printAddress);
                mPrinterConnection.open();

                if (mPrinterConnection.isConnected()) {
                    result = true;
                    PrinterStatus printerStatus = null;
                    try {
                        ZebraPrinter mPrinter = ZebraPrinterFactory.getInstance(mPrinterConnection);
                        printerStatus = mPrinter.getCurrentStatus();

                        for (String label : labels) {
                            for (int i = 0; i < numeroEtiquetas; i++) {
                                mPrinter.printStoredFormat(label, new HashMap<>());
                            }
                        }

                        Thread.sleep(500); //Give the printer time to receive the data before closing the connection
                        mPrinterConnection.close();
                    } catch (ConnectionException e) {
                        result = false;
                    }
                } else {
                    result = false;
                }
            } catch (Exception exc) {
                result = false;
            }
        }).start();
    }

    /**
     * @return true - impresión exitosa
     *         false - impresión fállida
     *
     * A tener en cuenta que la conexión con la impresora puede tomar algunos segundos en devolver el
     * valor correcto
     */
    public static boolean getResult(){
        return result;
    }
}
