package br.ufba.engc50.bluelock.remote;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.ufba.engc50.bluelock.model.Usuario;

/**
 * Created by raffaello.salvetti on 10/02/2017.
 */
public final class MicrocontrollerActions {

    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private OutputStream mmOutputStream;
    private InputStream mmInputStream;
    private Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    private List<MicrocontrollerActionsEvent> listeners = Collections.synchronizedList(new ArrayList<MicrocontrollerActionsEvent>());

    private static MicrocontrollerActions microcontrollerActions;

    private static boolean estadoTranca = false;

    public void toggleTranca() {
        estadoTranca = !estadoTranca;
        sendData((estadoTranca ? "c" : "o"));
    }

    public boolean getStatusTranca() {
        return estadoTranca;
    }

    public MicrocontrollerActions(Activity activity) {
        this.activity = activity;
    }
//
//    public static MicrocontrollerActions getInstance() {
//        if(microcontrollerActions == null) {
//            microcontrollerActions = new MicrocontrollerActions();
//        }
//        return microcontrollerActions;
//    }

    public void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            fireBluetoothEvent("No bluetooth adapter available");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("FBT06"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        fireBluetoothEvent("Bluetooth Device Found");
    }

    public void openBT()
    {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            fireBluetoothEvent("Bluetooth Opened");
        } catch (Exception ex) {
            fireBluetoothError(ex);
        }
    }

    private void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10;

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            fireBluetoothInput(data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public void sendData(String msg)
    {
        try {
            //msg += "\n";
            mmOutputStream.write(msg.getBytes());
            fireBluetoothEvent("Data Sent");
        } catch (IOException ioex) {
            fireBluetoothError(ioex);
        }
    }

    public void closeBT()
    {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            fireBluetoothEvent("Bluetooth Closed");
        }catch(IOException ioex){
            fireBluetoothError(ioex);
        }
    }

    public void addListener(MicrocontrollerActionsEvent listener) {
        listeners.add(listener);
    }

    public void removeListener(MicrocontrollerActionsEvent listener) {
        listeners.remove(listener);
    }

    private void fireBluetoothEvent(String event) {
        for ( MicrocontrollerActionsEvent actionsEvent : listeners) {
            actionsEvent.onBluetoothEvent(event);
        }
    }

    private void fireBluetoothInput(String event) {
        for ( MicrocontrollerActionsEvent actionsEvent : listeners) {
            actionsEvent.onBluetoothInput(event);
        }
    }

    private void fireBluetoothError(Exception ex) {
        for ( MicrocontrollerActionsEvent actionsEvent : listeners) {
            actionsEvent.onBluetoothError(ex);
        }
    }

    public interface MicrocontrollerActionsEvent {
        void onBluetoothEvent(String event);
        void onBluetoothInput(String data);
        void onBluetoothError(Exception ex);
    }
}
