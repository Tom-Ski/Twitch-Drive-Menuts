package com.asidik.twitchbot.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.IBinder;
import android.view.View;
import android.widget.RadioButton;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.asidik.twitchbot.Bot;

public class AndroidLauncher extends Activity {

    ServiceConnection serviceConnection;
    NetworkService boundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                boundService = ((NetworkService.MenutsBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                boundService = null;
            }
        };
    }

    public void startService (View view) {
        startService(new Intent(this, NetworkService.class));
        bindService(new Intent(this, NetworkService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        ((RadioButton) findViewById(R.id.radioButton)).setChecked(true);
    }

    public void stopService (View view) {
        stopService(new Intent(this, NetworkService.class));
        if (serviceConnection != null)
            unbindService(serviceConnection);
        ((RadioButton) findViewById(R.id.radioButton)).setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        if (boundService != null) {
            boundService.onDestroy();
        }
    }
}
