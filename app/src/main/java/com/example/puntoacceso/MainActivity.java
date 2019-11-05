package com.example.puntoacceso;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;
    WifiConfiguration wificonfiguration = null;


    private static Context appContext;

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;
        batteryPct = batteryPct * 100;

        Toast toast1 = Toast.makeText(getApplicationContext(), ""+batteryPct+"% de batería", Toast.LENGTH_SHORT);
        toast1.show();

        if(batteryPct < 110){
/*
            try {
                java.net.URL url = new URL("http://127.0.0.1:5000/");
                HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
                httpConn.setRequestMethod("GET");

                InputStream inputStream = httpConn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            new MiTareaAsincrona().execute();
*/

            ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                URL url = null;
                try {
                    url = new URL("http://127.0.0.1:5000/");
                    Log.e("UVA1", "okis?");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    Log.e("UVA2", "okis?");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // Acciones a realizar con el flujo de datos
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }

            } else {
                Log.e("UVA2", "No hay conexión");
            }

            levantarPA();

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void levantarPA(){

        appContext = getApplicationContext();
        boolean settingsCanWrite = Settings.System.canWrite(appContext);
        if(!settingsCanWrite) {
            // If do not have write settings permission then open the Can modify system settings panel.
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            startActivity(intent);
        }else {
            // If has permission then show an alert dialog with message.
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage("You have system write settings permission now.");
            alertDialog.show();

            wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
            Method method = null;
            try {
                Log.e("UVAS", "okis?");
                wifiManager.setWifiEnabled(false);
                method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method.invoke(wifiManager, wificonfiguration, true);
                Log.i("UVASaa", "okis?");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            //esto se lanza en el execute
            Log.e("--uva", "doInBackGround");


            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Boolean result) {

            Toast.makeText(MainActivity.this, "Tarea finalizada!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {

        }
    }
}
