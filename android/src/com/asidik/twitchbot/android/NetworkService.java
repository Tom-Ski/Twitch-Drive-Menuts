package com.asidik.twitchbot.android;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.asidik.twitchbot.InputType;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Tom on 10/02/2015.
 */
public class NetworkService extends Service {

    String TAG = "MenutsDroid";

    String SERVER = "192.168.2.6";
    int port = 5000;

    Socket socket;
    private InetAddress serverAddress;

    IBinder binder = new MenutsBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service Created");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "Service Started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Runnable connect = new SocketConnection(this);
        new Thread(connect).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service Destroyed");
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void sendBluetoothInstruction(InputType type) {

    }

    public class MenutsBinder extends Binder {

        public NetworkService getService() {
            return NetworkService.this;
        }
    }


}
