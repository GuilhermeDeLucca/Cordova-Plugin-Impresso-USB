package br.com.plugimpre.musbprinter;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.Map;

import com.tx.printlib.Const;
import com.tx.printlib.UsbPrinter;

public class MUsbPrinter extends CordovaPlugin {
    private UsbPrinter mUsbPrinter;
    private CallbackContext callbackContext;
    private JSONObject params;
/*    private class MyThread extends Thread {
        @Override
        public void run() {
            mUsbPrinter = new UsbPrinter(getApplicationContext());
        }
    }*/
    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        this.params = args.getJSONObject(0);
        //Evento Obter status da impressora
        if (action.equals("getState")) {
            final UsbDevice dev = getCorrectDevice();
            if (dev != null && mUsbPrinter.open(dev)) {
                final long stat1 = mUsbPrinter.getStatus();
                final long stat2 = mUsbPrinter.getStatus2();
                mUsbPrinter.close();
                callbackContext.success(String.format("%04XH, %04XH", stat1, stat2));
            }
        }
        //Imprimir evento de ticket
        if (action.equals("printTicket")) {
            // TODOVerifique o status da impressora antes de imprimir
            final UsbDevice dev = getCorrectDevice();
            if (dev != null && mUsbPrinter.open(dev)) {
                mUsbPrinter.init();
                //Procurando imagens
                mUsbPrinter.printImage(cordova.getActivity().getExternalFilesDir(null).getPath()+"../res/vopakTicket.png");
                /**
                 * Conteúdo comercial
                 */
                mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_PIXEL, 0);
                mUsbPrinter.doFunction(Const.TX_FEED, 140, 0);
                mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_FULL, 0);
                mUsbPrinter.close();
            }
        }
        return true;
    }
    private UsbDevice getCorrectDevice() {
        final UsbManager usbMgr = (UsbManager)cordova.getActivity().getSystemService(Context.USB_SERVICE);
        final Map<String, UsbDevice> devMap = usbMgr.getDeviceList();
        for(String name : devMap.keySet()) {
            if (UsbPrinter.checkPrinter(devMap.get(name)))
                return devMap.get(name);
        }
        return null;
    }
}
