
/* Detta är en app som visar tiden, genom en NTP server och genom system klockan. Genom att trycka på
"Connectivity" knappen så kan du stänga av och på internetåtkomsten. Det enda jag inte hann klart med var
"Pause" and "Resume" funktionerna */


package com.example.dnk_project_unn_gledje.main;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dnk_project_unn_gledje.R;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("SimpleDateFormat")
public class MainActivity extends AppCompatActivity {

        NTP ntpServer = new NTP();
        private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final TextView textView = findViewById(R.id.timeField);
        final Button startButton = findViewById(R.id.button);
        final Button wiFiButton = findViewById(R.id.button2);

        //When you press Start
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button startButton = (Button) v;
                startButton.setText("Pause");   // Did not finish Pause and resume function
                updateTime();
            }
        });
    }

    //Checking whether the device has Internet connection by returning 'true' or 'false'
    public boolean findConnection() {
        Button wiFiButton = findViewById(R.id.button2);
        boolean b;
        b = wiFiButton.getText() != "Connect";
        return b;
    }

    // Setting NTPTime or System time every minute depending if there's a connection or not
    public void updateTime() {
        TextView textView = findViewById(R.id.timeField);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (findConnection()) {
                                String ntpTime = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(ntpServer.getNTPTime());
                                Log.d("utv", "ntp updateTimeView");
                                textView.setText(ntpTime);
                            } else {
                                String systemTime = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date());
                                Log.d("utv", "system updateTimeView");
                                textView.setText(systemTime);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 0, 60000);//60000 is a refreshing Time one minute
    }

    /*
    The Java functions --> setWifiEnabled among others are deprecated.
    Instead i found a way to turn the internet connection on by calling an Intent.
    This seems to be a better way to handle this task than the deprecated functions
    since the setWifiEnabled function only was available to android's having Android Q and below.
     */
    public void handleConnect(View v){
        // If it is Android Q and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
            startActivity(panelIntent);
            if (wifiManager.isWifiEnabled()) {
                Button button = (Button) v;
                button.setText("Connect");
            } else {
                Button button = (Button) v;
                button.setText("Disconnect");
            }
        }
    }
}